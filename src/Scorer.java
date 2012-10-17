/*
 * @author David Anthony Bau
   @author Weihang Fan
 */

import java.util.*;

class Scorer {
	public static double score(String[] input, Hashtable<String,Hashtable<String,Double>> acceptableBigrams, Hashtable<String,Hashtable<String,Double>> allBigrams, Hashtable<String,Double> acceptableOccurrence, Hashtable<String,Double> allOccurrence, double acceptableProb) {
		/*
		 * Given an input (input), a pair of hashtables for the markov model
		 * of all acceptable Wikipedia entries, and a pair of hashtables for the
		 * markov model of all possible Wikipedia entries, and the
		 * probability that any given Wikipedia entry is acceptable,
		 * return the probability that the input belongs in the "acceptable" category.
		 */
		
		//Initiate all our Markov models to begin at the first token:
		Hashtable<String,Double> lastAcceptableHash = acceptableBigrams.get(input[0]);
		Hashtable<String,Double> lastAllHash = allBigrams.get(input[0]);
		
		//Initiate our probability as the occurrence probability of the first token times
		//the overall probability of acceptability.
		Double probability = acceptableOccurrence.get(input[0])
				- allOccurrence.get(input[0])
				+ acceptableProb;
		
		for (int i = 1; i < input.length; i += 1) {
			//Per token, get our estimate of the probability that an acceptable
			//document has this token and multiply:
			if (lastAcceptableHash.containsKey(input[i])) {
				probability  += lastAcceptableHash.get(input[i]);
			}
			else {
				probability += lastAcceptableHash.get("__UNSEEN__");
			}
			
			//Similarly, get our estimate of the probability that any document
			//has this token and divide:
			if (lastAllHash.containsKey(input[i])) {
				probability -= lastAllHash.get(input[i]);
			}
			else {
				probability -= lastAllHash.get("__UNSEEN__");
			}
			
			//Update lastAcceptableHash and lastAllHash to move along the Markov
			//model.
			lastAcceptableHash = acceptableBigrams.get(input[i]);
			lastAllHash = allBigrams.get(input[i]);
		}
		//Convert probability from ln(probability) back to normal
		probability = Math.pow(probability, Math.E);
		return probability;
	}
}