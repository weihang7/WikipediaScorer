import javax.swing.*;
import java.awt.*;
public class MasterGUI extends JFrame {
	private JFrame frame;
	 public JFrame createGUI(){
		 frame = new JFrame("MasterGUI");
		 return frame;
	 }
	 public JPanel makeTopPanel(){
	 JPanel topPanel= new JPanel();
	 }
	 public static void main(String [] args){
		 MasterGUI gui=new MasterGUI();
		 gui.createGUI();
	 }
}
	


