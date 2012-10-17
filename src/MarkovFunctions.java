/*
 * @author David Anthony Bau
 */

import java.util.*;

class MarkovFunctions {
	/*
	 * A set of methods that help deal with
	 * the markov models we make in Counter.java.
	 */
	
	public static Hashtable<Double, ArrayList<String>> invertHashtable(Hashtable<String, Double> table) {
		/*
		 * Given a hashtable (table), return a hashtable
		 * mapping all the values in the original table
		 * to all the keys that had those values in the original
		 * table.
		 */
		
		//Get the hashtable's keys:
		Enumeration<String> keys = table.keys();
		
		//Initiate our return hashtable:
		Hashtable<Double, ArrayList<String>> inverted = new Hashtable<Double, ArrayList<String>>();
		
		//Get the first element of our enumeration:
		String currentKey = keys.nextElement();
		
		//Loop through the rest of them:
		while (keys.hasMoreElements()) {
			if (!inverted.containsKey(table.get(currentKey))) {
				//If we've never seen this value before, initiate it with
				//a new ArrayList containing only the current key.				
				ArrayList<String> list = new ArrayList<String>();
				list.add(currentKey);
				inverted.put(table.get(currentKey),list);
			}
			else {
				//Otherwise, add the current key to the list of keys with this
				//value.
				inverted.get(table.get(currentKey)).add(currentKey);
			}
		}
		
		return inverted;
	}
	
	public static Hashtable<String, Double> averageHashtables(Hashtable<String, Double> a,
			Hashtable<String, Double> b) {
				
		//Initiate our result hashtable with nothing in it:
		Hashtable<String, Double> result = new Hashtable<String, Double>();
		
		//Loop through the keys of the hashtable and average the values.
		for (Enumeration<String> keys = a.keys(); keys.hasMoreElements();) {
			
			String key = keys.nextElement();
			
			//Get the approximated counts for keys[i] in each hashtable 
			//(__UNSEEN__ if not in the hashtable).
			Double aValue = a.containsKey(key) ? a.get(key)
						  : a.get("__UNSEEN__");
			Double bValue = b.containsKey(key) ? b.get(key)
					      : b.get("__UNSEEN__");
			
			//Put the average of those values into result.
			result.put(key, (aValue + bValue / 2.0));
		}
		
		return result;
	}
}