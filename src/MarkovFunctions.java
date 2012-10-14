import java.util.*;

class MarkovFunctions {
	/*
	 * A set of methods that help deal with
	 * the markov models we make in Counter.java.
	 */
	
	private static double hashtableSum(Hashtable table) {
		/*
		 * Given a hashtable (table) mapping strings to doubles,
		 * return the sum of all the values in that hashtable.
		 */
		
		//Get the hashtable's keys:
		String[] keys = (String[])table.keySet().toArray();
		
		//Initiate our counter:
		double total = 0.0;
		
		for (int i = 0; i < keys.length; i += 1) {
			//Add each value to the counter:
			total += (Integer)table.get(keys[i]);
		}

		return total;
	}
	private static Hashtable logarithmicScaleHashtable(Hashtable table, double scalar) {
		/*
		 * Given a hashtable (table) and the logarithm of a scalar (scalar),
		 * scale each value DOWN in the hashtable by the scalar,
		 * but in logarithms to avoid precision loss.
		 */
		
		//Get the hashtable's keys:
		String[] keys = (String[])table.keySet().toArray();
		
		//Initate our return hashtable:
		Hashtable tableToReturn = new Hashtable();

		for (int i = 0; i < keys.length; i += 1) {
			//For each key, get the value associated with it and put
			//the natural log of that value times the scalar into tableToReturn.
			tableToReturn.put(keys[i],Math.log((Double)table.get(keys[i])) - scalar);
		}
		
		return tableToReturn;
	}

	public static Hashtable averageHashtables(Hashtable a, Hashtable b) {
		String[] keys = (String[])a.keySet().toArray();
		Hashtable result = new Hashtable();
		for (int i = 0; i < keys.length; i += 1) {
			result.put(keys[i], ((Integer)a.get(keys[i]) + (Integer)b.get(keys[i])) / 2.0);
		}
		return result;
	}
	
	public static Hashtable logarithmicScaleToOne(Hashtable a) {
		/*
		 * Given a hashtable (a), return a hashtable
		 * whose values are in the same ratio as (a),
		 * but sum to one.
		 */
		
		return logarithmicScaleHashtable(a,Math.log(hashtableSum(a)));
	}
	
	public static Hashtable invertHashtable(Hashtable table) {
		/*
		 * Given a hashtable (table), return a hashtable
		 * mapping all the values in the original table
		 * to all the keys that had those values in the original
		 * table.
		 */
		
		//Get the hashtable's keys:
		String[] keys = (String[])table.keySet().toArray();
		
		//Initiate our return hashtable:
		Hashtable inverted = new Hashtable();
		
		for (int i = 0; i < keys.length; i += 1) {
			if (!inverted.containsKey(table.get(keys[i]))) {
				//If we've never seen this value before, initiate it with
				//a new ArrayList containing only the current key.
				ArrayList list = new ArrayList();
				list.add(keys[i]);
				inverted.put(table.get(keys[i]),list);
			}
			else {
				//Otherwise, add the current key to the list of keys with this
				//value.
				((ArrayList)inverted.get(table.get(keys[i]))).add(keys[i]);
			}
		}
		
		return inverted;
	}
}