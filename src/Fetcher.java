import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class Fetcher {
	private static DocumentBuilderFactory XMLParserFactory = DocumentBuilderFactory.newInstance();
	
	private static abstract class AnonymousFunction {
		
		private static final AnonymousFunction ECHOER = new AnonymousFunction() {
			public Object call(Object... args) {
				return args[0];
			}
		};
		
		//Super simple implementation of something that can act like an anonymous function
		public abstract Object call(Object... args);
	}
	
	private static Node getFirstChildWithName(Node node, String name) {
		/*
		 * Given an xml element (node) and a node name
		 * (name), return the first child of (node) that has
		 * the name (name), or null if nonexistent.
		 */

		//Get the children of node:
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i += 1) {
			if (children.item(i).getNodeName() == name) {
				//For each child, if the child has the desired name,
				//return it.
				return children.item(i);
			}
		}
		
		//If no child was returned, return null.
		return null;
	}
	
	private static class simpleNodeList implements NodeList {
		private Node[] nodes;
		
		public simpleNodeList(Node[] nodes) {
			this.nodes = nodes;
		}
		
		public int getLength() {
			return nodes.length;
		}
		
		public Node item(int i) {
			return nodes[i];
		}
	}
	
	private static NodeList getChildrenWithName(Node node, String name) {
		/*
		 * Given an xml element (node), and a node name
		 * (name), return a NodeList of all the children of
		 * (node) with name (name).
		 */
		
		NodeList children = node.getChildNodes();
		ArrayList filteredChildren = new ArrayList();
		for (int i = 0; i < children.getLength(); i += 1) {
			if (children.item(i).getNodeName() == name) {
				filteredChildren.add(children.item(i));
			}
		}
		return new simpleNodeList((Node[])filteredChildren.toArray());
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
	
	private static String[] getRandomTitles(int n, int namespace, AnonymousFunction filter) {
		/*
		 * Given a number (n) of random ids to fetch,
		 * get (n) ids corresponding to real Wikipedia
		 * pages.
		 */
		
		try {
			ArrayList titles = new ArrayList();
			for (int i = 0; i <= n/20; i += 1) {
				//Make a request to Wikipedia's API for a list of random pages in XML:
				Document list = XMLRequest("http://en.wikipedia.org/w/api.php?format=xml&"+
										   "action=query&list=random&" +
										   "rnnamespace=" + namespace + "&" +
										   "rnlimit=" + ((i + 1) * 20  < n ? 20 : n % 20));
				
				//Get all the pages in a NodeList:
				NodeList pages = list.getElementsByTagName("page");
				
				//For each page, get its id and save it in "ids":
				for (int x = 0; x < pages.getLength(); x += 1) {
					titles.add(pages.item(i).getAttributes().getNamedItem("title").getNodeValue());
				}
			}
			
			//Run our filter over titles and convert to an array, then return.
			return (String[])((ArrayList)filter.call(titles)).toArray();
		}
		catch (Exception e) {
			//Debugging
			System.out.println("Exception in getRandomIDs:" + e.getMessage());
		}
		
		//If exception was thrown, return null.
		return null;
	}
	
	
	private static String[] getRandomTitles(int n, int namespace) {
		return getRandomTitles(n, namespace, new AnonymousFunction() {
			public Object call(Object... args) {
				return args[0];
			}
		});
	}
	
	private static String[][] getPageRevisions(String[] titles, int revs, AnonymousFunction filter) {
		/*
		 * Given an array of page ids (ids),
		 * gets the texts of those pages.
		 */
		
		try {
			String[] blocksOfFifty = new String[titles.length/50 + (titles.length % 50 == 0 ? 0 : 1)];
			ArrayList pageTexts = new ArrayList();
			
			//Separate the page ids into blocks of fifty.
			for (int i = 0; i < titles.length; i += 1) {
				if (i%50 == 0) {
					blocksOfFifty[i/50] = "";
				}
				else {
					blocksOfFifty[i/50] += "|";
				}
				
				blocksOfFifty[i/50] += titles[i];
			}
			
			//Get the content of each page.
			for (int i = 0; i < blocksOfFifty.length; i += 1) {
				//Make the request for fifty pages and parse:
				Document xml = XMLRequest("http://en.wikipedia.org/w/api.php?format=xml&" +
									      "action=query&prop=revisions&rvprop=content&" +
									      "rvlimit=" + revs + "&" +
									      "titles=" + URLEncoder.encode(blocksOfFifty[i]));
				
				//Extract the pages from the response:
				NodeList pages = xml.getElementsByTagName("page");
				
				//Now that we've got our pages, get the content of each one.
				for (int x = 0; x < pages.getLength(); x += 1) {
					//Search the pages' child nodes for a node named "revisions"
					Node revisions = getFirstChildWithName(pages.item(x), "revisions");
					
					//Search that child node for a node named "rev"
					NodeList revElements = getChildrenWithName(revisions, "rev");
					ArrayList revTexts = new ArrayList();
					
					for (int z = 0; z < revElements.getLength(); z += 1) {
						revTexts.add(revElements.item(z).getChildNodes().item(0).getNodeValue());
					}
					
					pageTexts.add(revTexts.toArray());
				}
			}
			
			//Run our filter over pageTexts and convert it to an array, then return.
			return (String[][])((ArrayList)filter.call(pageTexts)).toArray();
		}
		catch (Exception e) {
			System.out.println("Exception in getPageTexts:" + e);
		}
		
		//If exception was thrown, return null.
		return null;
	}
	
	private static String[][] getPageRevisions(String[] titles, int revs) {
		return getPageRevisions(titles, revs, AnonymousFunction.ECHOER);
	}
	
	public static String[] getPageTexts(String[] titles, AnonymousFunction filter) {
		String[][] packed = getPageRevisions(titles,1,filter);
		String[] result = new String[packed.length];
		for (int i = 0; i < packed.length; i += 1) {
			result[i] = packed[i][0];
		}
		return result;
	}
	
	public static String[] getPageTexts(String[] titles) {
		return getPageTexts(titles, AnonymousFunction.ECHOER);
	}
	
	public static String[][] getRandomPageRevisions(int n, int namespace, int revs, AnonymousFunction titleFilter, AnonymousFunction textFilter) {
		return getPageRevisions(getRandomTitles(n, namespace, titleFilter), revs, textFilter);
	}
	
	public static String[][] getRandomPageRevisions(int n, int namespace, int revs) {
		return getPageRevisions(getRandomTitles(n, namespace), revs);
	}
	
	public static String[] getRandomPageTexts(int n, int namespace) {
		return getPageTexts(getRandomTitles(n, namespace));
	}
	
	private static String[][] getRandomArticleWithTalkQualification(int n, int revs, final AnonymousFunction qualifier) {
		/*
		 * Given an int (n), a number of revisions (revs),
		 * and a qualification AnonymousFunction (qualifier) that accepts
		 * a string and returns a boolean, makes requests for (rev)
		 * revisions of (n) pages, and returns only those whose talk pages
		 * fit (qualifier).
		 */

		
		AnonymousFunction titleFilter = new AnonymousFunction() {
			public Object call(Object... args) {
				//Clone the first argument to avoid namespace clashes,
				//and call it "titles" to be convenient.
				ArrayList titles = (ArrayList) ((ArrayList)args[0]).clone();
				
				String[] talkTitles = new String[titles.size()];
				
				//Get the talk page titles for each title.
				for (int i = 0; i < titles.size(); i += 1) {
					talkTitles[i] = "Talk:" + titles.get(i);
				}
				
				//Get the texts of the talk pages.
				String[] talkPages = getPageTexts(talkTitles, AnonymousFunction.ECHOER);
				
				//Remove the titles for all the articles that are not good.
				for (int i = 0; i < talkPages.length; i += 1) {
					if (!(Boolean)qualifier.call(talkPages[i])) {
						titles.remove(i);
					}
				}
				
				return titles;
			}
		};
		
		return getRandomPageRevisions(n, 0, revs, titleFilter, AnonymousFunction.ECHOER);
	}
}