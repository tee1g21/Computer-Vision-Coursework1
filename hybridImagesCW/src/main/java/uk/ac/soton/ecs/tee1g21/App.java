package uk.ac.soton.ecs.tee1g21;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.processing.convolution.FGaussianConvolve;
import org.openimaj.image.processing.edges.CannyEdgeDetector;
import org.openimaj.image.typography.hershey.HersheyFont;
import org.openimaj.math.geometry.shape.Ellipse;

import java.io.IOException;
import java.net.URL;

/**
 * OpenIMAJ Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws IOException {

        MBFImage image = ImageUtilities.readMBF(new URL("http://static.openimaj.org/media/tutorial/sinaface.jpg"));

        System.out.println(image.colourSpace);

        DisplayUtilities.display(image);
        DisplayUtilities.display(image.getBand(0), "Red Channel");

        MBFImage clone = image.clone();

        clone.getBand(1).fill(0f);
        clone.getBand(2).fill(0f);

        DisplayUtilities.display(clone);

        image.processInplace(new CannyEdgeDetector());

        image.drawShapeFilled(new Ellipse(700f, 450f, 20f, 10f, 0f), RGBColour.WHITE);
        image.drawShapeFilled(new Ellipse(650f, 425f, 25f, 12f, 0f), RGBColour.WHITE);
        image.drawShapeFilled(new Ellipse(600f, 380f, 30f, 15f, 0f), RGBColour.WHITE);
        image.drawShapeFilled(new Ellipse(500f, 300f, 100f, 70f, 0f), RGBColour.WHITE);
        image.drawText("OpenIMAJ is", 425, 300, HersheyFont.ASTROLOGY, 20, RGBColour.BLACK);
        image.drawText("Awesome", 425, 330, HersheyFont.ASTROLOGY, 20, RGBColour.BLACK);
        DisplayUtilities.display(image);












        /* original hello word

        //Create an image
        MBFImage image = new MBFImage(320,70, ColourSpace.RGB);

        //Fill the image with white
        image.fill(RGBColour.WHITE);
        		        
        //Render some test into the image
        image.drawText("Hello World", 10, 60, HersheyFont.CURSIVE, 50, RGBColour.BLACK);

        //Apply a Gaussian blur
        image.processInplace(new FGaussianConvolve(2f));
        
        //Display the image
        DisplayUtilities.display(image);

         */


    }
}
