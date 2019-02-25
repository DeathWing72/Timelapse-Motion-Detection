import java.io.File;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
@SuppressWarnings("serial")
public class EndFrame extends JFrame
{
	private boolean buttonPressed;
	public EndFrame(File file)
	{
		Container cp = getContentPane();
		cp.setLayout(new GridLayout(3,1));
		JLabel instLabel = new JLabel("Target folder analysis finished.  Analysis results and process logs can be found in:",SwingConstants.CENTER);
		cp.add(instLabel);
		JLabel pathLabel = new JLabel(file.getAbsolutePath(),SwingConstants.CENTER);
		cp.add(pathLabel);
		JPanel botPane = new JPanel();
		botPane.setLayout(new GridLayout(1,2));
		cp.add(botPane);
		JButton conButton = new JButton("Close Program");
		botPane.add(conButton);
		JButton dirButton = new JButton("Open Directory");
		botPane.add(dirButton);
		conButton.addActionListener(new ActionListener() {
		 @Override
         public void actionPerformed(ActionEvent evt) {
        	 buttonPressed = true;
			 setVisible(false);
			 dispose();
         }
        });
		dirButton.addActionListener(new ActionListener() {
		 @Override
         public void actionPerformed(ActionEvent evt) {
        	 open(file);
         }
        });
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Exit program if close-window button clicked
        setTitle("Timelapse Motion Detection"); // "super" JFrame sets title
        setSize(900, 120);        // "super" JFrame sets initial size
        setVisible(true);          // "super" JFrame shows
	}
	public static void main(File file)
	{
	    // Run the GUI construction in the Event-Dispatching thread for thread-safety
	    SwingUtilities.invokeLater(new Runnable() {
	        @Override
	        public void run() {
	            new EndFrame(file); // Let the constructor do the job
	        }
	    });
	}
	public static boolean open(File file)
	{
	    try
	    {
	        if (OSDetector.isWindows())
	        {
	            Runtime.getRuntime().exec(new String[]
	            {"rundll32", "url.dll,FileProtocolHandler",
	             file.getAbsolutePath()});
	            return true;
	        } else if (OSDetector.isLinux() || OSDetector.isMac())
	        {
	            Runtime.getRuntime().exec(new String[]{"/usr/bin/open",
	                                                   file.getAbsolutePath()});
	            return true;
	        } else
	        {
	            // Unknown OS, try with desktop
	            if (Desktop.isDesktopSupported())
	            {
	                Desktop.getDesktop().open(file);
	                return true;
	            }
	            else
	            {
	                return false;
	            }
	        }
	    } catch (Exception e)
	    {
	        e.printStackTrace(System.err);
	        return false;
	    }
	}
	public boolean getButtonPressed()
	{
		return buttonPressed;
	}
}
