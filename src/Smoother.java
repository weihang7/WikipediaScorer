/*
 * @author David Anthony Bau
 */
import java.util.*;

class Smoother {
	public static Hashtable heldOutSmoothing(Hashtable training, Hashtable held, int alphabetSize) {
		/*
		 * Perform held out smoothing on a data set.
		 */
		
		//Initiate our hashtables:
		Hashtable invertedTraining = MarkovFunctions.invertHashtable(training);
		Hashtable finalSmoothedCounts = new Hashtable();
		Double[] invertedKeys = (Double[])invertedTraining.keySet().toArray(new Double[invertedTraining.keySet().size()]);
		
		//Get the tokens that were seen in the held out corpus:
		String[] heldKeys = (String[])held.keySet().toArray(new String[held.keySet().size()]);
		
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
			if ((Double) finalSmoothedCounts.get(keys[i]) < 0) {
				System.out.println(keys[i]);
			}
		}
		
		return finalSmoothedCounts;
	}
	
	public static Hashtable fullSmoothing(Hashtable training, Hashtable held, int alphabetSize) {
		/*
		 * Perform held out smoothing on a data set,
		 * and also scale to one.
		 */
		
		return heldOutSmoothing(training, held, alphabetSize);
	}
	
	public static Hashtable fullCrossSmoothing(Hashtable a, Hashtable b, int aLPHABET_SIZE) {
		return MarkovFunctions.averageHashtables(fullSmoothing(a, b, aLPHABET_SIZE),fullSmoothing(b, a, aLPHABET_SIZE));
	}
}