import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
@SuppressWarnings("serial")
public class EndEarlyConfirmFrame extends JFrame
{
	private boolean buttonPressed, isEnd;
	public EndEarlyConfirmFrame()
	{
		Container cp = getContentPane();
		cp.setLayout(new GridLayout(1,1));
		
		JPanel backPane = new JPanel();
		backPane.setLayout(new GridLayout(2,1));
		cp.add(backPane);
		
		JLabel warnLabel = new JLabel("Do you really want to stop match confirmation early?",SwingConstants.CENTER);
        backPane.add(warnLabel);
        
        JPanel botPanel = new JPanel();
        botPanel.setLayout(new GridLayout(1,2));
        backPane.add(botPanel);
        
        JButton okButton = new JButton("Confirm End");
        botPanel.add(okButton);
        JButton noButton = new JButton("Cancel");
        botPanel.add(noButton);
        		        
        okButton.addActionListener(new ActionListener() {
		 @Override
         public void actionPerformed(ActionEvent evt) {
        	 buttonPressed = true;
        	 isEnd = true;
         }
        });
        noButton.addActionListener(new ActionListener() {
   		 @Override
         public void actionPerformed(ActionEvent evt) {
           	 buttonPressed = true;
         }
        });
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Exit program if close-window button clicked
        setTitle("End Early Confirmation"); // "super" JFrame sets title
        setSize(600, 400);        // "super" JFrame sets initial size
        setVisible(true);          // "super" JFrame shows
	}
	public void killFrame()
	{
		setVisible(false);
		dispose();
	}
	public static void main()
	{
	    // Run the GUI construction in the Event-Dispatching thread for thread-safety
	    SwingUtilities.invokeLater(new Runnable() {
	        @Override
	        public void run() {
	            new EndEarlyConfirmFrame(); // Let the constructor do the job
	        }
	    });
	}
	public boolean getButtonPressed()
	{
		return buttonPressed;
	}
	public boolean getIsEnd()
	{
		return isEnd;
	}
}
