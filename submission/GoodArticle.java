/*
 * @author Weihang Fan
 */

public class GoodArticle {
	public static String getTalkPage(String title){
		return "Talk:"+title;
	}
	public static boolean isGood(String page){
		return page.contains("currentstatus=GA");
	}
	public static boolean isFeature(String page){
		return page.contains("currentstatus=FA");
	}
}