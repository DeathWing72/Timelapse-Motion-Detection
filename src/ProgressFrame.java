import java.awt.*;
import javax.swing.*;
@SuppressWarnings("serial")
public class ProgressFrame extends JFrame
{
	private int maximum;
	private JProgressBar pb;
	public ProgressFrame(int min, int max)
	{
		Container cp = getContentPane();
		JPanel topPane = new JPanel();
		topPane.setLayout(new GridLayout(2,1));
		cp.add(topPane);
		JLabel progLabel1 = new JLabel("Image Processing in Progress.  Please Wait...",SwingConstants.CENTER);
        topPane.add(progLabel1);
        pb = new JProgressBar(min,max);
        pb.setStringPainted(true);
        topPane.add(pb);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Exit program if close-window button clicked
        setTitle("Timelapse Motion Detection"); // "super" JFrame sets title
        setSize(300, 100);        // "super" JFrame sets initial size
        setVisible(true);          // "super" JFrame shows
	}
	public void pbSetMax(int max)
	{
		maximum = max;
		pb.setMaximum(max);
	}
	public void pbUpdate(int prog)
	{
		pb.setValue(prog);
		pb.setString("Comparison "+prog+"/"+maximum);
	}
	public void endProgress()
	{
		setVisible(false);
		dispose();
	}
	public static void main(int min, int max)
	{
	    // Run the GUI construction in the Event-Dispatching thread for thread-safety
	    SwingUtilities.invokeLater(new Runnable() {
	        @Override
	        public void run() {
	            new ProgressFrame(min, max); // Let the constructor do the job
	        }
	    });
	}
}
