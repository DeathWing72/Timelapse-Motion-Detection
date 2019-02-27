/*
 * Copyright (c) 2019, Joseph Tyler Jones. All rights reserved.
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
/**
 * JFrame which warns the user of opened files associated with the target folder before starting comparison
 * @author Tyler Jones
 * @version 1.0
 */
@SuppressWarnings("serial")
public class FileOpenWarningFrame extends JFrame
{
	/**
	 * Has a button been pressed?
	 */
	private boolean buttonPressed;
	/**
	 * Constructs FileOpenWarningFrame
	 */
	public FileOpenWarningFrame()
	{
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		JLabel warnLabel = new JLabel("Please close any open file associated with the target folder before proceeding. NOTE: Please strictly use .bmp image files originating from SkyStudio Pro for full program functionality.",SwingConstants.CENTER);
        warnLabel.setPreferredSize(new Dimension(1100,100));
		cp.add(warnLabel,BorderLayout.NORTH);
        JButton okButton = new JButton("Confirm Files Closed");
        cp.add(okButton,BorderLayout.CENTER);
        okButton.addActionListener(new ActionListener() {
		 @Override
         public void actionPerformed(ActionEvent evt) {
        	 buttonPressed = true;
			 setVisible(false);
			 dispose();
         }
        });
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Exit program if close-window button clicked
        setTitle("Timelapse Motion Detection"); // "super" JFrame sets title
        setSize(1100, 300);        // "super" JFrame sets initial size
        setVisible(true);          // "super" JFrame shows
	}
	/**
	 * Runs constructor
	 */
	public static void main()
	{
	    // Run the GUI construction in the Event-Dispatching thread for thread-safety
	    SwingUtilities.invokeLater(new Runnable() {
	        @Override
	        public void run() {
	            new FileOpenWarningFrame(); // Let the constructor do the job
	        }
	    });
	}
	/**
	 * Get buttonPressed
	 * @return buttonPressed boolean
	 */
	public boolean getButtonPressed()
	{
		return buttonPressed;
	}
}
