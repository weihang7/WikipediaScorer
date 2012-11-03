/*
 * @author David Anthony Bau
 */
import java.util.*;

class Smoother {

  private static double[] oneAverage(double[] a, double[] b) {
    double[] avg = new double[a.length];

    for (int i = 0; i < a.length; i += 1) {
      avg[i] = (a[i] + b[i]) / 2;
    }

    return avg;
  }

  private static double[] oneSmoothOneWay(double[] t, double[] h) {
    HashMap<Double, Integer> inverseT = new HashMap<Double, Integer>();
    HashMap<Double, Double> inverseTCount = new HashMap<Double, Double>();
    
    double[] s = new double[t.length];

    for (int i = 0; i < t.length; i += 1) {
      //Invert our counts.
      if (inverseT.containsKey(t[i])) {
        inverseT.put(t[i], inverseT.get(t[i]) + 1);
      }
      else {
        inverseT.put(t[i], 1);
        inverseTCount.put(t[i], 0.0);
      }
    }

    for (int i = 0; i < h.length; i += 1) {
      inverseTCount.put(t[i], inverseTCount.get(t[i]) + 1);
    }

    for (int i = 0; i < t.length; i += 1) {
      s[i] = inverseTCount.get(t[i]) / inverseT.get(t[i]);
    }

    return s;
  }

  private static double[] oneSmooth(double[] a, double[] b) {
    return oneAverage(oneSmoothOneWay(a, b), oneSmoothOneWay(b, a));
  }

  public static double[][] smooth(double[][] a, double[][] b) {
    double[][] s = new double[a.length][a[0].length];
    
    for (int i = 0; i < a.length; i += 1) {
      s[i] = oneSmooth(a[i], b[i]);
    }
    
    return s;
  }
}
