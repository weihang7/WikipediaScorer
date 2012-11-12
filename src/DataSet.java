import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JOptionPane;

class DataSet {
  private File db;
  private String dbPath;
  private File[] wikis;
  private File[] tokens;
  private File alphabetFile;

  public class BufferedDatabaseWriter {
    private static final int BUFFER_LIMIT = 800;
    private File db;
    private String dbPath = db.getAbsolutePath();
    private Hashtable<String, Integer> buffer; //We use a hashtable here for easier enumeration.

    public BufferedDatabaseWriter(File db) {
      this.db = db;
    }

    public void flush() throws SQLException {
      //Set up a command to select all pertinent rows:
      String command = "SELECT * FROM alphabet WHERE token IN (";

      for (Enumeration<String> keys = buffer.keys(); keys.hasMoreElements();) {
        command += keys.nextElement() + (keys.hasMoreElements() ? ", " : ")");
      }
      
      //Open a connection
      Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
      //Make a query to that connection for pertinent rows:
      Statement statement = connection.createStatement();
      ResultSet results = statement.executeQuery(command);
      
      //Find which columns contain the counts and the token names:
      int countColumn = results.findColumn("count");
      int nameColumn = results.findColumn("token");

      //Add our counts to the existent counts and empty the hashtable as we move:
      while (results.next()) {
        String name = results.getString(nameColumn);
        results.updateInt(countColumn,
                          results.getInt(countColumn) + buffer.get(name));
        buffer.remove(name);
      }

      //Any values that were not emptied (are not already in the table), we should add:
      for (Enumeration<String> keys = buffer.keys(); keys.hasMoreElements();) {
        String key = keys.nextElement();
        statement.executeUpdate("INSERT INTO alpahbet VALUES (" + key + "," + buffer.get(key));
        buffer.remove(key);
      }
    }

    public void add(String key, int value) throws SQLException {
      buffer.put(key, (buffer.containsKey(key) ? buffer.get(key) : 0) + value);
      if (buffer.size() > BUFFER_LIMIT) flush();
    }
  }

  public class countEnumeration implements Enumeration<double[]> {
    private ResultSet r;
    private int c;

    public countEnumeration(ResultSet r) throws SQLException {
      this.r = r;
      this.c = r.getMetaData().getColumnCount();
    }

    public double[] nextElement() {
      double[] rtn;
      try {
		r.next();
	    rtn = new double[c - 1];
	    for (int i = 2; i <= c; i += 1) {
	      rtn[i - 2] = r.getDouble(i);
	    }
      }
      catch (SQLException e) {
        rtn = new double[0];
      }
      return rtn;
    }

    public boolean hasMoreElements() {
      try {
        boolean rtn = r.next();
        r.previous();
        return rtn;
      }
      catch (SQLException e) {
        return false;
      }
    }
  }

  public DataSet (String baseFilePath) {
    //Initialize all the files we link to.
    db = new File(baseFilePath + ".db");
    dbPath = db.getAbsolutePath();
    wikis[0] = new File(baseFilePath + "_w_g");
    wikis[1] = new File(baseFilePath + "_w_b");
    tokens[0] = new File(baseFilePath + "_t_g");
    tokens[1] = new File(baseFilePath + "_t_b");
    alphabetFile = new File(baseFilePath + "_alphabet");
  }

  private int[] execute(String[] sql) throws SQLException {
    //General method for executing non-SELECT sql statements.

    int[] rtn = new int[sql.length];

    //Open a connection to our database.
    Connection connection = DriverManager.getConnection("jdbc:sqlite:"+dbPath);

    //Get ready to make a statement.
    Statement statement = connection.createStatement();
    
    //Execute the commands.
    for (int i = 0; i < sql.length; i += 1) {
      rtn[i] = statement.executeUpdate(sql[i]);
    }
    
    //Close the connection.
    connection.close();

    return rtn;
  }

  //---------------------COUNT TABLES--------------------------
  
  private void createCountTable(String name, int alphabetSize) throws SQLException {
    
    //The commands we need to make a table:
    String[] commands = {
      "DROP TABLE IF EXISTS " + name,
      "CREATE TABLE " + name + "(name DOUBLE, "
    };
    
    for (int i = 0; i < alphabetSize; i += 1) {
      //Add the necessary columns to our table.
      commands[1] += "\"" + i + "\" DOUBLE"
                  + (i == alphabetSize - 1 ? ")" : ",");
    }

    //Execute the commands.
    execute(commands);
  }
  
