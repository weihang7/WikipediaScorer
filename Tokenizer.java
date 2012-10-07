import WikipediaTemplateParser.info.bliki.wiki.filter.PlainTextConverter;
import WikipediaTemplateParser.info.bliki.wiki.model.WikiModel;
import java.util.*;

public class Tokenizer {
	static WikiModel wikiModel =  new WikiModel("http://www.mywiki.com/wiki/${image}",
												"http://www.mywiki.com/wiki/${title}");
	public static String[] tokenize(String templatedText) {
		String plaintext = wikiModel.render(new PlainTextConverter(),templatedText);
		String[] tokens =  plaintext.toLowerCase()
							 .replaceAll("\\{\\{[^\\{\\}]*\\}\\}","")
							 .replaceAll("\\n+",".")
							 .replaceAll("[^\\w\\. ]","")
							 .replaceAll(" +"," ")
							 .replaceAll("(?: ?\\. ?)+"," . ")
							 .split(" ");
		return tokens;
	}
	
	public static String[] getAlphabet(String[] text, int size) {
		Hashtable counts = new Hashtable();
		String[] alphabet = new String[size];
		for (int i = 0; i < text.length; i += 1) {
			if (counts.containsKey(text[i])) {
				counts.put(text[i],(Integer)counts.get(text[i]) + 1);
			}
			else {
				counts.put(text[i], 1);
			}
			for (int x = 0; x < size; x += 1) {
				if (alphabet[x] == null) {
					alphabet[x] = text[i];
				}
				else if ((Integer)counts.get(text[i]) > (Integer)counts.get(alphabet[x])) {
					alphabet[x] = text[i];
				}
			}
		}
		return alphabet;
	}
	
	public static String[] stripTokens(String[] text, String[] alphabet) {
		String[] textToReturn = text.clone();
		for (int i = 0; i < text.length; i += 1) {
			boolean delete = true;
			for (int x = 0; x < alphabet.length; x += 1) {
				if (alphabet[x] == textToReturn[i]) {
					delete = false;
					break;
				}
			}
			if (delete) {
				textToReturn[i] = "*";
			}
		}
		return textToReturn;
	}
	
	public static String[][] fullTokenization(String text, int alphabetSize) {
		String[][] returnValue = new String[2][2];
		returnValue[0] = tokenize(text);
		returnValue[1] = getAlphabet(returnValue[0],alphabetSize);
		returnValue[0] = stripTokens(returnValue[0],returnValue[1]);
		return returnValue;
	}
	
	public static void main(String[] args) {
		String[] tokens = Fetcher.getRandomPageTexts(Integer.decode(args[0]));
		tokens = tokenize(tokens[0]);
		for (int i = 0; i < tokens.length; i += 1) {
			System.out.print(tokens[i] + " ");
		}
	}
}