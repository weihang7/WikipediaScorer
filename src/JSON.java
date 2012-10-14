import java.util.*;
public class JSON {
	public static String serialize(Hashtable table){
		//create a return string
		String ret = "{";
		// create an array of strings that corresponds to the keys of the entered Hashtable
		Object[] objectKeySet = table.keySet().toArray();
		String[] keySet = Arrays.asList(objectKeySet).toArray(new String[objectKeySet.length]);
		// if it is a hashtable within a hashtable, read table's component Hashtables.
		if (table.get(keySet[0]) instanceof Hashtable){
			for (int i = 0; i < table.size();i++){
				Hashtable currentElement = (Hashtable) table.get(keySet[i]);
				Object[] currentObjectArray = currentElement.keySet().toArray();
				String[] currentKeySet = Arrays.asList(currentObjectArray).toArray(
						new String[currentObjectArray.length]);
				ret+=keySet[i]+":{";
				for (int k=0;k<currentElement.size();k++){
					ret+=currentKeySet[k]+":"+currentElement.get(currentKeySet[k])+
							(k == currentKeySet.length - 1 ? "" : ",");
				}
				// add comma if it is not the final key-value pair
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
		//create the return Hashtable
		Hashtable ret = new Hashtable();
		//define variables for keeping track of the progress of the function
		boolean inName = true;
		int currentIndex = 0;
		int nameFinishedIndex = 0;
		int startingIndex = 1;
		String currentName = "";
		boolean continueSearching = false;
		String preliminaryEvaluation = "";
		String valueString = "";
		//do the process to the whole string
		while (currentIndex<inputJson.length()){
			currentIndex+=1;// ignore the first brace
			startingIndex = currentIndex;//store the starting index of the key
			//label the outer loop to make breaking more convenient
			nameOuterLoop:
			//continually seek ":", which is the separation symbol between key and value
			while (currentIndex<inputJson.length()){
				if (inputJson.charAt(currentIndex)==':'){
					inName = false;
					//store the name in a string
					currentName = inputJson.substring(startingIndex, currentIndex);
					break nameOuterLoop;
				}
				currentIndex+=1;
			}
			nameFinishedIndex = currentIndex;
			currentIndex+=1;
			//label the outer loop
			valueOuterLoop:
			//continually parse the content
			while (currentIndex<inputJson.length()){
				if (inputJson.charAt(currentIndex)==','||inputJson.charAt(currentIndex)=='}'){
					preliminaryEvaluation = inputJson.substring(nameFinishedIndex+1, currentIndex);
					// if it is a Hashtable
					if (preliminaryEvaluation.startsWith("{")){
						//search until finding "}"
						continueSearching = true;
						if (preliminaryEvaluation.endsWith("}")){
							continueSearching = false;
							//recursively parse the component Hashtable
							ret.put(currentName, parse(preliminaryEvaluation));
							break valueOuterLoop;
						}
					}
					else{
						//parse the value into double
						valueString = inputJson.substring(nameFinishedIndex+1, currentIndex);
						ret.put(currentName,Double.parseDouble(valueString));
						break valueOuterLoop;
					}
				}
				currentIndex+=1;
			}
		}
		return ret;
	}
}
