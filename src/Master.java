class Master {
	public static void main(String[] args) {
		// get parsed xml into pages
		String[] pages = Fetcher.getRandomPageTexts(30);
		//declare variable words
		String[][] words;
		//Split the pages into tokens.
		for (int i=0;i<pages.length;i++){
			words = Tokenizer.fullTokenization(pages[i], 3000);
		}
	}
}