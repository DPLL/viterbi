import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import org.jgrapht.VertexFactory;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.alg.FloydWarshallShortestPaths;
import org.jgrapht.generate.RandomGraphGenerator;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.ClassBasedVertexFactory;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;


public class GraphGenSimulation  implements Serializable {
	
	AbstractBaseGraph<VertexSimulation, DefaultWeightedEdge> randomGraph;
	VertexFactory<VertexSimulation> vFactory;
	
	// number of edges
	int numEdge;
	// density of the DAG
	double density;
	// number of objects per node
	// It is assumed that the objectPerNode is the same across different nodes.
	int objectPerNode;
	// the number of dimension of objects
	int dimension;
	// ground truth of states
	ArrayList<String> trueStates;
	// ground truth of objects
	ArrayList<double[]> trueObjects;
	// noiseAddedResults is the trueObjects with noise added
	ArrayList<double[]> noiseAddedResults;
	// number of vertex
	int numVertex;
	// the value of the coordinates can vary within [0, range)
	double range; 
    /*
     * stat for noise adding
     */
    // mean is the average of sensor error
    double mean;
    // stdDev is the standard deviation of sensor error
    double stdDev;
	
	
	GraphGenSimulation() 
	{
	}
	
	public AbstractBaseGraph<VertexSimulation, DefaultWeightedEdge> GraphGen(double densityOfGraph, int objectNumPerNode, 
			int n, int nodeNum, double rangeValue, double meanValue, double stdDvValue) throws FileNotFoundException 
	{
		density = densityOfGraph;
		objectPerNode = objectNumPerNode;
		numVertex = nodeNum;
		range = rangeValue;
		// cast numEdge to integer
		numEdge = (int) (density * numVertex * (numVertex-1));
		System.out.println("numEdge is: " + numEdge);
		dimension = n;
		mean = meanValue;
		stdDev = stdDvValue;
		
        // Create the simple directed weighted graph object; it is null at this point
		// A simple directed graph is a non-simple directed graph in which neither multiple edges between any two vertices nor loops are permitted. 
    	randomGraph = new SimpleDirectedWeightedGraph<VertexSimulation, DefaultWeightedEdge>(DefaultWeightedEdge.class);   	
        // Create the randomGraphGenerator object
    	RandomGraphGenerator<VertexSimulation, DefaultWeightedEdge> randomGenerator = 
    			new RandomGraphGenerator<VertexSimulation, DefaultWeightedEdge>(numVertex, numEdge);
        // Create the VertexFactory so the generator can create vertices
        vFactory = new ClassBasedVertexFactory<VertexSimulation>(VertexSimulation.class);
        // Use the randomGraphGenerator object to make randomGraph a random graph with [numVertex] number of vertices
        randomGenerator.generateGraph(randomGraph, vFactory, null);
        
        // update the information for each node in the graph by IDing and assigning objects to them.
        Set<VertexSimulation> vertices1 = new HashSet<VertexSimulation>();
        vertices1.addAll(randomGraph.vertexSet());
        Integer counter = 0;
        for (VertexSimulation ver : vertices1) {
            replaceVertexID(ver, counter++);
        }       
        
        // Output all the vertexes and the edges
        System.out.println("The randomGraph.vertexSet() is: "+ randomGraph.vertexSet().toString());
        Iterator<VertexSimulation> iter2 = new DepthFirstIterator<VertexSimulation, DefaultWeightedEdge>(randomGraph);
        VertexSimulation ver2;
        while (iter2.hasNext()) {
        	ver2 = iter2.next();
            System.out.println("VertexSimulation " + ver2.toString() + " is connected to: "
                    + randomGraph.edgesOf(ver2).toString());
        }
        return randomGraph;
	}
	
	/*
	 * Find the first path with specified depth 
	 */
	public boolean DFS(int depth, VertexSimulation v, ArrayList<VertexSimulation> path) 
	{
		if (v == null)
			return false;
		if (depth == 0) {
			System.out.println(path);
			return true;
		}
		
		Set<DefaultWeightedEdge> outgoingEdges;
		outgoingEdges = randomGraph.outgoingEdgesOf(v);
		if (outgoingEdges.isEmpty()) {
			return false;
		} else {
			for (DefaultWeightedEdge e: outgoingEdges) {
				VertexSimulation ver = randomGraph.getEdgeTarget(e);
				path.add(ver);
				if (DFS(depth-1, ver, path))
					break;
				int size = path.size();
				path.remove(size-1);
			}
			return true;
		}
	}
	
