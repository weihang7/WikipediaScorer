import java.util.Enumeration;
import java.io.BufferedWriter;

/*
* @author Anthony Bau
*/
class Master {
  public static void main(String[] args) throws Exception {
    final int DATA_SIZE = Integer.parseInt(args[0]);
    final int ALPHABET_SIZE = Integer.parseInt(args[1]);
    final String BASE_FILE_NAME = args[2];

    int goodLength = 0;
    int badLength = 0;
    
    DataSet data = new DataSet(BASE_FILE_NAME);
    data.initAlphabet();
    data.initCounts(ALPHABET_SIZE);
    
    //Hard-coded probability for good article.
    //TODO: Fetch dynamically.
    data.addNum(true, 4094871);
    data.addNum(false, 16083);
    
    //FETCHING AND INITIAL TOKENIZATION:
    
    //Get string writers for the files we'll save these to:
    BufferedWriter goodLocalWiki = data.writeWiki(true);
    BufferedWriter badLocalWiki = data.writeWiki(false);
    
    //Make a new Fetcher to fetch our data:
    Fetcher fetcher = new Fetcher();

    for (int i = 0; i + 50 <= DATA_SIZE; i += 50) {
      //Fetch 50 random (probably bad) pages:
      String[] random = fetcher.getRandom(50);
      for (int x = 0; x < 50; x += 1) {
        //Tokenize each of them:
        String[] tokenized = Tokenizer.tokenize(random[x]);
        goodLength += tokenized.length;
        for (int t = 0; t < tokenized.length; t += 1) {
          //Write each token to memory.
          badLocalWiki.write(" " + tokenized[t]);
        }
      }

      //Fetch 50 good pages:
      String[] good = fetcher.getGood(50);
      for (int x = 0; x < 50; x += 1) {
        //Tokenize each of them:
        String[] tokenized = Tokenizer.tokenize(good[x]);
        badLength += tokenized.length;
        for (int t = 0; t < tokenized.length; t += 1) {
          //Write each token to memory.
          goodLocalWiki.write(" " + tokenized[t]);
        }
      }
    }

      System.out.println("Counting alphabet.");

      //ALPHABET COUNTING:
      DataSet.BufferedDatabaseWriter alphabetWriter = data.writeAlphabetCounts();
      
      System.out.println("(good)");
      Tokenizer.countForAlphabet(alphabetWriter, data.readWiki(true));
      
      System.out.println("(bad)");
      Tokenizer.countForAlphabet(alphabetWriter, data.readWiki(false));

      
      System.out.println("Measuring best alphabet");
      //Get best alphabet:
      String[] alphabet = data.getBestAlphabet(ALPHABET_SIZE);
	  
      //Store it somewhere:
      data.finalizeAlphabet(alphabet);
	
      //Strip tokens:
      Tokenizer.stripTokens(data.readWiki(true), data.writeTokens(true), alphabet);
      Tokenizer.stripTokens(data.readWiki(false), data.writeTokens(false), alphabet);
	
      //COUNTING:
      Enumeration<Integer> readGoodTokens = data.readTokens(true);
      Enumeration<Integer> readBadTokens = data.readTokens(false);
      double[][][] goodCounts = {Counter.count(readGoodTokens, (goodLength + 1) / 2), Counter.count(readGoodTokens, goodLength / 2)};
      double[][][] badCounts = {Counter.count(readBadTokens, (badLength + 1) / 2), Counter.count(readBadTokens, badLength / 2)};
	
      //SMOOTHING:
      double[][] smoothGoodCounts = Smoother.smooth(goodCounts[0], goodCounts[1]);
      double[][] smoothBadCounts = Smoother.smooth(badCounts[0], badCounts[1]);
	
      //Store smoothed counts:
      data.add(true, smoothGoodCounts);
      data.add(false, smoothBadCounts);
  }
}
