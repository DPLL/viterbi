/*
 * Simulation2.java is java file that implements our NIUBI algorithm
 */

import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
 
public class Simulation2
{
    
    // graphGen is the randomGraph generator that generates a weigthed random graph
	static SimulationGraph2 graphGen;
	// objectSeq is the initial recognition results from Sphinx as a String array
	static ObjectSimulation2[] objectSeq;
    // states store the all the different states the workflow has
	static String[] states;
    // trueObjectSet stores all the trueObject the workflow has
	static ObjectSimulation2[] trueObjectSet;
    // emission_probability stores the emission probability matrix
	static Hashtable<String, Hashtable<ObjectSimulation2, Double>> emission_probability;
    // transition_probability stores the transition probability matrix
	static Hashtable<String, Hashtable<String, Double>> transition_probability;
    // transition_probability stores the start probability matrix
	static Hashtable<String, Double> start_probability;
    // confusion_probability stores the confusion probability matrix
	static Hashtable<ObjectSimulation2, Hashtable<ObjectSimulation2, Double>> confusion_probability;
    // myWordPercentage is the percentage of object that my correction algo. is right
	static double myWordPercentage;
    // myStatePercentage is the percentage of state that my correction algo. is right
	static double myStatePercentage;
    // asrWordPercentage is the percentage of object that ASR is right
	static double asrWordPercentage;

    
    public static final boolean DEBUG_MODE = false;
    
    public static void main(String[] args) throws IOException, InterruptedException 
    {
    	//
    	int runTime = 1;
    	double totalMyWordPercentage = (double) 0.0;
    	double totalASRWordPercentage = (double) 0.0;
    	double totalMyStatePercentage = (double) 0.0;
    	
    	double totalPathLength = 0.0;
    	
		AbstractBaseGraph<VertexSimulation2, DefaultWeightedEdge> graph = null;
    	ArrayList<DefaultWeightedEdge> diameterPath = new ArrayList<DefaultWeightedEdge>();
		ArrayList<ObjectSimulation2> classifiedResult = new ArrayList<ObjectSimulation2>();
		
		if (DEBUG_MODE) {
			// read from the predefined class
			graphGen = new SimulationGraph2();

			try {
				FileInputStream fileIn = new FileInputStream("graph2.out");
				ObjectInputStream in = new ObjectInputStream(fileIn);   			
				graph = (AbstractBaseGraph<VertexSimulation2, DefaultWeightedEdge>) in.readObject();
		        in.close();
		        fileIn.close();
			} catch(IOException ex)
		    {
		         ex.printStackTrace();
		         return;
		    } catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println(graph.toString());
			graphGen.randomGraph = graph;
			graphGen.density = 0.5;
			graphGen.objectPerNode = 2;
			graphGen.numVertex = 3;
			graphGen.recall = 0.5;
			
			graphGen.numEdge = 6;

			diameterPath = graphGen.findDiameter();
			graphGen.setGroundTruth(diameterPath);
			classifiedResult = graphGen.trueObjects;
			objectSeq = new ObjectSimulation2[classifiedResult.size()];
	        objectSeq = classifiedResult.toArray(objectSeq);
	        System.out.println(Arrays.toString(objectSeq));

		} else {
    	 	// Generate a random graph
    		graphGen = new SimulationGraph2();
    		// 1.[densityOfGraph] 2.[objectNumPerNode] 3.[nodeNum] 4.[recall] 5.[pathLength]
			graph = graphGen.GraphGen(0.6, 10, 20, 0.9, 5);
			System.out.println(graphGen.numVertex);
			System.out.println(graph.toString());	
    		/*
    		 *  Graph Interface
    		 */
    		graphParse(graph);
    		
    		// pathLength records the length of the path in terms of number of vertex.
    		double pathLength;
    		// use the diameter as the ground truth
			diameterPath = graphGen.findDiameter();
			graphGen.setGroundTruth(diameterPath);
			pathLength = diameterPath.size() + 1;
    		
			// instead of choosing the diameter as the path, choose a path specified length 
/*			System.out.println(graphGen.pathLength);
			ArrayList<VertexSimulation2> pathInVertex = graphGen.findPath(graphGen.pathLength);
			pathLength = pathInVertex.size();
			if (!graphGen.setGroundTruthInVertex(pathInVertex)) 
			{
				System.out.println("could not find such path!");
				System.exit(-1);
			}	*/			
			
			/*
			 * Sensor simulator -- simulate the classification process of sensors
			 */
	    	for(int i = 0; i < runTime; i++) {
		        //SimpleDirectedWeightedGraph<VertexSimulation2, DefaultWeightedEdge> graph;
				classifiedResult= graphGen.classify();
				objectSeq = new ObjectSimulation2[classifiedResult.size()];
		        objectSeq = classifiedResult.toArray(objectSeq);
		        System.out.println("objectSeq is " + Arrays.toString(objectSeq));
		        
	    		/*
	    		 * generate the confusion probability matrix
	    		 */
	            confusion_probability =	confustionGen(objectSeq, trueObjectSet);
	            
	            correct(objectSeq,
	            		trueObjectSet, states,
	                    start_probability,
	                    transition_probability,
	                    emission_probability,
	                    confusion_probability,
	                    graphGen.trueObjects, 
	                    graphGen.trueStates
	                    );

	            StringBuilder str = new StringBuilder();
	            for(int m = 0; m < graphGen.trueObjects.size(); m++) {
	            	str.append(graphGen.trueObjects.get(m) + " ");
	            }
	            //System.out.println(str);
	    		//System.out.println("The trueObjects is as follows:" + graphGen.trueObjects);
	            
	    		System.out.println("The trueObjects is as follows:" + str);
	    		/*
	    		for(int j = 0; j < graphGen.trueObjects.size(); j++) {
	    			System.out.println(Arrays.toString(graphGen.trueObjects.get(j)));
	    		}*/
	    		System.out.println("The trueStates is: " + graphGen.trueStates);	
	    		
	    		totalMyWordPercentage += myWordPercentage;
	    		totalMyStatePercentage += myStatePercentage;
	        	totalASRWordPercentage += asrWordPercentage;
	        	totalPathLength += pathLength;
	      	
	        	System.out.println("in the " + i + " th interation, haha!!!!");
	    	}
	    	
	    	System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	    	System.out.println("myObjectScore: " + (double)totalMyWordPercentage/runTime);
	    	System.out.println("sensorObjectScore: " + (double)totalASRWordPercentage/runTime);
	    	System.out.println("myStateScore: " + (double)totalMyStatePercentage/runTime);    
	    	System.out.println("averagePathLength: " + (double)totalPathLength/runTime);     
	    	System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	    	System.out.println("");
    	}
		
    }
    
