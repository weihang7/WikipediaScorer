import java.sql.*;
import java.util.Hashtable;
import java.io.*;

import javax.swing.JOptionPane;

import org.apache.poi.hwpf.extractor.*;
import org.apache.poi.hwpf.*;

import org.apache.poi.xwpf.*;
import org.apache.poi.xwpf.extractor.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 * @author weihangfan
 *
 */
public class DataSet {
  private File database;

  public void execute(String[] commands, File file) throws ClassNotFoundException{
    Class.forName("org.sqlite.JDBC");
    Connection connection = null;
    try{
      connection = DriverManager.getConnection("jdbc:sqlite:"+file.getAbsolutePath());
      Statement statement = connection.createStatement();
      for (int i=0;i<commands.length;i++)
        statement.executeUpdate(commands[i]);
    }
    catch(SQLException e){
      System.err.println(e.getMessage());
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
  
  public void createTable(Hashtable table,File file,String name){
    String[] commandsToExecute = new String[2];
    commandsToExecute[0] = "drop table if exists "+name;
    commandsToExecute[1] = "create table "+name; 
    try {
      execute(commandsToExecute,file);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
  
  public static void insert(String key, Double value){
    
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
  
  public static void overwriteString(String stringToWrite, String path){
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
}
