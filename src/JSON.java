import java.io.File;
import java.util.*;
public class JSON {
	public static String serialize(Hashtable table){
		String ret = "{";
		Object[] objectArray = table.keySet().toArray();
		String[] keySet = Arrays.asList(objectArray).toArray(new String[objectArray.length]);
		if (table.get(keySet[0]) instanceof Hashtable){
			for (int i = 0; i < table.size();i++){
				Hashtable currentElement = (Hashtable) table.get(keySet[i]);
				Object[] currentObjectArray = currentElement.keySet().toArray();
				String[] currentKeySet = Arrays.asList(currentObjectArray).toArray(
						new String[currentObjectArray.length]);
				ret+=keySet[i]+":{";
				for (int k=0;k<currentElement.size();k++){
					ret+=currentKeySet[k]+":"+currentElement.get(currentKeySet[k])+(k == currentKeySet.length - 1 ? "" : ",");
				}
				ret+="}" + (i == currentKeySet.length - 1 ? "" : ",");
			}
			ret+="}";
		}
		else{
			for (int k=0;k<keySet.length-1;k++){
				ret+=keySet[k]+":"+table.get(keySet[k])+",";
			}
			ret+=keySet[keySet.length-1]+":"+table.get(keySet[keySet.length-1]);
			ret+="}";
		}
		return ret;
	}
	public static void writeSerializedTable(String serialized){
		
	}
	public static void main (String[] args){
		Hashtable a = new Hashtable();
		Hashtable b = new Hashtable();
		Hashtable c = new Hashtable();
		c.put("d", 7);
		c.put("e", 5);
		b.put("a", 1);
		b.put("b", 2);
		a.put("b",b);
		a.put("c", c);
		System.out.println(serialize(a));
	}

}
