/*
 * @author Weihang Fan
 */

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

class WikiGUI extends JFrame {
  
  /**
   * 
   */
  private static final long serialVersionUID = -17538959359997845L;
  // Field declarations.
  private JFrame window;
  
  private JTextArea input;

  private JLabel output;
  
  private DataSet data;
  
  // Constructor declaration.
  public WikiGUI(String baseFileName) {
    window = new JFrame("Wikipedia Scorer");
    data = new DataSet(baseFileName);
  }
  
  // Create the Graphical User Interface.
  public JFrame createGUI(){
    // Initialize the temporary JPanel variable p.
    JPanel p = null;
    
    // Set the basic parameters of the window.
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setSize(500,500);
    window.setLayout(new GridLayout(3,1));
    
    // Add the Output panel to the window.
    p = makeOutputPanel();
    window.add(p);
    
    // Add the Button panel to the window.
    p = makeButtonPanel();
    window.add(p);
    
    // Add the Input panel to the window.
    p = makeInputPanel();
    window.add(p);
    
    // Finalize the window.
    window.pack();
    return window;
  }
  
  // Make the Input panel.
  private JPanel makeInputPanel(){
    // Make the JPanel.
    JPanel p = new JPanel();
    
    // Make the input text area and add it to the JPanel.
    input = new JTextArea();
    p.add(input);
    
    return p;
  }
  
  // Make the Button panel.
  private JPanel makeButtonPanel() {
    // Make the JPanel.
    JPanel p = new JPanel();
    
    // Make the calculate button.
    JButton b = new JButton("Calculate");
    
    // Add a listener to the button.
    ButtonListener l = new ButtonListener();
    b.addActionListener(l);
    
    // Add the button to the panel.
    p.add(b);
    return p;
  }

  // Create the Output panel.
  private JPanel makeOutputPanel() {
    // Make the JPanel and sets its layout.
    JPanel p = new JPanel();
    p.setLayout(new BorderLayout());
    
    // Make the text area and sets its line wrap options.
    JTextArea a = new JTextArea(5,20);
    a.setLineWrap(true);
    a.setWrapStyleWord(true);
    
    // Make the JScrollPane to the JTextArea and sets its scroll bar policy.
    JScrollPane sp = new JScrollPane(a);
    sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    
    // Make the Scroll Pane always at the center with spaces on the sides and add it to the panel.
    p.add(sp,BorderLayout.CENTER);
    p.add( Box.createHorizontalStrut(30), BorderLayout.WEST );
    p.add( Box.createHorizontalStrut(30), BorderLayout.EAST );
    return p;
  }

  // Create the class ButtonListener which implements the class ActionListener to be able to score the input when the button is pressed.
  public class ButtonListener implements ActionListener{
    public void actionPerformed(ActionEvent e){
      // Scores the input.
      Scorer theScorer = new Scorer(data);
      output.setText(Double.toString(Double.valueOf(theScorer.score(Tokenizer.stripTokens(Tokenizer.tokenize(input.getText()),DataSet.loadAlphabet())))));
    }
  }
  
  public static void main(String[] args) {
    // Start the program.
    WikiGUI app = new WikiGUI("default");
    app.createGUI();
  }
}
