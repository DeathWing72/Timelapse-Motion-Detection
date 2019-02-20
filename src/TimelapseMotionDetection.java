import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
public class TimelapseMotionDetection
{
    private static final double THRESHOLD = .88;
    static FileOutput fOut = new FileOutput(), fLog = new FileOutput();
    public static void main(String[] args)
    {
        FileFrame frame = new FileFrame();
        File subDir = new File(frame.file.getAbsoluteFile(), "Analysis Results");
        subDir.mkdir();
        fOut.setFiles(frame.file, subDir.getAbsolutePath(), frame.file.getName()+" Analysis Match Results", ".csv");
        fLog.setFiles(frame.file, subDir.getAbsolutePath(), frame.file.getName()+" Analysis Data Log", ".txt");
        File[] files = getFile(frame.file);
        PotMatches[] pm = findMatches(files);
        confirmMatches(pm);
        fOut.endPrint();
        fLog.endPrint();
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
    	ArrayList<PotMatches> matches = new ArrayList<PotMatches>();
        for(int i=0;i<images.length-1;i++)
        {
            SsimCalculator ssim;
            double simIndex = 0;
            try
            {
                ssim = new SsimCalculator(images[i]);
                simIndex = ssim.compareTo(images[i+1]);
            } catch(SsimException | IOException e) {}
            if(simIndex <= THRESHOLD)
            {
                matches.add(new PotMatches(images[i],images[i+1],simIndex));
                System.err.println(timeStamp() + "New Potential Match - " + simIndex);
                fLog.newPrintln(timeStamp() + "New Potential Match - " + String.valueOf(simIndex));
            }
        }
        return matches.toArray(new PotMatches[matches.size()]);
    }
    public static void confirmMatches(PotMatches[] files)
    {
    	System.out.println("File Name a,File Name b,Similarity Index,Date,Start Time,End Time,Match? (No-0/Yes-1)");
        fOut.newPrintln("File Name a,File Name b,Similarity Index,Date,Start Time,End Time,Match? (No-0/Yes-1)");
    	for(int i=0;i < files.length;i++)
    	{
    		CompareFrame frame = new CompareFrame(files[i].f1,files[i].f2,files[i].simIndex,fileDate(files[i].f1),fileTime(files[i].f1),fileTime(files[i].f2),i+1,files.length);
            while(!frame.buttonPressed)
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
            printMatch(files[i].f1,files[i].f2,files[i].simIndex);
            if(frame.isMatch)
            {
                // printMatch(images[i],images[i+1],simIndex);
                System.out.println(",1");
                fOut.newPrintln(",1");
            }
            else
            {
                System.out.println(",0");
                fOut.newPrintln(",0");
            }
    	}
    	System.out.println(",,,,,Match Percentage:,=SUM(G2:G"+(files.length+1)+")/"+files.length);
    	fOut.newPrintln(",,,,,Match Percentage:,=SUM(G2:G"+(files.length+1)+")/"+files.length);
    	System.err.println(timeStamp() + "Analysis Finished");
    	fLog.newPrintln(timeStamp() + "Analysis Finished");
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
        System.out.print(a.getName()+","+b.getName()+","+c+","+fileDate(a)+","+fileTime(a)+","+fileTime(b));
        fOut.newPrint(a.getName()+","+b.getName()+","+c+","+fileDate(a)+","+fileTime(a)+","+fileTime(b));
        //System.out.println("=HYPERLINK("+a.getPath()+"),  ,=HYPERLINK("+b.getPath()+")");
    }
    public static String timeStamp()
    {
    	Calendar c = Calendar.getInstance();
    	java.text.SimpleDateFormat timeStamp = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    	return timeStamp.format(c.getTime()) + " --> ";
    }
    public static class PotMatches
    {
    	public File f1,f2;
    	public double simIndex;
    	public PotMatches(File f1,File f2,double simIndex)
    	{
    		this.f1 = f1;
    		this.f2 = f2;
    		this.simIndex = simIndex;
    	}
    }
}