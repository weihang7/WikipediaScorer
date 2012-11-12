/*
 * @author David Anthony Bau
 */

import java.io.Writer;
import java.util.Enumeration;
import java.sql.SQLException;

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
  
  public static void stripTokens(Enumeration<String> input, Writer output, String[] alphabet) {
    /*
     * Given a string of tokens (text), and an
     * alphabet (alphabet), return (text) with every word
     * not in (alphabet) replaced with "*."
     */
    
    for (; input.hasMoreElements(); ) {
      String token = input.nextElement();
      for (int x = 0; x < alphabet.length; x += 1) {
        if (token == alphabet[x]) {
          try {
           output.write(Integer.toString(x + 1) + (input.hasMoreElements() ? " " : "")); 
          }
          catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
  }
  
  public static int[] stripTokens(String[] tokenArray, String[] alphabet){
    int[] ret = new int[tokenArray.length];
    for(int i=0;i<ret.length;i++){
      for(int k=0;k<alphabet.length;k++){
        if(tokenArray[i]==alphabet[k]){
          ret[i]=k+1;
        }
        if(k==alphabet.length-1&&tokenArray[i]!=alphabet[k]){
          ret[i]=0;
        }
      }
    }
    return ret;
  }

  public static void countForAlphabet(DataSet.BufferedDatabaseWriter writer, Enumeration<String> document) throws SQLException {
    while(document.hasMoreElements()) {
      writer.add(document.nextElement(), 1);
    }
    writer.flush();
  }
}
