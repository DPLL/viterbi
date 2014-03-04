import java.util.Arrays;

/**
 * Vertex defines the properties of a vertex used in a random graph
 *
 * @author Yunlong Gao
 * @since Feb 06, 2014
 */

public class VertexSimulation {
	int vertexID;
	// objectNum is the number of objects for each node
	int objectNum;
	int dimension;
	double[][] objectMatrix;
	
	public VertexSimulation(int id, int numberOfWords, int n, double[][] matrixOfObjects) 
	{
		vertexID = id;
		objectNum = numberOfWords;
		dimension = n;
		/*
		if (numberOfWords != listOfWords.length) {
			System.out.println("Number of objects does not match!!!");
			System.exit(-1);
		}*/
		// TODO
		//objectMatrix = new double[objectNum][dimension];
		objectMatrix = matrixOfObjects;
	}
	
	public VertexSimulation(int id) 
	{
		this(id, 0, 0, null);
	}
	
	public VertexSimulation() 
	{
		this(0);
	}
	/*
	public String toString() 
	{
		return "vertexID("+ vertexID + ")"; 
	}*/
	
	public String toString() 
	{
		StringBuilder str = new StringBuilder(Integer.toString(vertexID)+ " ");
		/*
		for (double[] arr : objectMatrix) {
			str.append(Arrays.toString(arr));
		}*/
		//System.out.println(Arrays.deepToString(objectMatrix));
		return str.toString(); 
	}
	
	public static void main(String args[]) 
	{
		VertexSimulation v = new VertexSimulation(1, 2, 2, new double[][]{{0,1},{2,3}});
		System.out.println("vertex v is: "+ v);
	}
}