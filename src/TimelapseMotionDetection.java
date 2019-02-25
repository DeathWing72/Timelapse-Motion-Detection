import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JOptionPane;
public class TimelapseMotionDetection
{
    private static double threshold = .91;
    private static FileOutput fOut = new FileOutput(), fLog = new FileOutput();
    private static ProgressFrame pg;
    public static void main(String[] args)
    {
    	fileStart();
    	fileOpenWarning();
        FileFrame frame = new FileFrame();
        pg = new ProgressFrame(0,1);
        File subDir = new File(frame.file.getAbsoluteFile(), "Analysis Results");
        subDir.mkdir();
        fOut.setFile(frame.file, subDir.getAbsolutePath(), frame.file.getName()+" Folder Image Analysis Match Results", ".csv");
        fLog.setFile(frame.file, subDir.getAbsolutePath(), frame.file.getName()+" Folder Image Analysis Data Log", ".txt");
        File[] files = getFile(frame.file);
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
    public static void thresholdDialog()
    {
    	String newThresh = JOptionPane.showInputDialog("Please input a new match\nthreshold between 0.0 and 1.0");
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
    public static String fileDate(File file)
    {
        String fileName = file.getName();
        return fileName.substring(14,16)+"/"+fileName.substring(17,19)+"/"+fileName.substring(9,13);
    }
    public static String fileTime(File file)
    {
        String fileName = file.getName();
        return fileName.substring(20,22)+":"+fileName.substring(23,25)+":"+fileName.substring(26,28);
    }
    public static void printMatch(File a, File b, double c)
    {
        System.out.println(a.getName()+","+b.getName()+","+c+","+fileDate(a)+","+fileTime(a)+","+fileTime(b));
        fOut.newPrintln(a.getName()+","+b.getName()+","+c+","+fileDate(a)+","+fileTime(a)+","+fileTime(b));
        System.err.println(a.getName()+","+b.getName()+","+c+","+fileDate(a)+","+fileTime(a)+","+fileTime(b));
        fLog.newPrintln(a.getName()+","+b.getName()+","+c+","+fileDate(a)+","+fileTime(a)+","+fileTime(b));
    }
    public static void setThreshold(double t)
    {
    	threshold = t;
    }
    public static String timeStamp()
    {
    	Calendar c = Calendar.getInstance();
    	java.text.SimpleDateFormat timeStamp = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    	return timeStamp.format(c.getTime()) + " --> ";
    }
    public static class PotMatches
    {
    	protected File f1,f2;
    	protected double simIndex;
    	public PotMatches(File f1,File f2,double simIndex)
    	{
    		this.f1 = f1;
    		this.f2 = f2;
    		this.simIndex = simIndex;
    	}
    }
}