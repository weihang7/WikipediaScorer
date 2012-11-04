/*
 * @author David Anthony Bau
 */

class Counter {
  public double[][] count(Enumeration<Integer> document, int alphabetSize) {
    double[][] r = new double[alphabetSize + 1][alphabetSize];
    
    for (int last = document.getNextElement(); document.hasMoreElements();) {
      r[last + 1][(last = document.getNextElement())] += 1;
    }

    return r;
  }
}
