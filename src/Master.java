class Manifester {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] pages = Fetcher.getRandomPageTexts(30);
		String[][] words;
		for (int i=0;i<pages.length;i++){
			words = Tokenizer.fullTokenization(pages[i], 3000);
		}
	}
}