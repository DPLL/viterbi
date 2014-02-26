import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
 
public class TranscriberSimulation
{
    
    // graphGen is the randomGraph generator that generates a weigthed random graph
    static GraphGenSimulation graphGen;
	// objectSeq is the initial recognition results from Sphinx as a String array
    static double[][] objectSeq;
    // states store the all the different states the workflow has
    static String[] states;
    // trueObjectSet stores all the trueObject the workflow has
    static double[][] trueObjectSet;
    // emission_probability stores the emission probability matrix
    static Hashtable<String, Hashtable<double[], Double>> emission_probability;
    // transition_probability stores the transition probability matrix
    static Hashtable<String, Hashtable<String, Double>> transition_probability;
    // transition_probability stores the start probability matrix
    static Hashtable<String, Double> start_probability;
    // confusion_probability stores the confusion probability matrix
    static Hashtable<double[], Hashtable<double[], Double>> confusion_probability;
    // myWordPercentage is the percentage of object that my correction algo. is right
    static double myWordPercentage;
    // myStatePercentage is the percentage of state that my correction algo. is right
    static double myStatePercentage;
    // asrWordPercentage is the percentage of object that ASR is right
    static double asrWordPercentage;
    /*
     * stat for noise adding
     */
    // mean is the average of sensor error
    static double mean = 0;
    // stdDev is the standard deviation of sensor error
    static double stdDev = 1;
    
    public static void main(String[] args) throws IOException, InterruptedException 
    {
    	//
    	int runTime = 1;
    	double totalMyWordPercentage = (double) 0.0;
    	double totalASRWordPercentage = (double) 0.0;
    	double totalMyStatePercentage = (double) 0.0;

    	
    	for(int i = 0; i < runTime; i++) {
    	 	// Generate a random graph
        	//SimpleDirectedWeightedGraph<VertexSimulation, DefaultWeightedEdge> graph;
        	AbstractBaseGraph<VertexSimulation, DefaultWeightedEdge> graph;
        	ArrayList<DefaultWeightedEdge> diameterPath = new ArrayList<DefaultWeightedEdge>();
    		graphGen = new GraphGenSimulation();
    		//sphinxResult stores the initial recognition results from Sphinx as an ArrayList
    		ArrayList<String> sphinxResult = new ArrayList<String>();
            
    		
			while(true){
				graph = graphGen.GraphGen(0.5, 2, 5);
				System.out.println(graph.toString());				
				diameterPath =graphGen.findDiameter();
				if (graphGen.setGourdTruth(diameterPath))
					break;
				else 
					System.out.println("\n@@@@@@@@@@@\nGraph is not ready, regenerating!!!\n@@@@@@@@@@@\n");
			}
			//System.out.println(graph.edgeSet().size());
			
	        /*
	         * ASR part -- calling Sphinx
	         */
			/*
			sphinxResult = callSphinx(graphGen);
			if (sphinxResult != null) {
		        // convert arrayList to array
				objectSeq = new String[sphinxResult.size()];
		        objectSeq = sphinxResult.toArray(objectSeq);
		        System.out.println(Arrays.toString(objectSeq));
		        break;
			}
			else {
				System.out.println("\n@@@@@@@@@@@\nSphinx is not working properly, " +
						"regenerating!!!\n@@@@@@@@@@@\n");
			}*/
    		
    		/*
    		 *  Graph Interface
    		 */
    		graphParse(graph);
            
    		/*
    		 * generate the confusion probability matrix
    		 */
            confusion_probability =	confustionGen(objectSeq, trueObjectSet);
            /*
            correct(objectSeq,
            		trueObjectSet, states,
                    start_probability,
                    transition_probability,
                    emission_probability,
                    confusion_probability,
                    graphGen.trueObjects, 
                    graphGen.trueStates
                    );*/
            
            /*
            // Testing by feeding in the right objects directly without going through ASR
            String[] trueWordSeq = new String[graphGen.trueWords.size()];
            trueWordSeq = graphGen.trueWords.toArray(trueWordSeq);
            Hashtable<String, Hashtable<String, Double>> confusion_probability =
            		confustionGen(trueWordSeq, trueObjectSet);
            
            forward_viterbi(trueWordSeq,
            		trueObjectSet, states,
                    start_probability,
                    transition_probability,
                    emission_probability,
                    confusion_probability);*/

    		System.out.println("The tureWords is: " + graphGen.trueObjects);
    		System.out.println("The trueStates is: " + graphGen.trueStates);	
    		
    		totalMyWordPercentage += myWordPercentage;
    		totalMyStatePercentage += myStatePercentage;
        	totalASRWordPercentage += asrWordPercentage;
      	
        	System.out.println("in the " + i + " th interation, haha!!!!");
    		
    	}
    	
    	System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    	System.out.println("myWordScore: " + (double)totalMyWordPercentage/runTime);
    	System.out.println("asrWordScore: " + (double)totalASRWordPercentage/runTime);
    	System.out.println("myStateScore: " + (double)totalMyStatePercentage/runTime);      	
		
    }
    
