/*
 * Copyright (c) 2019, Joseph Tyler Jones. All rights reserved.
 */
import javax.swing.*;
import javax.swing.SwingUtilities;
import java.io.File;
/**
 * JFrame which contains a file explorer for selecting a file directory
 * @author Tyler Jones
 * @version 1.0
 */
@SuppressWarnings("serial")
public class FileFrame extends JFrame
{
	/**
	 * JFileChooser object
	 */
    private JFileChooser fc;
    /**
     * user chosen target file directory File object
     */
    private File file;
    /**
     * FileFrame constructor
     */
    public FileFrame()
    {
        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fc.showOpenDialog(FileFrame.this);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            file = fc.getSelectedFile();
        }
        else if(returnVal == JFileChooser.CANCEL_OPTION)
        {
        	System.exit(0);
        }
    }
    /**
     * Runs constructor
     */
    public static void main() {
    	// Run the GUI construction in the Event-Dispatching thread for thread-safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FileFrame(); // Let the constructor do the job
            }
        });
    }
    /**
     * Get file
     * @return file File object
     */
    public File getFile()
    {
    	return file;
    }
}