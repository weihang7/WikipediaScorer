import java.net.*;
import javax.xml.parsers.*;
import java.io.*;
import org.w3c.dom.*;

class Fetcher {
	private static DocumentBuilderFactory XMLParserFactory = DocumentBuilderFactory.newInstance();
	
	private static Node getFirstChildWithName(Node node, String name) {
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i += 1) {
			if (children.item(i).getNodeName() == name) {
				return children.item(i);
			}
		}
		return null;
	}
	
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
			Document list = XMLRequest("http://en.wikipedia.org/w/api.php?format=xml&"+
									   "action=query&list=random&rnnamespace=0&" +
									   "rnlimit=" + n);
			
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
			String[] blocksOfFifty = new String[50];
			String[] pageTexts = new String[ids.length];
			
			//Separate the page ids into blocks of fifty.
			for (int i = 0; i < ids.length; i += 1) {
				if (i%50 == 0) {
					blocksOfFifty[i/50] = "";
				}
				else {
					blocksOfFifty[i/50] += "|";
				}
				
				blocksOfFifty[i/50] += ids[i];
			}
			
			//Get the content of each page. First, we make a marker
			for (int i = 0; i < blocksOfFifty.length; i += 1) {
				//Make the request for fifty pages and parse:
				Document xml = XMLRequest("http://en.wikipedia.org/w/api.php?format=xml&"+
									      "action=query&prop=revisions&rvprop=content&"+
									      "pageids="+blocksOfFifty[i]);

				//Extract the pages from the response:
				NodeList pages = xml.getElementsByTagName("page");
												
				//Now that we've got our pages, get the content of each one.
				for (int x = 0; x < pages.getLength(); x += 1) {
					//Search the pages' child nodes for a node named "revisions"
					Node revisions = getFirstChildWithName(pages.item(x), "revisions");
					
					//Search that child node for a node named "rev"
					Node rev = getFirstChildWithName(revisions, "rev");
					
					//Put the content of that node into pageTexts.
					pageTexts[i*50 + x] = rev.getChildNodes().item(0).getNodeValue();
				}
				
				return pageTexts;
			}
			
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