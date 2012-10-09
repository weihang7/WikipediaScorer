
public class GoodArticle {
	public boolean[] isGood(String[] page){
		boolean[] ret = null;
		for (int i=0;i<page.length;i++){
			if (page[i]=="GA"){
				ret[i]=true;
			}
		}
		return ret;
	}
}
