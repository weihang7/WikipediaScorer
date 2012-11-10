import org.sqlite.JDBC;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.HashMap;

class DataSet {
  private File db;
  private String dbPath;
  private File[] wikis;
  private File[] tokens;

  public class BufferedDatabaseWriter {
    private static BUFFER_LIMIT = 800;
    private File db;
    private String dpath = db.getAbsolutePath();
    private HashMap<String, Integer> buffer;

    public BufferedDatabaseWriter(File db) {
      this.db = db;
    }

    public void flush() {
      //Set up a command to select all pertinent rows:
      String command = "SELECT * FROM alphabet WHERE token IN (";

      for (Enumeration<String> keys = buffer.keys(); keys.hasMoreElements();) {
        command += keys.nextElement() + (keys.hasMoreElements() ? ", " : ")");
      }
      
      //Open a connection
      Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dpath);
      //Make a query to that connection for pertinent rows:
      Statement statment = connection.createStatement;
      ResultSet results = statement.executeQuery(command);
      
      //Find which columns contain the counts and the token names:
      int countColumn = results.findColumn("count");
      int nameColumn = results.findColumn("token");

      //Add our counts to the existent counts and empty the hashtable as we move:
      while (results.next()) {
        String name = results.getString(nameColumn);
        results.updateInt(countColumnm,
                          results.getInt(countColumn) + buffer.get(name));
        buffer.remove(name);
      }

      //Any values that were not emptied (are not already in the table), we should add:
      for (Enumeration<String> keys = buffer.keys(); keys.hasMoreElements();) {
        key = keys.nextElement();
        statment.executeUpdate("INSERT INTO alpahbet VALUES (" + key + "," + buffer.get(key));
        buffer.remove(key);
      }
    }

    public add(String key, int value) {
      buffer.put(key, (buffer.contains(key) ? buffer.get(key) : 0) + value);
      if (buffer.size() > BUFFER_LIMIT) flush();
    }
  }

  public DataSet (String baseFilePath) {
    //Initialize all the files we link to.
    db = new File(baseFilePath + "_db");
    dpath = db.getAbsolutePath();
    wikis[0] = new File(baseFilePath + "_w_g");
    wikis[1] = new File(baseFilePath + "_w_b");
    tokens[0] = new File(baseFilePath + "_t_g");
    tokens[1] = new File(baseFilePath + "_t_b");
  }

  private void execute(String[] sql) throws SQLException {
    //General method for executing non-SELECT sql statements.

    //Open a connection to our database.
    Connection connection = DriverManager.getConnection("jdbc:sqlite:"+dpath);

    //Get ready to make a statement.
    Statement statement = connection.createStatement();
    
    //Execute the commands.
    for (int i = 0; i < sql.length; i += 1) {
      statement.executeUpdate(sql);
    }
    
    //Close the connection.
    connection.close();
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
                  + (i == cells[0].length - 1 ? ")" : ",");
    }

    //Execute the commands.
    execute(commands);
  }

  private void insertCount(String table, int index, double[] value) {
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

  private void updateCount(String table, int index, double[] value) {
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

  private double[][] selectCounts(String table, int[] indices) {
    String commands = "SELECT * FROM " + table
                    + " WHERE name IN (";

    //Specify which rows we're selecting.
    for (int i = 0; i < indices.length; i += 1) {
      commands += indices[i] + (i == indices.length ? ")" : ", ");
    }
    
    //Open our connection.
    Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dpath);

    //Create our statement.
    Statement statement = connection.createStatement();

    //Make our query.
    ResultSet result = statement.executeQuery(command);

    //Scroll to the first result.

    //See how many columns there are:
    int len = result.getMetaData.getColumnCount();
    double[][] r = new double[indices][len - 1];
    
    //Scroll through our results and put them into a big array of doubles.
    for (int i = 1; i <= indices.length; i += 1) {
     result.absolute(i);
     for (int x = 1; x < len; x += 1) {
       r[i][x] = result.getDouble(i);
     }
    }

    return r;
  }


  //---------------------ALPHABET COUNT TABLES---------------------
  private void createAlphabetCountTable(String name) {
    //Execute this command.
    String[] commands = {
      "DROP TABLE IF EXISTS " + name,
      "CREATE TABLE " + name + "(name STRING, count INTEGER)"
    };
    execute(commands);
  }

  public BufferedDatabaseWriter writeAlphabet() {
    //Instatiate a new BufferedDatabaseWriter for our alphabet.
    return new BufferedDatabaseWriter(db);
  }

  public String[] getBestAlphabet(int alphabetSize) {
    //Get the best (alphabetSize) results (ordered by token count) from our database.
    String[] r = new String[alphabetSize];

    Connection connection = DriverHandler.getConnection("jdbc:sqlite:"+dpath);
    Statement statement = connection.createStatment();
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
    return new WordReader((which ? goodWiki : badWiki));
  }
  
  public Enumeration<Integer> readTokens(boolean which) {
    return new TokenReader((which ? goodTokens : badTokens));
  }
}
