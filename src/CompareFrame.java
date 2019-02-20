import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;
@SuppressWarnings("serial")
public class CompareFrame extends JFrame
{
    public boolean isMatch, buttonPressed;
    public CompareFrame(File f1,File f2,double i,String d,String t1,String t2,int ci,int len)
    {
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
    	Container cp = getContentPane();
        cp.setLayout(new BorderLayout(3,3));
        
        JPanel topPane = new JPanel();
        topPane.setPreferredSize(new Dimension(1440,45));
        topPane.setLayout(new GridLayout(2,3));
        cp.add(topPane,BorderLayout.PAGE_START);
        
        JLabel nameLabel1 = new JLabel(f1.getName(),SwingConstants.CENTER);
        topPane.add(nameLabel1);
        JLabel blankLabel1 = new JLabel("Potential Match "+ci+"/"+len,SwingConstants.CENTER);
        topPane.add(blankLabel1);
        JLabel nameLabel2 = new JLabel(f2.getName(),SwingConstants.CENTER);
        topPane.add(nameLabel2);
        JLabel timeLabel1 = new JLabel(t1,SwingConstants.CENTER);
        topPane.add(timeLabel1);
        JLabel dateLabel1 = new JLabel(d,SwingConstants.CENTER);
        topPane.add(dateLabel1);
        JLabel timeLabel2 = new JLabel(t2,SwingConstants.CENTER);
        topPane.add(timeLabel2);
        
        JPanel cenPane = new JPanel();
        cenPane.setPreferredSize(new Dimension(1440,480));
        cenPane.setLayout(new GridLayout(1,2));
        cp.add(cenPane,BorderLayout.CENTER);
        
        ImagePanel img1 = new ImagePanel(f1);
        cenPane.add(img1);
        ImagePanel img2 = new ImagePanel(f2);
        cenPane.add(img2);
        
        JPanel botPane = new JPanel();
        botPane.setPreferredSize(new Dimension(1440,45));
        botPane.setLayout(new GridLayout(1,3));
        cp.add(botPane,BorderLayout.PAGE_END);
        
        JButton matchButton = new JButton("Confirm Movement");
        botPane.add(matchButton);
        JLabel indexLabel = new JLabel(Double.toString(i),SwingConstants.CENTER);
        botPane.add(indexLabel);
        JButton noMatchButton = new JButton("No Movement");
        botPane.add(noMatchButton);
        
        matchButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent evt) {
            buttonPressed = true;
            isMatch=true;
            setVisible(false);
            dispose();
         }
        });
        noMatchButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent evt) {
            buttonPressed = true;
            setVisible(false);
            dispose();
         }
        });
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Exit program if close-window button clicked
        setTitle("Image Comparison"); // "super" JFrame sets title
        setSize(1443, 570);        // "super" JFrame sets initial size
        setVisible(true);          // "super" JFrame shows
    }
    public static void main(File f1,File f2,double i,String d,String t1,String t2,int ci,int len) {
        // Run the GUI construction in the Event-Dispatching thread for thread-safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CompareFrame(f1,f2,i,d,t1,t2,ci,len); // Let the constructor do the job
            }
        });
    }
}