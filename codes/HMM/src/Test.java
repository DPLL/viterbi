import java.util.ArrayList;
import java.util.Arrays;


public class Test {
	static public void main (String[] args) {
		ArrayList<String> strArr = new ArrayList<String>();	
		for (int i = 0; i < 3; i++) {
			strArr.add(String.valueOf(i));
		}
		
        String[] actualVocSet = strArr.toArray(new String[strArr.size()]);
        System.out.println(Arrays.toString(actualVocSet));
	}
	

}
