/*
 * SimulationGraph2.java & Simulation2RandomGraph.java & VertexSimulation2.java & ObjectSimulation2.java  & Simulation2.java are in the same set.
 * Simulation2RandomGraph.java is java file that implements our NIUBI algorithm
 * 
 * PAY ATTENTION that Simulation2RandomGraph is similar to Simulation2, the difference lies in the fact that this file generates a random graph 
 * and tests it, whereas Simulation2 generates a perfect k-ary tree and tests it.
 * 
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

import edu.uci.ics.jung.graph.DelegateTree;
 

public class Simulation2RandomGraph
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
    // asrStatePercentage is the percentage of state that ASR is right
	static double asrStatePercentage;
    
    //
	public static final boolean DEBUG_MODE = false;
    
    public static void main(String[] args) throws IOException, InterruptedException 
    {	
    	
		AbstractBaseGraph<VertexSimulation2, DefaultWeightedEdge> graph = null;
		DelegateTree<VertexSimulation2, DefaultWeightedEdge> tree = null;
		
    	ArrayList<DefaultWeightedEdge> diameterPath = new ArrayList<DefaultWeightedEdge>();
		ArrayList<ObjectSimulation2> classifiedResult = new ArrayList<ObjectSimulation2>();
			
    	// 1.[degree] 2.[objectNumPerNode] 3.[nodeNum] 4.[recall] 5.[pathLength] 6.[graphNum] 7.[runPerGraph]	
		if (args.length < 7) {
			System.out.println("Correct Usage: ./Simulation2RandomGraph 1.[degree] 2.[objectNumPerNode] "
					+ "3.[nodeNum] 4.[recall] 5.[pathLength] 6.[graphNum] 7.[runPerGraph]");
			System.exit(-1);
		}
		
		int degreeVal = Integer.parseInt(args[0]);
		int objNumPerNodeVal = Integer.parseInt(args[1]);
		int nodeNumVal = Integer.parseInt(args[2]);
		double recallVal = Double.parseDouble(args[3]);
		int pathLengthVal = Integer.parseInt(args[4]);
		int graphNumVal = Integer.parseInt(args[5]);
		int runPerGraphVal = Integer.parseInt(args[6]);
		
		System.out.println("degree is " + degreeVal + ", objectNumPerNode is " + objNumPerNodeVal + ", nodeNum is " 
				+ nodeNumVal + ", recall is " + recallVal + ", pathLength is " +
				 pathLengthVal + ", graphNum is " + graphNumVal + ", runPerGraph is " + runPerGraphVal);	
		
    	// runTime is how many runs in total
    	int runTime;
    	// graphNum is the number of graphs generated
    	int graphNum = graphNumVal;
    	// runPerGraph is number of runs for each graph
    	int runPerGraph = runPerGraphVal;
    	runTime = graphNum * runPerGraph; 
		
    	double totalMyWordPercentage = (double) 0.0;
    	double totalASRWordPercentage = (double) 0.0;
    	double totalMyStatePercentage = (double) 0.0;
    	double totalASRStatePercentage = (double) 0.0;
    	
    	double totalPathLength = 0.0;
    	
    	// counts records the actual time of running, it should be equal to runTime at the end.
    	int count = 0;
    	for (int n = 0; n < graphNum; n++) {
    	 	// Generate a random graph
    		graphGen = new SimulationGraph2();
    		double pathLength;
    		
    		/*
    		 * Generate a random graph-----------------------------------------------------
    		 */
    		//keep generating graphs that meet the requirement of pathLength
    		while (true) {
	    		// 1.[avgDegreeOfGraph] 2.[objectNumPerNode] 3.[nodeNum] 4.[recall] 5.[pathLength]
				graph = graphGen.GraphGen(degreeVal, objNumPerNodeVal, nodeNumVal, recallVal, pathLengthVal);
				System.out.println(graphGen.numVertex);
				System.out.println(graph.toString());	
	    		
	    		//Graph Interface 
	    		graphParse(graph);
	    		
	    		// pathLength records the length of the path in terms of number of vertex.
	    		// use the diameter as the ground truth
/*				diameterPath = graphGen.findDiameter();
				graphGen.setGroundTruth(diameterPath);
				pathLength = diameterPath.size() + 1;*/
	    		
				// instead of choosing the diameter as the path, choose a path specified length 
				System.out.println(graphGen.pathLength);
				ArrayList<VertexSimulation2> pathInVertex = graphGen.findPath(graphGen.pathLength);
				pathLength = graphGen.pathLength;
/*				if (!graphGen.setGroundTruthInVertex(pathInVertex)) 
				{
					System.out.println("could not find such path!");
					System.exit(-1);
				}	
				pathLength = pathInVertex.size();*/
				
				if (!graphGen.setGroundTruthInVertex(pathInVertex)) 
				{
					System.out.println("could not find such path!");
					System.out.println("BAD LUCK!!!");
					// Purely for testing the length of the path
					diameterPath = graphGen.findDiameter();
					if (diameterPath.size() >= graphGen.pathLength) {
						System.out.println("WHAT???");
						System.exit(-1);
					}
				} else {
					// if such path could be found, then break out of while loop and keep on with the rest
					break;
				}
    		} // end of while, meaning the graph generation is successful now
		
			/*
			 * Sensor simulator -- simulate the classification process of sensors
			 */
	    	for(int i = 0; i < runPerGraph; i++) {
	    		count++;	    		
		        
	    		//use the evenly distributed confusion matrix to do classification
				//classifiedResult= graphGen.classify();
	    		
				//use the confusion matrix which favors more on the in-state similarity 
	    		//classifiedResult= graphGen.newClassify();
		        
	    		/*
	    		 * generate the confusion probability matrix (evenly distribution)
	    		 */
	            //confusion_probability =	confusionGen(objectSeq, trueObjectSet);
	            
	            // generate new confusion matrix by making within-state similarity higher than others (only conisders the in-state probability, and the intra-state
		        // probabiliyt is just 0)
		        //confusion_probability =	newConfusionGen(objectSeq, trueObjectSet, objNumPerNodeVal);
		        
		        /* 
		         * generate confusionMatrix based on both in-state probability and intra-state probability
		         * the final argument represents the total probability that the objects are sharing within one state.
		         * 
		         * Pay very much attention that the last argument will have an effect on the final results.  Normally, the
		         * the improvement of sensor accuracy comes from the intra-state objects, which is 1-recall-inStateProb
		         * So it is an important parameter to tune
		         */
		        double[][][] matrixes = confusionMatrixGen(nodeNumVal, objNumPerNodeVal, recallVal, (recallVal/3));
		        /*
		         * The confusionMatrix is the cumulative similarity matrix, and the invertedMatrix is just similarity matrix.
		         * 
		         * For instance, the similarity matrix is 
		         *    0    1    2    3    4    5
		         * 0  0.6  0.3  0.1  0    0    0
		         * 
		         * 1  0.3  0.6  0.1  0    0    0
		         * 
		         * 2  0    0    0.6  0.3  0.1  0
		         * 
		         * 3  0    0    0.3  0.6  0.1  0
		         * 
		         * 4  0.1  0    0    0    0.6  0.3
		         * 
		         * 5  0.1  0    0    0    0.3  0.6
		         * 
		         * And then the confusionMatrix will be:
				 *    0    1    2    3    4    5
		         * 0  0.6  0.9  1.0  0    0    0
		         * 
		         * 1  0.3  0.9  1.0  0    0    0
		         * 
		         * 2  0    0    0.6  0.9  1.0  0
		         * 
		         * 3  0    0    0.3  0.9  1.0  0
		         * 
		         * 4  1.0  0    0    0    0.6  0.9
		         * 
		         * 5  1.0  0    0    0    0.3  0.9
		         */
		        double[][] confusionMatrix = matrixes[0];
		        double[][] invertedMatrix = matrixes[1];
		        
/*			    	for (int j = 0; j < confusionMatrix.length; j++) {
			    		System.out.println(Arrays.toString(confusionMatrix[j]));
			    	}
			    	for (int j = 0; j < invertedMatrix.length; j++) {
			    		System.out.println(Arrays.toString(invertedMatrix[j]));
			    	}*/
		        
	    		// use the confusion matrix which considers both in-state and intra-state similarity
	    		classifiedResult= graphGen.newClassify(confusionMatrix, nodeNumVal, objNumPerNodeVal);
	    		
				objectSeq = new ObjectSimulation2[classifiedResult.size()];
		        objectSeq = classifiedResult.toArray(objectSeq);
		        //System.out.println("objectSeq is " + Arrays.toString(objectSeq));
		        
		        // generate confusion probability based on the previous invertedMatrix.
		        confusion_probability =	newConfusionGen(invertedMatrix, objectSeq, trueObjectSet, nodeNumVal, objNumPerNodeVal);
	            
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
	            
	    		//System.out.println("The trueObjects is as follows:" + str);
	    		/*
	    		for(int j = 0; j < graphGen.trueObjects.size(); j++) {
	    			System.out.println(Arrays.toString(graphGen.trueObjects.get(j)));
	    		}*/
	    		//System.out.println("The trueStates is: " + graphGen.trueStates);	
	    		
	    		totalMyWordPercentage += myWordPercentage;
	    		totalMyStatePercentage += myStatePercentage;
	        	totalASRWordPercentage += asrWordPercentage;
	        	totalASRStatePercentage += asrStatePercentage;
	        	totalPathLength += pathLength;
	      	
	        	System.out.println("in the " + count + " th interation, haha!!!!");
	    	}// end for (within the same graph)
    	}// end for (between different graphs)
    	System.out.println("count is: " + count);
    	System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    	System.out.println("myObjectScore: " + (double)totalMyWordPercentage/runTime);
    	System.out.println("sensorObjectScore: " + (double)totalASRWordPercentage/runTime);
    	System.out.println("myStateScore: " + (double)totalMyStatePercentage/runTime);  
    	System.out.println("asrStatePercentage: " + (double)totalASRStatePercentage/runTime);  
    	System.out.println("averagePathLength: " + (double)totalPathLength/runTime);     
    	System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    	System.out.println("");
    	}
    
    
    /*
     * read the kAryPerfectTree and create interface for it.
     */
    public static void treeParse(DelegateTree<VertexSimulation2, DefaultWeightedEdge> kAryTree)
    {
    	// verSet contains all the vertexes of the graph
        Set<VertexSimulation2> verSet = new HashSet<VertexSimulation2>();
        verSet.addAll(kAryTree.getVertices());	        
        // for states
        ArrayList<String> stateList = new ArrayList<String>();
        // for start_probability
        /*
         * Version I: make every node have equal possibility of getting started
         */
        start_probability = new Hashtable<String, Double>();
        double start_prob = (double)1/(verSet.size()); // start_prob is evenly distributed among all the states
        //System.out.println("start_prob: " + start_prob + " verSet.size(): " + verSet.size());
        
        /*
         * Version II: make the root node as the source
         */
        //TODO
        
        // for trueObjectSet
        ArrayList<ObjectSimulation2> trueObjectSetList = new ArrayList<ObjectSimulation2>();
        // for emission_probability
        emission_probability = new Hashtable<String, Hashtable<ObjectSimulation2, Double>>();
        double emission_prob = (double)1/(graphGen.objectPerNode);// emission_prob is evenly distributed among all the objects for a node.
        //System.out.println("emission_prob: " + emission_prob + " graphGen.objectPerNode: " + graphGen.objectPerNode);
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
        	outgoingEdges.addAll(kAryTree.getOutEdges(ver));
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
        	double transition_prob = (double)1/(kAryTree.outDegree(ver));
        	//System.out.println("transition_prob is " + transition_prob);
        	//System.out.println("transition_prob: " + transition_prob + " graph.outDegreeOf(ver): " + graph.outDegreeOf(ver));
            Hashtable<String, Double> t = new Hashtable<String, Double>();
            for(DefaultWeightedEdge edge : outgoingEdges) {
            	//System.out.println("the current kAryTree.getSource(edge).vertexID is " + kAryTree.getSource(edge).vertexID);
            	t.put(Integer.toString(kAryTree.getDest(edge).vertexID), transition_prob);
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
        //System.out.println("transition_probability is as follows: ");
        //System.out.println(transition_probability);
    	return;
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
        //System.out.println("start_prob: " + start_prob + " verSet.size(): " + verSet.size());
        // for trueObjectSet
        ArrayList<ObjectSimulation2> trueObjectSetList = new ArrayList<ObjectSimulation2>();
        // for emission_probability
        emission_probability = 
        		new Hashtable<String, Hashtable<ObjectSimulation2, Double>>();
        double emission_prob = (double)1/(graphGen.objectPerNode);// emission_prob is evenly distributed among all the objects for a node.
        //System.out.println("emission_prob: " + emission_prob + " graphGen.objectPerNode: " + graphGen.objectPerNode);
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
	
    
	// actualObs is the initial result from the sensor, i.e., Y; obs is the all the possible objects, i.e., X.
    public static void correct(ObjectSimulation2[] actualObs, ObjectSimulation2[] obs, String[] states,
                    Hashtable<String, Double> start_p,
                    Hashtable<String, Hashtable<String, Double>> trans_p,
                    Hashtable<String, Hashtable<ObjectSimulation2, Double>> emit_p,
		Hashtable<ObjectSimulation2, Hashtable<ObjectSimulation2, Double>> conf_p,
		ArrayList<ObjectSimulation2> trueObjects, ArrayList<String> trueStates)
    {
    	
    	// for debugging interest:
/*    	System.out.println(Arrays.toString(actualObs));
    	System.out.println(Arrays.toString(obs));
    	System.out.println(Arrays.toString(states));
		
    	System.out.println(start_p);
    	System.out.println(trans_p);
    	System.out.println(emit_p);
        System.out.println(conf_p);
    	
    	for (ObjectSimulation2 e : trueObjects) {
    		System.out.print(e + " ");
    	}
    	System.out.println("---");
    	for (String s : trueStates) {
    		System.out.print(s + " ");
    	}
    	System.out.println("---");*/
    	
    	
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
    	//System.out.println("states[] is: " + Arrays.toString(states));
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

        // just for debugging
/*        for (int n = 0; n < obs_num+1; n++)
        	System.out.println(Arrays.toString(V[n]));
        for (int n = 0; n < obs_num+1; n++)
        	System.out.println(Arrays.toString(B[n]));
        for (int n = 0; n < obs_num+1; n++) 
        	System.out.println(Arrays.toString(X[n]));*/
		
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
       	
/*        System.out.println("\n*************************************\n");         
        System.out.println(Arrays.toString(actualObs));
        
        for (int x = 0; x < obs_num+1; x++)
        {
        	System.out.println("state: " + path[x] + 
        			", with the object: " + objects[x]);
        }
        System.out.println("\n*************************************\n");*/
        
        // report the results of fidelity
        reportFidelity(actualObs, path, objects, trueObjects, trueStates);
        
/*        System.out.println("Purely for the sake of debugging");*/
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
    	// sensorStateScore is the score of states of my ASR.
    	double sensorStateScore = 0;
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
			// nodeID = objID / objPerNode by using this formula, we check the se state score
    		if (actualObs[i].objectID/graphGen.objectPerNode == Integer.parseInt(trueStates.get(i))) {
    			sensorStateScore += 1;
    		}
    	}
    	
    	// 
        myWordPercentage =  ((double)myObjectScore/groundObjectScore);
        asrWordPercentage = ((double)sensorObjectScore/groundObjectScore);
        myStatePercentage = ((double)myStateScore/groundStateScore);
        asrStatePercentage = ((double)sensorStateScore/groundStateScore);
    	
        
    	System.out.println("myObjectScore: " + myWordPercentage);
    	System.out.println("sensorObjectScore: " + asrWordPercentage);
    	System.out.println("myStateScore: " + myStatePercentage);      	
    	System.out.println("asrStatePercentage: " + asrStatePercentage);  
    }
    
    public static Hashtable<ObjectSimulation2, Hashtable<ObjectSimulation2, Double>> 
    	confusionGen(ObjectSimulation2[] obs, ObjectSimulation2[] trueObjectSet) throws IOException, InterruptedException
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
    	
    	Enumeration<ObjectSimulation2> obsObejct;	
    	obsObejct = confusion_probability.keys();
        while(obsObejct.hasMoreElements()) {
           ObjectSimulation2 key = (ObjectSimulation2) obsObejct.nextElement();
           System.out.println(key + ": " +
        		   confusion_probability.get(key));
        }
        /*
           for(ObjectSimulation2 obsObject: confusion_probability.keySet()) {
            	System.out.println("obsObject " + obsObject + ":");
            	for(ObjectSimulation2 trueObject : confusion_probability.get(obsObject).keySet())
            		System.out.println(trueObject + ":" + confusion_probability.get(obsObject).get(trueObject));
            }*/
		return confusion_probability;
    }
    
    // new version of confusion_probability
    // the objects within the same state are going to have very close confusion probability
    
    public static Hashtable<ObjectSimulation2, Hashtable<ObjectSimulation2, Double>> 
	newConfusionGen(ObjectSimulation2[] obs, ObjectSimulation2[] trueObjectSet, int objNumPerNodeVal) throws IOException, InterruptedException
	{
		// Due to the assumption that we assume all the objects evenly divide the misclassification probability, the 
		// misclassification and the inverted misclassification matrix are the same.
		Hashtable<ObjectSimulation2, Hashtable<ObjectSimulation2, Double>> confusion_probability = 
				new Hashtable<ObjectSimulation2, Hashtable<ObjectSimulation2, Double>>();
		for (ObjectSimulation2 obsObject : obs) {
			Hashtable<ObjectSimulation2, Double> c = new Hashtable<ObjectSimulation2, Double>();
			for (ObjectSimulation2 trueObject : trueObjectSet) {
				double similarityIndex;
				double inStateSimilarity = (1-graphGen.recall)/(objNumPerNodeVal-1);
				if (obsObject.objectID == trueObject.objectID) {
					similarityIndex = graphGen.recall;
				} else if (withinSameState(obsObject.objectID, trueObject.objectID, objNumPerNodeVal)) { // within the same state but different objects
					similarityIndex = inStateSimilarity;
				} else { // not within the same states
					// TODO maybe make it a little higher than 0
					similarityIndex = 0;
				}
				c.put(trueObject, similarityIndex);
			}
			confusion_probability.put(obsObject, c);
		}
		
    	Enumeration<ObjectSimulation2> obsObejct;	
    	obsObejct = confusion_probability.keys();
        while(obsObejct.hasMoreElements()) {
           ObjectSimulation2 key = (ObjectSimulation2) obsObejct.nextElement();
           System.out.println(key + ": " +
        		   confusion_probability.get(key));
        }
        
		return confusion_probability;
	}
    
    // detect whether two objects are in the same state
    public static boolean withinSameState(int ID1, int ID2, int objPerNode) { 
    	if (ID1 != ID2) {
    		int node_1_ID = (ID1-(ID1%objPerNode))/objPerNode;
    		int node_2_ID = (ID2-(ID2%objPerNode))/objPerNode;
    		if ( node_1_ID == node_2_ID ) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /* 
     * function function: generate the confusion matrix directly, instead of using the rules to do classification and probability lookup
     * 
     * inStateProb represents the total probability that the objects are sharing within one state. For instance, if the recall is 0.6, 
     * the inStateProb is 0.3, then the objects that are not within the same states (intra-state) are sharing 0.1.
     * 
     * confusionMatrix[i][j] represents at [i][j], the max total probability so far. e.g. k = 5. For i = 18, which is the 18th row:
     * 15      16     17     18     19     20     21     22     23     24     25
     * 0.075   0.15   0.225  0.825  0.9    0.92   0.94   0.96   0.98   1.0    0.0
     * 
     * This design provides convenience for classification, though it adds a little inconvenience to the similarity lookup, it even creates a
     * special case for the entry like 25, which is handled by newConfusionGen(...).
     * 
     * k other intra-state objects to share (1-recall-inStateProb), where k is 'objPerNode'.
     */
    
    public static double[][][] confusionMatrixGen(int nodeNum, int objPerNode, double recall, double inStateProb) {
    	int objNum = nodeNum * objPerNode;
    	int k = objPerNode;
    	// average probability each in-state object is sharing
    	double avgInStateProb = (double) inStateProb/(k - 1);
    	// average probability each intra-state object is sharing
    	double avgOutStateProb = (double) (1- recall - inStateProb)/k;
    	double[][] confusionMatrix = new double[objNum][objNum];
    	// non-cumulative confusion matrix
    	double[][] invertedMatrix = new double[objNum][objNum];
    	double[][][] res = new double[2][][];
    	
    	for (int i = 0; i < objNum; i++) {
    		/*
    		 * the k intra-state objects are the tailing k entries in the confusionMatrix.
    		 * when it comes to the end of the confusionMatrix, the tailing ones will wrind up and begin from the 0-index element,
    		 * which saves the codes and also maintains the symmetry of the confusion matrix.
    		 */
    		
    		int offset = i%k;
    		// startID is the starting index
    		int startID = i - offset;
    		// sum is the cumulative probability at the current position
			double sum = 0;
    		// first deal with in-state
			for (int j = 0; j < k; j++) {
				if (i == (j + startID)) { // the current element is the true object
					sum += recall;
					invertedMatrix[i][startID + j] = recall;
				} else {
					sum += avgInStateProb;
					invertedMatrix[i][startID + j] = avgInStateProb;
				}
				confusionMatrix[i][startID + j] = sum;
				
			}
			// second, deal with the intra-state
			for (int j = 0; j < k; j++) {
				sum += avgOutStateProb;
				confusionMatrix[i][(startID + k + j)%objNum] = sum;
				invertedMatrix[i][(startID + k + j)%objNum] = avgOutStateProb;
			}
    	}
    	
    	// print the confusionMatrix for debugging
/*    	for (int i = 0; i < objNum; i++) {
    		System.out.println(Arrays.toString(confusionMatrix[i]));
    	}
    	
    	for (int i = 0; i < objNum; i++) {
    		System.out.println(Arrays.toString(invertedMatrix[i]));
    	}*/
    	
    	res[0] = confusionMatrix;
    	res[1] = invertedMatrix;
		return res;
    }
    
    /*
     * function overloading: return the confusion probability based on the results of confusionMatrixGen
     */
    public static Hashtable<ObjectSimulation2, Hashtable<ObjectSimulation2, Double>> 
	newConfusionGen(double[][] invertedMatrix, ObjectSimulation2[] obs, ObjectSimulation2[] trueObjectSet, int nodeNum, int objPerNode) throws IOException, InterruptedException
	{
    	/*
    	 * Due to the way the confusionMatrix is constructed, the misclassification and the inverted misclassification matrix are no longer the same!!!
    	 * That is why we use invertedMatrix!!!
    	 * 
    	 */
    	
    	int objNum = nodeNum * objPerNode;
		Hashtable<ObjectSimulation2, Hashtable<ObjectSimulation2, Double>> confusion_probability = 
				new Hashtable<ObjectSimulation2, Hashtable<ObjectSimulation2, Double>>();
		for (ObjectSimulation2 obsObject : obs) {
			Hashtable<ObjectSimulation2, Double> c = new Hashtable<ObjectSimulation2, Double>();
			int objID = obsObject.objectID;
			for (ObjectSimulation2 trueObject : trueObjectSet) {
				double similarityIndex;
				// pay attention that when we look up in the invertedMatrix, the row is trueObject.objectID and the column is objID.
				similarityIndex = invertedMatrix[trueObject.objectID][objID];
				c.put(trueObject, similarityIndex);
			}
			confusion_probability.put(obsObject, c);
		}
		
/*    	Enumeration<ObjectSimulation2> obsObejct;	
    	obsObejct = confusion_probability.keys();
        while(obsObejct.hasMoreElements()) {
           ObjectSimulation2 key = (ObjectSimulation2) obsObejct.nextElement();
           System.out.println(key + ": " +
        		   confusion_probability.get(key));
        }*/
        
		return confusion_probability;
	}
    
}

