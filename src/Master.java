import java.util.*;
class Master {
	public static void main(String[] args) {
		// get parsed xml into pages
		String[] pages = Fetcher.getRandomPageTexts(30,0,1);
		//declare variable words
		ArrayList words = new ArrayList(pages.length);
		//Split the pages into tokens.
		for (int i=0;i<pages.length;i++){
			words.add(Tokenizer.fullTokenization(pages[i], 3000));
		}
		//concatenate the pages
		ArrayList firstHalf = new ArrayList(words.size()/2);
		for (int i=0;i<words.size()/2;i++){
			for (int k=0;k<((ArrayList)words.get(i)).size();k++){
				firstHalf.add(((String[])words.get(i))[k]);
			}
		}
		ArrayList secondHalf = new ArrayList(words.size()/2);
		for (int i=words.size()/2;i<words.size();i++){
			for (int k=0;k<((ArrayList) words.get(i)).size();k++){
				secondHalf.add(((String[])words.get(i))[k]);
			}
		}
		Hashtable[] firstCountResult=Counter.count((String[])firstHalf.toArray());
		Hashtable[] secondCountResult=Counter.count((String[])secondHalf.toArray());
		Hashtable smootherResult = new Hashtable();
		String[] firstKeys=(String[]) firstCountResult[1].keySet().toArray();
		Hashtable occurence = Smoother.heldOutSmoothingScaledLogarithmic(
				firstCountResult[0],
				secondCountResult[0],
				3000);
		Hashtable transition = new Hashtable();
		for (int i = 0;i<firstKeys.length;i++){
			transition.put(
					firstKeys[i],
					Smoother.heldOutSmoothingScaledLogarithmic(
							(Hashtable)firstCountResult[1].get(firstKeys[i]),
							(Hashtable)secondCountResult[1].get(firstKeys[i]), 
							3000
					)
			);
		}
	}
}