  public void initCounts(int alphabetSize) throws SQLException {
    //Check to see whether the tables exist already:
    String[] commands = {
      "SELECT name FROM sqlite_master"
    + "WHERE type='table' AND name IN (bad, good)"
    };
    
    if (execute(commands)[0] == 0) {
      try {
        //If they do, create them.
        createCountTable("bad", alphabetSize);
        createCountTable("good", alphabetSize);
      }
      catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  private void insertCount(String table, int index, double[] value) throws SQLException {
    //The command that we need to insert a row:
    String[] commands = {"INSERT INTO " + table
                         + " VALUES (" + index + ", "};

    //Add the necessary values to our row:
    for (int i = 0; i < value.length; i = 1) {
      commands[0] += value[i] + (i == value.length - 1 ? ")" : ", ");
    }
    
    //Execute the command.
    execute(commands);
  }

  private void updateCount(String table, int index, double[] value) throws SQLException {
    /*
      Probably won't actually use this. If possible, use the addCount() method.
    */

    //The command that we need to update a row:
    String[] commands = {"UPDATE " + table
                      +  " SET "};

    //Add the necessary values:
    for (int i = 0; i < value.length; i += 1) {
      commands[0] += i + "=" + value[i] + (i == value.length ? "" : ", ");
    }
  
    //Add the WHERE statement.
    commands[0] += "WHERE name=" + index;
    
    //Execute the commands.
    execute(commands);
  }

  private double[][] selectCounts(String table, int[] indices) throws SQLException {
    String command = "SELECT * FROM " + table
                    + " WHERE name IN (";

    //Specify which rows we're selecting.
    for (int i = 0; i < indices.length; i += 1) {
      command += indices[i] + (i == indices.length ? ")" : ", ");
    }
    
    //Open our connection.
    Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

    //Create our statement.
    Statement statement = connection.createStatement();

    //Make our query.
    ResultSet result = statement.executeQuery(command);

    //Scroll to the first result.

    //See how many columns there are:
    double[][] r = new double[indices.length][result.getMetaData().getColumnCount() - 1];
    
    //Scroll through our results and put them into a big array of doubles.
    for (int i = 1; i <= indices.length; i += 1) {
     result.absolute(i);
     for (int x = 2; x <= r[i].length + 1; x += 1) {
       r[i][x] = result.getDouble(i);
     }
    }

    connection.close();

    return r;
  }

  public double[][] loadAll(boolean which) throws SQLException {

    //Open a connection and make a statement from it.
    Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    Statement statement = connection.createStatement();
    
    //Get the number of rows in our databse.
    int rows = statement.executeUpdate("SELECT * FROM " + (which ? "good" : "bad"));
    
    //Actually select the rows into a ResultSet
    ResultSet result = statement.executeQuery("SELECT * FROM " + (which ? "good" : "bad"));
    
    double[][] r = new double[rows][result.getMetaData().getColumnCount() - 1];

    //Loop through our results and put them all into a big array.
    for (int i = 0; result.next(); i += 1) {
      for (int x = 2; x <= r[i].length + 1; x += 1) {
        r[i][x] = result.getDouble(x + 1);
      }
    }

    connection.close();

    return r;
  }

  public double lookup (boolean which, int a, int b) throws SQLException {
    Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    Statement statement = connection.createStatement();
    ResultSet results = statement.executeQuery("SELECT " + b + " FROM " + (which ? "good" : "bad") + " WHERE name=" + a);
    results.next();
    double r = results.getDouble(results.findColumn(Integer.toString(b)));
    connection.close();
    return r;
  }
  
  public void add(boolean which, double[][] add) throws SQLException {
    //Open a connection and make a statement from it.
    Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    Statement statement = connection.createStatement();
    
    //Get the number of rows in our database.
    int rows = statement.executeUpdate("SELECT * FROM " + (which ? "good" : "bad"));
    
    //Actually select the rows into a ResultSet
    ResultSet result = statement.executeQuery("SELECT * FROM " + (which ? "good" : "bad"));
    
    //Update all the rows using our big array.
    for (int i = 0; result.next(); i += 1) {
      for (int x = 1; x <= add[i].length; x += 1) {
        result.updateDouble(x, result.getDouble(x) + add[i][x]);
      }
    }

    connection.close();
  }

  public Enumeration<double[]> enumerate(boolean which) {
    //TODO do something in here
    return null;
  }
  //---------------------ALPHABET COUNT TABLES---------------------
  private void createAlphabetCountTable() throws SQLException {
    //Execute this command.
    String[] commands = {
      "DROP TABLE IF EXISTS alphabet",
      "CREATE TABLE alphabet (name STRING, count INTEGER)"
    };
    execute(commands);
  }

  public void initAlphabet() throws SQLException {
    //Check to see whether the alphabetCounts table already exists:
    String[] commands = {
      "SELECT name FROM sqlite_master"
    + "WHERE type='table' AND name=alphabet"
    };

    if (execute(commands)[0] == 0) {
      //If it doesn't, create it.
      createAlphabetCountTable();
    }
  }

  public BufferedDatabaseWriter writeAlphabet() {
    //Instatiate a new BufferedDatabaseWriter for our alphabet.
    return new BufferedDatabaseWriter(db);
  }

  public String[] getBestAlphabet(int alphabetSize) throws SQLException {
    //Get the best (alphabetSize) results (ordered by token count) from our database.
    String[] r = new String[alphabetSize];

    Connection connection = DriverManager.getConnection("jdbc:sqlite:"+dbPath);
    Statement statement = connection.createStatement();
    ResultSet results = statement.executeQuery(
      "SELECT * FROM alphabet ORDER BY count ASC"
    );

    int nameColumn = results.findColumn("token");
    
    for (int i = 0; i < alphabetSize; i += 1) {
      results.next();
      r[i] = results.getString(nameColumn);
    }

    return r;
  }

  //--------------------NORMAL FILE IO--------------------------
  /*
   * This should just be copied over from the old DataSet. It was fine.
  */
  private class WordReader implements Enumeration<String> {
    StringReader r;
    public WordReader(File f) {
      try {
    this.r = new StringReader(new BufferedInputStream(new FileInputStream(f)).toString());
  } catch (FileNotFoundException e) {
    e.printStackTrace();
  }
    }
    public String nextElement() {
      String s = "";
      char c;
      try {
    while ((c = (char) r.read()) != ' ') {
        s += c;
      }
  } catch (IOException e) {
    e.printStackTrace();
  }
      return s;
    }
    public boolean hasMoreElements() {
    try {
      return r.ready();
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    }
  }

  private class TokenReader implements Enumeration<Integer> {
    WordReader r;
    public TokenReader(File f) {
      this.r = new WordReader(f);
    }
    public Integer nextElement() {
      return new Integer(r.nextElement());
    }
    public boolean hasMoreElements() {
      return r.hasMoreElements();
    }
  }

  public Enumeration<String> readWiki(boolean which) {
    return new WordReader((which ? wikis[0] : wikis[1]));
  }
  
  public Enumeration<Integer> readTokens(boolean which) {
    return new TokenReader((which ? tokens[0] : tokens[1]));
  }
  

  public void finalizeAlphabet(String[] alphabet){
    BufferedWriter writer = new BufferedWriter(new FileWriter(alphabetFile));
    for (int i = 0; i < alphabet.length; i += 1) {
      writer.write(alphabet[i] + (i == alphabet.length ? "" : " "));
    }
  }
  
  public static String[] loadAlphabet(){
    return readFromText(new File("alphabet")).split(" ");
  }
  
  public BufferedWriter writeWiki(boolean which) {
    return new BufferedWriter(new FileWriter(wikis[which ? 0 : 1], true));
  }

  public BufferedWriter writeTokens(boolean which) {
    return new BufferedWriter(new FileWriter(tokens[which ? 0 : 1], true));
  }
  
  public static void overwrite(String stringToWrite, String path){
      try{
        FileWriter fr = new FileWriter(path);
        BufferedWriter br = new BufferedWriter(fr);
        br.write(stringToWrite);
        br.close();
      }
      catch(Exception ex){
        ex.printStackTrace();
      }
  }
  
  public static BufferedInputStream loadStringStream(String file) throws IOException{
    // Create file
    FileInputStream is = new FileInputStream(new File(file));
    return new BufferedInputStream(is);
  }
  
  //Reading from a text file
  public static String readFromText(File txtfile){
    String text="";
    try {
      FileReader fr=new FileReader(txtfile);
      BufferedReader br=new BufferedReader(fr);
      String currentline = "";
      while((currentline=br.readLine())!=null) {
        text+=currentline;
      }
      br.close();
     } catch(Exception ex) {
       ex.printStackTrace();
     }
     return text;
  }
}
