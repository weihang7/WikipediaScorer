/*
 * @author David Anthony Bau
 */

class Scorer {
  private double[][] good;
  private double[][] bad;
  private double logGoodNum;
  private double logBadNum;

  public Scorer(DataSet data) throws Exception {
      this.good = data.loadAll(true);
      this.bad = data.loadAll(false);
      this.logGoodNum = data.getNum(true);
      this.logBadNum = data.getNum(false);
  }

  public double score(int[] text) {
    
    double goodProbability = logGoodNum;
    double badProbability = logBadNum;
    
    int lastToken = 0;
    for (int i = 0; i < text.length; i += 1) {
      goodProbability += good[lastToken + 1][text[i]];
      badProbability += bad[lastToken + 1][text[i]];
      
      lastToken = text[i];
    }
    
    if (goodProbability > badProbability) {
      goodProbability -= badProbability;
      badProbability = 0;
    }
    else {
      badProbability -= goodProbability;
      goodProbability = 0;
    }
    
    return Math.pow(Math.E, goodProbability) / (Math.pow(Math.E, goodProbability) + Math.pow(Math.E, badProbability));
  }
}
