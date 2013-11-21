import java.util.Hashtable;
 
public class MHMM
{
        static final String HEALTHY = "Healthy";
        static final String FEVER = "Fever";
 
        static final String CUT = "cut";
        static final String HAT = "hat";
        static final String MAD = "mad";

	//added actual observations
	static final String MAC = "mac";
	static final String HIT = "hit";
	static final String CAT = "cat";
 
        public static void main(String[] args) 
        {
                String[] states = new String[] {HEALTHY, FEVER};
 
                //String[] accurate observations 
                String[] observations = new String[] {MAD, HAT, CUT};

		//String[] inaccurate observations 
		String[] actualObservations = new String[] {MAC, HIT, CAT};
 
                Hashtable<String, Float> start_probability = new Hashtable<String, Float>();
                start_probability.put(HEALTHY, 0.6f);
                start_probability.put(FEVER, 0.4f);
 
                // transition_probability
                Hashtable<String, Hashtable<String, Float>> transition_probability = 
                        new Hashtable<String, Hashtable<String, Float>>();
                        Hashtable<String, Float> t1 = new Hashtable<String, Float>();
                        t1.put(HEALTHY, 0.7f);
                        t1.put(FEVER, 0.3f);
                        Hashtable<String, Float> t2 = new Hashtable<String, Float>();
                        t2.put(HEALTHY, 0.4f);
                        t2.put(FEVER, 0.6f);
                transition_probability.put(HEALTHY, t1);
                transition_probability.put(FEVER, t2);
 
                // emission_probability
                Hashtable<String, Hashtable<String, Float>> emission_probability = 
                        new Hashtable<String, Hashtable<String, Float>>();
                        Hashtable<String, Float> e1 = new Hashtable<String, Float>();
                        e1.put(CUT, 0.1f);            
                        e1.put(HAT, 0.4f); 
                        e1.put(MAD, 0.5f);
                        Hashtable<String, Float> e2 = new Hashtable<String, Float>();
                        e2.put(CUT, 0.6f);            
                        e2.put(HAT, 0.3f); 
                        e2.put(MAD, 0.1f);
                emission_probability.put(HEALTHY, e1);
                emission_probability.put(FEVER, e2);

		//confusion_probability
		Hashtable<String, Hashtable<String, Float>> confusion_probability = 
			new Hashtable<String, Hashtable<String, Float>>();
			Hashtable<String, Float> c1 = new Hashtable<String, Float>();
			c1.put(CUT, 0.0f);
			c1.put(HAT, (1.0f/3.0f));
			c1.put(MAD, (2.0f/3.0f));
			Hashtable<String, Float> c2 = new Hashtable<String, Float>();
			c2.put(CUT, (1.0f/3.0f));
			c2.put(HAT, (2.0f/3.0f));
			c2.put(MAD, 0.0f);
			Hashtable<String, Float> c3 = new Hashtable<String, Float>();
			c3.put(CUT, (2.0f/3.0f));
			c3.put(HAT, (2.0f/3.0f));
			c3.put(MAD, (1.0f/3.0f));
		confusion_probability.put(MAC, c1);
		confusion_probability.put(HIT, c2);
		confusion_probability.put(CAT, c3);

 
                forward_viterbi(actualObservations,
                           observations, states,
                           start_probability,
                           transition_probability,
                           emission_probability,
			   confusion_probability);
        }
 
        public static void forward_viterbi(String[] actualObs, String[] obs, String[] states,
                        Hashtable<String, Float> start_p,
                        Hashtable<String, Hashtable<String, Float>> trans_p,
                        Hashtable<String, Hashtable<String, Float>> emit_p,
			Hashtable<String, Hashtable<String, Float>> conf_p)
        {

		int state_num = states.length;
		int obs_num = obs.length;
		//V[t][i] stores the overall largest probability ending at the state of i at time t
		float V[][] = new float[obs_num+1][state_num];
		//B[t][i]  stores the last source state corresponding to the V[t][i]
		int B[][] = new int[obs_num+1][state_num];
		//X[t][i] stores the word that has been chosen corresponding to the V[t][i]
		String X[][] = new String[obs_num+1][state_num];
		
		int m = 0;
		for (String state : states)
		{
			V[0][m] = start_p.get(state);
			B[0][m] = m;
			X[0][m] = "@";
			m++;
		}

		// t is the records the current time
		int t = 0;
                for (String input : actualObs)
                {
			// input is the current actual observation
			t++;
			// i is the current state
			int i = -1;
                        for (String next_state : states)
                        {
				i++;
                                int Smax = -1;
                                float Pmax = 0;
				String v_word = "";	

                                float v_prob = 1;       
			
				// x is the current accurate observation (word)	
				int x = -1;
				for (String word : obs)		
				{
					x++;
					// j is the previous state
					int j = -1; 
					for (String source_state : states)
					{
						j++;
						float p = emit_p.get(next_state).get(word) * 
						trans_p.get(source_state).get(next_state) * conf_p.get(input).get(word);

						v_prob = V[t-1][j] * p;
						
						if (v_prob > Pmax)
						{
							Pmax = v_prob;
							Smax = j;
							v_word = word;
						}
					}
				}
				V[t][i] = Pmax;
				B[t][i] = Smax;
				X[t][i] = v_word;
                        }
                }
 
                float total = 0;
                String argmax = "";
                float valmax = 0;
 
                float prob;
                String v_path;
                float v_prob;
	
			
		for (int n = 0; n < obs_num+1; n++)
			for (int z = 0; z < state_num; z++)
				System.out.println(V[n][z]);
		for (int n = 0; n < obs_num+1; n++)
			for (int z = 0; z < state_num; z++)
				System.out.println(B[n][z]);
		for (int n = 0; n < obs_num+1; n++) 
			for (int z = 0; z < state_num; z++)
				System.out.println(X[n][z]);
		
		int Smax = -1;
		float max = 0;
		for (int n = 0; n < state_num; n++ ) 
		{
			if (V[obs_num][n] > max) {
				max = V[obs_num][n];
				Smax = n;
			}
		}

		int path[] = new int[obs_num + 1];
		String Word[] = new String[obs_num + 1];
		path[obs_num]  = Smax;
		for (int x = obs_num-1; x >= 0; x--)
		{
			path[x] = B[x+1][path[x+1]];
		}

		for (int x = 0; x < obs_num+1; x++)
		{
			System.out.println(path[x]);
		}
        }
}

