import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class wikiGUI
{
    // decrlared variables to be used in the future
    private JFrame window;
    private JTextArea eatInput;
    private JButton ioButton;
    private File fileInUse;
    private JLabel fileNameLabel;
    
    public JFrame createGUI()
    {
        //created new JFrame window called Eat
        window = new JFrame("Eat");
        JPanel p = null;
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout( new GridLayout(4,1) );
        
        //Made 4 Jpanels
        p = makeInputPanel();
        window.add(p);
        p = makeAttachPanel();
        window.add(p);
        p = panel3();
        window.add(p);
        p = panel4();
        window.add(p);
        
        window.pack();
        
        
        window.getContentPane().setBackground(Color.red);
        
        //set size of window
        window.setSize(630,400);
        return window;
    }
    class ButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            //Create a file chooser
            JFileChooser choose = new JFileChooser();
            choose.showOpenDialog(null);
     		fileInUse = choose.getSelectedFile();
     		ioButton.setVisible(false);
     		fileNameLabel.setText(fileInUse.getName());
     		fileNameLabel.setVisible(true);
            //open the file.
        }
    }
    private JPanel makeInputPanel()
    {
    	JPanel p = new JPanel();
        JLabel l = new JLabel("Input Text Here:");
        p.setLayout( new GridLayout(1,2, 10, 10) );
        p.add(l);
        eatInput = new JTextArea(10,15);
        p.add(eatInput);
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
     ButtonListener readButtonlisten = new ButtonListener();
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
    private JPanel panel3()
    {
    JPanel p = new JPanel();
    JButton b = new JButton ("Calculate");
     p.add(b);
     
     Color bckgrnd = new Color(100,100,100);
     
     p.setBackground(bckgrnd);
     
     return p;
    }
    
    private JPanel panel4()
    {
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
        JFrame theWindow = app.createGUI();
        theWindow.setVisible(true);
        System.out.println(theWindow.getSize());
    }

}
