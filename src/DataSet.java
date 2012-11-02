import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.poi.hwpf.extractor.*;
import org.apache.poi.hwpf.*;

import org.apache.poi.xwpf.*;
import org.apache.poi.xwpf.extractor.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;


public class DataSet {
	public DataSet() {
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
