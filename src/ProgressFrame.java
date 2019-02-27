/*
 * Copyright (c) 2019, Joseph Tyler Jones. All rights reserved.
 */
import java.awt.*;
import javax.swing.*;
/**
 * JFrame which shows image comparison processing progress
 * @author Tyler Jones
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ProgressFrame extends JFrame
{
	/**
	 * Number of comparisons to make
	 */
	private int maximum;
	/**
	 * JProgressBar object
	 */
	private JProgressBar pb;
	/**
	 * ProgressFrame constructor
	 */
	public ProgressFrame()
	{
		Container cp = getContentPane();
		JPanel topPane = new JPanel();
		topPane.setLayout(new GridLayout(2,1));
		cp.add(topPane);
		JLabel progLabel1 = new JLabel("Image Processing in Progress.  Please Wait...",SwingConstants.CENTER);
        topPane.add(progLabel1);
        pb = new JProgressBar(0,1);
        pb.setStringPainted(true);
        topPane.add(pb);
        pb.setString("Calculating Number of Comparisons...");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Exit program if close-window button clicked
        setTitle("Timelapse Motion Detection"); // "super" JFrame sets title
        setSize(300, 100);        // "super" JFrame sets initial size
        setVisible(true);          // "super" JFrame shows
	}
	/**
	 * Set maximum integer
	 * @param max
	 */
	public void pbSetMax(int max)
	{
		maximum = max;
		pb.setMaximum(max);
	}
	/**
	 * Update progress bar to reflect processing progress
	 * @param prog number of comparisons already made
	 */
	public void pbUpdate(int prog)
	{
		pb.setValue(prog);
		pb.setString("Comparison "+prog+"/"+maximum);
	}
	/**
	 * Kills ProgressFrame
	 */
	public void endProgress()
	{
		setVisible(false);
		dispose();
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
	            new ProgressFrame(); // Let the constructor do the job
	        }
	    });
	}
}