	    /*
	     * read the graph and create interface for it. 
	     */
	    public static void graphParse(AbstractBaseGraph<VertexSimulation2, DefaultWeightedEdge> graph)
	    {
			// verSet contains all the vertexes of the graph
	        Set<VertexSimulation2> verSet = new HashSet<VertexSimulation2>();
	        verSet.addAll(graph.vertexSet());	        
	        // for states
	        ArrayList<String> stateList = new ArrayList<String>();
	        // for start_probability
	        start_probability = new Hashtable<String, Double>();
	        double start_prob = (double)1/(verSet.size()); // start_prob is evenly distributed among all the states
	        System.out.println("start_prob: " + start_prob + " verSet.size(): " + verSet.size());
	        // for trueObjectSet
	        ArrayList<ObjectSimulation2> trueObjectSetList = new ArrayList<ObjectSimulation2>();
	        // for emission_probability
	        emission_probability = 
	        		new Hashtable<String, Hashtable<ObjectSimulation2, Double>>();
	        double emission_prob = (double)1/(graphGen.objectPerNode);// emission_prob is evenly distributed among all the objects for a node.
	        System.out.println("emission_prob: " + emission_prob + " graphGen.objectPerNode: " + graphGen.objectPerNode);
	        // for transition_probability
	        transition_probability = 
	        		new Hashtable<String, Hashtable<String, Double>>();
	        
	        for (VertexSimulation2 ver : verSet) {
	        	// print the current vertex
	        	//System.out.println("The current vertex is: " + ver);
	        	// objects is the ArrayList form of objectList of this particular vertex. 
	        	ArrayList<ObjectSimulation2> objects = new ArrayList<ObjectSimulation2>(Arrays.asList(ver.objectMatrix));
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
	        	Hashtable<ObjectSimulation2, Double> e = new Hashtable<ObjectSimulation2, Double>();
	        	for (ObjectSimulation2 object : objects) {
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
	        //System.out.println(trueObjectSetList);
	        trueObjectSet = new ObjectSimulation2[trueObjectSetList.size()];
	        trueObjectSet = trueObjectSetList.toArray(trueObjectSet);
	        //System.out.println("print out the trueObejctSet as follows:");
	        //for(ObjectSimulation2 object : trueObjectSet)
	        //	System.out.println(Arrays.toString(object));
	        //System.out.println(Arrays.toString(trueObjectSet));
	        //System.out.println(trueObjectSetList.size());
	        // print the emission_probability
	        //System.out.println("emission_probability: " + emission_probability);
/*			System.out.println("emission_probability: "); 
            for(String state: emission_probability.keySet()) {
            	System.out.println("State " + state + ":");
            	for(ObjectSimulation2 object : emission_probability.get(state).keySet())
            		System.out.println(Arrays.toString(object) + ":" + emission_probability.get(state).get(object));
            }*/
	        // print the transition_probability
	        //System.out.println(transition_probability);
	    	return;
	    }
    	
	    
    	// actualObs is the initial result form ASR, i.e., Y; obs is the ground truth objects, i.e., X.
        public static void correct(ObjectSimulation2[] actualObs, ObjectSimulation2[] obs, String[] states,
                        Hashtable<String, Double> start_p,
                        Hashtable<String, Hashtable<String, Double>> trans_p,
                        Hashtable<String, Hashtable<ObjectSimulation2, Double>> emit_p,
			Hashtable<ObjectSimulation2, Hashtable<ObjectSimulation2, Double>> conf_p,
			ArrayList<ObjectSimulation2> trueObjects, ArrayList<String> trueStates)
        {
        	// state_num is the number of different states
        	int state_num = states.length;
        	// obs_num is the number of objects from ASR
        	int obs_num = actualObs.length;
        	//V[t][i] stores the overall largest probability ending at the state of i at time t
        	double V[][] = new double[obs_num+1][state_num];
        	//B[t][i]  stores the last source state corresponding to the V[t][i]
        	int B[][] = new int[obs_num+1][state_num];
        	//X[t][i] stores the object that has been chosen corresponding to the V[t][i]
        	ObjectSimulation2 X[][] = new ObjectSimulation2[obs_num+1][state_num];
		
        	//Arrays.sort(states);
        	System.out.println("states[] is: " + Arrays.toString(states));
/*        	int m = 0;
        	for (String state : states)
        	{
        		//V[0][m] = start_p.get(state);
        		V[0][m] = Math.log(start_p.get(state));
        		B[0][m] = m;
        		X[0][m] = new double[graphGen.dimension];
        		m++;
        	}*/
        	for (String state : states)
        	{
        		V[0][Integer.parseInt(state)] = Math.log(start_p.get(state));
        		B[0][Integer.parseInt(state)] = Integer.parseInt(state);
        		X[0][Integer.parseInt(state)] = new ObjectSimulation2();
        	}
        	
        	// t is the records the current time
        	int t = 0;
            for (ObjectSimulation2 input : actualObs)
            {
            	// input is the current actual observation
            	t++;
                for (String next_state : states)
                {
                	// Smax is the previous state that corresponds to the current 
                	// max probability
                    int Smax = -1;
                    // Pmax is the max probability that reaching the current state
                    //double Pmax = 0;
                    double Pmax = Double.NEGATIVE_INFINITY;
                    // v_object the object from the trueObject set who corresponds to Pmax.
                    ObjectSimulation2 v_object = new ObjectSimulation2();	
                    // intermediate variable for calculating Pmax 
                    double v_prob = 1;       			
                    // x is the current accurate observation (object)	
                    for (ObjectSimulation2 object : obs)		
                    {
                    	if (t == 1) {
                    		if (emit_p.get(next_state) == null || emit_p.get(next_state).get(object) == null) {
                    			v_prob = Double.NEGATIVE_INFINITY;
                    		}
                    		else {
                    			double start = Math.log(start_p.get(next_state));
                    			double emit = Math.log(emit_p.get(next_state).get(object));
                    			double conf = Math.log(conf_p.get(input).get(object));                    			
                    			//double v_prob_prime = Math.log(start_p.get(next_state)) + Math.log(emit_p.get(next_state).get(object)) + Math.log(conf_p.get(input).get(object));
                    			v_prob = start + emit + conf;
                    		}
                    		if (v_prob > Pmax) {
                    			Pmax = v_prob;
                    			Smax = Integer.parseInt(next_state);
                    			v_object = object;
                    		}
                    	} else {
	                    	for (String source_state : states)
	                    	{
	                    		double p;
	                    		if(emit_p.get(next_state) == null || trans_p.get(source_state) == null ||
	                    				emit_p.get(next_state).get(object) == null || trans_p.get(source_state).get(next_state) == null) {
	                    			//p = 0;
	                    			
	                    			p = Double.NEGATIVE_INFINITY;
	                    		} else {
		                    		//p = emit_p.get(next_state).get(object) * trans_p.get(source_state).get(next_state) * conf_p.get(input).get(object);
	                    			p = Math.log(emit_p.get(next_state).get(object)) + Math.log(trans_p.get(source_state).get(next_state)) +
	                    					Math.log(conf_p.get(input).get(object));
	                    		}
	                    		//v_prob = V[t-1][Integer.parseInt(source_state)] * p;
	                    		v_prob = V[t-1][Integer.parseInt(source_state)] + p;
							
	                    		if (v_prob > Pmax)
	                    		{
	                    			Pmax = v_prob;
	                    			Smax = Integer.parseInt(source_state);
	                    			v_object = object;
	                    		}
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
            //double pMax = 0;
            double pMax = Double.NEGATIVE_INFINITY;
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
            ObjectSimulation2 objects[] = new ObjectSimulation2[obs_num + 1];
            
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
            reportFidelity(actualObs, path, objects, trueObjects, trueStates);
        }
        
        /*
         * reportFidelity reports the speech recognition results and compare them. It is based on an important assumption that
         * ground truth objects and the actual observations have the same length, i.e., Sphinx does not miss any object.
         * actualObs[][] are the objects with noise added, objects[][] are the output of my algorithm, trueObjects are the groundTruth objects 
         */
        public static void reportFidelity(ObjectSimulation2[] actualObs, int[] path, ObjectSimulation2[] objects, ArrayList<ObjectSimulation2> trueObjects, ArrayList<String> trueStates) 
        {
        	// classifiedObjects is the output from the sensor after classification
        	//ArrayList<ObjectSimulation2> classifiedObjects = new ArrayList<ObjectSimulation2>();
        	
        	// gourndStateScore is the score of ground truth state
        	int groundStateScore = trueStates.size();
        	// groundObjectScore is the score of ground truth objects
        	int groundObjectScore = trueObjects.size();
        	if (groundObjectScore != groundStateScore || objects.length != path.length) {
        		System.out.println("Error!!!");
        	}
        	if (groundObjectScore != (objects.length-1) || actualObs.length != groundObjectScore) {
        		System.out.println("The ground truth and the speech recognition results do not match!!!");
        	}
        	
            // compute the classifiedObjects based on the results of confusion_probability and actualObs[][]
/*        	for (int i = 0; i < actualObs.length; i++) {
        		ObjectSimulation2 actualOb = actualObs[i];
        		Hashtable<ObjectSimulation2, Double> d = confusion_probability.get(actualOb);
        		
         		Enumeration<ObjectSimulation2> enumKey = d.keys();
        		Double maxSimilarity = (double) 0;
        		ObjectSimulation2 maxMatch = null;
        		while(enumKey.hasMoreElements()) {
        			ObjectSimulation2 key = enumKey.nextElement();
        			Double val = d.get(key);
        			if (val >= maxSimilarity) {
        				maxSimilarity = val;
        				maxMatch = key;
        			}
        		}
        		classifiedObjects.add(maxMatch);
        	}
            */
        	// myStateScore is the score of state of my algo.
        	double myStateScore = 0;
        	// myObjectScore is the score of objects of my algo.
        	double myObjectScore = 0;
        	// sensorObjectScore is the score of objects of my ASR.
        	double sensorObjectScore = 0;
        	// for the object recovery
        	for (int i = groundObjectScore-1; i >= 0; i--) {
        		//System.out.println("objects[i+1]: " + objects[i+1]);
        		//System.out.println("trueobjects.get(i): " + trueObjects.get(i));

        		//double mySimilarity;
        		//double asrSimilarity;
        		// treat the similarity between the objects as the metric 
        		//mySimilarity = computeSimilarity(objects[i+1], trueObjects.get(i));
        		//asrSimilarity = computeSimilarity(actualObs[i], trueObjects.get(i));
        		//myObjectScore += mySimilarity;
        		//sensorObjectScore += asrSimilarity;
        		
        		if (objects[i+1].objectID == trueObjects.get(i).objectID) {
        			myObjectScore += 1;
        		}
        		// Calculate sensor score based on 'misclassification_probability'
        		//sensorObjectScore += misclassification_probability.get(trueObjects.get(i)).get(trueObjects.get(i));       		
        		// calculate sensor score based on 'classifiedObjects'
        		if (actualObs[i].objectID == trueObjects.get(i).objectID) {
        			sensorObjectScore += 1;
        		}
        		// Only if the states matches, will myStateScore be increased
    			if (path[i+1] == Integer.parseInt(trueStates.get(i))) {
    				myStateScore += 1;
    			}
        	}
        	
        	// 
            myWordPercentage =  ((double)myObjectScore/groundObjectScore);
            asrWordPercentage = ((double)sensorObjectScore/groundObjectScore);
            myStatePercentage = ((double)myStateScore/groundStateScore);
        	
            
        	System.out.println("myObjectScore: " + myWordPercentage);
        	System.out.println("sensorObjectScore: " + asrWordPercentage);
        	System.out.println("myStateScore: " + myStatePercentage);      	
        	
        }
        
        public static Hashtable<ObjectSimulation2, Hashtable<ObjectSimulation2, Double>> 
        	confustionGen(ObjectSimulation2[] obs, ObjectSimulation2[] trueObjectSet) throws IOException, InterruptedException
        {
        	// Due to the assumption that we assume all the objects evenly divide the misclassification probability, the 
        	// misclassification and the inverted misclassification matrix are the same.
    		Hashtable<ObjectSimulation2, Hashtable<ObjectSimulation2, Double>> confusion_probability = 
    				new Hashtable<ObjectSimulation2, Hashtable<ObjectSimulation2, Double>>();
        	for (ObjectSimulation2 obsObject : obs) {
        		Hashtable<ObjectSimulation2, Double> c = new Hashtable<ObjectSimulation2, Double>();
        		for (ObjectSimulation2 trueObject : trueObjectSet) {
        			double similarityIndex;
        			if (obsObject.objectID == trueObject.objectID) {
        				similarityIndex = graphGen.recall;
        			} else {
        				similarityIndex = graphGen.errorGranularity;
        			}
        			c.put(trueObject, similarityIndex);
        		}
        		confusion_probability.put(obsObject, c);
        	}
        	
        	//System.out.println("Hi, I am here");
        	/*
        	Enumeration<ObjectSimulation2> obsObejct;	
        	obsObejct = confusion_probability.keys();
            while(obsObejct.hasMoreElements()) {
               ObjectSimulation2 key = (ObjectSimulation2) obsObejct.nextElement();
               System.out.println(key + ": " +
            		   confusion_probability.get(key));
            }*/
            
/*            for(ObjectSimulation2 obsObject: confusion_probability.keySet()) {
            	System.out.println("obsObject " + obsObject + ":");
            	for(ObjectSimulation2 trueObject : confusion_probability.get(obsObject).keySet())
            		System.out.println(trueObject + ":" + confusion_probability.get(obsObject).get(trueObject));
            }*/
			return confusion_probability;
        }

}

