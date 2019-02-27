/*
 * Copyright (c) 2019, Joseph Tyler Jones. All rights reserved. 
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;
/**
 * JFrame which displays pair of images and asks for user match confirmation
 * @author Tyler Jones
 * @version 1.0
 */
@SuppressWarnings("serial")
public class CompareFrame extends JFrame
{
	/**
	 * Is there a match?
	 */
    private boolean isMatch;
    /**
     * Has a button been pressed?
     */
    private boolean buttonPressed;
    /**
     * End the program early?
     */
    private boolean endEarly;
    /**
     * Content Pane Container
     */
    private Container cp;
    /**
     * Top JPanel
     */
    private JPanel topPane;
    /**
     * Middle JPanel
     */
    private JPanel cenPane;
    /**
     * Bottom JPanel
     */
    private JPanel botPane;
    /**
     * Constructs new CompareFrame
     */
    public CompareFrame()
    {
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
        botPane.setLayout(new GridLayout(1,1));
        cp.add(botPane,BorderLayout.SOUTH);
        
        addBotButtons();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Exit program if close-window button clicked
        setTitle("Image Comparison"); // "super" JFrame sets title
        setSize(1443, 570);        // "super" JFrame sets initial size
        setVisible(true);          // "super" JFrame shows
    }
    /**
     * Remove bottom buttons and add end early buttons
     */
    public void endEarly()
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
    /**
     * Remove end early buttons and add back bottom buttons
     */
    public void cancelEndEarly()
    {
    	botPane.removeAll();
    	addBotButtons();
    	cp.revalidate();
    }
    /**
     * Add bottom buttons
     */
    public void addBotButtons()
    {
    	JButton matchButton = new JButton("Confirm Movement");
        botPane.add(matchButton);
        JButton endEarlyButton = new JButton("End User Confirmation Early");
        botPane.add(endEarlyButton);
        JButton noMatchButton = new JButton("No Movement");
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
    }
    /**
     * Update CompareFrame with new information
     * @param f1 image File object
     * @param f2 image File object
     * @param i structural similarity index
     * @param d1 date String object associated with File f1
     * @param d2 date String object associated with File f2
     * @param t1 time String object associated with File f1
     * @param t2 time String object associated with File f2
     * @param ci current image pair index
     * @param len maximum image pair index
     */
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
    /**
     * Kills CompareFrame
     */
    public void killFrame()
    {
    	setVisible(false);
    	dispose();
    }
    /**
     * Runs constructor
     */
    public static void main() {
        // Run the GUI construction in the Event-Dispatching thread for thread-safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CompareFrame(); // Let the constructor do the job
            }
        });
    }
    /**
     * Get isMatch
     * @return isMatch boolean
     */
    public boolean getIsMatch()
    {
    	return isMatch;
    }
    /**
     * Get buttonPressed
     * @return buttonPressed boolean
     */
    public boolean getButtonPressed()
    {
    	return buttonPressed;
    }
    /**
     * Get endEarly
     * @return endEarly boolean
     */
    public boolean getEndEarly()
    {
    	return endEarly;
    }
    /**
     * Set all booleans to false
     */
    public void resetStates()
    {
    	buttonPressed = false;
    	isMatch = false;
    	endEarly = false;
    }
}