/*
 * @author David Anthony Bau
   @author Weihang Fan
 */

import java.util.*;

class Scorer {
	public static double score(String[] input, Count bad, Count good) {
		/*
		 * Given an input (input), a pair of hashtables for the markov model
		 * of all acceptable Wikipedia entries, and a pair of hashtables for the
		 * markov model of all possible Wikipedia entries, and the
		 * probability that any given Wikipedia entry is acceptable,
		 * return the probability that the input belongs in the "acceptable" category.
		 */
		
		/*
		 * We need: P(thisArticle | good) P(good),
		 * P(thisArticle | bad) P(bad)
		 */
		
		double logBadProbability = Math.log(((double) bad.num) / (bad.num + good.num))
								  + bad.occurs.get(input[0]);
		double logGoodProbability = Math.log(((double) good.num) / (bad.num + good.num))
								  + good.occurs.get(input[0]);		
		
		Hashtable<String, Double> lastBad;
		Hashtable<String, Double> lastGood;
		
		for (int i = 0; i < input.length; i += 1) {
			lastBad = bad.bigrams.get(input[i]);
			lastGood = good.bigrams.get(input[i]);
			
			logBadProbability += lastBad.containsKey(input[i]) ? lastBad.get(input[i])
							   : lastBad.get("__UNSEEN__");
			logGoodProbability += lastGood.containsKey(input[i]) ? lastGood.get(input[i])
								: lastGood.get("__UNSEEN__");
		}

		System.out.println("AFTER MARKOV RUN");
		System.out.println(logBadProbability);
		System.out.println(logGoodProbability);
		
		if (logGoodProbability > logBadProbability) {
			logBadProbability -= logGoodProbability;
			logGoodProbability = 0;
		}
		else {
			logGoodProbability -= logBadProbability;
			logBadProbability = 0;
		}
		
		System.out.println(logGoodProbability);
		System.out.println(logBadProbability);
		
		double goodProbability = Math.pow(Math.E, logGoodProbability);
		double badProbability = Math.pow(Math.E, logBadProbability);
		
		return Math.log ( goodProbability / (goodProbability + badProbability));
	}
}