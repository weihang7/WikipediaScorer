import java.net.*;
import javax.xml.parsers.*;
import java.io.*;
import org.w3c.dom.*;

class Fetcher {
	private static DocumentBuilderFactory XMLParserFactory = DocumentBuilderFactory.newInstance();
	
	private static Document XMLRequest(String url) {
		/*
		 * Given a URL in string form (url),
		 * make a request to the url and parse
		 * its response text as XML, returning 
		 * it as a Document object.
		 */	
		
		try {
			//Set up a new URL object pointing to the desired URL:
			URL urlToRequest = new URL(url);
			
			//Connect to it:
			URLConnection connection = urlToRequest.openConnection();
			
			//Instantiate an XML Parser (DocumentBuilder) to parse the response:
			DocumentBuilder XMLParser = XMLParserFactory.newDocumentBuilder();
			
			//Parse the response and return.
			return XMLParser.parse(connection.getInputStream());
		}
		catch (Exception e) {
			System.out.println("Exception in XMLRequest:" + e.getMessage());
		}
		return null;
	}
	
	private static int[] getRandomIDs(int n) {
		/*
		 * Given a number (n) of random ids to fetch,
		 * get (n) ids corresponding to real Wikipedia
		 * pages.
		 */
		
		try {
			int[] ids = {};

			//Make a request to Wikipedia's API for a list of random pages in XML:
			Document list = XMLRequest("http://en.wikipedia.org/w/api.php?format=xml&action=query&list=random&rnlimit=" + n);
			
			//Get all the pages in a NodeList:
			NodeList pages = list.getElementsByTagName("page");
			
			//For each page, get its id and save it in "ids":
			ids = new int[pages.getLength()];
			for (int i = 0; i < pages.getLength(); i += 1) {
				ids[i] = Integer.valueOf(
					pages.item(i).getAttributes().getNamedItem("id").getNodeValue()
				);
			}
			
			return ids;
		}
		catch (Exception e) {
			//Debugging
			System.out.println("Exception in getRandomIDs:" + e.getMessage());
		}
		//If exception was thrown, return null.
		return null;
	}
	
	private static String[] getPageTexts(int[] ids) {
		/*
		 * Given an array of page ids (ids),
		 * gets the texts of those pages.
		 */
		
		try {
			String[] pageTexts = new String[ids.length];
			
			//For each page, request the current revision, get the text from the
			//XML, and put it into pageTexts.
			for (int i = 0; i < ids.length; i += 1) {
				Document response = XMLRequest("http://en.wikipedia.org/w/api.php?format=xml&action=query&prop=revisions&rvprop=content&pageids="+ids[i]);
				pageTexts[i] = response.getElementsByTagName("rev").item(0).getChildNodes().item(0).getNodeValue();
			}
			
			return pageTexts;
		}
		catch (Exception e) {
			System.out.println("Exception in getPageTexts:" + e.getMessage());
		}
		
		//If exception was thrown, return null.
		return null;
	}
	
	public static void main(String args[]) {
		String[] pageTexts = getPageTexts(getRandomIDs(Integer.valueOf(args[0]).intValue()));
		for (int i = 0; i < pageTexts.length; i += 1) {
			System.out.println(pageTexts[i]);
		}
	}
}