	public ArrayList<VertexSimulation> findPath(int pathLength)
	{
		Set<VertexSimulation> vertexSet = randomGraph.vertexSet();
		ArrayList<VertexSimulation> path;
		for (VertexSimulation v : vertexSet) {
			path = new ArrayList<VertexSimulation>();
			if (DFS(pathLength, v, path))
				if (path.size() == pathLength)
					return path;
		}
		return null;
	}
	
	 /*
     * Find the diameter of the graph, and thus find a proper source and destination pair
     */
//	public ArrayList<DefaultWeightedEdge> findDiameter()
	public ArrayList<VertexSimulation> findDiameterInVertex()
	{
		// ArrayList of the diameter
		ArrayList<DefaultWeightedEdge> diameterPath;
		// ArrayList of the diameter in vertex form
		ArrayList<VertexSimulation> diameterPathInVertex;
		
		// see whether the graph is ready 
		//if (randomGraph == null || vFactory == null) {
		if (randomGraph == null) {
			System.out.println("The graph is not ready!");
			System.exit(-1);
		}
		
		VertexSimulation startVertex = new VertexSimulation();
        VertexSimulation endVertex = new VertexSimulation();
        ///The diameter of the graph
        double diameter;
    
        //System.out.println("Number of vertex is: "+ randomGraph.vertexSet().size());    
        FloydWarshallShortestPaths<VertexSimulation, DefaultWeightedEdge> shortestPaths = new FloydWarshallShortestPaths<VertexSimulation, DefaultWeightedEdge>(randomGraph);
        diameter = shortestPaths.getDiameter();
        //System.out.println("diameter of the graph is: "+ diameter);
        
        // Find the source and the destination node
        Iterator<VertexSimulation> nodeIterator1 = randomGraph.vertexSet().iterator();
        double maxShortestPath = 0;
        //int find = 0;
        int nodeCntr = 0;
        while (nodeIterator1.hasNext()) {
        	VertexSimulation vet1 = nodeIterator1.next();
        	//System.out.println("vet1 is: " + vet1);
        	Iterator<VertexSimulation> nodeIterator2 = randomGraph.vertexSet().iterator();
            while (nodeIterator2.hasNext()) {
            	VertexSimulation vet2 = nodeIterator2.next();
            	//System.out.println("vet2 is " + vet2);
            	if (vet2.vertexID != vet1.vertexID) {
            		nodeCntr++;
	            	double shortestPathDis = shortestPaths.shortestDistance(vet1, vet2);
	            	//System.out.println("The current shortestPathDis is: " + shortestPathDis);
	            	//if (shortestPathDis > maxShortestPath && shortestPathDis < numVertex*costBound) {//This is only valid when the link cost is not 1
            		if (shortestPathDis > maxShortestPath && shortestPathDis < numVertex*1) {
	            		startVertex = vet1;
	            		endVertex = vet2;
	            		maxShortestPath = shortestPathDis;
	            	}
            	}
            }
        } 

        //System.out.println("It has traversed " +  nodeCntr + " edges");
        //System.out.println("startVertex is: "+ startVertex.toString() + "!!!");
        //System.out.println("endVertex is: "+ endVertex.toString() + "!!!");
        //System.out.println("The current maxShorestPath is: " + maxShortestPath);
        // Sanity Check 1: see whether the diameter I got is the same with diameter = shortestPaths.getDiameter();
        if(diameter != maxShortestPath) {
        	System.out.println("diameter does not match!!!");
        	System.exit(-1);
        }
        // Sanity Check 2: see whether the startVertex and endVertex meet the requirement of diameter
        DijkstraShortestPath<VertexSimulation, DefaultWeightedEdge> dijkstraPath = 
        		new DijkstraShortestPath<VertexSimulation, DefaultWeightedEdge>(randomGraph, startVertex, endVertex);
        if(dijkstraPath.getPathLength() != maxShortestPath) {
        	System.out.println("startVertex and EndVertex cannot meet the requirement of diameter!!!");
        	System.exit(-1);
        }        

        System.out.println(dijkstraPath.getPathEdgeList());
        diameterPath = (ArrayList<DefaultWeightedEdge>) dijkstraPath.getPathEdgeList();
        
        // manipulate the diameterPath and extract the vertex array from it
        diameterPathInVertex = new ArrayList<VertexSimulation>();
        int count = 0;
        for (DefaultWeightedEdge e : diameterPath) {
			if (count++ == 0) { 
				diameterPathInVertex.add(randomGraph.getEdgeSource(e));
			} 
			diameterPathInVertex.add(randomGraph.getEdgeTarget(e));
		}
        
        //return diameterPath;
        return diameterPathInVertex;
	}
	
