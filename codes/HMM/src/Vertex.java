import java.util.Arrays;

/**
 * Vertex defines the properties of a vertex used in a random graph
 *
 * @author Yunlong Gao
 * @since Feb 06, 2014
 */

public class Vertex {
	int vertexID;
	// wordNum is the number of words for each node
	int wordNum;
	String[] wordList;
	
	public Vertex(int id, int numberOfWords, String[] listOfWords) 
	{
		vertexID = id;
		wordNum = numberOfWords;
		/*
		if (numberOfWords != listOfWords.length) {
			System.out.println("Number of words does not match!!!");
			System.exit(-1);
		}*/
		wordList = listOfWords;
	}
	
	public Vertex(int id) 
	{
		this(id, 0, null);
	}
	
	public Vertex() 
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
		return "vertexID("+ vertexID + ");  "
				+ "wordNum(" + wordNum + ");  " + "wordList(" 
				+ Arrays.toString(wordList) +")"; 
	}
	
	public static void main(String args[]) 
	{
		Vertex v = new Vertex(1, 2, new String[]{"hi", "hello"});
		System.out.println("vertex v is: "+ v);
	}
}