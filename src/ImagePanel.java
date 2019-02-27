/*
 * Copyright (c) 2019, Joseph Tyler Jones. All rights reserved.
 */
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
/**
 * JPanel with image file painted on top
 * @author Tyler Jones
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ImagePanel extends JPanel
{
	/**
	 * Image to be painted
	 */
    private BufferedImage image;
    /**
     * ImagePanel constructor
     * @param f File object to be converted to BufferedImage object
     */
    public ImagePanel(File f) {
        try {                
           image = ImageIO.read(f);
        } catch (IOException ex) {}
    }
    /**
     * Paint BufferedImage object on JPanel
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters
    }
}