	    /*
	     * read the graph and create interface for it. 
	     */
	    public static void graphParse(AbstractBaseGraph<VertexSimulation, DefaultWeightedEdge> graph)
	    {
			// verSet contains all the vertexes of the graph
	        Set<VertexSimulation> verSet = new HashSet<VertexSimulation>();
	        verSet.addAll(graph.vertexSet());
	        
	        // for states
	        ArrayList<String> stateList = new ArrayList<String>();
	        // for start_probability
	        start_probability = new Hashtable<String, Double>();
	        double start_prob = (double)1/(verSet.size()); // start_prob is evenly distributed among all the states
	        System.out.println("start_prob: " + start_prob + " verSet.size(): " + verSet.size());
	        // for trueObjectSet
	        ArrayList<double[]> trueObjectSetList = new ArrayList<double[]>();
	        // for emission_probability
	        emission_probability = 
	        		new Hashtable<String, Hashtable<double[], Double>>();
	        double emission_prob = (double)1/(graphGen.objectPerNode);// emission_prob is evenly distributed among all the objects for a node.
	        System.out.println("emission_prob: " + emission_prob + " graphGen.objectPerNode: " + graphGen.objectPerNode);
	        // for transition_probability
	        transition_probability = 
	        		new Hashtable<String, Hashtable<String, Double>>();
	        
	        for (VertexSimulation ver : verSet) {
	        	// print the current vertex
	        	//System.out.println("The current vertex is: " + ver);
	        	// objects is the ArrayList form of objectList of this particular vertex. 
	        	ArrayList<double[]> objects = new ArrayList<double[]>(Arrays.asList(ver.objectMatrix));
	        	//System.out.println(objects);
	        	// outgoingEdges is the ArrayList form of the outgoing edges of this particular vertex.
	        	Set<DefaultWeightedEdge> outgoingEdges = new HashSet<DefaultWeightedEdge>();
	        	outgoingEdges.addAll(graph.outgoingEdgesOf(ver));
	        	//System.out.println(outgoingEdges);
	        	
	        	// array of states
	        	stateList.add(Integer.toString(ver.vertexID));
	        	// start_probability
	        	start_probability.put(Integer.toString(ver.vertexID), start_prob);
	        	// trueObjectSet and emission_probability
	        	Hashtable<double[], Double> e = new Hashtable<double[], Double>();
	        	for (double[] object : objects) {
	            	// trueObjectSet
	        		trueObjectSetList.add(object);
	        		// emission_probability
	        		e.put(object, emission_prob);
	        	}
	        	// emission_probability
	        	emission_probability.put(Integer.toString(ver.vertexID), e);
	        	// transition_probability
	        	double transition_prob = (double)1/(graph.outDegreeOf(ver));
	        	//System.out.println("transition_prob: " + transition_prob + " graph.outDegreeOf(ver): " + graph.outDegreeOf(ver));
	            Hashtable<String, Double> t = new Hashtable<String, Double>();
	            for(DefaultWeightedEdge edge : outgoingEdges) {
	            	t.put(Integer.toString(graph.getEdgeTarget(edge).vertexID), transition_prob);
	            }
	            transition_probability.put(Integer.toString(ver.vertexID), t);     
	        }
	        
	        // print the states
	        //System.out.println(stateList);
	        states = new String[stateList.size()];
	        states = stateList.toArray(states);
	        //System.out.println(Arrays.toString(states));
	        // print the start_probability
	        //System.out.println(start_probability);
	        // print the trueObjectSet
	        System.out.println(trueObjectSetList);
	        trueObjectSet = new double[trueObjectSetList.size()][];
	        trueObjectSet = trueObjectSetList.toArray(trueObjectSet);
	        System.out.println("print out the trueObejctSet as follows:");
	        for(double[] object : trueObjectSet)
	        	System.out.println(Arrays.toString(object));
	        //System.out.println(trueObjectSetList.size());
	        // print the emission_probability
	        System.out.println(emission_probability);
	        // print the transition_probability
	        System.out.println(transition_probability);
	    	return;
	    }
    	/*
    	 * call sphinx to get initial recognition results and return as a string ArrayList.
    	 */
	    /*
    	public static ArrayList<String> callSphinx(GraphGenSimulation graphGen) 
    	{
            ArrayList<String> asrResults = new ArrayList<String>();
            
            for (String name: graphGen.trueObjects) {
                String path = "/home/david/Dropbox/DCOSS14/wavs/" + name + ".wav";
                //System.out.println(path);
                File f = new File(path);
                if (!f.exists()) {
                	System.out.println("File not found!");
                	System.exit(-1);
                }
                // call shell command to run Sphinx           
                String cmd = "java -jar /home/david/Downloads/sphinx4-1.0beta6/bin/LatticeDemo.jar " + path;
                System.out.println(cmd);
                Process p;
                StringBuffer output = new StringBuffer();
                
                try {
                	p = Runtime.getRuntime().exec(cmd);
                	p.waitFor();
     
    		        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));	        
    		        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
    		        // read the output from the command
    	            //System.out.println("Here is the standard output of the command:\n");
    	            String s = null;
    	            while ((s = stdInput.readLine()) != null) {
    	            	//System.out.println(s);
    	            	output.append(s + "\n");
    	            }
    	            // read any errors from the attempted command
    	            //System.out.println("Here is the standard error of the command (if any):\n");
    	            //while ((s = stdError.readLine()) != null) {
    	            //    System.out.println(s);
    	            //}
                } catch (Exception e) {
                	e.printStackTrace();
                }            
                //System.out.println(output.toString());
                // Manipulate the output and get the contents after 'I heard: '
                String pattern = "(I heard: )([\\w ']+)";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(output);
                if(m.find()) {
                	System.out.println("The matching is: " + m.group(2));
                	asrResults.add(m.group(2));
                } else {
                	System.out.println("ASR does not output anything!");
                	return null;
                }
            }
            
            System.out.println(asrResults);
    		return asrResults;
    	}*/
 
    	
    	// actualObs is the initial result form ASR, i.e., Y; obs is the ground true vocalbulary, i.e., X.
        public static void correct(String[] actualObs, String[] obs, String[] states,
                        Hashtable<String, Double> start_p,
                        Hashtable<String, Hashtable<String, Double>> trans_p,
                        Hashtable<String, Hashtable<String, Double>> emit_p,
			Hashtable<String, Hashtable<String, Double>> conf_p,
			ArrayList<String> trueWords, ArrayList<String> trueStates)
        {
        	// state_num is the number of different states
        	int state_num = states.length;
        	// obs_num is the number of objects from ASR
        	//int obs_num = obs.length;
        	int obs_num = actualObs.length;
        	//V[t][i] stores the overall largest probability ending at the state of i at time t
        	double V[][] = new double[obs_num+1][state_num];
        	//B[t][i]  stores the last source state corresponding to the V[t][i]
        	int B[][] = new int[obs_num+1][state_num];
        	//X[t][i] stores the object that has been chosen corresponding to the V[t][i]
        	String X[][] = new String[obs_num+1][state_num];
		
        	int m = 0;
        	Arrays.sort(states);
        	System.out.println("sorted states[] is: " + Arrays.toString(states));
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
                for (String next_state : states)
                {
                	// Smax is the previous state that corresponds to the current 
                	// max probability
                    int Smax = -1;
                    // Pmax is the max probability that reaching the current state
                    double Pmax = 0;
                    // v_object the object from the trueObject set who corresponds to Pmax.
                    String v_object = "";	
                    // intermediate variable for calculating Pmax 
                    double v_prob = 1;       
			
                    // x is the current accurate observation (object)	
                    for (String object : obs)		
                    {
                    	// j is the previous state
                    	int j = -1; 
                    	for (String source_state : states)
                    	{
                    		j++;
                    		double p;
                    		if(emit_p.get(next_state) == null || trans_p.get(source_state) == null ||
                    				emit_p.get(next_state).get(object) == null || trans_p.get(source_state).get(next_state) == null)
                    			p = 0;
                    		else
	                    		p = emit_p.get(next_state).get(object) * 
	                    				trans_p.get(source_state).get(next_state) * conf_p.get(input).get(object);
                    		v_prob = V[t-1][Integer.parseInt(source_state)] * p;
						
                    		if (v_prob >= Pmax)
                    		{
                    			Pmax = v_prob;
                    			Smax = Integer.parseInt(source_state);
                    			v_object = object;
                    		}
                    	}
                    }
                    // Update the corresponding arrays.
                    V[t][Integer.parseInt(next_state)] = Pmax;
                    B[t][Integer.parseInt(next_state)] = Smax;
                    X[t][Integer.parseInt(next_state)] = v_object;
                }
            }
	
