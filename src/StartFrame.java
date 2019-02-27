/*
 * Copyright (c) 2019, Joseph Tyler Jones. All rights reserved.
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
/**
 * JFrame which allows user to start program or change structural similarity index movement threshold
 * @author Tyler Jones
 * @version 1.0
 */
@SuppressWarnings("serial")
public class StartFrame extends JFrame
{
	/**
	 * Has the program start button been pressed?
	 */
	private boolean startButtonPressed;
	/**
	 * Has the change movement threshold button been pressed?
	 */
	private boolean changeButtonPressed;
	/**
	 * StartFrame constructor
	 * @param threshold structural similarity index movement threshold
	 */
	public StartFrame(double threshold)
	{
		Container cp = getContentPane();
		cp.setLayout(new GridLayout(2,1));
		JButton conButton = new JButton("Start Analysis");
		cp.add(conButton);
		JButton dirButton = new JButton("Change Match Threshold      Current Value = "+threshold);
		cp.add(dirButton);
		conButton.addActionListener(new ActionListener() {
		 @Override
         public void actionPerformed(ActionEvent evt) {
        	 startButtonPressed = true;
			 setVisible(false);
			 dispose();
         }
        });
		dirButton.addActionListener(new ActionListener() {
		 @Override
         public void actionPerformed(ActionEvent evt) {
        	 changeButtonPressed = true;
        	 setVisible(false);
        	 dispose();
         }
        });
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Exit program if close-window button clicked
        setTitle("Timelapse Motion Detection"); // "super" JFrame sets title
        setSize(900, 120);        // "super" JFrame sets initial size
        setVisible(true);          // "super" JFrame shows
	}
	/**
	 * Runs constructor
	 * @param threshold structural similarity index movement threshold
	 */
	public static void main(double threshold)
	{
	    // Run the GUI construction in the Event-Dispatching thread for thread-safety
	    SwingUtilities.invokeLater(new Runnable() {
	        @Override
	        public void run() {
	            new StartFrame(threshold); // Let the constructor do the job
	        }
	    });
	}
	/**
	 * Get startButtonPressed
	 * @return startButtonPressed boolean
	 */
	public boolean getStartButtonPressed()
	{
		return startButtonPressed;
	}
	/**
	 * Get changeButtonPressed
	 * @return changeButtonPressed boolean
	 */
	public boolean getChangeButtonPressed()
	{
		return changeButtonPressed;
	}
}
