import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.*;
import java.io.*;

public class Eat
{
    // decrlared variables to be used in the future
    private JFrame window;
    private JTextArea eatInput;
    
    
    public JFrame createGUI()
    {
        //created new JFrame window called Eat
        window = new JFrame("Eat");
        JPanel p = null;
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout( new GridLayout(4,1) );
        
        //Made 4 Jpanels
        p = panel1();
        window.add(p);
        p = panel2();
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
    
    private JPanel panel1()
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
    
    private JPanel panel2()
    {
     JPanel p = new JPanel();
     JLabel l = new JLabel("Attach File Here:");
     p.add(l);
     
     /* try{
     BufferedReader in = new BufferedReader(new FileReader("README.md")); 
     String text = in.readLine(); 
     in.close();
     
     JLabel n = new JLabel(text);
     
     System.out.print(text);
     p.add(n);
     
     return p;
    }
    catch (Exception Crap){
      System.out.println("You IDIOT");
    }
    */
   Color bckgrnd = new Color(50,50,50);
     
     p.setBackground(bckgrnd);
    return p;
    }
    
    private JPanel panel3()
    {
     JPanel p = new JPanel();
        JButton b = new JButton ("Eat");
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
        Eat app = new Eat();
        JFrame theWindow = app.createGUI();
        theWindow.setVisible(true);
        System.out.println(theWindow.getSize());
    }

}
