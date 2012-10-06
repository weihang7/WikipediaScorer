public class Tokenizer {
	public static void main(String[] args) {
		System.out.println(args[0].replaceAll("(==*)[^\\n=]*\\1"," . "));
	}
}