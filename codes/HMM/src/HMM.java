import java.util.Arrays;
import java.util.Hashtable;
 
public class HMM
{
        static final String HEALTHY = "Healthy";
        static final String FEVER = "Fever";
 
        static final String DIZZY = "dizzy";
        static final String COLD = "cold";
        static final String NORMAL = "normal";
 
        public static void main(String[] args) 
        {
        		String[] states = new String[] {HEALTHY, FEVER};
 
                //String[] observations = new String[] {DIZZY, COLD, NORMAL};
                String[] observations = new String[] {NORMAL, COLD, DIZZY};
 
                Hashtable<String, Double> start_probability = new Hashtable<String, Double>();
                start_probability.put(HEALTHY, 0.6d);
                start_probability.put(FEVER, 0.4d);
 
                // transition_probability
                Hashtable<String, Hashtable<String, Double>> transition_probability = 
                        new Hashtable<String, Hashtable<String, Double>>();
                        Hashtable<String, Double> t1 = new Hashtable<String, Double>();
                        t1.put(HEALTHY, 0.7d);
                        t1.put(FEVER, 0.3d);
                        Hashtable<String, Double> t2 = new Hashtable<String, Double>();
                        t2.put(HEALTHY, 0.4d);
                        t2.put(FEVER, 0.6d);
                transition_probability.put(HEALTHY, t1);
                transition_probability.put(FEVER, t2);
 
                // emission_probability
                Hashtable<String, Hashtable<String, Double>> emission_probability = 
                        new Hashtable<String, Hashtable<String, Double>>();
                        Hashtable<String, Double> e1 = new Hashtable<String, Double>();
                        e1.put(DIZZY, 0.1d);            
                        e1.put(COLD, 0.4d); 
                        e1.put(NORMAL, 0.5d);
                        Hashtable<String, Double> e2 = new Hashtable<String, Double>();
                        e2.put(DIZZY, 0.6d);            
                        e2.put(COLD, 0.3d); 
                        e2.put(NORMAL, 0.1d);
                emission_probability.put(HEALTHY, e1);
                emission_probability.put(FEVER, e2);
 
                forward_viterbi(observations,
                           states,
                           start_probability,
                           transition_probability,
                           emission_probability);

        }
 
        public static void forward_viterbi(String[] obs, String[] states,
                        Hashtable<String, Double> start_p,
                        Hashtable<String, Hashtable<String, Double>> trans_p,
                        Hashtable<String, Hashtable<String, Double>> emit_p)
        {
                /*Hashtable<String, Object[]> T = new Hashtable<String, Object[]>();
                for (String state : states)
                        T.put(state, new Object[] {state, start_p.get(state)});*/

		int state_num = states.length;
		int obs_num = obs.length;
		double V[][] = new double[obs_num+1][state_num];
		String B[][] = new String[obs_num+1][state_num];
		
		int m = 0;
		for (String state : states)
		{
			//V[0][m] = start_p.get(state);
			V[0][m] = Math.log(start_p.get(state));
			B[0][m] = state;
			m++;
		}

		// t is the records the current time
		int t = 0;
        for (String output : obs)
        {
        	t++;
            Hashtable<String, Object[]> U = new Hashtable<String, Object[]>();

			// i is the current state
			int i = -1;
            for (String next_state : states)
            {
				i++;
                String Smax = "";
                double Pmax = Double.NEGATIVE_INFINITY;
 
                double v_prob = 1;       
				
				// j is the previous state
				int j = -1; 
				if(t==1)
				{
					//v_prob = start_p.get(next_state) * emit_p.get(next_state).get(output);
					v_prob = Math.log(start_p.get(next_state)) + Math.log(emit_p.get(next_state).get(output));
					Pmax = v_prob;
					Smax = next_state;
				} else {
	                for (String source_state : states)
	                {
						j++;
	                    //Object[] objs = T.get(source_state);
						//v_path stores the best previous state
	                    //v_path = (String) objs[0];
						//v_prob stores the best probability ending at current state
	                    //v_prob = ((Double) objs[1]).doubleValue();
	 
	                    //double p = emit_p.get(next_state).get(output) *
	                    //                  trans_p.get(source_state).get(next_state);
	                    //v_prob *= p;
	                    //if (v_prob > valmax)
	                    //{
	                    //        argmax = v_path + "," + next_state;
	                    //        valmax = v_prob;
	                    //}
						double p ;

						//p = emit_p.get(next_state).get(output) * trans_p.get(source_state).get(next_state);
						p = Math.log(emit_p.get(next_state).get(output)) + Math.log(trans_p.get(source_state).get(next_state));
						//v_prob = V[t-1][j] * p;
						v_prob = V[t-1][j] + p;
						if (v_prob > Pmax)
						{
							Pmax = v_prob;
							Smax = source_state;
						}
	                }
				}
				//System.out.println("i is " + i + " , and t is " + t);
				V[t][i] = Pmax;
				B[t][i] = Smax;
            }
        }
 

		
		System.out.println(V);
		/* 
                for (String state : states)
                {
                        Object[] objs = T.get(state);
                        v_path = (String) objs[0];
                        v_prob = ((Double) objs[1]).doubleValue();
                        if (v_prob > valmax)
                        {
                                argmax = v_path;
                                valmax = v_prob;
                        }
                } */      
                //return new Object[]{argmax, valmax};
		
        // find the current max probability and its corresponding state.
        int Smax = -1;
        double pMax = 0;
        for (int n = 0; n < state_num; n++ ) 
        {
        	if (V[obs_num][n] >= pMax) {
        		pMax = V[obs_num][n];
        		Smax = n;
        	}
        }

        System.out.println("\n*************************************\n");  
        System.out.println("V");
        for (double[] arr : V) {
        	System.out.println(Arrays.toString(arr));
        }
        System.out.println("B");
        for (String[] arr : B) {
        	System.out.println(Arrays.toString(arr));
        }
        System.out.println("\n*************************************\n");
        }
}

