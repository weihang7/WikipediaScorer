/*
 * @author Raghav Bhat
 * @author Weihang Fan
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.Hashtable;

public class wikiGUI
{
    // decrlared variables to be used in the future
    private JFrame window;
    private JTextArea textInput;
    private JButton ioButton;
    private File fileInUse;
    private JLabel fileNameLabel;
    private String textInUse;
    private double score;
    
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
     		if (fileInUse!=null){
     			String name = fileInUse.getName();
	     		ioButton.setVisible(false);
	     		fileNameLabel.setText(name);
	     		fileNameLabel.setVisible(true);
	     		
	     		//Finds the extension of the file.
	     		int pos = name.lastIndexOf('.');
	     		String ext = name.substring(pos+1);
	     		
	     		// Determines which function to use based on extension.
	     		textInUse = (ext.equals("doc") ? ReadandWrite.readFromDoc(fileInUse)
	     				  : (ext.equals("docx") ? ReadandWrite.readFromDocx(fileInUse)
	     				  : ReadandWrite.readFromTxt(fileInUse)));
     		}
        }
    }
    class calcListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		if (textInUse==null)
    			textInUse=textInput.getText();
    		String[] tokenizedText = Tokenizer.tokenize(textInUse);
    		tokenizedText = Tokenizer.stripTokens(tokenizedText, Tokenizer.getAlphabet(tokenizedText, 1000));
    		Hashtable randomArticles = JSON.parse(ReadandWrite.readFromTxt(new File("data/bigrams.json")));
    		Hashtable goodArticles = Fetcher.extractGoodArticles(randomArticles);
    		score = Scorer.score(tokenizedText, randomArticles, goodArticles, (Double) randomArticles.get("num") / (Double) goodArticles.get("num"));
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
    calcListener calcListen = new calcListener();
    b.addActionListener(calcListen);
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
    	if (!new File("data").exists()){
    		Master.main(new String[]{"100","1000","data/occurrence.json","data/bigrams.json"});
    	}
        wikiGUI app = new wikiGUI();
        app.createGUI();
    }
}