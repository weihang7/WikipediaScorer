/*
 * @author David Anthony Bau
 */

import info.bliki.wiki.filter.*;
import info.bliki.wiki.model.*;

public class Tokenizer {
  //Make a WikiModel at compile-time for rendering Wikipedia's templates.
  static WikiModel wikiModel =  new WikiModel("http://www.mywiki.com/wiki/${image}",
                                              "http://www.mywiki.com/wiki/${title}");
  
  public static String[] tokenize(String templatedText) {
    /*
     * Given some Wikipedia text (templatedText),
     * return the plaintext render of it, stripped
     * of punctuation except periods, lowercased,
     * and split by spaces.
     */
    
    //Use the bliki api to render the Wikipedia templating text:
    String plaintext = wikiModel.render(new PlainTextConverter(),templatedText);
    
    //Do our regex modifications to the resultant plaintext:
    String[] tokens =  plaintext.toLowerCase()
                      .replaceAll("\\{\\{[^\\{\\}]*\\}\\}","") //Take out all braced information, which doesn't show to the reader
                      .replaceAll("\\n+",".") //Replace line breaks with periods
                      .replaceAll("[^\\w\\. ]","") //Take out punctuation and special characters
                      .replaceAll(" +"," ") //Remove duplicate spaces
                      .replaceAll("(?: ?\\. ?)+"," . ") //Remove duplicate periods
                      .split(" "); //Split by spaces.
    return tokens;
  }
  
  public static int[] stripTokens(Enumeration<String> input, StringWriter output) {
    /*
     * Given a string of tokens (text), and an
     * alphabet (alphabet), return (text) with every word
     * not in (alphabet) replaced with "*."
     */
    
    for (; input.hasMoreElements(); ) {
      token = input.nextElement();
      for (int x = 0; x < alphabet.length; x += 1) {
        if (token == alphabet[x]) {
          output.append(Integer.toString(x + 1));
        }
      }
    }
  }

  public AlphabetCount countForAlphabet(String[] document){
    AlphabetCount ret = new AlphabetCount();
    for(int i = 0; i < document.length; i += 1){
      ret.add(document[i], 1);
    }
    return ret;
  }
}
