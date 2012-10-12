public class ArticleProp {
	public static String getTalkPage(String url){
		return "Talk:"+url;
	}
	public static boolean isGood(String page){
		boolean ret=false;
		if (page.contains("currentstatus=GA"))
				ret=true;
		return ret;
	}
	public static boolean isFeature(String page){
		boolean ret=false;
		if (page.contains("currentstatus=FA"))
			ret=true;
		return ret;
	}
}