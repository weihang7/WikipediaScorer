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
	
	public static Object parse(String input) {
		if (input.charAt(0) == '"') {
			return input.substring(1, input.length() - 1);
		}
		else if (input.charAt(0) == '{') {
			Hashtable<Object, Object> result = new Hashtable<Object, Object>();
			String currentKey = "";
			String currentValue = "";
			boolean addToKey = true;
			int depth = 0;
			for (int i = 1; i < input.length() - 1; i += 1){
				if (input.charAt(i) == '{') {
					depth += 1;
				}
				else if (input.charAt(i) == '}') {
					depth -= 1;
				}
				if (depth == 0) {
					if (input.charAt(i) == ':') {
						addToKey = false;
						continue;
					}
					else if (input.charAt(i) == ',' || i == input.length() - 2) {
						currentValue += (i == input.length() - 2 ? input.charAt(i) : "");
						result.put(parse(currentKey), parse(currentValue));
						currentKey = "";
						currentValue = "";
						addToKey = true;
						continue;
					}
				}
				if (addToKey) currentKey += input.charAt(i);
				else currentValue += input.charAt(i);
			}
			return result;
		}
		else return Double.parseDouble(input);
	}
	
	public static Hashtable<String, Count> parseAll(String input) {
		Hashtable<String, Object> global = (Hashtable<String, Object>) parse(input);
		Hashtable<String, Object> allHash = (Hashtable<String, Object>) global.get("all");
		Hashtable<String, Object> goodHash = (Hashtable<String, Object>) global.get("good");
		Count all = new Count(((Double) allHash.get("num")).intValue(),
				(Hashtable<String, Double>) allHash.get("occurs"),
				(Hashtable<String, Hashtable<String, Double>>) allHash.get("bigrams"));
		Count good = new Count(((Double)goodHash.get("num")).intValue(),
				(Hashtable<String, Double>) goodHash.get("occurs"),
				(Hashtable<String, Hashtable<String, Double>>) goodHash.get("bigrams"));
		Hashtable<String, Count> result = new Hashtable<String, Count>();
		result.put("all", all);
		result.put("good", good);
		return result;
	}
}
