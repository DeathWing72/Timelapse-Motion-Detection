import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;
@SuppressWarnings("serial")
public class CompareFrame extends JFrame
{
    private boolean isMatch, buttonPressed, endEarly;
    private Container cp;
    private JPanel topPane, cenPane, botPane;
    private JButton endEarlyButton, matchButton, noMatchButton;
    public CompareFrame()
    {
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
    	cp = getContentPane();
        cp.setLayout(new BorderLayout(3,3));
        
        topPane = new JPanel();
        topPane.setPreferredSize(new Dimension(1440,45));
        topPane.setLayout(new GridLayout(2,3));
        cp.add(topPane,BorderLayout.NORTH);
        
        cenPane = new JPanel();
        cenPane.setPreferredSize(new Dimension(1440,480));
        cenPane.setLayout(new GridLayout(1,2));
        cp.add(cenPane,BorderLayout.CENTER);
        
        botPane = new JPanel();
        botPane.setPreferredSize(new Dimension(1440,45));
        botPane.setLayout(new GridLayout(1,3));
        cp.add(botPane,BorderLayout.SOUTH);
        
        matchButton = new JButton("Confirm Movement");
        botPane.add(matchButton);
        endEarlyButton = new JButton("End User Confirmation Early");
        botPane.add(endEarlyButton);
        noMatchButton = new JButton("No Movement");
        botPane.add(noMatchButton);
        
        matchButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent evt) {
            buttonPressed = true;
            isMatch=true;
         }
        });
        noMatchButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent evt) {
            buttonPressed = true;
         }
        });
        endEarlyButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent evt) {
     	    endEarly();
         }
        });
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Exit program if close-window button clicked
        setTitle("Image Comparison"); // "super" JFrame sets title
        setSize(1443, 570);        // "super" JFrame sets initial size
        setVisible(true);          // "super" JFrame shows
    }
    public void endEarly() //Add and subtract entire panes, not just buttons and labels
    {
    	botPane.removeAll();
    	
    	JButton yesButton = new JButton("Yes");
    	botPane.add(yesButton);
    	JLabel warnLabel = new JLabel("Do you really want to stop match confirmation early?",SwingConstants.CENTER);
    	botPane.add(warnLabel);
    	JButton cancelButton = new JButton("Cancel");
    	botPane.add(cancelButton);
    	
    	cp.revalidate();
    	
    	yesButton.addActionListener(new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent evt)
    		{
    			buttonPressed = true;
    			endEarly = true;
    		}
    	});
    	cancelButton.addActionListener(new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent evt)
    		{
    			cancelEndEarly();
    		}
    	});
    }
    public void cancelEndEarly()
    {
    	botPane.removeAll();
    	
    	botPane.add(matchButton);
    	botPane.add(endEarlyButton);
    	botPane.add(noMatchButton);
    	
    	cp.revalidate();
    }
    public void updateFrame(File f1,File f2,double i,String d1,String d2,String t1,String t2,int ci,int len)
    {
    	topPane.removeAll();
    	JLabel nameLabel1 = new JLabel(f1.getName(),SwingConstants.CENTER);
        topPane.add(nameLabel1);
        JLabel progLabel1 = new JLabel("Potential Match "+ci+"/"+len,SwingConstants.CENTER);
        topPane.add(progLabel1);
        JLabel nameLabel2 = new JLabel(f2.getName(),SwingConstants.CENTER);
        topPane.add(nameLabel2);
        JLabel timeLabel1 = new JLabel(d1+" "+t1,SwingConstants.CENTER);
        topPane.add(timeLabel1);
        JLabel indexLabel = new JLabel(Double.toString(i),SwingConstants.CENTER);
        topPane.add(indexLabel);
        JLabel timeLabel2 = new JLabel(d2+" "+t2,SwingConstants.CENTER);
        topPane.add(timeLabel2);
        
        cenPane.removeAll();
        ImagePanel img1 = new ImagePanel(f1);
        cenPane.add(img1);
        ImagePanel img2 = new ImagePanel(f2);
        cenPane.add(img2);
        
        cp.revalidate();
    }
    public void killFrame()
    {
    	setVisible(false);
    	dispose();
    }
    public static void main() {
        // Run the GUI construction in the Event-Dispatching thread for thread-safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CompareFrame(); // Let the constructor do the job
            }
        });
    }
    public boolean getIsMatch()
    {
    	return isMatch;
    }
    public boolean getButtonPressed()
    {
    	return buttonPressed;
    }
    public boolean getEndEarly()
    {
    	return endEarly;
    }
    public void resetStates()
    {
    	buttonPressed = false;
    	isMatch = false;
    	endEarly = false;
    }
}