	 /*
     * Find the diameter of the graph, and thus find a proper source and destination pair
     */
	public ArrayList<DefaultWeightedEdge> findDiameter()
	{
		// ArrayList of the diameter
		ArrayList<DefaultWeightedEdge> diameterPath;
		// ArrayList of the diameter in vertex form
		ArrayList<VertexSimulation> diameterPathInVertex;
		
		// see whether the graph is ready 
		//if (randomGraph == null || vFactory == null) {
		if (randomGraph == null) {
			System.out.println("The graph is not ready!");
			System.exit(-1);
		}
		
		VertexSimulation startVertex = new VertexSimulation();
        VertexSimulation endVertex = new VertexSimulation();
        ///The diameter of the graph
        double diameter;
    
        //System.out.println("Number of vertex is: "+ randomGraph.vertexSet().size());    
        FloydWarshallShortestPaths<VertexSimulation, DefaultWeightedEdge> shortestPaths = new FloydWarshallShortestPaths<VertexSimulation, DefaultWeightedEdge>(randomGraph);
        diameter = shortestPaths.getDiameter();
        //System.out.println("diameter of the graph is: "+ diameter);
        
        // Find the source and the destination node
        Iterator<VertexSimulation> nodeIterator1 = randomGraph.vertexSet().iterator();
        double maxShortestPath = 0;
        //int find = 0;
        int nodeCntr = 0;
        while (nodeIterator1.hasNext()) {
        	VertexSimulation vet1 = nodeIterator1.next();
        	//System.out.println("vet1 is: " + vet1);
        	Iterator<VertexSimulation> nodeIterator2 = randomGraph.vertexSet().iterator();
            while (nodeIterator2.hasNext()) {
            	VertexSimulation vet2 = nodeIterator2.next();
            	//System.out.println("vet2 is " + vet2);
            	if (vet2.vertexID != vet1.vertexID) {
            		nodeCntr++;
	            	double shortestPathDis = shortestPaths.shortestDistance(vet1, vet2);
	            	//System.out.println("The current shortestPathDis is: " + shortestPathDis);
	            	//if (shortestPathDis > maxShortestPath && shortestPathDis < numVertex*costBound) {//This is only valid when the link cost is not 1
            		if (shortestPathDis > maxShortestPath && shortestPathDis < numVertex*1) {
	            		startVertex = vet1;
	            		endVertex = vet2;
	            		maxShortestPath = shortestPathDis;
	            	}
            	}
            }
        } 

        //System.out.println("It has traversed " +  nodeCntr + " edges");
        //System.out.println("startVertex is: "+ startVertex.toString() + "!!!");
        //System.out.println("endVertex is: "+ endVertex.toString() + "!!!");
        //System.out.println("The current maxShorestPath is: " + maxShortestPath);
        // Sanity Check 1: see whether the diameter I got is the same with diameter = shortestPaths.getDiameter();
        if(diameter != maxShortestPath) {
        	System.out.println("diameter does not match!!!");
        	System.exit(-1);
        }
        // Sanity Check 2: see whether the startVertex and endVertex meet the requirement of diameter
        DijkstraShortestPath<VertexSimulation, DefaultWeightedEdge> dijkstraPath = 
        		new DijkstraShortestPath<VertexSimulation, DefaultWeightedEdge>(randomGraph, startVertex, endVertex);
        if(dijkstraPath.getPathLength() != maxShortestPath) {
        	System.out.println("startVertex and EndVertex cannot meet the requirement of diameter!!!");
        	System.exit(-1);
        }        
        
        System.out.println(dijkstraPath.getPathEdgeList());
        diameterPath = (ArrayList<DefaultWeightedEdge>) dijkstraPath.getPathEdgeList();
        
        return diameterPath;
	}
	
	// TODO
	// setGroundTruth will return true if it sets the states where the start vertex has incoming edges
	public boolean setGroundTruth(List<DefaultWeightedEdge> pathEdgeList)
	{
		trueStates = new ArrayList<String>();
		trueObjects = new ArrayList<double[]>();
		
		// specify the states that have been gone through
		// Version one: select the first object from the objectlist
		int count = 0;
		for (DefaultWeightedEdge e : pathEdgeList) {
			if (count++ == 0) { // this means it is the first state
				// check whether the source has incoming edges
				//if (!hasIncomingEdge(randomGraph.getEdgeSource(e))) { // if it does not have incoming edges
				//	return false;
				//}
				trueStates.add(Integer.toString(randomGraph.getEdgeSource(e).vertexID));
				trueObjects.add(randomGraph.getEdgeSource(e).objectMatrix[0]);
			} 
			trueStates.add(Integer.toString(randomGraph.getEdgeTarget(e).vertexID));
			trueObjects.add(randomGraph.getEdgeTarget(e).objectMatrix[0]);
		}
		System.out.println("The trueStates is: " + trueStates);
		System.out.println("The trueObjects is as the following:");
		for(double[] arr : trueObjects)
			System.out.println(Arrays.toString(arr));

		return true;
	}
	
