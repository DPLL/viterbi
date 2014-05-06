package jgraph.test;
import java.io.Serializable;
import java.util.Arrays;

public class ObjectSimulation2 implements Serializable {
	
	/**
	 * SimulationGraph2.java & Simulation2.java & VertexSimulation2.java & ObjectSimulation2.java are in the same set.
	 */
	private static final long serialVersionUID = 1L;
	int objectID;
	
	public String toString() 
	{
		return Integer.toString(objectID); 
	}
	
	public ObjectSimulation2(int idNum) 
	{
		objectID = idNum;
	}
	
	public ObjectSimulation2() 
	{
		this(-1);
	}
}
