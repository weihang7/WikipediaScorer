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

import javax.swing.JOptionPane;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 * @author Weihang Fan
 *
 */
public class DataSet {
  private File database;
  private File goodWiki;
  private File badWiki;
  private File goodTokens;
  private File badTokens;

  public DataSet(String baseFilePath){
	  database = new File(baseFilePath);
  }
  
  public void execute(String[] commands) throws ClassNotFoundException{
    Class.forName("org.sqlite.JDBC");
    Connection connection = null;
    try{
      connection = DriverManager.getConnection("jdbc:sqlite:"+database.getAbsolutePath());
      Statement statement = connection.createStatement();
      for (int i=0;i<commands.length;i++)
        statement.executeUpdate(commands[i]);
    }
    catch(SQLException e){
    	e.printStackTrace();
    }
    finally{
      try{
        if(connection!=null){
          connection.close();
        }
      }
      catch(SQLException e){
    	System.err.println(e);
      }
    }
  }
  
  public ResultSet executeQuery(String query) throws Exception{
	Class.forName("org.sqlite.JDBC");
	Connection connection = null;
	connection = DriverManager.getConnection("jdbc:sqlite:"+database.getAbsolutePath());
	Statement statement = connection.createStatement();
	ResultSet ret = statement.executeQuery(query);
	connection.close();
	return ret;
  }
  
  public void createTable(String name,double[][] cells){
    String[] commandsToExecute = new String[2];
    commandsToExecute[0] = "DROP TABLE if exists "+name;
    commandsToExecute[1] = "CREATE TABLE "+name+" ("; 
    for (int i=0;i<cells[0].length;i++){
    	commandsToExecute[1]+="\""+i+"\""+ " DOUBLE"+(i==cells[0].length-1?"":",");
    }
    commandsToExecute[1]+=")";
    try {
      execute(commandsToExecute);
    } 
    catch (Exception e) {
      e.printStackTrace();
    }
    for(int i=0;i<cells.length;i++){
		insert("test",cells[i]);
    }
  }
  
  public void insert(String table,double[] element){
	  String[] commandsToExecute = new String[1];
	  commandsToExecute[0]="insert into "+table+" (";
	  for(int i=0;i<element.length;i++){
		  commandsToExecute[0]+="\""+i+"\""+(i==element.length-1?"":",");
	  }
	  commandsToExecute[0]+=") values (";
	  for(int i=0;i<element.length;i++){
		  commandsToExecute[0]+=element[i]+(i==element.length-1?"":",");
	  }
	  commandsToExecute[0]+=")";
	  System.out.println(commandsToExecute[0]);
	  try{
		  execute(commandsToExecute);
	  }
	  catch(Exception e){
		  e.printStackTrace();
	  }
  }
  
  public double[][] loadAll(){
	  
  }
  
  private class WordReader implements Enumeration<String> {
    StringReader r;
    public WordReader(File f) {
      try {
		this.r = new StringReader(new BufferedInputStream(new FileInputStream(f)).toString());
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
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
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      return s;
    }
    public boolean hasMoreElements() {
		try {
			return r.ready();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
  
  public void finalizeAlphabet(String[] alphabet){
	  for(int i=0;i<alphabet.length;i++){
		  append("alphabet",alphabet[i]);
	  }
  }
  
  public static void append(String file, String value){
    try{
      // Create file 
      FileWriter fstream = new FileWriter(file,true);
      BufferedWriter out = new BufferedWriter(fstream);
      out.write(value);
      //Close the output stream
      out.close();
    }
    catch (Exception e){
      //Catch exception if any
      System.err.println("Error: " + e.getMessage());
    }
  }
  
  public static BufferedInputStream loadStringStream(String file) throws IOException{
    // Create file
    FileInputStream is = new FileInputStream(new File(file));
    return new BufferedInputStream(is);
  }
  
  public static String readFromDoc(File docfile){
    
    //Establishing the things to receive data from the FileInputStream
    String ret = "";
    
    try {
      //Create a FileInputStream that links the HWPFDocument and a WordExtractor
      FileInputStream fis=new FileInputStream(docfile);
      HWPFDocument document=new HWPFDocument(fis);
      WordExtractor extractor = new WordExtractor(document);
      
      //Extract the text
      ret=extractor.getText();
    }
    catch(Exception exep){
      // Catch exceptions
      JOptionPane.showMessageDialog(null,"docLoadingException");
    }
    
    return ret;
  }
  
  public static String readFromDocx(File docxfile){

    //Establishing the things to receive data from the FileInputStream
    String ret = "";
    
    try {
      //Create a FileInputStream that links the HWPFDocument and a WordExtractor
      FileInputStream fis=new FileInputStream(docxfile);
      XWPFDocument document=new XWPFDocument(fis);
      XWPFWordExtractor extractor = new XWPFWordExtractor(document);
      
      //Extract the text
      ret=extractor.getText();
    }
    catch(Exception exep){
      // Catch exceptions
      JOptionPane.showMessageDialog(null,"docxLoadingException");
    }
    
    return ret;
  }
  
  //Reading from a text file
  private static String readFromText(File txtfile){
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
  
  private static void overwriteString(String stringToWrite, String path){
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
  
  public static void main(String[] args){
	  DataSet t = new DataSet("db.db");
	  double[] a = {0.3,0.6,3.7};
	  double[] b = {52.4,6.3,2.4};
	  double[][] c = {a,b};
	  t.createTable("test",c);
  }
}
