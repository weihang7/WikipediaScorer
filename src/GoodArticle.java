public class GoodArticle {
	public static String getTalkPage(String url){
		
	}
	public static boolean isGood(String page){
		boolean ret=false;
		if (page.contains("currentstatus=GA")){
				ret=true;
		}
		return ret;
	}
}