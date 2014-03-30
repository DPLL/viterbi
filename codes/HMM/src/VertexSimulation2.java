import java.io.Serializable;
import java.util.Arrays;

/**
 * Vertex defines the properties of a vertex used in a random graph
 *
 * @author Yunlong Gao
 * @since Feb 06, 2014
 */

public class VertexSimulation2 implements Serializable {
	/**
	 * auto-generated serialiVersionUID
	 */
	int vertexID;
	// objectNum is the number of objects for each node
	int objectNum;
	boolean visit;
	ObjectSimulation2[] objectMatrix;
	
	public VertexSimulation2(int id, int numberOfWords, ObjectSimulation2[] matrixOfObjects) 
	{
		vertexID = id;
		objectNum = numberOfWords;
		objectMatrix = matrixOfObjects;
		visit = false;
	}
	
	public VertexSimulation2(int id) 
	{
		this(id, 0, null);
	}
	
	public VertexSimulation2() 
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
		StringBuilder str = new StringBuilder(Integer.toString(vertexID)+ ": ");
		
		for (ObjectSimulation2 obj : objectMatrix) {
			str.append(obj.objectID + " ");
		}
		System.out.println(Arrays.toString(objectMatrix));
		return str.toString(); 
	}
	
	public static void main(String args[]) 
	{
		VertexSimulation2 v = new VertexSimulation2(1, 2, new ObjectSimulation2[]{new ObjectSimulation2(0), new ObjectSimulation2(1)});
		System.out.println("vertex v is: "+ v);
	}
}