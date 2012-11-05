import javax.swing.*;
public class MasterGUI extends JFrame {
  private static final long serialVersionUID = -2918480137245589300L;

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

    JLabel test = new JLabel("TEST TEST TEST");
    middlePanel.add(test);

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
    JPanel content = new JPanel();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(630,400);
    content.add(makeTopPanel());
    content.add(makeMiddlePanel());
    content.add(makeBottomPanel());
    content.add(makeFinalPanel());
    setContentPane(content);
  }

  public static void main(String [] args){
    MasterGUI gui = new MasterGUI();
    gui.setVisible(true);
  }
}
