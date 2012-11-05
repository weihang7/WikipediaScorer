import java.util.Enumeration;

/*
 * @author David Anthony Bau
 */

class Counter {
  public static double[][] count(Enumeration<Integer> document, int alphabetSize) {
    double[][] r = new double[alphabetSize + 1][alphabetSize];
    
    for (int last = document.nextElement(); document.hasMoreElements();) {
      r[last + 1][(last = document.nextElement())] += 1;
    }
    return r;
  }
}