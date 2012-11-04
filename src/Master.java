/*
* @author Anthony Bau
*/
class Master {
  public static void main(String[] args) {
    final int DATA_SIZE = Integer.parseInt(args[0]);
    final int ALPHABET_SIZE = Integer.parseInt(args[1]);
    final String BASE_FILE_NAME = args[2];

    DataSet data = new DataSet(BASE_FILE_NAME);
      
    //FETCHING AND INITIAL TOKENIZATION:
    
    //Get string writers for the files we'll save these to:
    StringWriter goodLocalWiki = data.writeWiki(true);
    StringWriter badLocalWiki = data.writeWiki(false);
    
    //Make a new Fetcher to fetch our data:
    Fetcher fetcher = new Fetcher();

    for (int i = 0; i + 50 <= args[0]; i += 50) {

      //Fetch 50 random (probably bad) pages:
      String[] random = fetcher.getRandom(50);
      for (int x = 0; x < 50; x += 1) {
        //Tokenize each of them:
        String[] tokenized = Tokenizer.tokenize(random[x]);
        for (int t = 0; t < tokenized.length; t += 1) {
          //Write each token to memory.
          badLocalWiki.write(" " + t);
        }
      }

      //Fetch 50 good pages:
      String[] good = fetcher.getGood(50);
      for (int x = 0; x < 50; x += 1) {
        //Tokenize each of them:
        String[] tokenized = Tokenize.tokenize(good[x]);
        for (int t = 0; t < tokenized.length; t += 1) {
          //Write each token to memory.
          goodLocalWiki.write(" " + tokenized[t]);
        }
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
  double[][] goodCounts = Counter.count(data.readTokens(true));
  double[][] badCounts = Counter.count(data.readTokens(false));

  //SMOOTHING:
  goodCounts = Smoother.smooth(goodCounts);
  badCounts = Smoother.smooth(badCounts);

  //Store smoothed counts:
  data.add(true, goodCounts);
  data.add(false, badCounts);
}
