import java.io.File;
import java.io.FileNotFoundException;
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


public class GraphGenSimulation {
	
	static AbstractBaseGraph<VertexSimulation, DefaultWeightedEdge> randomGraph;
	VertexFactory<VertexSimulation> vFactory;
	
	// number of edges
	static int numEdge;
	// density of the DAG
	static double density;
	// number of objects per node
	// It is assumed that the objectPerNode is the same across different nodes.
	static int objectPerNode;
	// the number of dimension of objects
	static int dimension;
	// ground truth of states
	ArrayList<String> trueStates;
	// ground truth of objects
	ArrayList<double[]> trueObjects;
	// number of vertex
	static int numVertex = 10;
	// the value of the coordinates can vary within [0, range)
	static int range = 100; 

	
	
	GraphGenSimulation() 
	{
	}
	
	public AbstractBaseGraph<VertexSimulation, DefaultWeightedEdge> GraphGen(double densityOfGraph, int objectNumPerNode, int n) throws FileNotFoundException 
	{
		density = densityOfGraph;
		objectPerNode = objectNumPerNode;
		// cast numEdge to integer
		numEdge = (int) (density * numVertex * (numVertex-1));
		System.out.println("numEdge is: " + numEdge);
		dimension = n;
		
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
     * Find the diameter of the graph, and thus find a proper source and destination pair
     */
	public ArrayList<DefaultWeightedEdge> findDiameter()
	{
		// return the ArrayList of the diameter
		ArrayList<DefaultWeightedEdge> diameterPath;
		
		// see whether the graph is ready 
		if (randomGraph == null || vFactory == null) {
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
        
        /*
        // Traverse all the edges to see connection relationship.
        Set<DefaultWeightedEdge> edges = new HashSet<DefaultWeightedEdge>();
        edges.addAll(randomGraph.edgeSet());
        for (DefaultWeightedEdge edge : edges) {
            //Traverse all the edges in the edgeSet
        	System.out.println("source of the edge is: "+ randomGraph.getEdgeSource(edge));
        	System.out.println("target of the edge is: "+ randomGraph.getEdgeTarget(edge));
        	System.out.println("the weight of the edge is: "+ randomGraph.getEdgeWeight(edge));
        }
        */
	}
	
	
	// TODO
	// setGroundTruth will return true if it sets the states where the start vertex has incoming edges
	public boolean setGourdTruth(List<DefaultWeightedEdge> pathEdgeList)
	{
		trueStates = new ArrayList<String>();
		trueObjects = new ArrayList<double[]>();
		
		VertexSimulation startVer;
		// specify the states that have been gone through
		// Version one: select the first object from the objectlist
		int count = 0;
		for (DefaultWeightedEdge e : pathEdgeList) {
			if (count++ == 0) { // this means it is the first state
				// check whether the source has incoming edges
				if (!hasIncomingEdge(randomGraph.getEdgeSource(e))) { // if it does not have incoming edges
					return false;
				}
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
	
    public static boolean replaceVertexID(VertexSimulation oldVertex, Integer id)
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
    public static double uniformGen(double upperBound) {
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
    
	public static void main(String [] args) throws FileNotFoundException 
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
		AbstractBaseGraph<VertexSimulation, DefaultWeightedEdge> graph;
		ArrayList<DefaultWeightedEdge> diameterPath = new ArrayList<DefaultWeightedEdge>();
		GraphGenSimulation test = new GraphGenSimulation();
		graph = test.GraphGen(0.5, 2, 5);
		System.out.println(graph.toString());
		System.out.println(graph.edgeSet().size());
		diameterPath = test.findDiameter();
		test.setGourdTruth(diameterPath);

	}
}
