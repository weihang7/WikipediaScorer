import java.util.*;

class Scorer {
	public static double score(String[] input, Hashtable[] acceptable, Hashtable[] all, double acceptableProb) {
		/*
		 * Given an input (input), a pair of hashtables for the markov model
		 * of all acceptable Wikipedia entries, and a pair of hashtables for the
		 * markov model of all possible Wikipedia entries, and the
		 * probability that any given Wikipedia entry is acceptable,
		 * return the probability that the input belongs in the "acceptable" category.
		 */
		
		//Initiate all our Markov models to begin at the first token:
		Hashtable lastAcceptableHash = (Hashtable)acceptable[1].get(input[0]);
		Hashtable lastAllHash = (Hashtable)all[1].get(input[0]);
		
		//Initiate our probability as the occurrence probability of the first token times
		//the overall probability of acceptability.
		Double probability = (Double)acceptable[0].get(input[0])
				- (Double)all[0].get(input[0])
				+ acceptableProb;
		
		for (int i = 1; i < input.length; i += 1) {
			//Per token, get our estimate of the probability that an acceptable
			//document has this token and multiply:
			if (lastAcceptableHash.containsKey(input[i])) {
				probability  += (Double)lastAcceptableHash.get(input[i]);
			}
			else {
				probability += (Double)lastAcceptableHash.get("__UNSEEN__");
			}
			
			//Similarly, get our estimate of the probability that any document
			//has this token and divide:
			if (lastAllHash.containsKey(input[i])) {
				probability -= (Double)lastAllHash.get(input[i]);
			}
			else {
				probability -= (Double)lastAllHash.get("__UNSEEN__");
			}
			
			//Update lastAcceptableHash and lastAllHash to move along the Markov
			//model.
			lastAcceptableHash = (Hashtable)acceptable[1].get(input[i]);
			lastAllHash = (Hashtable)all[1].get(input[i]);
		}
		
		return probability;
	}
}