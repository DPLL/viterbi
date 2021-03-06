/*
 * GraphGenSimulation.java & TranscriberSimulation.java & VertexSimulation.java
 * are in the same set.
 * 
 * TransciberSimulation generates simulation results based on distance similarity measurements of objects, 
 * where objects are represented by N-dimensional real-number objects.
 * Its corresponding graph generation class is 'GraphGenSimulation.class'.
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
 
public class TranscriberSimulation
{
    
    // graphGen is the randomGraph generator that generates a weigthed random graph
	static GraphGenSimulation graphGen;
	// noiseAddedSeq is the intermediate results with noise added
	static double[][] noiseAddedSeq;
	// classifiedSeq output of the sensor, and it is also the input to our algorithm
	static double[][] classfiedSeq;

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
/*    // confusion_probability stores the confusion probability matrix
	static Hashtable<double[], Hashtable<double[], Double>> confusion_probability;*/
	
    //  classificationMatrix stores the probability matrix for the sensor classifying
	static Hashtable<double[], Hashtable<double[], Double>> classificationMatrix;
    // similarityMatrix stores the similarity metric between the true objects
	static Hashtable<double[], Hashtable<double[], Double>> similarityMatrix;
	
    // myWordPercentage is the percentage of object that my correction algo. is right
	static double myWordPercentage;
    // myStatePercentage is the percentage of state that my correction algo. is right
	static double myStatePercentage;
    // asrWordPercentage is the percentage of object that ASR is right
	static double asrWordPercentage;
	
	// similairtyFunction power
	static int similarityPower = 5;

    
    public static final boolean DEBUG_MODE = false;
    
    public static void main(String[] args) throws IOException, InterruptedException 
    {
    	
    	//SimpleDirectedWeightedGraph<VertexSimulation, DefaultWeightedEdge> graph;
		AbstractBaseGraph<VertexSimulation, DefaultWeightedEdge> graph = null;
    	ArrayList<DefaultWeightedEdge> diameterPath = new ArrayList<DefaultWeightedEdge>();
		ArrayList<double[]> noiseAddedResult = new ArrayList<double[]>();	
		ArrayList<double[]> classifiedResult = new ArrayList<double[]>();	
		
    	if (DEBUG_MODE) { 		
			// read from the predefined class
			graphGen = new GraphGenSimulation();

			try {
				FileInputStream fileIn = new FileInputStream("graph1.out");
				ObjectInputStream in = new ObjectInputStream(fileIn);   			
				graph = (AbstractBaseGraph<VertexSimulation, DefaultWeightedEdge>) in.readObject();
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
			graphGen.avgDegree = 5;
			graphGen.objectPerNode = 2;
			graphGen.dimension = 1;
			graphGen.numVertex = 30;
			graphGen.range = 100;
			graphGen.mean = 0;
			graphGen.stdDev = 30;
			
			graphGen.numEdge = 6;

			diameterPath = graphGen.findDiameter();
			graphGen.setGroundTruth(diameterPath);
			
	    	ArrayList<double[]> randomNoise = new ArrayList<double[]>();
	    	Random fRandom = new Random();
            for(double[] obj: graphGen.trueObjects) {
            	// assume that each dimension of the object is subject to error that is i.i.d.
            	double[] objNoiseAdded = new double[obj.length];
            	for(int j = 0; j < obj.length; j++) {
            		// added Gaussian noise with the distribution of N(mean, stdDev^2) to obj
            		double error = fRandom.nextGaussian()*graphGen.stdDev + graphGen.mean;
            		//System.out.println("error is: " + error);
            		objNoiseAdded[j] = error;            		
            	}
            	randomNoise.add(objNoiseAdded);
            }	            
            System.out.println("randomNoise is as follows");
            for(double[] arr : randomNoise)
            	System.out.println(Arrays.toString(arr));
			
			// manually add noise
			double[][] noise = {{20}, {24}, {10}};
			//noiseAddedResult = graphGen.trueObjects;
			for (int j = 0; j < 3; j++) {
				double[] temp = new double[noise[0].length];
				for (int m = 0; m < noise[0].length; m++) {
					temp[m] = noise[j][m] + graphGen.trueObjects.get(j)[m];
				}
				noiseAddedResult.add(temp);
			}
			System.out.println("noiseAdded result is:");
	        for(double[] arr : noiseAddedResult)
	        	System.out.println(Arrays.toString(arr));
			noiseAddedSeq = new double[noiseAddedResult.size()][];
	        noiseAddedSeq = noiseAddedResult.toArray(noiseAddedSeq);
	        System.out.println(Arrays.deepToString(noiseAddedSeq));

    	} else {
    		
        	// runTime is how many runs in total
        	int runTime;
        	// graphNum is the number of graphs generated
        	int graphNum = 1000;
        	// runPerGraph is number of runs for each graph
        	int runPerGraph = 1;
        	runTime = graphNum * runPerGraph; 
        			
        	double totalMyWordPercentage = (double) 0.0;
        	double totalASRWordPercentage = (double) 0.0;
        	double totalMyStatePercentage = (double) 0.0;
        	double totalPathLength = 0.0;
    	
        	int count = 0;
    		for (int n = 0; n < graphNum; n++) {
	    	 	// Generate a random graph
	    		graphGen = new GraphGenSimulation();
	    		double pathLength;
	    		
	    		//keep generating graphs that meet the requirement of pathLength
	    		while (true) {
		    		// 1.[avgDegreeOfGraph] 2.[objectNumPerNode] 3.[dimension] 4.[nodeNum] 5.[rangeValue] 6.[meanValue] 7.[stdDvValue] 8.[pathLength]
					graph = graphGen.GraphGen(2, 3, 2, 30, 100, 0, 30, 8);
					System.out.println(graphGen.numVertex);
					System.out.println(graph.toString());
		    		/*
		    		 *  Graph Interface
		    		 */
		    		graphParse(graph);
					
		    		/*
		    		 * Set groundTruth
		    		 */
		    		// use the diameter as the ground truth
/*					diameterPath = graphGen.findDiameter();
					graphGen.setGroundTruth(diameterPath);
					pathLength = diameterPath.size() + 1;	
					break;	*/		
					// instead of choosing the diameter as the path, choose a path specified length 
					System.out.println(graphGen.pathLength);
					ArrayList<VertexSimulation> pathInVertex = graphGen.findPath(graphGen.pathLength);
					pathLength = graphGen.pathLength;
					
					if (!graphGen.setGroundTruthInVertex(pathInVertex)) 
					{
						System.out.println("could not find such path!");
						// Purely for testing the length of the path
						diameterPath = graphGen.findDiameter();
						if (diameterPath.size() >= graphGen.pathLength) {
							System.out.println("WHAT???");
							System.exit(-1);
						}
						System.out.println("BAD LUCK!!!");
					} else {
						// if such path could be found, then break out of while loop and keep on with the rest
						break;
					}
					
	    		}
	    		
	    		/*
	    		 * generate the similarityMatrix for my algorithm
	    		 */
	            similarityMatrix =	confustionGen(trueObjectSet, trueObjectSet);			
								
				for(int i = 0; i < runPerGraph; i++) {		
					count++;
					//Sensor simulator -- add noise to the trueObjects
					noiseAddedResult= graphGen.addNoise();
					noiseAddedSeq = new double[noiseAddedResult.size()][];
			        noiseAddedSeq = noiseAddedResult.toArray(noiseAddedSeq);
			        //System.out.println("noiseAddedSeq is: " + Arrays.deepToString(noiseAddedSeq));
			        
		    		/*
		    		 * generate the classificationMatrix
		    		 */
		            classificationMatrix =	confustionGen(noiseAddedSeq, trueObjectSet);
			        
			        //Sensor simulator -- classify
		            classifiedResult = graphGen.classify(classificationMatrix);
		            classfiedSeq = new double[classifiedResult.size()][];
		            classfiedSeq = classifiedResult.toArray(classfiedSeq);
		            //System.out.println("classfiedSeq is " + Arrays.deepToString(classfiedSeq));
		        
			        correct(classfiedSeq,
			        		trueObjectSet, states,
			                start_probability,
			                transition_probability,
			                emission_probability,
			                similarityMatrix,
			                graphGen.trueObjects, 
			                graphGen.trueStates
			                );
		        
		        
			        // Testing by feeding in the right objects directly without going through ASR
			        /*
			        double[][] trueObjectSeq = new double[graphGen.trueObjects.size()][];
			        trueObjectSeq = graphGen.trueObjects.toArray(trueObjectSeq);
			        System.out.println("The trueObjectSeq is as follows:");
			        for(double[] object : trueObjectSeq) {
			        	System.out.println(Arrays.toString(object));
			        }
			        Hashtable<double[], Hashtable<double[], Double>> confusion_probability =
			        		confustionGen(trueObjectSeq, trueObjectSet);
			        
			        correct(trueObjectSeq,
			        		trueObjectSet, states,
			                start_probability,
			                transition_probability,
			                emission_probability,
			                confusion_probability,
			                graphGen.trueObjects, 
			                graphGen.trueStates);*/
			
/*			        StringBuilder str = new StringBuilder();
			        for(int m = 0; m < graphGen.trueObjects.size(); m++) {
			        	str.append(Arrays.toString(graphGen.trueObjects.get(m)));
			        }			        
					System.out.println("The trueObjects is as follows:" + str);*/
					/*
					for(int j = 0; j < graphGen.trueObjects.size(); j++) {
						System.out.println(Arrays.toString(graphGen.trueObjects.get(j)));
					}*/
					System.out.println("The trueStates is: " + graphGen.trueStates);	
					
					totalMyWordPercentage += myWordPercentage;
					totalMyStatePercentage += myStatePercentage;
			    	totalASRWordPercentage += asrWordPercentage;
			    	totalPathLength += pathLength;
			  	
			    	System.out.println("in the " + count + " th interation, haha!!!!\n----------------");
				} // end for (within the same graph)
    		} // end for (between different graphs)
	    	System.out.println("count is: " + count);
	    	System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	    	System.out.println("myObjectScore: " + (double)totalMyWordPercentage/runTime);
	    	System.out.println("sensorObjectScore: " + (double)totalASRWordPercentage/runTime);
	    	System.out.println("myStateScore: " + (double)totalMyStatePercentage/runTime + "\n");      	
	    	System.out.println("averagePathLength: " + (double)totalPathLength/runTime);  
	    	System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	    	System.out.println("");
    	}   	
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
	        //System.out.println(trueObjectSetList);
	        trueObjectSet = new double[trueObjectSetList.size()][];
	        trueObjectSet = trueObjectSetList.toArray(trueObjectSet);
	        //System.out.println("print out the trueObejctSet as follows:");
	        //for(double[] object : trueObjectSet)
	        //	System.out.println(Arrays.toString(object));
	        //System.out.println(Arrays.deepToString(trueObjectSet));
	        //System.out.println(trueObjectSetList.size());
	        // print the emission_probability
	        //System.out.println("emission_probability: " + emission_probability);
/*			System.out.println("emission_probability: "); 
            for(String state: emission_probability.keySet()) {
            	System.out.println("State " + state + ":");
            	for(double[] object : emission_probability.get(state).keySet())
            		System.out.println(Arrays.toString(object) + ":" + emission_probability.get(state).get(object));
            }*/
	        // print the transition_probability
	        //System.out.println(transition_probability);
	    	return;
	    }
	    
	    /*
	    public static ArrayList<double[]> addNoise(GraphGenSimulation graphGen)
	    {
	    	ArrayList<double[]> noiseAddedResults = new ArrayList<double[]>();
	    	Random fRandom = new Random();
            for(double[] obj: graphGen.trueObjects) {
            	// assume that each dimension of the object is subject to error that is i.i.d.
            	double[] objNoiseAdded = new double[obj.length];
            	for(int i = 0; i < obj.length; i++) {
            		// added Gaussian noise with the distribution of N(mean, stdDev^2) to obj\
            		double error = fRandom.nextGaussian()*GraphGenSimulation.stdDev + GraphGenSimulation.mean;
            		System.out.println("error is: " + error);
            		objNoiseAdded[i] = obj[i] + error;            		
            	}
            	noiseAddedResults.add(objNoiseAdded);
            }
            
            System.out.println("noiseAddedResults is as follows");
            for(double[] arr : noiseAddedResults)
            	System.out.println(Arrays.toString(arr));
    		return noiseAddedResults;
	    }*/
    	
    	// actualObs is the initial result form ASR, i.e., Y; obs is the ground truth objects, i.e., X.
        public static void correct(double[][] actualObs, double[][] obs, String[] states,
                        Hashtable<String, Double> start_p,
                        Hashtable<String, Hashtable<String, Double>> trans_p,
                        Hashtable<String, Hashtable<double[], Double>> emit_p,
			Hashtable<double[], Hashtable<double[], Double>> conf_p,
			ArrayList<double[]> trueObjects, ArrayList<String> trueStates)
        {
        	// state_num is the number of different states
        	int state_num = states.length;
        	// obs_num is the number of objects from ASR
        	int obs_num = actualObs.length;
        	//V[t][i] stores the overall largest probability ending at the state of i at time t
        	double V[][] = new double[obs_num+1][state_num];
        	//B[t][i]  stores the last source state corresponding to the V[t][i]
        	int B[][] = new int[obs_num+1][state_num];
        	//X[t][i][] stores the object that has been chosen corresponding to the V[t][i]
        	double X[][][] = new double[obs_num+1][state_num][];
		

        	// PAY ATTENTION! We have to sort the states first so that int value of states will be 
        	// acting as index in the later steps.
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
        		X[0][Integer.parseInt(state)] = new double[graphGen.dimension];
        	}
        	
        	// t is the records the current time
        	int t = 0;
            for (double[] input : actualObs)
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
                    double[] v_object = new double[graphGen.dimension];	
                    // intermediate variable for calculating Pmax 
                    double v_prob = 1;       			
                    // x is the current accurate observation (object)	
                    for (double[] object : obs)		
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
            double objects[][] = new double[obs_num + 1][];
            
            path[obs_num]  = Smax;
            objects[obs_num] = X[obs_num][Smax];
            for (int x = obs_num-1; x >= 0; x--)
            {
            	path[x] = B[x+1][path[x+1]];
            	objects[x] = X[x][path[x]]; 
            }
           	
/*            System.out.println("\n*************************************\n");         
            System.out.println(Arrays.deepToString(actualObs));
            
            for (int x = 0; x < obs_num+1; x++)
            {
            	System.out.println("state: " + path[x] + 
            			", with the object: " + Arrays.toString(objects[x]));
            }
            System.out.println("\n*************************************\n");*/
            
            // report the results of fidelity
            reportFidelity(actualObs, path, objects, trueObjects, trueStates);
            
            //System.out.println("Purely for the sake of debugging");
        }
        
        /*
         * reportFidelity reports the speech recognition results and compare them. It is based on an important assumption that
         * ground truth objects and the actual observations have the same length, i.e., Sphinx does not miss any object.
         * actualObs[][] are the objects classified by the sensors, objects[][] are the output of my algorithm, trueObjects are the groundTruth objects 
         */
        public static void reportFidelity(double[][] actualObs, int[] path, double[][] objects, ArrayList<double[]> trueObjects, ArrayList<String> trueStates) 
        {
        	// misclassification_probability measures the misclassification probability, and it comes from confusion_probability, acutalObs and trueObjects
        	Hashtable<double[], Hashtable<double[], Double>> misclassification_probability = new Hashtable<double[], Hashtable<double[], Double>>();
        	// classifiedObjects is the output from the sensor after classification
        	ArrayList<double[]> classifiedObjects = new ArrayList<double[]>();
        	
        	// gourndStateScore is the score of ground truth state
        	int groundStateScore = trueStates.size();
        	// groundObjectScore is the score of ground truth objects
        	int groundObjectScore = trueObjects.size();
        	if (groundObjectScore != groundStateScore || objects.length != path.length) {
        		System.out.println("Error!!!");
        	}
        	if(groundObjectScore != (objects.length-1) || actualObs.length != groundObjectScore) {
        		System.out.println("The ground truth and the speech recognition results do not match!!!");
        	}
        	// compute the misclassification_prbability
/*        	for (int i = 0; i < actualObs.length; i++) {
        		Hashtable<double[], Double> c = new Hashtable<double[], Double>();
        		Hashtable<double[], Double> d = confusion_probability.get(actualObs[i]);
        		Enumeration<double[]> enumKey = d.keys();
        		// 'total' is the sum of all the values of hashtable d, used for normalization. 
        		Double total = (double) 0;
        		while(enumKey.hasMoreElements()) {
        			double[] key = enumKey.nextElement();
        			Double val = d.get(key);
        			total += val;
        		}
        		for (double[] key : d.keySet()) {
        			double misclassificationProb;

        			misclassificationProb = d.get(key)/total;
        			c.put(key, misclassificationProb);
        		}
        		misclassification_probability.put(trueObjects.get(i), c);
        	}
        	System.out.println("print out the misclassification_probability");
            for(double[] object: misclassification_probability.keySet()) {
            	System.out.println("object " + Arrays.toString(object) + ":");
            	for(double[] trueObject : misclassification_probability.get(object).keySet())
            		System.out.println(Arrays.toString(trueObject) + ":" + misclassification_probability.get(object).get(trueObject));
            }*/
        	
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
        		
        		if (objects[i+1] == trueObjects.get(i)) {
        			myObjectScore += 1;
        		}
        		// Calculate sensor score based on 'misclassification_probability'
        		//sensorObjectScore += misclassification_probability.get(trueObjects.get(i)).get(trueObjects.get(i));       		
        		// calculate sensor score based on actualObs[][]
        		if (actualObs[i] == trueObjects.get(i)) {
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
        
        public static Hashtable<double[], Hashtable<double[], Double>> 
        	confustionGen(double[][] obs, double[][] trueObjectSet) throws IOException, InterruptedException
        {
        	
    		Hashtable<double[], Hashtable<double[], Double>> confusion_probability = 
    				new Hashtable<double[], Hashtable<double[], Double>>();
        	for (double[] obsObject : obs) {
        		Hashtable<double[], Double> c = new Hashtable<double[], Double>();
        		for (double[] trueObject : trueObjectSet) {
        			double similarityIndex;
        			/*
        			double EDistance = computeEuclideanDistance(obsObject, trueObject);
        			double maxDistance = (double)(Math.sqrt(GraphGenSimulation.dimension)*(GraphGenSimulation.range + mean + stdDev*3));
        			similarityIndex = ( EDistance <= maxDistance ? (1 - ((double)EDistance/maxDistance)) : 0);
        			*/
        			similarityIndex = computeSimilarity(obsObject, trueObject);
        			c.put(trueObject, similarityIndex);
        		}
        		confusion_probability.put(obsObject, c);
        	}
        	
        	//System.out.println("Hi, I am here");
        	/*
        	Enumeration<double[]> obsObejct;	
        	obsObejct = confusion_probability.keys();
            while(obsObejct.hasMoreElements()) {
               double[] key = (double[]) obsObejct.nextElement();
               System.out.println(key + ": " +
            		   confusion_probability.get(key));
            }*/
            
/*            for(double[] obsObject: confusion_probability.keySet()) {
            	System.out.println("obsObject " + Arrays.toString(obsObject) + ":");
            	for(double[] trueObject : confusion_probability.get(obsObject).keySet())
            		System.out.println(Arrays.toString(trueObject) + ":" + confusion_probability.get(obsObject).get(trueObject));
            }*/
			return confusion_probability;
        }
        
        // computeSimilarity computes the similarity between two objects
        public static double computeSimilarity(double[] obsObject, double[] trueObject)
        {
			double similarityIndex;
			double EDistance = computeEuclideanDistance(obsObject, trueObject);
			double maxDistance = (double)(Math.sqrt(graphGen.dimension)*(graphGen.range + graphGen.mean + graphGen.stdDev*3));
			similarityIndex = ( EDistance <= maxDistance ? (1 - ((double)EDistance/maxDistance)) : 0);
			// make the similarity function non-linear
			similarityIndex = Math.pow(similarityIndex, similarityPower);
			return similarityIndex;
        }
        
        // Calculate the Euclidean distance between the observed object and true object  
        public static double computeEuclideanDistance(double[]obsObject, double[]trueObject)
        {
        	double EDistance = 0;
        	if(obsObject.length != trueObject.length) {
        		System.out.println("objects do not match!!!");
        		System.exit(-1);
        	}
        	for (int i = 0; i < obsObject.length; i++) {
        		EDistance += Math.pow((obsObject[i] - trueObject[i]), 2);
        	}
			return Math.sqrt(EDistance);
        }

}

