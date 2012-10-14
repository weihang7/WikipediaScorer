import java.io.*;
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
		String[] pages = Fetcher.getRandomPageTexts(NUMBER_OF_ARTICLES,0,1);

		//Set up a place to put all our tokens:
		ArrayList concatenatedTokenArrayList = new ArrayList();
		
		//Add each token in order to it, placing <EOF> and <BOF> between each file.
		for (int i = 0; i < pages.length; i += 1) {
			String[] tokens = Tokenizer.tokenize(pages[i]);
			for (int x = 0; x < tokens.length; x += 1) {
				concatenatedTokenArrayList.add(tokens[i]);
			}
		}
		
		//Convert the ArrayList into an Array:
		String[] concatenatedTokens = (String[]) concatenatedTokenArrayList.toArray(new String[concatenatedTokenArrayList.size()]);
		
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
		Hashtable[] frontHalfCountsPacked = Counter.count(frontHalf);
		Hashtable frontHalfOccurrence = frontHalfCountsPacked[0];
		Hashtable frontHalfBigrams = frontHalfCountsPacked[1];
		
		//Count bigrams and occurrences in the back half.
		Hashtable[] backHalfCountsPacked = Counter.count(backHalf);
		Hashtable backHalfOccurrence = backHalfCountsPacked[0];
		Hashtable backHalfBigrams = backHalfCountsPacked[1];
		
		//Smooth the occurence counts.
		Hashtable smoothedCounts = Smoother.fullCrossSmoothing(frontHalfOccurrence, backHalfOccurrence, ALPHABET_SIZE);
		
		//Smooth the bigram counts.
		Hashtable smoothedBigrams = new Hashtable();
		for (int i = 0; i < ALPHABET_SIZE.length; i += 1) {
			smoothedBigrams.put(
					ALPHABET_SIZE[i],
					Smoother.fullCrossSmoothing(
							(Hashtable)frontHalfBigrams.get(ALPHABET_SIZE[i]),
							(Hashtable)backHalfBigrams.get(ALPHABET_SIZE[i]),
							ALPHABET_SIZE.length
					)
			);
		}
		
		FileWriter occurrenceFileWriter = new FileWriter(OCCURRENCE_TARGET_PATH);
		FileWriter bigramsFileWriter = new FileWriter(BIGRAMS_TARGET_PATH);
		
		occurrenceFileWriter.write(JSON.serialize(smoothedCounts));
		bigramsFileWriter.write(JSON.serialize(smoothedBigrams));
	}
}