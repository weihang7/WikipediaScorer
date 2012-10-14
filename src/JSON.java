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
	public static Hashtable parse(String inputJson){
		Hashtable ret = new Hashtable();
		boolean inName = true;
		int currentIndex = 0;
		int nameFinishedIndex = 1;
		int startingIndex = 1;
		String currentName = "";
		boolean continueSearching = false;
		String preliminaryEvaluation = "";
		String valueString = "";
		while (currentIndex<inputJson.length()){
			currentIndex+=1;
			startingIndex = currentIndex;
			nameOuterLoop:
			while (currentIndex<inputJson.length()){
				if (inputJson.charAt(currentIndex)==':'){
					inName = false;
					currentName = inputJson.substring(startingIndex, currentIndex);
					break nameOuterLoop;
				}
				currentIndex+=1;
			}
			System.out.println("Name:"+currentName);
			nameFinishedIndex = currentIndex;
			currentIndex+=1;
			valueOuterLoop:
			while (currentIndex<inputJson.length()){
				System.out.println(inputJson.charAt(currentIndex));
				if (inputJson.charAt(currentIndex)==','||inputJson.charAt(currentIndex)=='}'){
					preliminaryEvaluation = inputJson.substring(nameFinishedIndex+1, currentIndex);
					System.out.println("pelim"+preliminaryEvaluation);
					if (preliminaryEvaluation.startsWith("{")){
						continueSearching = true;
						if (preliminaryEvaluation.endsWith("}")){
							continueSearching = false;
							ret.put(currentName, parse(preliminaryEvaluation));
							break valueOuterLoop;
						}
					}
					else{
						valueString = inputJson.substring(nameFinishedIndex+1, currentIndex);
						ret.put(currentName,Double.parseDouble(valueString));
						System.out.println("value:"+valueString);
						break valueOuterLoop;
					}
				}
				currentIndex+=1;
			}
		}
		return ret;
	}
}
