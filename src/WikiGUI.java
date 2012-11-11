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
  
  private JFrame window;
  
  private JPanel inputPanel;
  private JTextArea input;

  private JPanel buttonPanel;
  
  private JPanel outputPanel;
  private JLabel output;
  
  private DataSet data;

  public WikiGUI(String baseFileName) {
    window = new JFrame("Wikipedia Scorer");
   // data = new DataSet(baseFileName);
    
    //Set up the input panel.
    inputPanel = makeInputPanel();
    
    //Set up the button panel.
    buttonPanel = makeButtonPanel();
    
    //Set up the output panel.
    outputPanel = makeOutputPanel();
    
    window.pack();
    window.setVisible(true);
  }
  
  public JFrame createGUI(){
    JPanel p = null;
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setSize(500,500);
    window.setLayout( new GridLayout(3,1) );
    
    p = makeOutputPanel();
    window.add(p);
    
    p = makeButtonPanel();
    window.add(p);
    
    p = makeInputPanel();
    window.add(p);
    
    window.pack();
    return window;
  }

  private JPanel makeInputPanel(){
    JPanel p = new JPanel();
    input = new JTextArea();
    p.add(input);
    return p;
  }
  
  private JPanel makeButtonPanel() {
    JPanel p = new JPanel();
    JButton b = new JButton("Calculate");
    ButtonListener l = new ButtonListener();
    b.addActionListener(l);
    p.add(b);
    return p;
  }

  private JPanel makeOutputPanel() {
    JPanel p = new JPanel();
    p.setLayout(new BorderLayout());
    JTextArea a = new JTextArea(5,20);
    a.setLineWrap(true);
    a.setWrapStyleWord(true);
    JScrollPane sp = new JScrollPane(a);
    sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    p.add(sp,BorderLayout.CENTER);
    p.add( Box.createHorizontalStrut(30), BorderLayout.WEST );
    p.add( Box.createHorizontalStrut(30), BorderLayout.EAST );
    return p;
  }

  
  public class ButtonListener implements ActionListener{
    public void actionPerformed(ActionEvent e){
      Scorer theScorer = new Scorer(data);
      output.setText(Double.toString(Double.valueOf(theScorer.score(Tokenizer.stripTokens(Tokenizer.tokenize(input.getText()),DataSet.loadAlphabet())))));
    }
  }
  
  public static void main(String[] args) {
    WikiGUI app = new WikiGUI("default");
    app.createGUI();
  }
}
