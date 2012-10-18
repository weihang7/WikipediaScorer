/*
 * @author David Anthony Bau
   @author Weihang Fan
 */

import java.util.*;

class Scorer {
	public static double score(String[] input, Count all, Count good) {
		/*
		 * Given an input (input), a pair of hashtables for the markov model
		 * of all acceptable Wikipedia entries, and a pair of hashtables for the
		 * markov model of all possible Wikipedia entries, and the
		 * probability that any given Wikipedia entry is acceptable,
		 * return the probability that the input belongs in the "acceptable" category.
		 */
				
		//Initiate all our Markov models to begin at the first token:
		Hashtable<String,Double> lastAcceptableHash = good.bigrams.get(input[0]);
		Hashtable<String,Double> lastAllHash = all.bigrams.get(input[0]);
		Double acceptableProb = Math.log(good.num) - Math.log(all.num);
		
		//Initiate our probability as the occurrence probability of the first token times
		//the overall probability of acceptability.
		Double probability = good.occurs.get(input[0])
				- all.occurs.get(input[0])
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
			lastAcceptableHash = good.bigrams.get(input[i]);
			lastAllHash = all.bigrams.get(input[i]);
		}
		
		//Convert probability from ln(probability) back to normal

		probability = Math.pow(Math.E, probability);
		return probability;
	}
}