/*
 * @author David Anthony Bau
 */

import java.util.*;

class Counter {
	public static Count count(String[] corpus, String[] alphabet) {
		/*
		 * Given an array of tokens (corpus),
		 * constructs a degree 2 Markov model out of it
		 * and returns both (occurs), the occurence probabilities
		 * of each token, and (bigrams), the transition probabilities
		 * of each pair of tokens.
		 */
		
		//Declare all our hashtables:
		Hashtable<String, Double> occurs = new Hashtable<String, Double>();
		Hashtable<String, Hashtable<String, Double>> bigrams = new Hashtable<String, Hashtable<String, Double>>();

		//This is the Markov transition probabiliities from the previous token:
		Hashtable<String, Double> lastHash = new Hashtable<String, Double>();
		
		for (int i = 0; i < corpus.length; i += 1) {
			if (!occurs.containsKey(corpus[i])) {
				//If we've never seen this token before, set it up:
				occurs.put(corpus[i], 1.0);
				bigrams.put(corpus[i], new Hashtable<String, Double>());
			}
			else {
				//Otherwise, add one to the number of times we've seen it
				occurs.put(corpus[i], occurs.get(corpus[i]) + 1);
			}
			
			if (!lastHash.containsKey(corpus[i])) {
				//If we've never seen this transition before, set it up:
				lastHash.put(corpus[i], 1.0);
			}
			else {
				//Otherwise, add one to the number of times we've seen it.
				lastHash.put(corpus[i], (Double)lastHash.get(corpus[i]) + 1.0);
			}

			//Update lastHash for the next iteration.
			lastHash = bigrams.get(corpus[i]);
		}
		
		//For any tokens that were in our alphabet but did not
		//appear, say so:
		for (int i = 0; i < alphabet.length; i += 1) {
			if (!bigrams.containsKey(alphabet[i])) {
				bigrams.put(alphabet[i], new Hashtable<String, Double>());
			}
			if (!occurs.containsKey(alphabet[i])) {
				occurs.put(alphabet[i], 0.0);
			}
		}
		
		//Make a Count out of this (with num = 1, since we don't know what
		//num should be), and return it.
		return new Count(1, occurs, bigrams);
	}
}