import java.io.*;

import org.apache.poi.hwpf.extractor.*;
import org.apache.poi.hwpf.*;

public class ReadFromFile {
	public static String readFromDoc(File docfile){
		WordExtractor extractor = null ;
		String ret = "";
		try {
		   FileInputStream fis=new FileInputStream(docfile.getAbsolutePath());
		   HWPFDocument document=new HWPFDocument(fis);
		   extractor = new WordExtractor(document);
		   String [] fileData = extractor.getParagraphText();
		   for(int i=0;i<fileData.length;i++){
		     if(fileData[i] != null)
		       ret+=fileData[i];
		   }
		}
		catch(Exception exep){
			
		}
		return ret;
	}
	public static String readFromTxt(File txtfile){
		String text="";
		int read,N=1024*1024;
		char[] buffer=new char[N];
		try {
			FileReader fr=new FileReader(txtfile);
			BufferedReader br=new BufferedReader(fr);
			while(true) {
				read=br.read(buffer,0,N);
				text+=new String(buffer,0,read);
				if(read<N) break;
			}
			br.close();
		 } catch(Exception ex) {
			 ex.printStackTrace();
		 }
		 return text;
	}
}