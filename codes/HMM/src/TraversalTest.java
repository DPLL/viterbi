import java.util.ArrayList;
import java.util.Set;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedGraph;


public class TraversalTest {
	AbstractBaseGraph<String, DefaultEdge> randomGraph;
	
	/*
	 * Find the first path with depth l
	 */
	public boolean DFS(int depth, String v, ArrayList<String> path) 
	{
		if (v == null)
			return false;
		if (depth == 0) {
			System.out.println(path);
			return true;
		}
		
		Set<DefaultEdge> outgoingEdges;
		outgoingEdges = randomGraph.outgoingEdgesOf(v);
		if (outgoingEdges.isEmpty()) {
			return false;
		} else {
			for (DefaultEdge e: outgoingEdges) {
				String ver = randomGraph.getEdgeTarget(e);
				path.add(ver);
				if (DFS(depth-1, ver, path))
					break;
				int size = path.size();
				path.remove(size-1);
			}
			return true;
		}
	}
	
	public ArrayList<String> findPath(int pathLength)
	{
		Set<String> vertexSet = randomGraph.vertexSet();
		ArrayList<String> path;
		for (String v : vertexSet) {
			System.out.println("current is vertex is: " + v);
			path = new ArrayList<String>();
			if (DFS(pathLength, v, path))
				if (path.size() == pathLength)
					return path;
		}
		return null;
	}
	
	public void DFS2(int depth, String v, ArrayList<String> path) 
	{
		if (v == null)
			return;
		if (depth == 0) {
			System.out.println(path);
			return;
		}
		
		Set<DefaultEdge> outgoingEdges;
		outgoingEdges = randomGraph.outgoingEdgesOf(v);
		for (DefaultEdge e: outgoingEdges) {
			String ver = randomGraph.getEdgeTarget(e);
			path.add(ver);
			DFS2(depth-1, ver, path);
			int size = path.size();
			path.remove(size-1);
		}		
		return;
	}
	
	public ArrayList<String> findPath2(int pathLength)
	{
		Set<String> vertexSet = randomGraph.vertexSet();
		ArrayList<String> path;
		for (String v : vertexSet) {
			System.out.println("current is vertex is: " + v);
			path = new ArrayList<String>();
			DFS2(pathLength, v, path);
		}
		return null;
	}
	
	void formGraph()
	{
		randomGraph = new SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
		randomGraph.addVertex("1");
		randomGraph.addVertex("2");
		randomGraph.addVertex("3");
		randomGraph.addVertex("4");
		randomGraph.addVertex("5");
		randomGraph.addVertex("6");
		randomGraph.addVertex("7");
		randomGraph.addVertex("8");
		randomGraph.addVertex("9");
		
		
		randomGraph.addEdge("1", "2");
		randomGraph.addEdge("1", "3");
		randomGraph.addEdge("3", "6");
		randomGraph.addEdge("3", "7");
		
		randomGraph.addEdge("2", "4");
		randomGraph.addEdge("2", "5");
		//randomGraph.addEdge("4", "8");
		randomGraph.addEdge("5", "9");

		
		return;
	}
	
	public static void main(String[] args) {
		
		TraversalTest test = new TraversalTest();
		test.formGraph();
		//test.findPath(3);
		//System.out.println(test.findPath2(3));
		if (test.findPath(4) == null) 
		{
			System.out.println("could not find such path!");
		}
		
	}

}
