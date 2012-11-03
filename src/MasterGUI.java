import javax.swing.*;
import java.awt.*;
import java.awt.ActiveEvent;
public class MasterGUI extends JFrame {
	public JFrame frame;
	
	 public JPanel makeTopPanel(){
		 JPanel topPanel= new JPanel();
		 
		 JLabel pages = new JLabel("Number of Pages to Fetch");
		 topPanel.add(pages);
		 
		 JTextField input = new JTextField();
		 return topPanel;
	 }
	 public JPanel makeMiddlePanel(){
		 JPanel middlePanel = new JPanel();
		 return middlePanel;
	 }
	 public JPanel makeBottomPanel(){
		 JPanel bottomPanel = new JPanel();
		 return bottomPanel;
	 }
	 public JPanel makeFinalPanel(){
		 JPanel finalPanel = new JPanel();
		 return finalPanel;
		 
	 }
	 public MasterGUI(){
	        frame = new JFrame("Wikipedia Analysis Tool");
	        
		 frame.add(makeTopPanel());
		 frame.add(makeMiddlePanel());
		 frame.add(makeBottomPanel());
		 frame.add(makeFinalPanel());
	 }
	 public static void main(String [] args){
		 MasterGUI gui=new MasterGUI();
		 gui.frame.setVisible(true);
	 }
	 public JFrame returnFrame(){
		 return frame;
	 }
}
	


