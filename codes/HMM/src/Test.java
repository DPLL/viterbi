import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

import cern.jet.random.Uniform;
import cern.jet.random.engine.DRand;
import cern.jet.random.engine.RandomEngine;
 
public class Test
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
		System.out.println(((Float) ret[0]).floatValue());		
		System.out.println((String) ret[1]);
		System.out.println(((Float) ret[2]).floatValue());
		classify();
	}
 
	
    public static void classify()
    {
    	double recall = 0.7;
		double min = 0.0;
		double max = min + recall;

    	int runTime = 10;
    	int score = 0;
    	
        for(int i = 0; i < runTime; i++) {
/*        	RandomEngine engine = new DRand();
        	Uniform uni = new Uniform(engine);
        	double error = uni.nextDouble();*/
    		double error = Math.random(); 
/*         	Random generator = new Random();
        	double error = generator.nextDouble();*/
    		System.out.println("*********************");
    		System.out.println("error is " + error);
    		System.out.println("*********************");
    		// min and max are the lower and upper bound of obj, respectively.

    		//System.out.println("min is " + min + ", and max is " + max);
    		int id;
/*    		if (0 <= error && error < min) {
    			id = 0;
    		} else if (1 > error && error >= max) {
    			id = 0;
    		} else {
    			id = 1;
    		}*/
    		if (min <= error && error < max) {
    			id = 1;
    		} else {
    			id = 0;
    		}
        	score += id;
        }
/*        System.out.println("classifiedResults is as follows");
        for(ObjectSimulation2 arr : classifiedResults)
        	System.out.println(arr);*/
		System.out.println("score is: " + ((double)score/runTime));
    }
	
	public static Object[] forward_viterbi(String[] obs, String[] states,
			Hashtable<String, Float> start_p,
			Hashtable<String, Hashtable<String, Float>> trans_p,
			Hashtable<String, Hashtable<String, Float>> emit_p)
	{
		Hashtable<String, Object[]> T = new Hashtable<String, Object[]>();
		for (String state : states)
			T.put(state, new Object[] {start_p.get(state), state, start_p.get(state)});
 
		for (String output : obs)
		{
			Hashtable<String, Object[]> U = new Hashtable<String, Object[]>();
			for (String next_state : states)
			{
				float total = 0;
				String argmax = "";
				float valmax = 0;
 
				float prob = 1;
				String v_path = "";
				float v_prob = 1;	
 
				for (String source_state : states)
				{
					Object[] objs = T.get(source_state);
					prob = ((Float) objs[0]).floatValue();
					v_path = (String) objs[1];
					v_prob = ((Float) objs[2]).floatValue();
 
					float p = emit_p.get(source_state).get(output) *
							  trans_p.get(source_state).get(next_state);
					prob *= p;
					v_prob *= p;
					total += prob;
					if (v_prob > valmax)
					{
						argmax = v_path + "," + next_state;
						valmax = v_prob;
					}
				}
				U.put(next_state, new Object[] {total, argmax, valmax});
			}
			T = U;			
		}
 
		float total = 0;
		String argmax = "";
		float valmax = 0;
 
		float prob;
		String v_path;
		float v_prob;
 
		for (String state : states)
		{
			Object[] objs = T.get(state);
			prob = ((Float) objs[0]).floatValue();
			v_path = (String) objs[1];
			v_prob = ((Float) objs[2]).floatValue();
			total += prob;
			if (v_prob > valmax)
			{
				argmax = v_path;
				valmax = v_prob;
			}
		}	
		return new Object[]{total, argmax, valmax};	
	}
}