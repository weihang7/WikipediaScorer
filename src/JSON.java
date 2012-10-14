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
	public static Hashtable JSONParser(String inputJson){
		Hashtable ret = new Hashtable();
		boolean inName = true;
		int currentIndex = 1;
		int nameFinishedIndex = 1;
		String currentName = "";
		boolean continueSearching = false;
		String preliminaryEvaluation = "";
		String valueString = "";
		while (currentIndex<inputJson.length()){
			nameOuterLoop:
			while (currentIndex<inputJson.length()){
				if (inputJson.charAt(currentIndex)==':'){
					inName = false;
					currentName = inputJson.substring(1, currentIndex-1);
					break nameOuterLoop;
				}
				currentIndex+=1;
			}
			nameFinishedIndex = currentIndex+1;
			currentIndex+=1;
			valueOuterLoop:
			while (currentIndex<inputJson.length()){
				if (inputJson.charAt(currentIndex)==','){
					preliminaryEvaluation = inputJson.substring(nameFinishedIndex, currentIndex-1);
					System.out.println(valueString);
					if (preliminaryEvaluation.startsWith("{")){
						continueSearching = true;
						if (preliminaryEvaluation.endsWith("}"))
							continueSearching = false;
					}
					else{
						valueString = preliminaryEvaluation;
						ret.put(currentName,Double.parseDouble(valueString));
					}
					if (!continueSearching)
						break valueOuterLoop;
				}
				currentIndex+=1;
			}
		}
		return ret;
	}
	public static void main (String[] args){
		System.out.println(JSONParser("{b:{b:2,a:1},c:{e:5,d:7}}"));
	}
}
