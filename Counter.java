import java.util.*;

class Counter {
	static Hashtable[] count(String[] corpus) {
		Hashtable occurs = new Hashtable();
		Hashtable bigrams = new Hashtable();
		Hashtable[] both = {occurs,bigrams};
		Hashtable lastHash = new Hashtable();
		for (int i = 0; i < corpus.length; i += 1) {
			if (!occurs.containsKey(corpus[i])) {
				occurs.put(corpus[i], 1);
				bigrams.put(corpus[i], new Hashtable());
			}
			if (!lastHash.containsKey(corpus[i])) {
				lastHash.put(corpus[i], 1);
			}
			else {
				lastHash.put(corpus[i], (Integer)lastHash.get(corpus[i]) + 1);
			}
			lastHash = (Hashtable)bigrams.get(corpus[i]);
		}
		lastHash.put("<EOF>",1);
		return both;
	}
}