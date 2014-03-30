/*
 * This class generates objects and misclassification matrix. It does not depend on the distance measure
 */
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


public class SimulationGraph2  implements Serializable {
	
	AbstractBaseGraph<VertexSimulation2, DefaultWeightedEdge> randomGraph;
	VertexFactory<VertexSimulation2> vFactory;
	
	// number of edges
	int numEdge;
	// density of the DAG
	double density;
	// number of objects per node
	// It is assumed that the objectPerNode is the same across different nodes.
	int objectPerNode;
	// ground truth of states
	ArrayList<String> trueStates;
	// ground truth of objects
	ArrayList<ObjectSimulation2> trueObjects;
	// classifiedResults is the trueObjects with noise added
	ArrayList<ObjectSimulation2> classifiedResults;
	// number of vertex
	int numVertex;
	// recall represents the recall of the graph
	double recall;
	// errorGranularity is the probability of misclassification of one object
	double errorGranularity;
    // pathLength
    int pathLength; 
	
	
	SimulationGraph2() 
	{
	}
	
	public AbstractBaseGraph<VertexSimulation2, DefaultWeightedEdge> GraphGen(double densityOfGraph, int objectNumPerNode, 
			int nodeNum, double recallValue, int length) throws FileNotFoundException 
	{
		density = densityOfGraph;
		objectPerNode = objectNumPerNode;
		numVertex = nodeNum;
		// cast numEdge to integer
		numEdge = (int) (density * numVertex * (numVertex-1));
		System.out.println("numEdge is: " + numEdge);
		recall = recallValue;
		pathLength = length;
		
        // Create the simple directed weighted graph object; it is null at this point
		// A simple directed graph is a non-simple directed graph in which neither multiple edges between any two vertices nor loops are permitted. 
    	randomGraph = new SimpleDirectedWeightedGraph<VertexSimulation2, DefaultWeightedEdge>(DefaultWeightedEdge.class);   	
        // Create the randomGraphGenerator object
    	RandomGraphGenerator<VertexSimulation2, DefaultWeightedEdge> randomGenerator = 
    			new RandomGraphGenerator<VertexSimulation2, DefaultWeightedEdge>(numVertex, numEdge);
        // Create the VertexFactory so the generator can create vertices
        vFactory = new ClassBasedVertexFactory<VertexSimulation2>(VertexSimulation2.class);
        // Use the randomGraphGenerator object to make randomGraph a random graph with [numVertex] number of vertices
        randomGenerator.generateGraph(randomGraph, vFactory, null);
        
        // update the information for each node in the graph by IDing and assigning objects to them.
        Set<VertexSimulation2> vertices1 = new HashSet<VertexSimulation2>();
        vertices1.addAll(randomGraph.vertexSet());
        Integer counter = 0;
        for (VertexSimulation2 ver : vertices1) {
            replaceVertexID(ver, counter++);
        }       
        
        // Output all the vertexes and the edges
        System.out.println("The randomGraph.vertexSet() is: "+ randomGraph.vertexSet().toString());
        Iterator<VertexSimulation2> iter2 = new DepthFirstIterator<VertexSimulation2, DefaultWeightedEdge>(randomGraph);
        VertexSimulation2 ver2;
/*        while (iter2.hasNext()) {
        	ver2 = iter2.next();
            System.out.println("VertexSimulation2 " + ver2.toString() + " is connected to: "
                    + randomGraph.edgesOf(ver2).toString());
        }*/
        return randomGraph;
	}
	
