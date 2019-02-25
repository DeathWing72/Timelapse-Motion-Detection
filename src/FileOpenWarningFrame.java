import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
@SuppressWarnings("serial")
public class FileOpenWarningFrame extends JFrame
{
	private boolean buttonPressed;
	public FileOpenWarningFrame()
	{
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		JLabel warnLabel = new JLabel("Please close any open file associated with the target folder before proceeding.",SwingConstants.CENTER);
        warnLabel.setPreferredSize(new Dimension(500,100));
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
        setSize(500, 300);        // "super" JFrame sets initial size
        setVisible(true);          // "super" JFrame shows
	}
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
	public boolean getButtonPressed()
	{
		return buttonPressed;
	}
}
