import javax.swing.*;
//import java.awt.*;
public class MasterGUI extends JFrame {
	public JFrame frame;
	public JPanel makeMiddlePanel;
	public JPanel makeTopPanel;
	public JPanel makeBottomPanel;
	public JPanel makeFinalPanel;
	
	 public JPanel makeTopPanel(){
		 JPanel topPanel= new JPanel();
		 
		 JLabel pages = new JLabel("Number of Pages to Fetch");
		 topPanel.add(pages);
		 
		 JTextField addInput = new JTextField(5);
		 topPanel.add(addInput);
		 return topPanel;
	 }
	 public JPanel makeMiddlePanel(){
		 JPanel middlePanel = new JPanel();

		 JLabel alphaSize = new JLabel("Alphabet Size");
		 middlePanel.add(alphaSize);
		 
		 JTextField addInput = new JTextField(5);
		 middlePanel.add(addInput);
		 
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
	     JPanel bigPanel = null;
	     
		 frame.add(makeTopPanel());
		 frame.add(makeMiddlePanel());
		 frame.add(makeBottomPanel());
		 frame.add(makeFinalPanel());
		 frame.pack();
		 
	     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     
	     frame.setSize(630,400);
	     
	     	bigPanel = makeTopPanel();
	        frame.add(bigPanel);
	        
	        bigPanel = makeMiddlePanel();
	        frame.add(bigPanel);
	        
	        bigPanel = makeBottomPanel();
	        frame.add(bigPanel);
	        bigPanel = makeFinalPanel();
	        frame.add(bigPanel);
	        //frame.pack();
	 }
	 public static void main(String [] args){
		 MasterGUI gui=new MasterGUI();
		 gui.frame.setVisible(true);
	 }
	 public JFrame returnFrame(){
		 return frame;
	 }
}