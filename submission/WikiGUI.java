import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.Hashtable;
import java.awt.Container.*;
import javax.swing.text.*;

public class WikiGUI extends JPanel
{
    // decrlared variables to be used in the future
	private Count all;
	private Count good;
	private String[] alphabet;
    private JFrame window;
    private JTextArea wordInput;
    private JButton ioButton;
    private File fileInUse;
    private JLabel fileNameLabel;
    private JLabel scoreLabel;
    private JProgressBar progBar;
    private final int MY_MIN=0;
    private final int MY_MAX=100;
    public JMenuBar menuBar;
    public JToolBar toolBar;
    public static final String QUESTION_MENU = "Questions?";

    public WikiGUI(Count all, Count good){
    	
    	this.all = all;
    	this.good = good;
    	this.alphabet = (String[]) all.occurs.keySet().toArray(
    			new String[all.occurs.keySet().size()]);
    	
        //creates the menuBar
        menuBar = new JMenuBar();
        
        //creates and adds drop down menu Help to menuBar
        JMenu menu_Help = new JMenu ("Help");
        menuBar.add(menu_Help);
        
        //refer to WikiGUIMenu (inner class) and finds parameters;
        //tells computer to add Questions (view variable declarations) to the menuBar
        WikiGUIMenu menuQuestion = new WikiGUIMenu(QUESTION_MENU, new ImageIcon("action.gif"));

        //tells computer to make a new questionItem with menuQuestion in it
        JMenuItem questionItem = new JMenuItem(menuQuestion);

        //adds questionItem to menu_Help
        menu_Help.add(questionItem);
        
        //makes menuBar visible
        menu_Help.setVisible(true);

    }

    
    public JFrame createGUI()
    {
        //created new JFrame window called Wikipedia Scorer
        window = new JFrame("Wikipedia Scorer");
        JPanel p = null;
        
        //Exit the app when the close button is clicked
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //sets grid layout for window
        window.setLayout( new GridLayout(5,0) );

        //Makes 6 Jpanels
        p = makeInputLabelPanel();
        System.out.println(p.getPreferredSize());
        p.setPreferredSize(new Dimension(50,400));
        window.add(p);
        p= makeTextAreaPanel();
        window.add(p);
        p = makeAttachPanel();
        window.add(p);
        p = panel5();
        window.add(p);
        p = panel6();
        window.add(p);
        window.pack();

        //set size of window
        window.setSize(630,400);
        return window;
    }
    
    //ActionListener for FileChooser
    class ButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            //Create a file chooser, opens nothing
            JFileChooser choose = new JFileChooser();
            //recieves file name
            fileInUse = choose.getSelectedFile();

            fileNameLabel.setText(fileInUse.getName());
            fileNameLabel.setVisible(true);
            //open the file.
        }
    }

    class CalculateListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		String text = wordInput.getText();
    		String[] tokens = Tokenizer.tokenize(text);
    		tokens = Tokenizer.stripTokens(tokens, alphabet);
    		scoreLabel.setText(Scorer.score(tokens, all, good) + "");
    	}
    }
    
    //Make a new panel. Add a label to the panel.
    private JPanel makeInputLabelPanel()
    {
        JPanel p = new JPanel(new GridLayout(1, 1, 1, 1));
        p.setSize(70,70);

        JLabel l = new JLabel("Input Text Below or Attach File:", SwingConstants.CENTER);

        p.add(l);

        return p;
    }

    //Make a new panel.  Add a text area and a scroll bar
    // that appears after text with a certain
    // length is typed.  getlineoffset() method can throw an
    // exception
    private JPanel makeTextAreaPanel()
    {
        JPanel p = new JPanel();
        p.setLayout( new BorderLayout() );
        wordInput = new JTextArea(20,20);
        wordInput.setLineWrap(true);
        wordInput.setWrapStyleWord(true);
        JScrollPane scroller = new JScrollPane(wordInput);

        try{
            int n =0;
            while (true){
                System.out.print("offset " + n + "is on");
                System.out.println("line " + wordInput.getLineOfOffset(n));
                n += 1;
            }

        } catch ( BadLocationException ex) { System.out.println(ex);}
        p.add(scroller, java.awt.BorderLayout.CENTER);
        p.add( Box.createHorizontalStrut( 30 ), BorderLayout.WEST );
        p.add( Box.createHorizontalStrut( 30 ), BorderLayout.EAST );
        return p;
    }

    // Make a new panel and add a button to the panel.
    // The button, when clicked has the functionality
    // of browsing and attaching a file on the local system.
    private JPanel makeAttachPanel()
    {
        //create the panel for attachment files
        JPanel p = new JPanel();
        //add label,buttons
        JLabel l = new JLabel("Attach File Here:");
        ioButton = new JButton("Attach File");
        ButtonListener readButtonlisten = new ButtonListener();
        ioButton.addActionListener(readButtonlisten);
        fileInUse = new File("zero");
        fileNameLabel = new JLabel("");
        fileNameLabel.setVisible(false);
        p.add(ioButton);
        p.add(fileNameLabel);
        return p;
    }

    // A calculate button in its own panel that calculates the 
    // Wikipedia score
    private JPanel panel5()
    {
        JPanel p = new JPanel();
        JButton b = new JButton ("Calculate");
        b.addActionListener(new CalculateListener());
        p.add(b);
        return p;
    }

    //A panel with the results and progress bar
    private JPanel panel6()
    {
        JPanel p = new JPanel();
        int x = 0;
        scoreLabel = new JLabel("You wrote "+x+"% in the encyclopedic style.");
        p.add(scoreLabel);

        progBar = new JProgressBar();
        progBar.setMinimum(MY_MIN);
        progBar.setMaximum(MY_MAX);
        add(progBar);
        return p;
    }

    public class WikiGUIMenu extends AbstractAction
    {
        //This is used to create AbstractAction class, which is used to use "get" and "set" methods to the object
        public WikiGUIMenu(String text, Icon icon){
            super (text, icon);
        }

        //tells the program what action was performed
        //if buttoon QUESTION_MENU was selected, make a new JFrame and add text

        public void actionPerformed(ActionEvent ap)
        {
            System.out.println("Action " + ap.getActionCommand()+ " performed...");
            if (ap.getActionCommand().equals(WikiGUI.QUESTION_MENU))
            {
                JFrame p = new JFrame();
                p.setBounds(50,50,415,50);
                JLabel a = new JLabel("Please Contact Raghav Bhat by emailing him at rbhat@exeter.edu");
                p.add(a);
                
                p.setVisible(true);
               
            }

        }
    }

    //Main Method
    public static void main( String[] args )
    {
    	Hashtable<String, Count> loaded = JSON.parseAll(ReadandWrite.readFromTxt(new File("probs.json")));
    	Count all = loaded.get("all");
    	Count good = loaded.get("good");
        WikiGUI app = new WikiGUI(all, good);
        JFrame theWindow = app.createGUI();
        theWindow.setJMenuBar(app.menuBar);
        theWindow.setVisible(true);
        System.out.println(theWindow.getSize());
    }

}