            /*
            for (int n = 0; n < obs_num+1; n++)
            	for (int z = 0; z < state_num; z++)
            		System.out.println(V[n][z]);
            for (int n = 0; n < obs_num+1; n++)
            	for (int z = 0; z < state_num; z++)
            		System.out.println(B[n][z]);
            for (int n = 0; n < obs_num+1; n++) 
            	for (int z = 0; z < state_num; z++)
            		System.out.println(X[n][z]);
			*/
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

            // If at the current stage, the output of ASR is too far-away from the trueObject set,
            // it needs special handling.
            int path[] = new int[obs_num + 1];
            String objects[] = new String[obs_num + 1];
            
            path[obs_num]  = Smax;
            objects[obs_num] = X[obs_num][Smax];
            for (int x = obs_num-1; x >= 0; x--)
            {
            	path[x] = B[x+1][path[x+1]];
            	objects[x] = X[x][path[x]]; 
            }
           	
            System.out.println("\n*************************************\n");         
            System.out.println(Arrays.toString(actualObs));
            
            for (int x = 0; x < obs_num+1; x++)
            {
            	System.out.println("state: " + path[x] + 
            			", with the object: " + objects[x]);
            }
            System.out.println("\n*************************************\n");
            
            // report the results of fidelity
            reportFidelity(actualObs, path, objects, trueWords, trueStates);
        }
        
        /*
         * reportFidelity reports the speech recognition results and compare them. It is based on an important assumption that
         * ground truth objects and the actual observations have the same length, i.e., Sphinx does not miss any object.
         */
        public static void reportFidelity(String[] actualObs, int[] path, String[] objects, ArrayList<String> trueWords, ArrayList<String> trueStates) 
        {
        	// gourndStateScore is the score of ground truth state
        	int groundStateScore = trueStates.size();
        	// groundWordScore is the score of ground truth objects
        	int groundWordScore = trueWords.size();
        	if (groundWordScore != groundStateScore || objects.length != path.length) {
        		System.out.println("Error!!!");
        	}
        	if(groundWordScore != (objects.length-1) || actualObs.length != groundWordScore) {
        		System.out.println("The ground truth and the speech recognition results do not match!!!");
        	}
        	// myStateScore is the score of state of my algo.
        	int myStateScore = 0;
        	// myWordScore is the score of objects of my algo.
        	int myWordScore = 0;
        	// asrWordScore is the score of objectsof my ASR.
        	int asrWordScore = 0;
        	// for the object recovery
        	for (int i = groundWordScore-1; i >= 0; i--) {
        		//System.out.println("objects[i+1]: " + objects[i+1]);
        		//System.out.println("trueobjects.get(i): " + trueWords.get(i));
    			//if (objects[i+1] == trueWords.get(i)) {
        		if (objects[i+1].equals(trueWords.get(i))) {
    				myWordScore++;
    			}
    			//System.out.println("actualObs[i]: " + actualObs[i]);
    			//System.out.println("trueobjects.get(i): " + trueWords.get(i));
    			//if (actualObs[i] == trueWords.get(i)) {
    			if (actualObs[i].equals(trueWords.get(i))) {
    				asrWordScore++;
    			}
    			//System.out.println("path[i+1]: " + path[i+1]);
    			//System.out.println("trueStates.get(i): " + trueStates.get(i));
    			if (path[i+1] == Integer.parseInt(trueStates.get(i))) {
    				myStateScore++;
    			}
        	}
        	
        	// 
            myWordPercentage =  ((double)myWordScore/groundWordScore);
            asrWordPercentage = ((double)asrWordScore/groundWordScore);
            myStatePercentage = ((double)myStateScore/groundStateScore);
        	
            
        	System.out.println("myWordScore: " + myWordPercentage);
        	System.out.println("asrWordScore: " + asrWordPercentage);
        	System.out.println("myStateScore: " + myStatePercentage);      	
        	
        }
        
		// convolution_index calculates the overlapping similarity of two strings
		// Notice that p_src is the string in the vocabulariy set, and p_dest is the
		// the actual string that is heard.
		public static double convolution_index(String p_src, String p_dest)
    	{
		    int len_src = p_src.length();
		    int len_dest = p_dest.length();
		    int steps = 2*len_src + len_dest;
		    int i,c;
		    double retval = 0;
	   
		    for(i=0; i<steps; ++i) {
		        int cur_conv_index = 0;
		        int start_s = Math.max(0, (len_src - 1 - i));
		        int start_d = Math.max(0, i - len_src + 1);
		        while(start_s < len_src && start_d < len_dest) {
		            //System.out.println(p_src.substring(start_s,start_s+1) + " == " + p_dest.substring(start_d,start_d+1));
		            if(p_src.substring(start_s,start_s+1).equals(p_dest.substring(start_d,start_d+1))) {
		                ++cur_conv_index;
		            }
		            ++start_s;
		            ++start_d;
		        }
		        if(cur_conv_index > retval)
		            retval = cur_conv_index;
		    }
			//System.out.println("The convolution index value between " + p_src + " and " + p_dest + " is " + retval);
		    return retval/len_dest;       
		}
        
        
        public static Hashtable<double[], Hashtable<double[], Double>> 
        	confustionGen(double[][] obs, double[][] trueObjectSet) throws IOException, InterruptedException
        {
        	/*
        	 * convert the object sequence into phonetic sequence by calling eSpeak.
        	 */        	
        	//String[] obsPhonemes;
        	//String[] vocalPhonemes;
        	
        	//obsPhonemes = phonemeConversion(obs);
        	
        	//vocalPhonemes = phonemeConversion(trueObjectSet);        	
        	
/*        	for (String temp : obsPhonemes)
        		System.out.println(temp);
        	
        	for (String temp : vocalPhonemes)
        		System.out.println(temp);
        	*/
        	
    		Hashtable<double[], Hashtable<double[], Double>> confusion_probability = 
    				new Hashtable<double[], Hashtable<double[], Double>>();
        	for (double[] obsObject : obs) {
        		Hashtable<double[], Double> c = new Hashtable<double[], Double>();
        		for (double[] trueObject : trueObjectSet) {
        			double similarityIndex;
        			double EDistance = computeEuclideanDistance(obsObject, trueObject);
        			double maxDistance = (double)(Math.sqrt(graphGen.dimension)*(graphGen.range+mean+stdDev));
        			similarityIndex = ( EDistance <= maxDistance ? (1 - ((double)EDistance/maxDistance)) : 0);
        			c.put(trueObject, similarityIndex);
        		}
        		confusion_probability.put(obsObject, c);
        	}
        	
        	//System.out.println("Hi, I am here");
        	
        	Enumeration<double[]> obsObejct;	
        	obsObejct = confusion_probability.keys();
            while(obsObejct.hasMoreElements()) {
               double[] key = (double[]) obsObejct.nextElement();
               System.out.println(key + ": " +
            		   confusion_probability.get(key));
            }
        	
			return confusion_probability;
        }
        
        // Calcalate 
        public static double computeEuclideanDistance(double[]obsObject, double[]trueObject)
        {
			return 0;
        }
    	
        private static int minimum(int a, int b, int c) {
            return Math.min(Math.min(a, b), c);
        }

        public static int computeLevenshteinDistance(String str1,String str2) {
            int[][] distance = new int[str1.length() + 1][str2.length() + 1];

            for (int i = 0; i <= str1.length(); i++)
            	distance[i][0] = i;
            for (int j = 1; j <= str2.length(); j++)
                distance[0][j] = j;

            for (int i = 1; i <= str1.length(); i++)
                for (int j = 1; j <= str2.length(); j++)
                    distance[i][j] = minimum(
                    		distance[i - 1][j] + 1,
                            distance[i][j - 1] + 1,
                            // TODO change the weight of substuting one character of the object to 2
                            // distance[i - 1][j - 1]+ ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 2));
                            distance[i - 1][j - 1]+ ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1));

            return distance[str1.length()][str2.length()];    
        }
}

