import java.io.Serializable;
import java.util.Arrays;

public class ObjectSimulation2 implements Serializable {
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
