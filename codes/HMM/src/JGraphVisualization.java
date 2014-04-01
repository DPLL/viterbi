/* This program and the accompanying materials are dual-licensed under
 * either
 *
 * (a) the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation, or (at your option) any
 * later version.
 *
 * or (per the licensee's choosing)
 *
 * (b) the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation.
 */

import com.mxgraph.layout.*;
import com.mxgraph.swing.*;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.*;

import org.jgrapht.*;
import org.jgrapht.demo.JGraphAdapterDemo;
import org.jgrapht.ext.*;
import org.jgrapht.generate.RandomGraphGenerator;
import org.jgrapht.graph.*;
import org.jgrapht.traverse.DepthFirstIterator;


/**
 * A demo applet that shows how to use JGraphX to visualize JGraphT graphs.
 * Applet based on JGraphAdapterDemo.
 *
 * @since July 9, 2013
 */
public class JGraphVisualization
    extends JApplet
{
    

    private static final long serialVersionUID = 2202072534703043194L;
    private static final Dimension DEFAULT_SIZE = new Dimension(530, 320);

    //private JGraphXAdapter<String, DefaultEdge> jgxAdapter;
    private JGraphXAdapter<VertexSimulation2, DefaultWeightedEdge> jgxAdapter;
    

    /**
     * An alternative starting point for this demo, to also allow running this
     * applet as an application.
     *
     * @param args ignored.
     */
    public static void main(String [] args)
    {
        JGraphAdapterDemo applet = new JGraphAdapterDemo();
        applet.init();
        

        JFrame frame = new JFrame();
        frame.getContentPane().add(applet);
        frame.setTitle("JGraphT Adapter to JGraph Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
    public AbstractBaseGraph<VertexSimulation2, DefaultWeightedEdge> graphGen(double densityOfGraph, int objectNumPerNode, 
    		int nodeNum) throws FileNotFoundException 
    {
    	
    	int numEdge;
    	// density of the DAG
    	double density;
    	// number of objects per node
    	// It is assumed that the objectPerNode is the same across different nodes.
    	int objectPerNode;
    	// number of vertex
    	int numVertex;
    	
    	AbstractBaseGraph<VertexSimulation2, DefaultWeightedEdge> randomGraph;
    	VertexFactory<VertexSimulation2> vFactory;
    	
    	density = densityOfGraph;
    	objectPerNode = objectNumPerNode;
    	numVertex = nodeNum;
    	// cast numEdge to integer
    	numEdge = (int) (density * numVertex * (numVertex-1));

    	
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
        
        // create a visualization using JGraph, via an adapter
        jgxAdapter = new JGraphXAdapter<VertexSimulation2, DefaultWeightedEdge>(randomGraph);
        
        getContentPane().add(new mxGraphComponent(jgxAdapter));
        resize(DEFAULT_SIZE);
        
        // update the information for each node in the graph by IDing and assigning objects to them.
        Set<VertexSimulation2> vertices1 = new HashSet<VertexSimulation2>();
        vertices1.addAll(randomGraph.vertexSet());
        Integer counter = 0;
        for (VertexSimulation2 ver : vertices1) {
            replaceVertexID(ver, counter++, randomGraph, objectPerNode);
        }       
        
        // Output all the vertexes and the edges
        //System.out.println("The randomGraph.vertexSet() is: "+ randomGraph.vertexSet().toString());
        Iterator<VertexSimulation2> iter2 = new DepthFirstIterator<VertexSimulation2, DefaultWeightedEdge>(randomGraph);
        VertexSimulation2 ver2;
    /*        while (iter2.hasNext()) {
        	ver2 = iter2.next();
            System.out.println("VertexSimulation2 " + ver2.toString() + " is connected to: "
                    + randomGraph.edgesOf(ver2).toString());
        }*/
        
        // positioning via jgraphx layouts
        mxCircleLayout layout = new mxCircleLayout(jgxAdapter);
        layout.execute(jgxAdapter.getDefaultParent());

        return randomGraph;
    }
    
    public boolean replaceVertexID(VertexSimulation2 oldVertex, Integer id, AbstractBaseGraph<VertexSimulation2, DefaultWeightedEdge> randomGraph, int objectPerNode)
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
        {
        	System.out.println(row);
        	System.out.println("calls in replacenment");
        }
        
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

    /**
     * {@inheritDoc}
     */
   public void init()
    {
/*        // create a JGraphT graph
        ListenableGraph<String, DefaultEdge> g =
            new ListenableDirectedGraph<String, DefaultEdge>(
                DefaultEdge.class);

        // create a visualization using JGraph, via an adapter
        jgxAdapter = new JGraphXAdapter<String, DefaultEdge>(g);

        getContentPane().add(new mxGraphComponent(jgxAdapter));
        resize(DEFAULT_SIZE);

        String v1 = "v1";
        String v2 = "v2";
        String v3 = "v3";
        String v4 = "v4";

        // add some sample data (graph manipulated via JGraphX)
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);

        g.addEdge(v1, v2);
        g.addEdge(v2, v3);
        g.addEdge(v3, v1);
        g.addEdge(v4, v3);

        // positioning via jgraphx layouts
        mxCircleLayout layout = new mxCircleLayout(jgxAdapter);
        layout.execute(jgxAdapter.getDefaultParent());

        // that's all there is to it!...
*/    
	   try {
			AbstractBaseGraph<VertexSimulation2, DefaultWeightedEdge> g = graphGen(0.2, 10, 20);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

//End JGraphXAdapterDemo.java