/*
 * @author David Anthony Bau
 */
import java.util.*;
class Master {
	public static Hashtable fullAnalysis(String[] concatenatedTokens, String[] alphabet)
	{
		/*
		 * Does the entire analysis sequence.
		 * Requires (0) number of articles to fetch,
		 * (1) alphabet size, (2) target path for occurrence counts,
		 * and (3) target path for bigram counts.
		 */
				
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
				
		//Smooth the occurrence counts.
		Hashtable smoothedCounts = Smoother.fullCrossSmoothing(frontHalfOccurrence, backHalfOccurrence, alphabet.length);
		
		//Smooth the bigram counts.
		Hashtable smoothedBigrams = new Hashtable();
		for (int i = 0; i < alphabet.length; i += 1) {
			smoothedBigrams.put(
					alphabet[i],
					Smoother.fullCrossSmoothing(
							(Hashtable)frontHalfBigrams.get(alphabet[i]),
							(Hashtable)backHalfBigrams.get(alphabet[i]),
							alphabet.length
					)
			);
		}
		
		//Put our results into one Hashtable with keys "occurrence" and "bigrams."
		Hashtable result =  new Hashtable();
		result.put("occurrence", smoothedCounts);
		result.put("bigrams", smoothedBigrams);
		
		return result;
	}
	public static void main(String[] args) {
		final int NUMBER_OF_ARTICLES = Integer.parseInt(args[0]);
		final int ALPHABET_SIZE = Integer.parseInt(args[1]);
		final String ALL_TARGET_PATH = args[2];
		final String GOOD_TARGET_PATH = args[3];
		
		//Fetch random pages and parse:
		Hashtable pageHashtable = Fetcher.getRandomPageTexts(NUMBER_OF_ARTICLES,0);
		String[] pages = (String[]) pageHashtable.values()
												 .toArray(new String[pageHashtable.values().size()]);
		
		//Extract good articles from our random pages:
		Hashtable goodArticleHashtable = Fetcher.extractGoodArticles(pageHashtable);
		String[] goodArticles = (String[]) goodArticleHashtable.values()
															   .toArray(new String[goodArticleHashtable.values().size()]);
		
		//Get the probability that any article is good, saved for later:
		double goodArticleDensity = goodArticles.length / pages.length;
		
		//Set up a place to put all our tokens:
		ArrayList concatenatedTokenArrayList = new ArrayList();
		
		//Add each token in order to it, placing <EOF> and <BOF> between each file.
		for (int i = 0; i < pages.length; i += 1) {
			String[] tokens = Tokenizer.tokenize(pages[i]);
			for (int x = 0; x < tokens.length; x += 1) {
				concatenatedTokenArrayList.add(tokens[x]);
			}
		}
		
		//Do the same for good articles:
		ArrayList concatenatedGoodArticleArrayList = new ArrayList();
		
		for (int i = 0; i < goodArticles.length; i += 1) {
			String[] tokens= Tokenizer.tokenize(goodArticles[i]);
			for (int x = 0; x < tokens.length; x += 1) {
				concatenatedGoodArticleArrayList.add(tokens[x]);
			}
		}
		
		//Convert the ArrayLists into an arrays:
		String[] concatenatedPages = (String[]) concatenatedTokenArrayList.toArray(new String[concatenatedTokenArrayList.size()]);
		String[] concatenatedGoodArticles = (String[]) concatenatedGoodArticleArrayList.toArray(new String[concatenatedGoodArticleArrayList.size()]);
		
		//Get an alphabet from our tokens:
		String[] uncutAlphabet = Tokenizer.getAlphabet(concatenatedPages, ALPHABET_SIZE);
		
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
				
		//Copy over the first (non-null) part of uncutAlphabet.
		for (int i = 0; i < alphabet.length; i += 1) {
			alphabet[i] = uncutAlphabet[i];
		}

		//Run the full analysis sequence on all our pages:
		Hashtable pagesResult = fullAnalysis(concatenatedPages, alphabet);
		
		//Do the same same with only good articles:
		Hashtable goodArticlesResult = fullAnalysis(concatenatedGoodArticles, alphabet);

		pagesResult.put("num", concatenatedPages.length + 0.0);
		goodArticlesResult.put("num", concatenatedGoodArticles.length + 0.0);
		
		//Serialize and write the counts to json files
		ReadandWrite.writeString(JSON.serialize(pagesResult), ALL_TARGET_PATH);
		ReadandWrite.writeString(JSON.serialize(goodArticlesResult), GOOD_TARGET_PATH);
	}
}