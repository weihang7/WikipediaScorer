/*
 * @author Weihang Fan
 */
import java.io.*;

import javax.swing.JOptionPane;

import org.apache.poi.hwpf.extractor.*;
import org.apache.poi.hwpf.*;
//Import needed functions
public class ReadandWrite {
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
	
	//Reading from a text file
	public static String readFromTxt(File txtfile){
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
	
	public static void writeString(String stringToWrite, String path){
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