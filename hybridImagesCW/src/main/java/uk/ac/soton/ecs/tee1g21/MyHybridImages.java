package uk.ac.soton.ecs.tee1g21;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;

import java.io.File;
import java.io.IOException;

public class MyHybridImages {

    public static void main(String[] args) throws IOException {

        MBFImage lowImage = ImageUtilities.readMBF(new File("hybrid-images/data/putin.bmp"));
        MBFImage highImage = ImageUtilities.readMBF(new File("hybrid-images/data/trumpHappy.bmp"));

        float lowSigma = 1;
        float highSigma = 8;

        MBFImage hybrid = makeHybrid(lowImage, lowSigma, highImage, highSigma);

        DisplayUtilities.display(hybrid);
        //ImageUtilities.write(hybrid, new File("output-images/putin-trump.bmp"));
    }

    /**
     * Compute a hybrid image combining low-pass and high-pass filtered images
     *
     * @param lowImage  the image to which apply the low pass filter
     * @param lowSigma  the standard deviation of the low-pass filter
     * @param highImage the image to which apply the high pass filter
     * @param highSigma the standard deviation of the low-pass component of computing the high-pass filtered image
     * @return the computed hybrid image
     */
    public static MBFImage makeHybrid(MBFImage lowImage, float lowSigma, MBFImage highImage, float highSigma) {
        // Implement your hybrid images functionality here.
        // Your submitted code must contain this method, but you can add
        // additional static methods or implement the functionality through
        // instance methods on the `MyHybridImages` class of which you can create
        // an instance of here if you so wish.
        // Note that the input images are expected to have the same size, and the output
        // image will also have the same height & width as the inputs.

        //create low pass image using MyConvolution
        //use .process method to run process image on all 3 colour bands of MBFI image
        MyConvolution lowConvolution = new MyConvolution(makeGaussianKernel(lowSigma));
        lowImage = lowImage.process(lowConvolution);

        //create high pass image using MyConvolution
        //subtract convoluted image from the input image to create high pass
        MyConvolution highConvolution = new MyConvolution(makeGaussianKernel(highSigma));
        highImage = highImage.subtract(highImage.process(highConvolution));

        //add images together to create hybrid image
        MBFImage hybridImage = lowImage.add(highImage);

        return hybridImage;
    }

    public static float[][] makeGaussianKernel(float sigma) {
        // Use this function to create a 2D gaussian kernel with standard deviation sigma.
        // The kernel values should check to 1.0, and the size should be floor(8*sigma+1) or
        // floor(8*sigma+1)+1 (whichever is odd) as per the assignment specification.

        int size = (int) (8.0f * sigma + 1.0f); // (this implies the window is +/- 4 sigmas from the centre of the Gaussian)
        if (size % 2 == 0) size++; // size must be odd

        float[][] kernel = new float[size][size];
        int r = size / 2;
        float total = 0;

        //use Guassian function to calculate kernel values
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                //generate cell value using function
                kernel[x + r][y + r] = (float) (Math.exp(-(x * x + y * y) / (2 * sigma * sigma)) / ( 2 * Math.PI * sigma * sigma));

                //add to total for normalisation
                total += kernel[x + r][y + r];
            }
        }

        // Normalise the kernel so that values add to 1
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                kernel[i][j] /= total;
            }
        }

        return kernel;
    }
}


