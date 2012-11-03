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
		 JLabel pages = new JLabel("Number of Pages to Fetch");
		 topPanel.add(pages);
		 return makeTopPanel;
	 }
	 public JPanel makeMiddlePanel(){
		 JPanel middlePanel = new JPanel();
		 return makeMiddlePanel;
	 }
	 public JPanel makeBottomPanel(){
		 JPanel bottomPanel = new JPanel();
		 return makeBottomPanel;
	 }
	 public static void main(String [] args){
		 MasterGUI gui=new MasterGUI();
		 gui.createGUI();
	 }
}
	


