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
		
		//Initiate our return hashtable:
		Hashtable<Double, ArrayList<String>> inverted = new Hashtable<Double, ArrayList<String>>();
		
		//Get the hashtable's keys:
		Enumeration<String> keys = table.keys();
		
		//If it exists, get the first element of our enumeration.
		String currentKey = null;
		if (keys.hasMoreElements()) {
			//Get the first element of our enumeration:
			currentKey = keys.nextElement();
		}
		
		//Loop through the rest of them:
		while (keys.hasMoreElements()) {
			currentKey = keys.nextElement();
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
		/*
		 * Given two hashtables (a) and (b) matching Strings to Doubles,
		 * return a hashtable 
		 */
		
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
	
	public static Hashtable<String, Double> logScaleHashtable(
			Hashtable<String, Double> table) {
		/*
		 * Given a hashtable (table) matching
		 * strings to doubles, scale the table
		 * so that its values sum to one, and
		 * take the (natural) logarithms of each value.
		 */

		//Initialize the table we're going to return:
		Hashtable<String, Double> result = new Hashtable<String, Double>();
		
		//Get the sum of all the values in (table):
		double t = 0;
		for (Enumeration<String> keys = table.keys(); keys.hasMoreElements();) {
			String key = keys.nextElement();
			t += table.get(key);
		}
		
		//Take the logarithm of it:
		t = Math.log(t);

		//Scale and take the logarithms of all the other values:
		for (Enumeration<String> keys = table.keys(); keys.hasMoreElements();) {
			String key = keys.nextElement();
			result.put(key, Math.log(table.get(key)) - t);
		}
		
		return result;
	}
	
	public static Count logScaleCount(Count a) {
		/*
		 * Given a Count object (a), logarithmically
		 * scale all the <String, Double> hashtables in it.
		 */
		
		//Logarithmially scale the occurrence counts:
		Hashtable<String, Double> occurs = logScaleHashtable(a.occurs);
		
		//Set up an empty hashtable to ultimately store the log
		//bigram probabilities:
		Hashtable<String, Hashtable<String, Double>> bigrams = 
				new Hashtable<String, Hashtable<String, Double>>();
		
		//Log scale all the hashtables in the bigrams hashtable:
		for (Enumeration<String> keys = a.bigrams.keys(); keys.hasMoreElements();) {
			String key = keys.nextElement();
			bigrams.put(key, logScaleHashtable(a.bigrams.get(key)));
		}
		
		//Make a new Count object with them and return.
		return new Count(a.num, occurs, bigrams);
	}
}