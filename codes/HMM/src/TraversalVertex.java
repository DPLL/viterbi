
public class TraversalVertex {
	/**
	 * auto-generated serialiVersionUID
	 */
	String vertexID;
	boolean visit;
	
	public TraversalVertex(String name) 
	{
		vertexID = name;
		visit = false;
	}
	
	
	public TraversalVertex() 
	{
		this(null);
	}
	/*
	public String toString() 
	{
		return "vertexID("+ vertexID + ")"; 
	}*/
	public String toString()
	{
		StringBuilder str = new StringBuilder(vertexID);
		return str.toString(); 
	}
	
	public static void main(String args[]) 
	{
		TraversalVertex v = new TraversalVertex("hi");
		System.out.println("vertex v is: "+ v);
	}
}
