import java.io.StringWriter;
import java.util.Enumeration;

/*
* @author Anthony Bau
*/
class Master {
  public static void main(String[] args) {
    final int DATA_SIZE = Integer.parseInt(args[0]);
    final int ALPHABET_SIZE = Integer.parseInt(args[1]);
    final String BASE_FILE_NAME = args[2];

    int goodLength;
    int badLength;
    
    DataSet data = new DataSet(BASE_FILE_NAME);
    
    //FETCHING AND INITIAL TOKENIZATION:
    
    //Get string writers for the files we'll save these to:
    StringWriter goodLocalWiki = data.writeWiki(true);
    StringWriter badLocalWiki = data.writeWiki(false);
    
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
          badLocalWiki.write(" " + t);
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


      //ALPHABET COUNTING:
      BufferedDatabaseWriter alphabetWriter = data.writeAlphabetCounts();
      Tokenizer.countForAlphabet(alphabetWriter, data.readWiki(true));
      Tokenizer.countForAlpahbet(alphabetWriter, data.readWiki(false));
	
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
      double[][] smoothgBadCounts = Smoother.smooth(badCounts[0], badCounts[1]);
	
      //Store smoothed counts:
      data.add(true, smoothGoodCounts);
      data.add(false, smoothBadCounts);
  }
}
