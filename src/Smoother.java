/*
 * @author David Anthony Bau
 */
import java.util.*;

class Smoother {
	public static Hashtable<String, Double> heldOutSmoothing(
			Hashtable<String, Double> training,
			Hashtable<String, Double> held,
			int alphabetSize) {
		/*
		 * Perform held out smoothing on a data set.
		 */
		
		//Initiate our hashtables:
		Hashtable<Double, ArrayList<String>> invertedTraining = 
				MarkovFunctions.invertHashtable(training);
		Hashtable<String, Double> finalSmoothedCounts = new Hashtable<String, Double>();
				
		for (Enumeration<Double> keys = invertedTraining.keys(); keys.hasMoreElements();) {
			Double key = keys.nextElement();
			
			//Get all the tokens who were seen this many times:
			ArrayList<String> tokens = invertedTraining.get(key);
			
			//Initiate our counter:
			double totalSeen = 0.0;
			
			for (int x = 0; x < tokens.size(); x += 1) {
				//For each token that was seen this many times in the
				//training corpus, get how many times it was seen in the
				//held out corpus and add it to totalSeen.
				if (held.containsKey(tokens.get(x))) {
					totalSeen += held.get(tokens.get(x));					
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
		
		for (Enumeration<String> keys = held.keys(); keys.hasMoreElements();) {
			if (!finalSmoothedCounts.containsKey(keys)) {
				//For each token that was unseen in the training corpus,
				//add the number of times it was seen in the held out corpus
				//to unseenCounts:
				totalUnseen += held.get(keys);
			}
		}
		
		if (totalUnseen == 0.0) {
			//If there were no unseen tokens seen in the held out
			//corpus, accommodate anyway.
			totalUnseen = 1.0;
			numberUnseen = 1;
		}

		//Save this number:
		finalSmoothedCounts.put("__UNSEEN__",totalUnseen/numberUnseen);
		
		String[] keys = (String[]) finalSmoothedCounts.keySet().toArray(new String[finalSmoothedCounts.keySet().size()]);
		for (int i = 0; i < keys.length; i += 1) {
			if (finalSmoothedCounts.get(keys[i]) < 0) {
				System.out.println(keys[i]);
			}
		}
		
		return finalSmoothedCounts;
	}
	
	public static Hashtable<String, Double> fullSmoothing(
			Hashtable<String, Double> training,
			Hashtable<String, Double> held,
			int alphabetSize) {
		/*
		 * Perform held out smoothing on a data set,
		 * and also scale to one.
		 */
		
		return heldOutSmoothing(training, held, alphabetSize);
	}
	
	public static Hashtable<String, Double> fullCrossSmoothing(
			Hashtable<String, Double> a,
			Hashtable<String, Double> b,
			int alphabetSize) {
		/*
		 * Perform held out smoothing with (a) as the training data
		 * and (b) as the held out data and vice versa, and average
		 * the two results.
		 */
		return MarkovFunctions.averageHashtables(
				fullSmoothing(a, b, alphabetSize),
				fullSmoothing(b, a, alphabetSize));
	}
	
	public static Count fullCountSmoothing(Count a, Count b, int alphabetSize) {
		
		//Do cross HO smoothing for the occurrence counts:
		Hashtable<String, Double> occurs = 
				fullCrossSmoothing(a.occurs,
						b.occurs,
						alphabetSize);
		
		//Start our smoothed bigram counts as an empty Hashtable:
		Hashtable<String, Hashtable<String, Double>> bigrams = 
				new Hashtable<String, Hashtable<String, Double>>();
		
		//Go through all the alphabet tokens and smooth the bigram
		//counts between (a) and (b) for each one.
		for (Enumeration<String> keys = a.bigrams.keys(); keys.hasMoreElements();) {
			String key = keys.nextElement();
			bigrams.put(key, fullCrossSmoothing(a.bigrams.get(key), b.bigrams.get(key), alphabetSize));
		}
		
		return new Count(a.num + b.num / 2, occurs, bigrams);
	}
}