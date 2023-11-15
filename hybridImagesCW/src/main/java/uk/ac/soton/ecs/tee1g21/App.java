package uk.ac.soton.ecs.tee1g21;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.processing.convolution.FGaussianConvolve;
import org.openimaj.image.processing.edges.CannyEdgeDetector;
import org.openimaj.image.processing.resize.ResizeProcessor;
import org.openimaj.image.typography.hershey.HersheyFont;
import org.openimaj.math.geometry.shape.Ellipse;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

/**
 * OpenIMAJ Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws IOException {

        // Load the images
        MBFImage image2 = ImageUtilities.readMBF(new File("hybrid-images/data/putin.bmp"));
        MBFImage image1 = ImageUtilities.readMBF(new File("hybrid-images/data/trumpHappy.bmp"));

          ArrayList<MBFImage> results = new ArrayList<>();
        ResizeProcessor resize = new ResizeProcessor(200, 200); // define the resize processor

        // Add original images to the results
        MBFImage original1 = image1.clone();
        original1.drawText("Original 1", 10, 50, HersheyFont.TIMES_BOLD, 20, RGBColour.WHITE);
        results.add(original1);

        MBFImage original2 = image2.clone();
        original2.drawText("Original 2", 10, 50, HersheyFont.TIMES_BOLD, 20, RGBColour.WHITE);
        results.add(original2);

        // Different sigma values you want to test
        float[] lowSigmas = {0.5f,1.0f,2.0f, 4.0f, 6.0f,8.0f,10.0f,12.0f}; // Adjust these as needed
        float[] highSigmas = {0.6f,8.0f,10.0f, 12.0f, 14.0f,16.0f,18.0f,20.0f}; // Adjust these as needed



        for (int i = 0; i < lowSigmas.length; i++) {
            float lowSigma = lowSigmas[i];
            float highSigma = highSigmas[i];
            MBFImage hybrid = MyHybridImages.makeHybrid(image1, lowSigma, image2, highSigma);
            hybrid.drawText("Low Sigma: " + lowSigma + ", High Sigma: " + highSigma, 10, 50, HersheyFont.TIMES_BOLD, 20, RGBColour.WHITE);

            // Resize the hybrid image
            hybrid = hybrid.process(resize);
            results.add(hybrid);
        }

        // Resize the original images and add to results
        original1 = original1.process(resize);
        original2 = original2.process(resize);
        results.add(0, original1);
        results.add(1, original2);

        // Display all the hybrid images next to each other
        DisplayUtilities.display("Hybrid Images", results);
        //DisplayUtilities.display(MyHybridImages.makeHybrid(image1,12.0f,image2,20.0f));


    }
}


/*
        float check = 0;
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel[0].length; j++) {
                //System.out.print(kernel[i][j] + "\t");
                check += kernel[i][j];
            }
            //System.out.println();
        }
        System.out.println("\nSum of kernel values: " + check);


         */