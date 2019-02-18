import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
public class TimelapseMotionDetection
{
    private static final double THRESHOLD = .9;
    static FileOutput fOut = new FileOutput();
    public static void main(String[] args)
    {
        FileFrame frame = new FileFrame();
        fOut.setFile(frame.file);
        File[] files = getFile(frame.file);
        findMatchesGUI(files);
        fOut.endPrint();
    }
    public static File[] getFile(File dir)
    {
        System.err.println(dir.getAbsoluteFile()); //Print folder path to err dialog
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
    public static void findMatchesGUI(File[] images)
    {
        int goodMatch = 0;
        int badMatch = 0;
        System.out.println("File Name a,File Name b,Similarity Index,Date,Start Time,End Time,Good/Bad Match");
        fOut.newPrintln("File Name a,File Name b,Similarity Index,Date,Start Time,End Time,Good/Bad Match");
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
                CompareFrame frame = new CompareFrame(images[i],images[i+1],simIndex,fileDate(images[i]),fileTime(images[i]),fileTime(images[i+1]),i+1,images.length-1);
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
                printMatch(images[i],images[i+1],simIndex);
                if(frame.isMatch)
                {
                    // printMatch(images[i],images[i+1],simIndex);
                    System.out.println(",good match");
                    fOut.newPrintln(",good match");
                    goodMatch++;
                }
                else
                {
                    System.out.println(",bad match");
                    fOut.newPrintln(",bad match");
                    badMatch++;
                }
            }
        }
        double matchPercent = (double)((goodMatch/(goodMatch+badMatch))*100);
        System.out.println("Good match percentage: "+matchPercent+"%");
        fOut.newPrintln("Good match percentage: "+matchPercent+"%");
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
}