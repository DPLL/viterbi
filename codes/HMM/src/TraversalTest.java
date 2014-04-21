import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.collections15.Factory;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.alg.FloydWarshallShortestPaths;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import edu.uci.ics.jung.graph.DelegateTree;


/*
 * This class tests the implementation of DFS searching of length-specified paths
 * and TraversalVertex.class is the vertex class for this file
 */

public class TraversalTest {
	AbstractBaseGraph<TraversalVertex, DefaultWeightedEdge> randomGraph;
	DelegateTree<TraversalVertex, DefaultWeightedEdge> kAryTree;
	int height;
	/*
	 * Find the first path with depth l
	 */
	public boolean DFS(int depth, TraversalVertex v, ArrayList<TraversalVertex> path) 
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
				TraversalVertex ver = randomGraph.getEdgeTarget(e);
				if (ver.visit == false) {
					ver.visit = true;
					path.add(ver);
					if (DFS(depth-1, ver, path))
						return true;
					int size = path.size();
					// remove because it does not meet the pathlength requirement.
					// But we have to make it visit untrue so that even though it is 
					// not in the current path, it might be selected in the future.
					path.get(size-1).visit = false;
					path.remove(size-1);
				}
			}
			return false;
		}
	}
	
	public ArrayList<TraversalVertex> findPath(int pathLength)
	{
		Set<TraversalVertex> vertexSet = randomGraph.vertexSet();
		ArrayList<TraversalVertex> path;
		for (TraversalVertex v : vertexSet) {
			// need to renew all the nodes in the graph by setting 'visit' to be false
			Set<TraversalVertex> vers = randomGraph.vertexSet();
			for (TraversalVertex ver : vers) {
				ver.visit = false;
			}		
			System.out.println("current is vertex is: " + v);
			path = new ArrayList<TraversalVertex>();
			if (DFS(pathLength, v, path))
				System.out.println("hoho");
				if (path.size() == pathLength)
					return path;
		}
		// Here we return null means we have loop through every outgoing adjacent node but could not
		// find a path that meets the requirement.  Otherwise, it will return in the above 'return path' statement.
		return null;
	}
	

	
	public void DFS2(int depth, TraversalVertex v, ArrayList<TraversalVertex> path) 
	{
		if (v == null)
			return;
		if (depth == 0) {
			System.out.println(path);
			return;
		}
		
		Set<DefaultWeightedEdge> outgoingEdges;
		outgoingEdges = randomGraph.outgoingEdgesOf(v);
		for (DefaultWeightedEdge e: outgoingEdges) {
			TraversalVertex ver = randomGraph.getEdgeTarget(e);
			if (ver.visit == false) {
				ver.visit = true;
				path.add(ver);
				DFS2(depth-1, ver, path);
				int size = path.size();
				// remove because it does not meet the pathlength requirement.
				// But we have to make it visit untrue so that even though it is 
				// not in the current path, it might be selected in the future.
				path.get(size-1).visit = false;
				path.remove(size-1);
			}
		}		
		return;
	}
	
	public void findPath2(int pathLength)
	{
		Set<TraversalVertex> vertexSet = randomGraph.vertexSet();
		ArrayList<TraversalVertex> path;
		for (TraversalVertex v : vertexSet) {
			// need to renew all the nodes in the graph by setting 'visit' to be false
			Set<TraversalVertex> vers = randomGraph.vertexSet();
			for (TraversalVertex ver : vers) {
				ver.visit = false;
			}			
			System.out.println("current is vertex is: " + v);
			path = new ArrayList<TraversalVertex>();
			DFS2(pathLength, v, path);
			//System.out.println(path);
		}
		return;
	}

	/*
	 * Difference between DFS2 and DFS is that DFS2 outputs all the possible paths
	 */
	
	
	
	Factory<DefaultWeightedEdge> edgeFactory = new Factory<DefaultWeightedEdge>() {
		//DefaultWeightedEdge i=0;
		public DefaultWeightedEdge create() {
			return new DefaultWeightedEdge();
		}};
	/*
	 * Find the first path with specified depth 
	 */
	public boolean DFSTree(int depth, TraversalVertex v, ArrayList<TraversalVertex> path) 
	{
		if (v == null)
			return false;
		if (depth == 0) {
			//System.out.println(path);
			return true;
		}
		
		Set<TraversalVertex> children;
		children = new HashSet<TraversalVertex>(kAryTree.getChildren(v));
		if (children.isEmpty()) {
			return false;
		} else {
			for (TraversalVertex ver: children) {
				if (ver.visit == false) {
					ver.visit = true;
					path.add(ver);
					if (DFSTree(depth-1, ver, path))
						return true;
					int size = path.size();
					// remove because it does not meet the pathlength requirement.
					// But we have to make its 'visit' untrue so that even though it is 
					// not in the current path, it might be selected in the future.
					path.get(size-1).visit = false;
					path.remove(size-1);
				}
			}
			return false;
		}
	}
	
	public ArrayList<TraversalVertex> findTreePath(int pathLength)
	{
		if (pathLength < 0 || pathLength > height+1) {
			System.out.println("wrong choise of pathLength for the tree!");
			System.exit(-1);
		}
		
		Set<TraversalVertex> vertexSet = new HashSet<TraversalVertex>(kAryTree.getVertices());
		ArrayList<TraversalVertex> path;
		TraversalVertex v = kAryTree.getRoot();

		// need to renew all the nodes in the graph by setting 'visit' to be false
		Set<TraversalVertex> vers = new HashSet<TraversalVertex>(kAryTree.getVertices());
		for (TraversalVertex ver : vers) {
			ver.visit = false;
		}
		path = new ArrayList<TraversalVertex>();
		if (DFSTree(pathLength-1, v, path))
			//System.out.println("hoho");
			if (path.size() == (pathLength-1)) {
				path.add(0, v);
				return path;
			}
		
		return null;
	}
	
	void formTree()
	{
		// kAryTree is a perfect k-ary tree
		kAryTree = new DelegateTree<TraversalVertex, DefaultWeightedEdge>();
		height = 2;
		ArrayList<TraversalVertex> vertexArray = new ArrayList<TraversalVertex>();
        
        for(int i = 0; i < 7; i++) {
        	vertexArray.add(new TraversalVertex(Integer.toString(i)));
        }
        
		// add the root to the tree
    	if(!kAryTree.addVertex(vertexArray.get(0))) {
    		System.out.println("adding vertex error");
    	}
		/*
		 * add edges to this tree
		 */
		//Set<VertexSimulation2> vertexSet = (Set<VertexSimulation2>) kAryTree.getVertices();		
		for (int i= 0; i < 3; i++) {
			// add edges to the nonleaf nodes
			for (int j = 0; j < 2; j++) {
				kAryTree.addEdge(edgeFactory.create(), vertexArray.get(i), vertexArray.get(2*i+1+j));
			}
		}
		
		return;
	}
	
	void formGraph()
	{
		randomGraph = new SimpleDirectedGraph<TraversalVertex, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		TraversalVertex v0 = new TraversalVertex("0");
		TraversalVertex v1 = new TraversalVertex("1");
		TraversalVertex v2 = new TraversalVertex("2");
		TraversalVertex v3 = new TraversalVertex("3");
		TraversalVertex v4 = new TraversalVertex("4");
		TraversalVertex v5 = new TraversalVertex("5");
		
		randomGraph.addVertex(v0);
		randomGraph.addVertex(v1);
		randomGraph.addVertex(v2);
		randomGraph.addVertex(v3);
		randomGraph.addVertex(v4);
		//randomGraph.addVertex(v5);
		
		
		randomGraph.addEdge(v0, v1);
		randomGraph.addEdge(v0, v2);
		randomGraph.addEdge(v0, v4);
		randomGraph.addEdge(v1, v0);
		randomGraph.addEdge(v1, v3);
		randomGraph.addEdge(v2, v4);
		randomGraph.addEdge(v3, v0);
		randomGraph.addEdge(v3, v1);
		randomGraph.addEdge(v3, v4);
		randomGraph.addEdge(v4, v1);
		//randomGraph.addEdge(v5, v4);
		//randomGraph.addEdge(v5, v2);

		
		return;
	}
	
	public ArrayList<DefaultWeightedEdge> findDiameter()
	{
		// ArrayList of the diameter
		ArrayList<DefaultWeightedEdge> diameterPath;
		// ArrayList of the diameter in vertex form
		ArrayList<TraversalVertex> diameterPathInVertex;
		
		if (randomGraph == null) {
			System.out.println("The graph is not ready!");
			System.exit(-1);
		}
		
		TraversalVertex startVertex = new TraversalVertex();
        TraversalVertex endVertex = new TraversalVertex();
        ///The diameter of the graph
        double diameter;
    
        //System.out.println("Number of vertex is: "+ randomGraph.vertexSet().size());    
        FloydWarshallShortestPaths<TraversalVertex, DefaultWeightedEdge> shortestPaths = new FloydWarshallShortestPaths<TraversalVertex, DefaultWeightedEdge>(randomGraph);
        diameter = shortestPaths.getDiameter();
        //System.out.println("diameter of the graph is: "+ diameter);
        
        // Find the source and the destination node
        Iterator<TraversalVertex> nodeIterator1 = randomGraph.vertexSet().iterator();
        double maxShortestPath = 0;
        //int find = 0;
        int nodeCntr = 0;
        while (nodeIterator1.hasNext()) {
        	TraversalVertex vet1 = nodeIterator1.next();
        	//System.out.println("vet1 is: " + vet1);
        	Iterator<TraversalVertex> nodeIterator2 = randomGraph.vertexSet().iterator();
            while (nodeIterator2.hasNext()) {
            	TraversalVertex vet2 = nodeIterator2.next();
            	//System.out.println("vet2 is " + vet2);
            	if (vet2.vertexID != vet1.vertexID) {
            		nodeCntr++;
	            	double shortestPathDis = shortestPaths.shortestDistance(vet1, vet2);
            		if (shortestPathDis > maxShortestPath && shortestPathDis < 9*1) {
	            		startVertex = vet1;
	            		endVertex = vet2;
	            		maxShortestPath = shortestPathDis;
	            	}
            	}
            }
        } 

        if(diameter != maxShortestPath) {
        	System.out.println("diameter does not match!!!");
        	System.exit(-1);
        }
        // Sanity Check 2: see whether the startVertex and endVertex meet the requirement of diameter
        DijkstraShortestPath<TraversalVertex, DefaultWeightedEdge> dijkstraPath = 
        		new DijkstraShortestPath<TraversalVertex, DefaultWeightedEdge>(randomGraph, startVertex, endVertex);
        if(dijkstraPath.getPathLength() != maxShortestPath) {
        	System.out.println("startVertex and EndVertex cannot meet the requirement of diameter!!!");
        	System.exit(-1);
        }        
        
        System.out.println(dijkstraPath.getPathEdgeList());
        diameterPath = (ArrayList<DefaultWeightedEdge>) dijkstraPath.getPathEdgeList();
        
        return diameterPath;
	}
	
	public static void main(String[] args) {
		
		TraversalTest test = new TraversalTest();
		test.formGraph();
		
		//test.findPath(5);		
		//test.findPath2(5);
/*		ArrayList<DefaultWeightedEdge> diameter = new ArrayList<DefaultWeightedEdge>();
		diameter = test.findDiameter();
		System.out.println("The diameter is " + diameter);
		
		if (test.findPath(3) == null) 
		{
			System.out.println("could not find such path!");
		}*/
		
		test.formTree();
		System.out.println(test.kAryTree);
		ArrayList<TraversalVertex> pathInVertex = test.findTreePath(3);
		System.out.println(pathInVertex);
		
	}

}
