import java.util.ArrayList;
import java.util.List;

import org.jgrapht.DirectedGraph;
import org.jgrapht.VertexFactory;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;


public class DemoGraph {
	
	static AbstractBaseGraph<Vertex, DefaultWeightedEdge> g;
	VertexFactory<Vertex> vFactory;
	static Vertex zero;
	static Vertex one;
	static Vertex two;
	static Vertex three;
	static Vertex four;
	
	ArrayList<String> trueStates;
	// ground truth of words
	ArrayList<String> trueWords;
	int wordPerNode = 3;
	
    public DemoGraph()
    {
    } // ensure non-instantiability.
    
    public static void main(String [] args)
    {
        // create a graph based on URL objects
    	AbstractBaseGraph<Vertex, DefaultWeightedEdge> hrefGraph = createDirectedfGraph();

        // note directed edges are printed as: (<v1>,<v2>)
        System.out.println(hrefGraph.toString());
    }

    
    public static AbstractBaseGraph<Vertex, DefaultWeightedEdge> createDirectedfGraph()
    {
    	g = new DefaultDirectedGraph<Vertex, DefaultWeightedEdge>(DefaultWeightedEdge.class);

		zero  = new Vertex(0, 3, new String[]{"oxygen", "defibrillator", "chest"});
		one   = new Vertex(1, 3, new String[]{"asystole", "flat", "line"});
		two   = new Vertex(2, 3, new String[]{"electric", "shock", "oxygen"});
		three = new Vertex(3, 3, new String[]{"intravenous", "push", "defibrillator"});
		four  = new Vertex(4, 3, new String[]{"epinephrine", "amiodarone", "medicine"});
		
		// add the vertices
		g.addVertex(zero);
		g.addVertex(one);
		g.addVertex(two);
		g.addVertex(three);
		g.addVertex(four);
		
		// add edges to create linking structure
		g.addEdge(zero, zero);
		g.addEdge(zero, one);
		g.addEdge(zero, two);
		g.addEdge(one, one);
		g.addEdge(one, two);
		g.addEdge(two, two);
		g.addEdge(two, three);
		g.addEdge(two, four);
		return g;
	}
    
    public ArrayList<DefaultWeightedEdge> findDiameter() 
    {
		// return the ArrayList of the diameter
		ArrayList<DefaultWeightedEdge> diameterPath;
		
        DijkstraShortestPath<Vertex, DefaultWeightedEdge> dijkstraPath = 
        		new DijkstraShortestPath<Vertex, DefaultWeightedEdge>(g, zero, three);  
        
        System.out.println(dijkstraPath.getPathEdgeList());
        diameterPath = (ArrayList<DefaultWeightedEdge>) dijkstraPath.getPathEdgeList();
        return diameterPath;
    }
    
}
