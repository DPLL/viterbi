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
                        e1.put(DIZZY, 0.1f);            
                        e1.put(COLD, 0.4f); 
                        e1.put(NORMAL, 0.5f);
                        Hashtable<String, Float> e2 = new Hashtable<String, Float>();
                        e2.put(DIZZY, 0.6f);            
                        e2.put(COLD, 0.3f); 
                        e2.put(NORMAL, 0.1f);
                emission_probability.put(HEALTHY, e1);
                emission_probability.put(FEVER, e2);
 
                Object[] ret = forward_viterbi(observations,
                           states,
                           start_probability,
                           transition_probability,
                           emission_probability);
                System.out.println((String) ret[0]);
                System.out.println(((Float) ret[1]).floatValue());
        }
 
        public static Object[] forward_viterbi(String[] obs, String[] states,
                        Hashtable<String, Float> start_p,
                        Hashtable<String, Hashtable<String, Float>> trans_p,
                        Hashtable<String, Hashtable<String, Float>> emit_p)
        {
                /*Hashtable<String, Object[]> T = new Hashtable<String, Object[]>();
                for (String state : states)
                        T.put(state, new Object[] {state, start_p.get(state)});*/

		int state_num = states.length;
		int obs_num = obs.length;
		float V[][] = new float[obs_num+1][state_num];
		String B[][] = new String[obs_num+1][state_num];
		
		int m = 0;
		for (String state : states)
		{
			V[0][m] = start_p.get(state);
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
                                float Pmax = 0;
 
                                float v_prob = 1;       
				
				// j is the previous state
				int j = -1; 
                                for (String source_state : states)
                                {
					j++;
                                        //Object[] objs = T.get(source_state);
					//v_path stores the best previous state
                                        //v_path = (String) objs[0];
					//v_prob stores the best probability ending at current state
                                        //v_prob = ((Float) objs[1]).floatValue();
 
                                        //float p = emit_p.get(next_state).get(output) *
                                        //                  trans_p.get(source_state).get(next_state);
                                        //v_prob *= p;
                                        //if (v_prob > valmax)
                                        //{
                                        //        argmax = v_path + "," + next_state;
                                        //        valmax = v_prob;
                                        //}
					float p = emit_p.get(next_state).get(output) *
                                                          trans_p.get(source_state).get(next_state);
					v_prob = V[t-1][j] * p;
					if (v_prob > Pmax)
					{
						Pmax = v_prob;
						Smax = source_state;
					}
                                }
				//System.out.println("i is " + i + " , and t is " + t);
				V[t][i] = Pmax;
				B[t][i] = Smax;
                        }
                }
 
                float total = 0;
                String argmax = "";
                float valmax = 0;
 
                float prob;
                String v_path;
                float v_prob;
		
		System.out.println(V);
		/* 
                for (String state : states)
                {
                        Object[] objs = T.get(state);
                        v_path = (String) objs[0];
                        v_prob = ((Float) objs[1]).floatValue();
                        if (v_prob > valmax)
                        {
                                argmax = v_path;
                                valmax = v_prob;
                        }
                } */      
                return new Object[]{argmax, valmax};     
        }
}