	// setGroundTruthInVertex will return true if it sets the states where the start vertex has incoming edges
	// Notice that the difference between 'setGroundTruthInVertex' and 'setGroundTruth' is that setGroundTruthInVertex takes the pathList in the form of vertex list 
	public boolean setGroundTruthInVertex(List<VertexSimulation> pathVertexList)
	{
		trueStates = new ArrayList<String>();
		trueObjects = new ArrayList<double[]>();
		
		// specify the states that have been gone through
		// Version one: select the first object from the objectlist
		for (VertexSimulation v : pathVertexList) {
			trueStates.add(Integer.toString(v.vertexID));
			trueObjects.add(v.objectMatrix[0]);
		}
		System.out.println("The trueStates is: " + trueStates);
		System.out.println("The trueObjects is as the following:");
		for(double[] arr : trueObjects)
			System.out.println(Arrays.toString(arr));
		return true;
	}
	
	
    public boolean replaceVertexID(VertexSimulation oldVertex, Integer id)
    {
    	VertexSimulation newVertex;
    	
        if((oldVertex == null) || (id == null)) {
        	System.out.println("Error in replacement of nodes");
            return false;
        }
        Set<DefaultWeightedEdge> relatedEdges = randomGraph.edgesOf(oldVertex);
        
        // objects is the objectList for this particular node
        double[][] objects = new double[objectPerNode][dimension];
        
        for(int i = 0; i < objectPerNode; i++) {
        	for(int j = 0; j < dimension; j++) {
        		objects[i][j] = Math.random() * range;
        	}
        }
        for (double[] row : objects)
        	System.out.println(Arrays.toString(row));
        
    	newVertex = new VertexSimulation(id, objectPerNode, dimension, objects);
        
        randomGraph.addVertex(newVertex);

        VertexSimulation sourceVertex;
        VertexSimulation targetVertex;
        DefaultWeightedEdge addedEdge;
        for (DefaultWeightedEdge e : relatedEdges) {
            sourceVertex = randomGraph.getEdgeSource(e);
            targetVertex = randomGraph.getEdgeTarget(e);
            if (sourceVertex.equals(oldVertex)
                && targetVertex.equals(oldVertex))
            {
            	addedEdge = randomGraph.addEdge(newVertex, newVertex);
            } else {
                if (sourceVertex.equals(oldVertex)) {
                	addedEdge = randomGraph.addEdge(newVertex, targetVertex);
                } else {
                	addedEdge = randomGraph.addEdge(sourceVertex, newVertex);
                }
            }
            // Set the weight of the graph edge.
            //randomGraph.setEdgeWeight(addedEdge, uniformGen(costBound));
            randomGraph.setEdgeWeight(addedEdge, 1.0);
        }
        randomGraph.removeVertex(oldVertex);
        return true;
    }
	
    /*
     * uniformGen generates a random double number randomly distributed
     * in the range: 1...upperBound+1.
     */
    public double uniformGen(double upperBound) {
    	Random randomGen = new Random();
    	double uniRandomInt = randomGen.nextDouble()*upperBound + 1;
    	//System.out.println("uniRandomInt is: " + uniRandomInt);
    	//System.out.println("Int uniRandomInt is: " + (int)uniRandomInt);
    	return uniRandomInt;
    }
    
    public boolean hasIncomingEdge(VertexSimulation ver) {
    	if (randomGraph.incomingEdgesOf(ver).isEmpty()) {
    		System.out.println("The start vertex has no incoming edges!");
    		return false;
    	}
    	return true;
    }
    
    public ArrayList<double[]> addNoise()
    {
    	noiseAddedResults = new ArrayList<double[]>();
    	Random fRandom = new Random();
        for(double[] obj: trueObjects) {
        	// assume that each dimension of the object is subject to error that is i.i.d.
        	double[] objNoiseAdded = new double[obj.length];
        	for(int i = 0; i < obj.length; i++) {
        		// added Gaussian noise with the distribution of N(mean, stdDev^2) to obj\
        		double error = fRandom.nextGaussian()*stdDev + mean;
        		System.out.println("error is: " + error);
        		objNoiseAdded[i] = obj[i] + error;            		
        	}
        	noiseAddedResults.add(objNoiseAdded);
        }
        
        System.out.println("noiseAddedResults is as follows");
        for(double[] arr : noiseAddedResults)
        	System.out.println(Arrays.toString(arr));
		return noiseAddedResults;
    }
    
