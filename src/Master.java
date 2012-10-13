import java.util.*;
class Master {
	public static void main(String[] args) {
		// get parsed xml into pages
		String[] pages = Fetcher.getRandomPageTexts(30);
		//declare variable words
		ArrayList words = new ArrayList(pages.length);
		//Split the pages into tokens.
		for (int i=0;i<pages.length;i++){
			words.add(Tokenizer.fullTokenization(pages[i], 3000));
		}
		//concatenate the pages
		ArrayList ret = new ArrayList(words.size());
		for (int i=0;i<words.size();i++){
			for (int k=0;i<words.size();i++){
				ret.add(words.get(i)[k]);
			}
		}
	
	}
}