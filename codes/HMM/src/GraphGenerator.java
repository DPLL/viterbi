import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import org.jgrapht.VertexFactory;
import org.jgrapht.WeightedGraph;
import org.jgrapht.generate.RandomGraphGenerator;
import org.jgrapht.graph.ClassBasedVertexFactory;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;


public class GraphGenerator {
	
	static WeightedGraph<Vertex, DefaultWeightedEdge> randomGraph;
	
	// number of vertex
	static int numVertex = 15;
	// number of edges
	static int numEdge;
	// density of the DAG
	static double density;
	// number of words per node
	static int wordPerNode;
	// length of word
	static int wordLength;
	// TODO what if OOP style coding? set it as constructor?
	private GraphGenerator() 
	{
	}
	
	public void GraphGen(double densityOfGraph, int wordNumPerNode, int lengthOfWord) throws FileNotFoundException 
	{
		density = densityOfGraph;
		wordPerNode = wordNumPerNode;
		wordLength = lengthOfWord;
		// cast numEdge to integer
		numEdge = (int) (density * numVertex * (numVertex-1));
		System.out.println("numEdge is: " + numEdge);
		
        // Create the default directed weighted graph object; it is null at this point
		// A default directed graph is a non-simple directed graph in which multiple edges between any two vertices are not permitted, but loops are. 
    	randomGraph = new DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge>(DefaultWeightedEdge.class);

        // Create the randomGraphGenerator object
    	RandomGraphGenerator<Vertex, DefaultWeightedEdge> randomGenerator = 
    			new RandomGraphGenerator<Vertex, DefaultWeightedEdge>(numVertex, numEdge);

        // Create the VertexFactory so the generator can create vertices
        VertexFactory<Vertex> vFactory = new ClassBasedVertexFactory<Vertex>(Vertex.class);

        // Use the randomGraphGenerator object to make randomGraph a random graph with [numVertex] number of vertices
        randomGenerator.generateGraph(randomGraph, vFactory, null);
        
        // Now, replace all the vertices with sequential numbers so we can ID them ,in the same time, we assign wordList to the nodes.
        Set<Vertex> vertices1 = new HashSet<Vertex>();
        vertices1.addAll(randomGraph.vertexSet());
        
        // Firstly, we read from the corresponding file to fetch the random words.
        String path = "/home/david/Dropbox/projects/ER/speech/viterbi/" + wordPerNode + ".txt";
        //System.out.println(path);
        
        Scanner s = new Scanner(new File(path));
        ArrayList<String> list = new ArrayList<String>();
        while (s.hasNext()) {
        	list.add(s.next());
        }
        s.close();
        System.out.println(list);
        String[] wordList = new String[list.size()];
        wordList = list.toArray(wordList);
        /*
        for (String str : wordList)
        	System.out.println(str);*/
        
        // generate a random starting point
        int startPoint = (int)(Math.random() * list.size());
        System.out.println("startPoint is " + startPoint);
        
        // update the information for each node in the graph by IDing and assigning words to them.
        
        Integer counter = 0;
        for (Vertex ver : vertices1) {
            replaceVertexID(ver, counter++, startPoint, wordList);
        }
        
        System.out.println("The randomGraph.vertexSet() is: "+ randomGraph.vertexSet().toString());
		
	}
	
    public static boolean replaceVertexID(Vertex oldVertex, Integer id, int startIndex, String[] wordPool)
    {
    	Vertex newVertex;
    	
        if ((oldVertex == null) || (id == null)) {
        	System.out.println("Error in replacement of nodes");
            return false;
        }
        Set<DefaultWeightedEdge> relatedEdges = randomGraph.edgesOf(oldVertex);
        
        // words is the wordList for this particular node
        String[] words = new String[wordPerNode];
        
        for (int i = 0; i < wordPerNode; i++) {
        	int index = (startIndex+id*wordPerNode + i)%wordPool.length;
        	words[i] = wordPool[index];
        }
        System.out.println(Arrays.toString(words));
        
    	newVertex = new Vertex(id, wordPerNode, words);
        
        randomGraph.addVertex(newVertex);

        Vertex sourceVertex;
        Vertex targetVertex;
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
    public static double uniformGen (double upperBound) {
    	Random randomGen = new Random();
    	double uniRandomInt = randomGen.nextDouble()*upperBound + 1;
    	//System.out.println("uniRandomInt is: " + uniRandomInt);
    	//System.out.println("Int uniRandomInt is: " + (int)uniRandomInt);
    	return uniRandomInt;
    }
    
	public static void main(String [] args) throws FileNotFoundException 
	{

		/*
		if (args.length != 3) {
			System.out.println("Correct Usage: ./GraphGenerator [density] [wordPerNode] [wordLength]");
			System.exit(-1);
		}
		
		density = Double.parseDouble(args[0]);
		wordPerNode = Integer.parseInt(args[1]);
		wordLength = Integer.parseInt(args[2]);
		// cast numEdge to integer
		numEdge = (int) (density * numVertex * (numVertex-1));
			
		*/
		
		GraphGenerator test = new GraphGenerator();
		test.GraphGen(0.5, 5, 5);
	}
}