	public static void main(String [] args) 
	{

		/*
		if (args.length != 3) {
			System.out.println("Correct Usage: ./GraphGenerator [density] [objectPerNode] [objectLength]");
			System.exit(-1);
		}
		
		density = Double.parseDouble(args[0]);
		objectPerNode = Integer.parseInt(args[1]);
		objectLength = Integer.parseInt(args[2]);
		// cast numEdge to integer
		numEdge = (int) (density * numVertex * (numVertex-1));
			
		*/
		
/*		AbstractBaseGraph<VertexSimulation, DefaultWeightedEdge> graph = null;
		ArrayList<DefaultWeightedEdge> diameterPath = new ArrayList<DefaultWeightedEdge>();
		GraphGenSimulation test = new GraphGenSimulation();
		try {
			graph = test.GraphGen(0.5, 2, 5, 3, 100, 0 , 10);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(graph.toString());
		System.out.println(graph.edgeSet().size());
		diameterPath = test.findDiameter();
		test.setGroundTruth(diameterPath);*/
		
		/*
		 * Serialize the graphGen
		 */
		GraphGenSimulation graphGen1 = new GraphGenSimulation();
		AbstractBaseGraph<VertexSimulation, DefaultWeightedEdge> graph1 = null;
		ArrayList<DefaultWeightedEdge> diameterPath1 = new ArrayList<DefaultWeightedEdge>();
		ArrayList<VertexSimulation> diameterPath1InVertex = new ArrayList<VertexSimulation>();
		try {
			//[densityOfGraph] [objectNumPerNode] [dimension] [nodeNum] [rangeValue] [meanValue] [stdDvValue]
			graph1 = graphGen1.GraphGen(0.5, 2, 5, 10, 100, 0, 0);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(graph1.toString());
		System.out.println(graph1.edgeSet().size());
		diameterPath1InVertex = graphGen1.findDiameterInVertex();
		graphGen1.setGroundTruthInVertex(diameterPath1InVertex);
		diameterPath1 = graphGen1.findDiameter();
		graphGen1.setGroundTruth(diameterPath1);
		
		if (graphGen1.findPath(4) == null) 
		{
			System.out.println("could not find such path!");
		}
		graphGen1.setGroundTruthInVertex(graphGen1.findPath(4));
		//graphGen1.addNoise();
		
/*		GraphGenSimulation graphGen2 = new GraphGenSimulation();
		AbstractBaseGraph<VertexSimulation, DefaultWeightedEdge> graph2 = null;
		ArrayList<DefaultWeightedEdge> diameterPath2 = new ArrayList<DefaultWeightedEdge>();
		try {
			graph2 = graphGen2.GraphGen(0.5, 2, 1, 3, 10, 0, 1);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(graph2.toString());
		System.out.println(graph2.edgeSet().size());
		diameterPath2 = graphGen2.findDiameter();
		graphGen2.setGroundTruth(diameterPath2);
		graphGen2.addNoise();
		
		GraphGenSimulation graphGen3 = new GraphGenSimulation();
		AbstractBaseGraph<VertexSimulation, DefaultWeightedEdge> graph3 = null;
		ArrayList<DefaultWeightedEdge> diameterPath3 = new ArrayList<DefaultWeightedEdge>();
		try {
			graph3 = graphGen3.GraphGen(0.75, 2, 2, 5, 10, 0, 10);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(graph3.toString());
		System.out.println(graph3.edgeSet().size());
		diameterPath3 = graphGen3.findDiameter();
		graphGen3.setGroundTruth(diameterPath3);
		graphGen3.addNoise();*/
		
		try
		{
			ObjectOutputStream out1 = new ObjectOutputStream(new FileOutputStream("graph1.out"));
			out1.writeObject(graph1);
			out1.close();
			
/*			// output the second graphGen: graph3Nodes6Edge2Obj
			ObjectOutputStream out2 = new ObjectOutputStream(new FileOutputStream("graph2.out"));
			out2.writeObject(graphGen2);
			out2.close();
			
			// output the second graphGen: graph3Nodes6Edge2Obj
			ObjectOutputStream out3 = new ObjectOutputStream(new FileOutputStream("graph3.out"));
			out3.writeObject(graphGen3);
			out3.close();*/
		} catch(IOException i)
		{
			i.printStackTrace();
		}

	}
}
