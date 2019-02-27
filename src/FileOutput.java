/*
 * Copyright (c) 2019, Joseph Tyler Jones. All rights reserved.
 */
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
/**
 * Outputs text to a new file
 * @author Tyler Jones
 * @version 1.0
 */
public class FileOutput
{
	/**
	 * PrintWriter object
	 */
    private PrintWriter out;
    /**
     * Creates new file in custom location or default target folder
     * @param file Target file folder File object
     * @param customLoc Custom file location String object
     * @param name Custom file name String object
     * @param ext Custom file extension String object
     */
    public void setFile(File file, String customLoc, String name, String ext)
    {
    	if(customLoc.length() != 0)
    	{
    		try 
    		{
                out = new PrintWriter(new FileWriter(customLoc+"\\"+name+ext));
            } catch (IOException e) {}
    	}
    	else
    	{
    		try {
                out = new PrintWriter(new FileWriter(file.getAbsolutePath()+"\\"+name+ext));
            } catch (IOException e) {}
    	}
    }
    /**
     * Print string to file without carriage return
     * @param print text to be printed to file
     */
    public void newPrint(String print)
    {
        out.print(print);
    }
    /**
     * Print string to file with carriage return
     * @param print text to be printed to file
     */
    public void newPrintln(String print)
    {
    	out.println(print);
    }
    /**
     * Ends PrintWriter and establishes file
     */
    public void endPrint()
    {
        out.close();
    }
}
