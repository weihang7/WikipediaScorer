/*
 * @author Weihang Fan
 */
import java.io.*;

import javax.swing.JOptionPane;

//Import needed functions
public class ReadandWrite {	
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