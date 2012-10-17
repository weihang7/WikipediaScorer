/*
 * @author David Anthony Bau
 */
class Master {
	public static void main(String[] args) {
		//Accept some arguments.
		final int DATA_SIZE = Integer.parseInt(args[0]);
		final int ALPHABET_SIZE = Integer.parseInt(args[1]);
		final String STORE_LOCATION = args[2];
		
		//Fetch:
		String[] allArticles = Fetcher.getRandomArticles(DATA_SIZE);
		String[] goodArticles = Fetcher.getGoodArticles(DATA_SIZE);
		
		//Concatenate:
		String allConcat = "";
		String goodConcat = "";
		
		for (int i = 0; i < allArticles.length; i += 1) {
			allConcat += allArticles[i];
		}
		for (int i = 0; i < goodArticles.length; i += 1) {
			goodConcat += goodArticles[i];
		}
		
		//Tokenize:
		String[] all = Tokenizer.tokenize(allConcat);
		String[] good = Tokenizer.tokenize(goodConcat);
		
		//Put all the tokens in one place to get the alphabet for
		//the whole thing:
		//TODO: Inefficient! change Tokenizer to accept both all and good
		//simultaneously
		String[] consummate = new String[all.length + good.length];
		for (int i = 0; i < all.length; i += 1) {
			consummate[2 * i] = all[i];
			consummate[2 * i + 1] = good[i];
		}
		
		//Get alphabet:
		String[] alphabet = Tokenizer.getAlphabet(consummate, ALPHABET_SIZE);
		
		//Strip down to only alphabetic tokens:
		all = Tokenizer.stripTokens(all, alphabet);
		good = Tokenizer.stripTokens(good, alphabet);
				
		//Divide the general corpus into two:
		String[][] allDivided = {new String[all.length / 2],
				new String[all.length / 2 + (all.length % 2 == 0 ? 0 : 1)]};
		for (int i = 0; i < all.length; i += 1) {
			if (i < all.length / 2) {
				allDivided[0][i] = all[i];
			}
			else {
				allDivided[1][i - all.length / 2] = all[i];
			}
		}
		
		//Divide the good corpus into two:
		String[][] goodDivided = {new String[good.length / 2],
				new String[good.length / 2 + (good.length % 2 == 0 ? 0 : 1)]};
		for (int i = 0; i < good.length; i += 1) {
			if (i < good.length / 2) {
				goodDivided[0][i] = good[i];
			}
			else {
				goodDivided[1][i - good.length / 2] = good[i];
			}
		}
		
		//Count:
		Count[] allCounts = {Counter.count(allDivided[0], alphabet),
				Counter.count(allDivided[1], alphabet)};
		Count[] goodCounts = {Counter.count(goodDivided[0], alphabet),
				Counter.count(goodDivided[1], alphabet)};
		
		//Smooth:
		Count allSmooth = Smoother.fullCountSmoothing(allCounts[0], allCounts[1], alphabet.length);
		Count goodSmooth = Smoother.fullCountSmoothing(goodCounts[0], goodCounts[1], alphabet.length);
		
		//Input proper file ratios (hardcoded; gotten from
		//http://en.wikipedia.org/wiki/Wikipedia:Good articles)
		//TODO: Fetch these numbers dynamically, maybe with a crawler.
		allSmooth.num = 4076900;
		goodSmooth.num = 15949;
		
		//Serialize:s
		//TODO: get JSON.java to do all of this (reduce to one method call):
		String jsonToStore = "{"
				+ "\"all\":"
				+ JSON.serializeCount(allSmooth)
				+ "\"good\":"
				+ JSON.serializeCount(goodSmooth)
				+ "}";
		
		//Write to file:
		ReadandWrite.writeString(jsonToStore, STORE_LOCATION);
	}
}