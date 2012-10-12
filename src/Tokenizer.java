import bliki.wiki.filter.*;
import bliki.wiki.model.*;
import java.util.*;

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
							 .replaceAll(" +"," ") //Remove duplicatespaces
							 .replaceAll("(?: ?\\. ?)+"," . ") //Remove duplicate periods
							 .split(" "); //Split by spaces.
		
		return tokens;
	}
	
	public static String[] getAlphabet(String[] text, int size) {
		/*
		 * Given an array of tokens (text), output
		 * the (size) most common tokens, for use as
		 * an alphabet in later Markov counting.
		 */
		
		//Initiate the table where we'll store our counts:
		Hashtable counts = new Hashtable();
		
		//Initiate the alphabet array:
		String[] alphabet = new String[size];
		
		for (int i = 0; i < text.length; i += 1) {
			if (counts.containsKey(text[i])) {
				//If we've seen this word before, add one to our count of it:
				counts.put(text[i],(Integer)counts.get(text[i]) + 1);
			}
			else {
				//Otherwise, intiate the count of this word as 1.
				counts.put(text[i], 1);
			}
			for (int x = 0; x < size; x += 1) {
				if (alphabet[x] == null) {
					//If the alphabet array is not full yet, add this token to it:
					alphabet[x] = text[i];
				}
				else if ((Integer)counts.get(text[i]) > (Integer)counts.get(alphabet[x])) {
					//If it is full, but the current word is now more common than a word already in it, replace
					//the less common word with the current one.
					alphabet[x] = text[i];
				}
			}
		}
		
		return alphabet;
	}
	
	public static String[] stripTokens(String[] text, String[] alphabet) {
		/*
		 * Given a string of tokens (text), and an
		 * alphabet (alphabet), return (text) with every word
		 * not in (alphabet) replaced with "*."
		 */
		
		//Clone text so that we don't effect it:
		String[] textToReturn = text.clone();

		for (int i = 0; i < text.length; i += 1) {
			//Initiate the delete flag for this token to be on:
			boolean delete = true;
			
			for (int x = 0; x < alphabet.length; x += 1) {
				if (alphabet[x] == textToReturn[i]) {
					//If the token is in the alphabet, turn off the delete flag:
					delete = false;
					break;
				}
			}
			
			if (delete) {
				//If the token is still flagged for deletion, replace with "*".
				textToReturn[i] = "*";
			}
		}
		
		return textToReturn;
	}
	
	public static String[][] fullTokenization(String text, int alphabetSize) {
		/*
		 * Given a Wikipedia template string (text) and an alphabet size
		 * (alphabetSize), tokenizes (text), gets the alphabet of size
		 * (alphabetSize) from it, and strips (text) using that alphabet.
		 */
		
		//Initiate a an array of string arrays that will utimately contain
		//the stripped tokens and the alphabet:
		String[][] returnValue = new String[0][2];
		
		returnValue[0] = tokenize(text); //First, tokenize the text and store it
		returnValue[1] = getAlphabet(returnValue[0],alphabetSize); //Then get the alphabet for that token string
		returnValue[0] = stripTokens(returnValue[0],returnValue[1]); //Then strip the token string using that alphabet.
		
		return returnValue;
	}

}