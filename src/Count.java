import java.util.*;

public class Count {
	/*
	 * A simple model of Markov counts over a single
	 * document.
	 */
	
	public int num;
	public Hashtable<String, Double> occurs;
	public Hashtable<String, Hashtable<String, Double>> bigrams;
	
	public Count(int num,
				 Hashtable<String, Double> occurs,
				 Hashtable<String, Hashtable<String, Double>> bigrams) {
		this.num = num;
		this.occurs = occurs;
		this.bigrams= bigrams;
	}
	
	public String toString() {
		return "num: " + this.num + "; occurs: " + this.occurs + "; bigrams " + this.bigrams;
	}
}
