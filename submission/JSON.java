/*
 * @author Weihang Fan
 */

import java.util.*;
public class JSON {
	public static String serialize(Hashtable table){
		//Create a return string
		String ret = "{";
		
		//Create an array of strings that corresponds to the keys of the entered Hashtable
		Object[] objectKeySet = table.keySet().toArray();
		String[] keySet = Arrays.asList(objectKeySet).toArray(new String[objectKeySet.length]);
		
		for (int i = 0; i < keySet.length; i += 1) {
			
			ret += (i == 0 ? "" : ",");
			
			ret += "\"" + keySet[i] + "\":";
			
			if (table.get(keySet[i]) instanceof Hashtable){
				ret+=serialize((Hashtable) table.get(keySet[i]));
			}
			
			else{
				ret += (Double) table.get(keySet[i]);
			}
		}
		
		//Add the closing brace.
		ret += "}";
		
		return ret;
	}
	
	public static String serializeCount(Count c) {
		return "{\"num\":"+c.num+"," +
				"\"occurs\":"+serialize(c.occurs) + "," +
				"\"bigrams\":"+serialize(c.bigrams) +
				"}";
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
	
	public static Hashtable<String, Count> parseAll(String input) {
		Hashtable<Object, Object> global = parse(input);		
	}
}
