/*
 * Copyright (c) 2019, Joseph Tyler Jones. All rights reserved. 
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JOptionPane;
/**
 * Main Timelapse Motion Detection class
 * @author Tyler Jones
 * @version 1.0
 */
public class TimelapseMotionDetection
{
	/**
	 * The threshold index for predicting movement between images
	 */
    private static double threshold = .91;
    /**
     * Data file output
     */
    private static FileOutput fOut = new FileOutput();
    /**
     * Error log file output
     */
    private static FileOutput fLog = new FileOutput();
    /**
     * Progress bar JFrame
     */
    private static ProgressFrame pg;
    /**
     * Main function
     * @param args unused
     */
    public static void main(String[] args)
    {
    	fileStart();
    	fileOpenWarning();
        FileFrame frame = new FileFrame();
        pg = new ProgressFrame();
        File subDir = new File(frame.getFile().getAbsoluteFile(), "Analysis Results");
        subDir.mkdir();
        fOut.setFile(frame.getFile(), subDir.getAbsolutePath(), frame.getFile().getName()+" Folder Image Analysis Match Results", ".csv");
        fLog.setFile(frame.getFile(), subDir.getAbsolutePath(), frame.getFile().getName()+" Folder Image Analysis Data Log", ".txt");
        File[] files = getFile(frame.getFile());
        PotMatches[] pm = findMatches(files);
        pg.endProgress();
        double matchPer = confirmMatches(pm);
        System.err.println(timeStamp() + "Confirmed Match Percentage: "+(matchPer*100)+"%");
		fLog.newPrintln(timeStamp() + "Confirmed Match Percentage: "+(matchPer*100)+"%");
		endWarning(subDir);
        fOut.endPrint();
        fLog.endPrint();
        System.exit(0);
    }
    /**
     * Open StartFrame and hold open until button pressed
     */
    public static void fileStart()
    {
    	StartFrame frame = new StartFrame(threshold);
    	while(!frame.getStartButtonPressed() && !frame.getChangeButtonPressed())
        {
            try
            {
                Thread.sleep(1);
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }
        }
    	if(frame.getChangeButtonPressed())
    	{
    		thresholdDialog();
    		fileStart();
    	}
    }
    /**
     * Open JOptionPane for setting threshold
     */
    public static void thresholdDialog()
    {
    	String newThresh = JOptionPane.showInputDialog("Please input a new match\nthreshold between 0.0 and 1.0");
    	if(newThresh != null && !newThresh.equals(""))
    	{
    		double tmpThresh = Double.parseDouble(newThresh);
    		if(tmpThresh >= 0.0 && tmpThresh <= 1.0)
    		{
    			threshold = tmpThresh;
    		}
    		else
    		{
    			thresholdDialog();
    		}
    	}
    }
    /**
     * Open FileOpenWarningFrame and hold open until button pressed
     */
    public static void fileOpenWarning()
    {
    	FileOpenWarningFrame frame = new FileOpenWarningFrame();
    	System.err.println(timeStamp() + "File Open Warning Frame Opened");
    	while(!frame.getButtonPressed())
        {
            try
            {
                Thread.sleep(1);
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }
        }
    	System.err.println(timeStamp() + "File Open Warning Heeded by User");
    }
    /**
     * Open EndFrame and hold open until button pressed
     * @param file output data file directory
     */
    public static void endWarning(File file)
    {
    	EndFrame frame = new EndFrame(file);
    	while(!frame.getButtonPressed())
        {
            try
            {
                Thread.sleep(1);
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }
        }
    }
    /**
     * Gets .bmp image file objects from file directory input
     * @param dir root image file directory File object
     * @return .bmp image File object array
     */
    public static File[] getFile(File dir)
    {
        System.err.println(timeStamp() + dir.getAbsoluteFile()); //Print folder path to err dialog
        fLog.newPrintln(timeStamp() + dir.getAbsolutePath());
        File[] dirContents = dir.listFiles(); //Initialize File array
        ArrayList<File> files = new ArrayList<File>();
        for(int fn = 0; fn < dirContents.length; fn++)
        {
            String fileName = dirContents[fn].getName(); //Save file name as a String
            if(!dirContents[fn].isDirectory() && fileName.contains(".bmp"))
            {
                files.add(dirContents[fn]); //Add File object to files ArrayList
            }
        }
        return files.toArray(new File[files.size()]);
    }
    /**
     * Gets pairs of image files with potential movement using structural similarity index
     * @param images image File object array
     * @return PotMatches object array
     */
    public static PotMatches[] findMatches(File[] images)
    {
    	pg.pbSetMax(images.length-1);
    	ArrayList<PotMatches> matches = new ArrayList<PotMatches>();
        for(int i=0;i<images.length-1;i++)
        {
            pg.pbUpdate(i+1);
            System.err.println(timeStamp() + "Comparing " + images[i].getName() + " and " + images[i].getName());
        	fLog.newPrintln(timeStamp() + "Comparing " + images[i].getName() + " and " + images[i].getName());
        	SsimCalculator ssim;
            double simIndex = 0;
            try
            {
                ssim = new SsimCalculator(images[i]);
                simIndex = ssim.compareTo(images[i+1]);
            } catch(SsimException | IOException e) {}
            if(simIndex <= threshold)
            {
                matches.add(new PotMatches(images[i],images[i+1],simIndex));
                System.err.println(timeStamp() + "New Potential Match - " + simIndex);
                fLog.newPrintln(timeStamp() + "New Potential Match - " + String.valueOf(simIndex));
            }
        }
        return matches.toArray(new PotMatches[matches.size()]);
    }
    /**
     * Opens a CompareFrame and displays pairs of image files with potential movement for user confirmation
     * @param files PotMatches object array
     * @return Match confirmed percentage
     */
    public static double confirmMatches(PotMatches[] files)
    {
    	System.out.println("File Name a,File Name b,Similarity Index,Date,Start Time,End Time");
        fOut.newPrintln("File Name a,File Name b,Similarity Index,Date,Start Time,End Time");
    	CompareFrame frame = new CompareFrame();
    	int matches = 0;
    	int comparisons = 0;
        for(int i=0;i < files.length;i++)
    	{
    		frame.updateFrame(files[i].f1,files[i].f2,files[i].simIndex,fileDate(files[i].f1),fileDate(files[i].f2),fileTime(files[i].f1),fileTime(files[i].f2),i+1,files.length);
    		while(!frame.getButtonPressed())
            {
                try
                {
                    Thread.sleep(1);
                }
                catch(InterruptedException ex)
                {
                    Thread.currentThread().interrupt();
                }
            }
    		if(frame.getEndEarly())
    		{
    			System.err.println(timeStamp() + "User Confirmation Ended Early");
    			fLog.newPrintln(timeStamp() + "User Confirmation Ended Early");
    			break;
    		}
            if(frame.getIsMatch())
            {
                printMatch(files[i].f1,files[i].f2,files[i].simIndex);
            	matches++;
                System.err.println(timeStamp() + "Potential Match Confirmed by User");
    			fLog.newPrintln(timeStamp() + "Potential Match Confirmed by User");
            }
            else
            {
                System.err.println(timeStamp() + "Potential Match Denied by User");
    			fLog.newPrintln(timeStamp() + "Potential Match Denied by User");
            }
            frame.resetStates();
            comparisons++;
    	}
        frame.killFrame();
    	System.err.println(timeStamp() + "Analysis Finished");
    	fLog.newPrintln(timeStamp() + "Analysis Finished");
    	return (double)matches/comparisons;
    }
    /**
     * Finds image file creation date from input file name
     * @param file .bmp image File object
     * @return date String object
     */
    public static String fileDate(File file)
    {
        String fileName = file.getName();
        return fileName.substring(14,16)+"/"+fileName.substring(17,19)+"/"+fileName.substring(9,13);
    }
    /**
     * Finds image file creation time from input file name
     * @param file .bmp image File object
     * @return time String object
     */
    public static String fileTime(File file)
    {
        String fileName = file.getName();
        return fileName.substring(20,22)+":"+fileName.substring(23,25)+":"+fileName.substring(26,28);
    }
    /**
     * Prints results data to fOut and fLog
     * @param a image File object
     * @param b image File object
     * @param c structural similarity index double
     */
    public static void printMatch(File a, File b, double c)
    {
        System.out.println(a.getName()+","+b.getName()+","+c+","+fileDate(a)+","+fileTime(a)+","+fileTime(b));
        fOut.newPrintln(a.getName()+","+b.getName()+","+c+","+fileDate(a)+","+fileTime(a)+","+fileTime(b));
        System.err.println(a.getName()+","+b.getName()+","+c+","+fileDate(a)+","+fileTime(a)+","+fileTime(b));
        fLog.newPrintln(a.getName()+","+b.getName()+","+c+","+fileDate(a)+","+fileTime(a)+","+fileTime(b));
    }
    /**
     * Set threshold variable
     * @param t threshold double
     */
    public static void setThreshold(double t)
    {
    	threshold = t;
    }
    /**
     * Create timestamp for fLog
     * @return timestamp String object
     */
    public static String timeStamp()
    {
    	Calendar c = Calendar.getInstance();
    	java.text.SimpleDateFormat timeStamp = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    	return timeStamp.format(c.getTime()) + " --> ";
    }
    /**
     * Object containing pairs of files and their structural similarity index
     * @author DeathWing72
     */
    public static class PotMatches
    {
    	/**
    	 * File 1
    	 */
    	protected File f1;
    	/**
    	 * File 2
    	 */
    	protected File f2;
    	/**
    	 * Structural similarity index
    	 */
    	protected double simIndex;
    	/**
    	 * Constructs a new PotMatches object
    	 * @param f1 image File object
    	 * @param f2 image File object
    	 * @param simIndex structural similarity index
    	 */
    	public PotMatches(File f1,File f2,double simIndex)
    	{
    		this.f1 = f1;
    		this.f2 = f2;
    		this.simIndex = simIndex;
    	}
    }
}