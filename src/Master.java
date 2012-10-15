//author: Anthony Bau, Weihang Fan
import java.util.*;
class Master {
	public static void main(String[] args) {
		/*
		 * Does the entire analysis sequence.
		 * Requires (0) number of articles to fetch,
		 * (1) alphabet size, (2) target path for occurrence counts,
		 * and (3) target path for bigram counts.
		 */
		
		final int NUMBER_OF_ARTICLES = Integer.parseInt(args[0]);
		final int ALPHABET_SIZE = Integer.parseInt(args[1]);
		final String OCCURRENCE_TARGET_PATH = args[2];
		final String BIGRAM_TARGET_PATH = args[3];
		
		//Fetch random pages and parse:
		Hashtable pageHashtable = Fetcher.getRandomPageTexts(NUMBER_OF_ARTICLES,0);
		String[] pages = (String[]) pageHashtable.values()
												 .toArray(new String[pageHashtable.values().size()]);
				
		//Set up a place to put all our tokens:
		ArrayList concatenatedTokenArrayList = new ArrayList();
		
		//Add each token in order to it, placing <EOF> and <BOF> between each file.
		for (int i = 0; i < pages.length; i += 1) {
			String[] tokens = Tokenizer.tokenize(pages[i]);
			for (int x = 0; x < tokens.length; x += 1) {
				concatenatedTokenArrayList.add(tokens[x]);
			}
		}
		
		//Convert the ArrayList into an Array:
		String[] concatenatedTokens = (String[]) concatenatedTokenArrayList.toArray(new String[concatenatedTokenArrayList.size()]);
		
		//Get an alphabet from our tokens:
		String[] uncutAlphabet = Tokenizer.getAlphabet(concatenatedTokens, ALPHABET_SIZE);
		
		//If we didn't fill the alphabet, shorten it:
		String[] alphabet = new String[uncutAlphabet.length];
		for (int i = 0; i < uncutAlphabet.length; i += 1) {
			if (uncutAlphabet[i] == null) {
				//If the ith uncutAlphabet element is null, then we don't care
				//about it or any element beyond:
				alphabet = new String[i];
				break;
			}
		}
				
		//Copy over the first part of uncutAlphabet.
		for (int i = 0; i < alphabet.length; i += 1) {
			alphabet[i] = uncutAlphabet[i];
		}
		
		//Split the tokens into front and back halves:
		String[] frontHalf = new String[concatenatedTokens.length/2];
		String[] backHalf = new String[concatenatedTokens.length/2 + (concatenatedTokens.length % 2 == 0 ? 0 : 1)];
		for (int i = 0; i < concatenatedTokens.length; i += 1) {
			if (i < concatenatedTokens.length/2) {
				frontHalf[i] = concatenatedTokens[i];
			}
			else {
				backHalf[i - concatenatedTokens.length/2] = concatenatedTokens[i];
			}
		}
				
		//Count bigrams and occurrences in the front half.
		Hashtable[] frontHalfCountsPacked = Counter.count(frontHalf, alphabet);
		Hashtable frontHalfOccurrence = frontHalfCountsPacked[0];
		Hashtable frontHalfBigrams = frontHalfCountsPacked[1];
		
		//Count bigrams and occurrences in the back half.
		Hashtable[] backHalfCountsPacked = Counter.count(backHalf, alphabet);
		Hashtable backHalfOccurrence = backHalfCountsPacked[0];
		Hashtable backHalfBigrams = backHalfCountsPacked[1];
		
		//Smooth the occurence counts.
		Hashtable smoothedCounts = Smoother.fullCrossSmoothing(frontHalfOccurrence, backHalfOccurrence, ALPHABET_SIZE);
		
		//Smooth the bigram counts.
		Hashtable smoothedBigrams = new Hashtable();
		for (int i = 0; i < alphabet.length; i += 1) {
			smoothedBigrams.put(
					alphabet[i],
					Smoother.fullCrossSmoothing(
							(Hashtable)frontHalfBigrams.get(alphabet[i]),
							(Hashtable)backHalfBigrams.get(alphabet[i]),
							ALPHABET_SIZE
					)
			);
		}
		
		//Serialize and write the counts to json files
		ReadandWrite.writeString(JSON.serialize(smoothedCounts), OCCURRENCE_TARGET_PATH);
		ReadandWrite.writeString(JSON.serialize(smoothedBigrams), BIGRAM_TARGET_PATH);
	}
}