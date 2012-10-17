import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;

/**
 * @author David Anthony Bau
 */
public class NewFetcher {

	private static DocumentBuilderFactory XMLParserFactory = DocumentBuilderFactory
			.newInstance();
	
	private static String getAttributeOf(Node n, String attribute) {
		/*
		 * Given a node (n) and an attribute name (attribute), return
		 * the (n)'s (attribute) attribute.
		 * 
		 * (this is mainly here as shorthand)
		 */
		
		return n.getAttributes().getNamedItem(attribute).getNodeValue();
	}
	
	private static String getTextOf(Node n) {
		/*
		 * Given a node (n) with only one child,
		 * a text node, return the content of that
		 * text node.
		 * 
		 * (this is mainly here as shorthand)
		 */
		
		return n.getChildNodes().item(0).getNodeValue();
	}
	
	private static Document XMLRequest(String url) {
		/*
		 * Given a URL in string form (url), make a request to the url and parse
		 * its response text as XML, returning it as a Document object.
		 */

		try {
			
			// Set up a new URL object pointing to the desired URL:
			URL urlToRequest = new URL(url);
			
			// Connect to it:
			URLConnection connection = urlToRequest.openConnection();
			
			// Instantiate an XML Parser (DocumentBuilder) to parse the
			// response:
			DocumentBuilder XMLParser = XMLParserFactory.newDocumentBuilder();
	
			// Parse the response and return.
			return XMLParser.parse(connection.getInputStream());
		
		}
		catch (Exception e) {
			System.out.println("Exception in XMLRequest:" + e.getMessage());
		}
		return null;
	}
	
	private static Document makeWikipediaRequest(String... args) {
		//Start with everything we need for every request to the API,
		//plus the first argument:
		String url = "http://en.wikipedia.org/w/api.php?format=xml&action=query"
				   + args[0];
		
		//Add the rest of our CGI arguments to it, joined by "&"
		//and encoded for URLs:
		for (int i = 0; i < args.length; i += 1) {
			try {
				url += "&" + URLEncoder.encode(args[i], "UTF-8");
			}
			catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		return XMLRequest(url);
					 
	}
	
	private static int[] parseIds(Document xml) {
		/*
		 * Given a url pointing to a request for a list of pages from
		 * Wikipedia's api, parse the results and return the pageids of
		 * all the listed pages.
		 */
				
		//Get all the page elements in the xml:
		NodeList pages = xml.getElementsByTagName("page");
		
		//Set up the array to put our pageids in:
		int[] pageIds = new int[pages.getLength()];
		
		for (int i = 0; i < pages.getLength(); i += 1) {
			//Put each page's pageid into pageIds:
			pageIds[i] = Integer.parseInt(getAttributeOf(pages.item(i),"pageid"));
		}
		
		return pageIds;
	}
	
	private static int[] getRandomIds(int n) {
		/*
		 * Given an integer (n), return (n) random page ids
		 * corresponding to real Wikipedia pages.
		 */
		
		//Initialize an array of pageids with length n but nothing
		//in it:
		int[] pageids= new int[n];
		
		for (int i = 0; i < n / 20; i += 1) {
			//Get twenty random pageIds:
			int[] twentyIds = parseIds(makeWikipediaRequest("list=random",
					"rnlimit=20",
					"rnnamespace=0"));
		
			//Put those twenty ids into pageTexts:
			for (int x = 0; x < 20; x += 1) {
				pageids[i * 20 + x] = twentyIds[x];
			}
		}
		//Get the remaining needed ids:
		int[] moreIds = parseIds(
				makeWikipediaRequest("list=random",
						"rnlimit=" + n % 20,
						"rnnamespace=0"));
		
		//Add them all into pageids:
		for (int i = 0; i < moreIds.length; i += 1) {
			pageids[n - (n % 20) + i] = moreIds[i];
		}
		
		return pageids;
	}
	
	private static int[] getGoodArticleIds(int n) {
		/*
		 * Give a number (n) of good articles to fetch, fetches (n) good
		 * articles. WILL NOT fetch more than 500 good articles;
		 * will simply return 500 articles.
		 * 
		 * TODO: add functionality for more than 500 articles.
		 */
		
		return parseIds(makeWikipediaRequest("list=categorymembers",
							"cmname=Category:+Good+article",
							"cmlimit=" + n,
							"cmsort=timestamp," +
							"cminfo="));
	}
	
	private static String[] getPageTexts(int[] ids) {
		//Make an array to store blocks of fifty ids that has
		//the appropriate length to store the ids in blocks of fifty:
		String[] blocksOfFifty = new String[ids.length / 50 + 
		                                    (ids.length % 50 == 0 ? 0 : 1)];
		
		//Make the place to store the resultant page texts:
		String[] pageTexts = new String[ids.length];
		
		//Construct blocks of fifty ids:
		for (int i = 0; i < ids.length; i += 1) {
			if (i % 50 == 0)
				//If we're in a new block, start it as just this id:
				blocksOfFifty[i] = String.valueOf(ids[i]);
			else
				//Otherwise, separate from the last id with "|" and add this id.
				blocksOfFifty[i/50] += "|" + ids[i];
		}
		
		//Make the request to each block of fifty:
		for (int i = 0; i < blocksOfFifty.length; i += 1) {
			Document XML = null;
			
			try {
				//Make the request to Wikipedia for these fifty pages:
				XML = makeWikipediaRequest("prop=revisions",
						"rvprop=content",
						"rvlimit=1",
						"pageids" + URLEncoder.encode(blocksOfFifty[i], "UTF-8"));
			}
			catch (Exception e) {
				//If UTF-8 is not supported by URLEncoder, something has gone
				//terribly wrong.
				e.printStackTrace();
			}
			
			//Get all the rev elements, which will contain
			//the content of each page:
			NodeList revs = XML.getElementsByTagName("rev");
			
			//Put the text of each rev element into pageTexts.
			for (int x = 0; x < revs.getLength(); x += 1) {
				pageTexts[i * 50 + x] = getTextOf(revs.item(i));
			}
		}
		
		return pageTexts;
	}
	
	public static String[] getRandomArticles(int n) {
		int[] pageIds = getRandomIds(n);
		return getPageTexts(pageIds);
	}
	
	public static String[] getGoodArticles(int n) {
		int[] articleIds = getGoodArticleIds(n);
		return getPageTexts(articleIds);
	}
}
