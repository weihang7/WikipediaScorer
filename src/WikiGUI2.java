class WikiGUI2 extends JFrame {
  
  private JPanel inputPanel; //Place for textarea
  private JTextArea input;

  private JPanel buttonPanel; //Place for "score" button
  private JButton button;

  private JPanel outputPanel; //Place for output number.
  private JLabel output;

  public WikiGUI2(String baseFileName) {
    DataSet data = new DataSet(baseFileName);
    
    JPanel contentPane = new 

    //Set up the input panel.
    inputPanel = new JPanel();
    input = new JTextArea();
    inputPanel.add(input);

    //Set up the button panel.
    button = new JPanel();
    output = new JPanel();
  }

  public static void main(String[] args) {
    
  }
}
