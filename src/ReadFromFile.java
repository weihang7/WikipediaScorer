import java.io.*;
import org.apache.poi.hwpf.extractor.*;
import org.apache.poi.hwpf.*;
//import needed functions
public class ReadFromFile {
	public static String readFromDoc(File docfile){
		//establishing the receptors of the FileInputStream
		WordExtractor extractor = null;
		String ret = "";
		try {
			//create a FileInputStream that links the document and the extractor
			FileInputStream fis=new FileInputStream(docfile.getAbsolutePath());
			HWPFDocument document=new HWPFDocument(fis);
			extractor = new WordExtractor(document);
			//extract the text
			String [] fileData = extractor.getParagraphText();
			for(int i=0;i<fileData.length;i++){
				if(fileData[i] != null)
					ret+=fileData[i];
			}
		}
		// catch exceptions
		catch(Exception exep){
			
		}
		return ret;
	}
	//reading from a txt file
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
}