	/*
	 * Find the first path with specified depth 
	 */
	public boolean DFS(int depth, VertexSimulation2 v, ArrayList<VertexSimulation2> path) 
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
				VertexSimulation2 ver = randomGraph.getEdgeTarget(e);
				if (ver.visit == false) {
					ver.visit = true;
					path.add(ver);
					if (DFS(depth-1, ver, path))
						break;
					int size = path.size();
					path.remove(size-1);
				}
			}
			return true;
		}
	}
	
	public ArrayList<VertexSimulation2> findPath(int pathLength)
	{
		Set<VertexSimulation2> vertexSet = randomGraph.vertexSet();
		ArrayList<VertexSimulation2> path;
		for (VertexSimulation2 v : vertexSet) {
			path = new ArrayList<VertexSimulation2>();
			if (DFS(pathLength, v, path))
				if (path.size() == pathLength)
					return path;
		}
		return null;
	}
	
	 /*
     * Find the diameter of the graph, and thus find a proper source and destination pair
     */
	public ArrayList<VertexSimulation2> findDiameterInVertex()
	{
		// ArrayList of the diameter
		ArrayList<DefaultWeightedEdge> diameterPath;
		// ArrayList of the diameter in vertex form
		ArrayList<VertexSimulation2> diameterPathInVertex;
		
		// see whether the graph is ready 
		//if (randomGraph == null || vFactory == null) {
		if (randomGraph == null) {
			System.out.println("The graph is not ready!");
			System.exit(-1);
		}
		
		VertexSimulation2 startVertex = new VertexSimulation2();
        VertexSimulation2 endVertex = new VertexSimulation2();
        ///The diameter of the graph
        double diameter;
    
        //System.out.println("Number of vertex is: "+ randomGraph.vertexSet().size());    
        FloydWarshallShortestPaths<VertexSimulation2, DefaultWeightedEdge> shortestPaths = new FloydWarshallShortestPaths<VertexSimulation2, DefaultWeightedEdge>(randomGraph);
        diameter = shortestPaths.getDiameter();
        //System.out.println("diameter of the graph is: "+ diameter);
        
        // Find the source and the destination node
        Iterator<VertexSimulation2> nodeIterator1 = randomGraph.vertexSet().iterator();
        double maxShortestPath = 0;
        //int find = 0;
        int nodeCntr = 0;
        while (nodeIterator1.hasNext()) {
        	VertexSimulation2 vet1 = nodeIterator1.next();
        	//System.out.println("vet1 is: " + vet1);
        	Iterator<VertexSimulation2> nodeIterator2 = randomGraph.vertexSet().iterator();
            while (nodeIterator2.hasNext()) {
            	VertexSimulation2 vet2 = nodeIterator2.next();
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
        DijkstraShortestPath<VertexSimulation2, DefaultWeightedEdge> dijkstraPath = 
        		new DijkstraShortestPath<VertexSimulation2, DefaultWeightedEdge>(randomGraph, startVertex, endVertex);
        if(dijkstraPath.getPathLength() != maxShortestPath) {
        	System.out.println("startVertex and EndVertex cannot meet the requirement of diameter!!!");
        	System.exit(-1);
        }        

        System.out.println(dijkstraPath.getPathEdgeList());
        diameterPath = (ArrayList<DefaultWeightedEdge>) dijkstraPath.getPathEdgeList();
        
        // manipulate the diameterPath and extract the vertex array from it
        diameterPathInVertex = new ArrayList<VertexSimulation2>();
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
		ArrayList<VertexSimulation2> diameterPathInVertex;
		
		// see whether the graph is ready 
		//if (randomGraph == null || vFactory == null) {
		if (randomGraph == null) {
			System.out.println("The graph is not ready!");
			System.exit(-1);
		}
		
		VertexSimulation2 startVertex = new VertexSimulation2();
        VertexSimulation2 endVertex = new VertexSimulation2();
        ///The diameter of the graph
        double diameter;
    
        //System.out.println("Number of vertex is: "+ randomGraph.vertexSet().size());    
        FloydWarshallShortestPaths<VertexSimulation2, DefaultWeightedEdge> shortestPaths = new FloydWarshallShortestPaths<VertexSimulation2, DefaultWeightedEdge>(randomGraph);
        diameter = shortestPaths.getDiameter();
        //System.out.println("diameter of the graph is: "+ diameter);
        
        // Find the source and the destination node
        Iterator<VertexSimulation2> nodeIterator1 = randomGraph.vertexSet().iterator();
        double maxShortestPath = 0;
        //int find = 0;
        int nodeCntr = 0;
        while (nodeIterator1.hasNext()) {
        	VertexSimulation2 vet1 = nodeIterator1.next();
        	//System.out.println("vet1 is: " + vet1);
        	Iterator<VertexSimulation2> nodeIterator2 = randomGraph.vertexSet().iterator();
            while (nodeIterator2.hasNext()) {
            	VertexSimulation2 vet2 = nodeIterator2.next();
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
        DijkstraShortestPath<VertexSimulation2, DefaultWeightedEdge> dijkstraPath = 
        		new DijkstraShortestPath<VertexSimulation2, DefaultWeightedEdge>(randomGraph, startVertex, endVertex);
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
		trueObjects = new ArrayList<ObjectSimulation2>();
		
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
		for(ObjectSimulation2 arr : trueObjects)
			System.out.println(arr);

		return true;
	}
	
	// setGroundTruthInVertex will return true if it sets the states where the start vertex has incoming edges
	// Notice that the difference between 'setGroundTruthInVertex' and 'setGroundTruth' is that setGroundTruthInVertex takes the pathList in the form of vertex list 
	public boolean setGroundTruthInVertex(List<VertexSimulation2> pathVertexList)
	{
		if (pathVertexList == null) {
			return false;
		}
		trueStates = new ArrayList<String>();
		trueObjects = new ArrayList<ObjectSimulation2>();
		
		// specify the states that have been gone through
		// Version one: select the first object from the objectlist
		for (VertexSimulation2 v : pathVertexList) {
			trueStates.add(Integer.toString(v.vertexID));
			trueObjects.add(v.objectMatrix[0]);
		}
		System.out.println("The trueStates is: " + trueStates);
		System.out.println("The trueObjects is as the following:");
		for(ObjectSimulation2 arr : trueObjects)
			System.out.println(arr);
		return true;
	}
	
	
    public boolean replaceVertexID(VertexSimulation2 oldVertex, Integer id)
    {
    	VertexSimulation2 newVertex;
    	
        if((oldVertex == null) || (id == null)) {
        	System.out.println("Error in replacement of nodes");
            return false;
        }
        Set<DefaultWeightedEdge> relatedEdges = randomGraph.edgesOf(oldVertex);
        
        // objects is the objectList for this particular node
        ObjectSimulation2[] objects = new ObjectSimulation2[objectPerNode];
        
        for(int i = 0; i < objectPerNode; i++) {
        	objects[i] = new ObjectSimulation2(id*objectPerNode+i);
        }
        for (ObjectSimulation2 row : objects)
        	System.out.println(row);
        
    	newVertex = new VertexSimulation2(id, objectPerNode, objects);
        
        randomGraph.addVertex(newVertex);

        VertexSimulation2 sourceVertex;
        VertexSimulation2 targetVertex;
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
    
    public boolean hasIncomingEdge(VertexSimulation2 ver) {
    	if (randomGraph.incomingEdgesOf(ver).isEmpty()) {
    		System.out.println("The start vertex has no incoming edges!");
    		return false;
    	}
    	return true;
    }
    
    
    public ArrayList<ObjectSimulation2> classify()
    {
    	// evenly distribute the errorGranularity among all the nodes
    	errorGranularity = (1-recall)/(objectPerNode*numVertex-1);
		System.out.println("errorGranularity is " + errorGranularity);
    	classifiedResults = new ArrayList<ObjectSimulation2>();
        for(ObjectSimulation2 obj: trueObjects) {
    		double error = Math.random();  
    		//System.out.println("error is " + error);
    		// min and max are the lower and upper bound of obj, respectively.
    		double min = obj.objectID * errorGranularity;
    		double max = obj.objectID * errorGranularity + recall;
    		//System.out.println("min is " + min + ", and max is " + max);
    		int id;
    		if (0 <= error && error < min) {
    			id = (int) (error/errorGranularity);
    		} else if (1 > error && error >= max) {
    			id = (int) (((error - max)/errorGranularity) + obj.objectID + 1);
    		} else {
    			id = obj.objectID;
    		}
        	classifiedResults.add(new ObjectSimulation2(id));
        }
/*        System.out.println("classifiedResults is as follows");
        for(ObjectSimulation2 arr : classifiedResults)
        	System.out.println(arr);*/
		return classifiedResults;
    }
    

	public static void main(String [] args) 
	{	
		/*
		 * Serialize the graphGen
		 */
		SimulationGraph2 graphGen1 = new SimulationGraph2();
		AbstractBaseGraph<VertexSimulation2, DefaultWeightedEdge> graph1 = null;
		ArrayList<DefaultWeightedEdge> diameterPath1 = new ArrayList<DefaultWeightedEdge>();
		ArrayList<VertexSimulation2> diameterPath1InVertex = new ArrayList<VertexSimulation2>();
		try {
			//1.[densityOfGraph] 2.[objectNumPerNode] 3.[nodeNum] 4.[recall] 5.[pathLength]
			graph1 = graphGen1.GraphGen(0.5, 2, 3, 0.5, 2);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(graph1.toString());
		System.out.println(graph1.edgeSet().size());
/*		diameterPath1InVertex = graphGen1.findDiameterInVertex();
		graphGen1.setGroundTruthInVertex(diameterPath1InVertex);*/
		diameterPath1 = graphGen1.findDiameter();
		graphGen1.setGroundTruth(diameterPath1);
		graphGen1.classify();
		
		// instead of choosing the diameter as the path, choose a path specified length 
		System.out.println("pathLength is " + graphGen1.pathLength);
		ArrayList<VertexSimulation2> pathInVertex = graphGen1.findPath(graphGen1.pathLength);
		if (!graphGen1.setGroundTruthInVertex(pathInVertex)) 
		{
			System.out.println("could not find such path!");
			System.exit(-1);
		}		
		
		try
		{
			ObjectOutputStream out1 = new ObjectOutputStream(new FileOutputStream("graph1.out"));
			out1.writeObject(graph1);
			out1.close();

		} catch(IOException i)
		{
			i.printStackTrace();
		}

	}
}
