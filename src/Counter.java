/*
 * @author David Anthony Bau
 */

class Counter {
  public double[][] count;
  public int alphabetSize;
  
  public Counter (int alphabetSize) {
    this.alphabetSize = alphabetSize;
    this.count = new double[alphabetSize + 1][alphabetSize];
  }
  
  public void add(int[] corpus) {
    for (int i = 0; i < corpus.length - 1; i += 1) {
      //Add one to this token's occurrence counts:
      count[0][i] += 1;

      //Add one to the bigram count:
      count[corpus[i]+1][corpus[i] + 1] += 1;
    }
  }
  
  public double[][] finish() {
    return count;
  }
}
