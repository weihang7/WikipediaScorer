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
		
		//Accommodate for the fact that * exists:
		alphabetSize += 1;
		
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
		
		//Accommodate for unseen tokens.
		double totalUnseen = 0.0;
		int numberUnseen = alphabetSize - finalSmoothedCounts.keySet().size();
		
		for (Enumeration<String> keys = held.keys(); keys.hasMoreElements();) {
			String key = keys.nextElement();
			if (!finalSmoothedCounts.containsKey(keys)) {
				//For each token that was unseen in the training corpus,
				//add the number of times it was seen in the held out corpus
				//to unseenCounts:
				totalUnseen += held.get(key);
			}
		}
		
		if (totalUnseen == 0.0 || numberUnseen == 0) {
			//If there were no unseen tokens seen in the held out
			//corpus, accommodate anyway.
			totalUnseen = 1.0;
			numberUnseen = 1;
		}
		
		if (totalUnseen / numberUnseen == Float.POSITIVE_INFINITY) {
			System.out.println("WARNING: Infinite unseen count.");
		}

		//Save this number:
		finalSmoothedCounts.put("__UNSEEN__",totalUnseen/numberUnseen);
				
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