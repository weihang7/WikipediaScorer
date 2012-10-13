import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class wikiGUI
{
    // decrlared variables to be used in the future
    private JFrame window;
    private JTextArea textInput;
    private JButton ioButton;
    private File fileInUse;
    private JLabel fileNameLabel;
    private String textInUse;
    
    public void createGUI()
    {
        //created new JFrame window called Eat
        window = new JFrame("Wikipedia Scorer");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout( new GridLayout(4,1) );
        
        //Make 4 Jpanels
        JPanel inputPanel = makeInputPanel();
        window.add(inputPanel);
        JPanel attachPanel = makeAttachPanel();
        window.add(attachPanel);
        JPanel calcPanel = makeCalcPanel();
        window.add(calcPanel);
        JPanel scorePanel = makeScorePanel();
        window.add(scorePanel);
        
        window.pack();
        
        
        window.getContentPane().setBackground(Color.red);
        
        //set size of window
        window.setSize(630,400);
        window.setVisible(true);
    }
    class FileListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            //Create a file chooser
            JFileChooser choose = new JFileChooser();
            choose.showOpenDialog(null);
            //open the file.
     		fileInUse = choose.getSelectedFile();
     		ioButton.setVisible(false);
     		fileNameLabel.setText(fileInUse.getName());
     		fileNameLabel.setVisible(true);
            //get the extension of the file and use the appropriate extractor
     		int pos = fileNameLabel.getText().lastIndexOf('.');
    		String ext = fileNameLabel.getText().substring(pos+1);
    		String ret = "";
    		if (ext=="txt")
    			ret=ReadFromFile.readFromTxt(fileInUse);
    		else if (ext=="doc"||ext=="docx")
    			ret=ReadFromFile.readFromDoc(fileInUse);
    		textInUse=ret;
        }
    }
    class calcListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		if (textInUse==null)
    			textInUse=textInput.getText();
    		//TODO add scoring function
    	}
    }
    private JPanel makeInputPanel()
    {
    	JPanel p = new JPanel();
        JLabel l = new JLabel("Input Text Here:");
        p.setLayout( new GridLayout(1,2, 10, 10) );
        p.add(l);
        textInput = new JTextArea(10,15);
        p.add(textInput);
        Color bckgrnd = new Color(166,166,166);
        p.setBackground(bckgrnd);
        return p;
    }
    
    private JPanel makeAttachPanel()
    {
	//create the panel for attachment files
     JPanel p = new JPanel();
     //add label,buttons
     JLabel l = new JLabel("Attach File Here:");
     ioButton = new JButton("Open File");
     FileListener readButtonlisten = new FileListener();
     ioButton.addActionListener(readButtonlisten);
     fileInUse = new File("zero");
     fileNameLabel = new JLabel("");
     fileNameLabel.setVisible(false);
     p.setLayout( new GridLayout(1,2, 10, 10) );
     p.add(l);
     p.add(ioButton);
     p.add(fileNameLabel);
     return p;
    }
    private JPanel makeCalcPanel()
    {
    JPanel p = new JPanel();
    JButton b = new JButton ("Calculate");
     p.add(b);
     
     Color bckgrnd = new Color(100,100,100);
     
     p.setBackground(bckgrnd);
     
     return p;
    }
    
    private JPanel makeScorePanel()
    {
     //create a new JPanel and displays the comment
     JPanel p = new JPanel();
     int x = 0;
     JLabel l = new JLabel("You Wrote "+x+"% in the encyclopedic style.");
     p.add(l);
     Color bckgrnd = new Color(50,50,50);
     
     p.setBackground(bckgrnd);
     
     return p;
    }

    public static void main( String[] args )
    {
        wikiGUI app = new wikiGUI();
        app.createGUI();
    }
}