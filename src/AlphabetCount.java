import java.util.HashMap;

public class AlphabetCount {
	HashMap<String, Integer> alphaCount;
	public AlphabetCount(){ 
		alphaCount =  new HashMap<String, Integer>();
	}
	
	public int get(String x){
		return alphaCount.get(x);
		
	}
	public void add(String x, int n){
	
		if (alphaCount.containsKey(x)){
			alphaCount.put(x,alphaCount.get(x)+n);
		}
		else {
			alphaCount.put(x, n);
		}
	}
}
