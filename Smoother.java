import java.util.*;
class Smoother {
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
	private static Hashtable scaleHashtable(Hashtable table, double scalar) {
		/*
		 * Given a hashtable (table) and a scalar (scalar),
		 * return the hashtable with the same keys as (table),
		 * each mapping to a value that is (scalar) times the value
		 * in the original hashtable.
		 */
		
		//Get the hashtable's keys:
		String[] keys = (String[])table.keySet().toArray();
		
		//Initiate our return hashtable:
		Hashtable tableToReturn = new Hashtable();
		
		for (int i = 0; i < keys.length; i += 1) {
			//For each key, get the value associated with it and put that
			//value times the scalar into tableToReturn.
			tableToReturn.put(keys[i], (Integer)table.get(keys[i]) * scalar);
		}
		
		return tableToReturn;
	}
	private static Hashtable invertHashtable(Hashtable table) {
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
	private static Hashtable scaleToOne(Hashtable a) {
		/*
		 * Given a hashtable (a), return a hashtable
		 * whose values are in the same ratio as (a),
		 * but sum to one.
		 */
		
		return scaleHashtable(a,1.0/hashtableSum(a));
	}
	public static Hashtable heldOutSmoothing(Hashtable training, Hashtable held, int alphabetSize) {
		/*
		 * Perform held out smoothing on a data set.
		 */
		
		//Initiate our hashtables:
		Hashtable invertedTraining = invertHashtable(training);
		Hashtable finalSmoothedCounts = new Hashtable();
		Double[] invertedKeys = (Double[])invertedTraining.keySet().toArray();
		
		//Get the tokens that were seen in the held out corpus:
		String[] heldKeys = (String[])held.keySet().toArray();
		
		for (int i = 0; i < invertedKeys.length; i += 1) {
			//Get all the tokens who were seen this many times:
			ArrayList tokens = (ArrayList)invertedTraining.get(invertedKeys[i]);
			
			//Initiate our counter:
			double totalSeen = 0.0;
			
			for (int x = 0; x < tokens.size(); x += 1) {
				//For each token that was seen this many times in the
				//training corpus, get how many times it was seen in the
				//held out corpus and add it to totalSeen.
				if (held.containsKey(tokens.get(x))) {
					totalSeen += (Double)held.get(tokens.get(x));					
				}
			}
			
			if (totalSeen == 0.0) {
				//If we never see this kind of word in the held out corpus,
				//pretend we did so that we don't have infinite surprise.
				totalSeen = 1.0;
			}
			
			for (int x = 0; x < tokens.size(); x += 1) {
				//Then distribute those counts into finalSmoothedCounts.
				finalSmoothedCounts.put(tokens.get(x), totalSeen/tokens.size());
			}
		}
		
		//Accomodate for unseen tokens.
		double totalUnseen = 0.0;
		int numberUnseen = alphabetSize - finalSmoothedCounts.size();
		for (int i = 0; i < heldKeys.length; i += 1) {
			if (!finalSmoothedCounts.containsKey(heldKeys[i])) {
				//For each token that was unseen in the training corpus,
				//add the number of times it was seen in the held out corpus
				//to unseenCounts:
				totalUnseen += (Double)held.get(heldKeys[i]);
			}
			
			if (totalUnseen == 0.0) {
				//If there were no unseen tokens seen in the held out
				//corpus, accomodate anyway.
				totalUnseen = 1.0;
				numberUnseen = 1;
			}
			
			//Save this number:
			finalSmoothedCounts.put("__UNSEEN__",totalUnseen/numberUnseen);
		}
		
		return finalSmoothedCounts;
	}
	public static Hashtable heldOutSmoothingScaled(Hashtable training, Hashtable held, int alphabetSize) {
		/*
		 * Perform held out smoothing on a data set,
		 * and also scale to one.
		 */
		
		return scaleToOne(heldOutSmoothing(training, held, alphabetSize));
	}
}