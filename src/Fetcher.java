//author : Anthony Bau
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class Fetcher {
	private static DocumentBuilderFactory XMLParserFactory = DocumentBuilderFactory
			.newInstance();

	private static abstract class AnonymousFunction {

		private static final AnonymousFunction ECHOER = new AnonymousFunction() {
			public Object call(Object... args) {
				return args[0];
			}
		};

		// Super simple implementation of something that can act like an
		// anonymous function
		public abstract Object call(Object... args);
	}

	private static Node getFirstChildWithName(Node node, String name) {
		/*
		 * Given an xml element (node) and a node name (name), return the first
		 * child of (node) that has the name (name), or null if nonexistent.
		 */

		// Get the children of node:
		NodeList children = node.getChildNodes();
		
		for (int i = 0; i < children.getLength(); i += 1) {
			if (children.item(i).getNodeName() == name) {
				// For each child, if the child has the desired name,
				// return it.
				return children.item(i);
			}
		}

		// If no child was returned, return null.
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
		 * Given an xml element (node), and a node name (name), return a
		 * NodeList of all the children of (node) with name (name).
		 */

		//Get all the children of node:
		NodeList children = node.getChildNodes();
		
		//Initialize an empty ArrayList for our ultimate children:
		ArrayList filteredChildren = new ArrayList();
		
		//For each child, check whether it has the desired
		//name, and if so add it to filteredChildren:
		for (int i = 0; i < children.getLength(); i += 1) {
			if (children.item(i).getNodeName() == name) {
				filteredChildren.add(children.item(i));
			}
		}
		
		//Convert the ArrayList into an Array, make a NodeList out of it,
		//and return.
		return new simpleNodeList((Node[]) filteredChildren.toArray(
				new Node[filteredChildren.size()]
		));
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
		} catch (Exception e) {
			System.out.println("Exception in XMLRequest:" + e.getMessage());
		}
		return null;
	}

	private static String[] getRandomTitles(int n, int namespace) {
		/*
		 * Given a number (n) of random ids to fetch, get (n) ids corresponding
		 * to real Wikipedia pages.
		 */

		try {
			ArrayList titles = new ArrayList();
			for (int i = 0; i <= n / 20; i += 1) {
				// Make a request to Wikipedia's API for a list of random pages
				// in XML:
				Document list = XMLRequest("http://en.wikipedia.org/w/api.php?format=xml&"
						+ "action=query&list=random&"
						+ "rnnamespace="
						+ namespace
						+ "&"
						+ "rnlimit="
						+ ((i + 1) * 20 < n ? 20 : n % 20));

				// Get all the pages in a NodeList:
				NodeList pages = list.getElementsByTagName("page");

				// For each page, get its id and save it in "ids":
				for (int x = 0; x < pages.getLength(); x += 1) {
					titles.add(pages.item(x).getAttributes()
							.getNamedItem("title").getNodeValue());
				}
			}

			// Run our filter over titles and convert to an array, then return.
			return (String[]) titles.toArray(new String[titles.size()]);
		} catch (Exception e) {
			// Debugging
			System.out.println("Exception in getRandomTitles:" + e.getMessage());
		}

		// If exception was thrown, return null.
		return null;
	}

	private static Hashtable getPageRevisions(String[] titles, int revs,
			AnonymousFunction filter) {
		/*
		 * Given an array of page ids (ids), gets the texts of those pages.
		 */

		try {
			String[] blocksOfFifty = new String[titles.length / 50
					+ (titles.length % 50 == 0 ? 0 : 1)];
			Hashtable pageTexts = new Hashtable();

			// Separate the page ids into blocks of fifty.
			for (int i = 0; i < titles.length; i += 1) {
				if (i % 50 == 0) {
					blocksOfFifty[i / 50] = "";
				} else {
					blocksOfFifty[i / 50] += "|";
				}

				blocksOfFifty[i / 50] += titles[i];
			}

			// Get the content of each page.
			for (int i = 0; i < blocksOfFifty.length; i += 1) {
				// Make the request for fifty pages and parse:
				Document xml = XMLRequest("http://en.wikipedia.org/w/api.php?format=xml&"
						+ "action=query&prop=revisions&rvprop=content&"
						+ "titles="
						+ URLEncoder.encode(blocksOfFifty[i],"UTF-8"));
				// Extract the pages from the response:
				NodeList pages = xml.getElementsByTagName("page");
				
				// Now that we've got our pages, get the content of each one.
				for (int x = 0; x < pages.getLength(); x += 1) {
					// Search the pages' child nodes for a node named
					// "revisions"
					Node revisions = getFirstChildWithName(pages.item(x),
							"revisions");

					// Search that child node for a node named "rev"
					NodeList revElements = getChildrenWithName(revisions, "rev");
					ArrayList revTexts = new ArrayList();

					for (int z = 0; z < revElements.getLength(); z += 1) {
						revTexts.add(revElements.item(z).getChildNodes()
								.item(0).getNodeValue());
					}

					pageTexts.put(
							pages.item(x)
								 .getAttributes()
							     .getNamedItem("title")
							     .getNodeValue(),
							revTexts);
				}
			}

			// Run our filter over pageTexts and convert it to an array, then
			// return.
			return pageTexts;
		} catch (Exception e) {
			e.printStackTrace();
		}

		// If exception was thrown, return null.
		return null;
	}

	private static Hashtable getPageRevisions(String[] titles, int revs) {
		return getPageRevisions(titles, revs, AnonymousFunction.ECHOER);
	}

	public static Hashtable getPageTexts(String[] titles,
										 AnonymousFunction filter) {
		//Get one revisions from each article:
		Hashtable packed = getPageRevisions(titles, 1, filter);
				
		//Initialize our resul as an empty Hashtable:
		Hashtable result = new Hashtable();
		
		//Put the latest revisions of each page into result.
		String[] keys = (String[]) packed.keySet()
										 .toArray(new String[packed.keySet().size()]);
		for (int i = 0; i < keys.length; i += 1) {
			result.put(keys[i],
					   ((ArrayList) packed.get(keys[i])).get(0));
		}
		
		return result;
	}

	private static Hashtable getPageTexts(String[] titles) {
		return getPageTexts(titles, AnonymousFunction.ECHOER);
	}

	private static Hashtable getRandomPageRevisions(int n,
													int namespace,
													int revs,
													AnonymousFunction textFilter) {
		return getPageRevisions(
				getRandomTitles(n, namespace),
				revs,
				textFilter);
	}

	public static Hashtable getRandomPageRevisions(int n, int namespace, int revs) {
		return getPageRevisions(getRandomTitles(n, namespace), revs);
	}

	public static Hashtable getRandomPageTexts(int n, int namespace) {
		return getPageTexts(getRandomTitles(n, namespace));
	}

	private static Hashtable extractArticlesWithTalkQualification(Hashtable articles, final AnonymousFunction qualifier) {
		/*
		 * Given an int (n), a number of revisions (revs),
		 * and a qualification AnonymousFunction (qualifier) that accepts
		 * a string and returns a boolean, makes requests for (rev)
		 * revisions of (n) pages, and returns only those whose talk pages
		 * fit (qualifier).
		 */
		
		Hashtable articlesClone = (Hashtable) articles.clone();
		String[] titles = (String[]) articles.keySet().toArray(new String[articles.keySet().size()]);
		String[] talkTitles = new String[titles.length];
		
		//Get the talk page titles for each title.
		for (int i = 0; i < titles.length; i += 1) {
			talkTitles[i] = "Talk:" + titles[i];
		}
		
		//Get the texts of the talk pages.
		Hashtable talkPages = getPageTexts(talkTitles, AnonymousFunction.ECHOER);
		
		//Remove the titles for all the articles that are not good.
		for (int i = 0; i < talkTitles.length; i += 1) {
			if (!(Boolean)qualifier.call(talkPages.get(talkTitles[i]))) {
				articlesClone.remove(titles[i]);
			}
		}
		
		return articlesClone;
	}
	
	public static Hashtable extractGoodArticles(Hashtable articles) {
		return extractArticlesWithTalkQualification(articles, new AnonymousFunction() {
			public Object call(Object... args) {
				return ((String) args[0]).contains("currentstatus=GA");
			}
		});
	}
}