package uk.ac.soton.ecs.tee1g21;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;

import java.io.File;
import java.io.IOException;

public class MyHybridImages {

    public static void main(String[] args) throws IOException {

        MBFImage lowImage = ImageUtilities.readMBF(new File("hybrid-images/data/cat.bmp"));
        MBFImage highImage = ImageUtilities.readMBF(new File("hybrid-images/data/dog.bmp"));


        float lowSigma = 10.0f;
        float highSigma = 3;

        DisplayUtilities.display(makeHybrid(lowImage, lowSigma, highImage, highSigma));


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

        MyConvolution lowConvolution = new MyConvolution(makeGaussianKernel(lowSigma));
        lowImage.process(lowConvolution);


        MBFImage temp = highImage;
        MyConvolution highConvolution = new MyConvolution(makeGaussianKernel(highSigma));
        highImage.process(highConvolution);

        temp.subtract(highImage);

        MBFImage hybridImage = lowImage.add(temp);
        hybridImage.normalise();

        return hybridImage;

    }

    public static float[][] makeGaussianKernel(float sigma) {
        // Use this function to create a 2D gaussian kernel with standard deviation sigma.
        // The kernel values should sum to 1.0, and the size should be floor(8*sigma+1) or
        // floor(8*sigma+1)+1 (whichever is odd) as per the assignment specification.

        int size = (int) (8.0f * sigma + 1.0f); // (this implies the window is +/- 4 sigmas from the centre of the Gaussian)
        if (size % 2 == 0) size++; // size must be odd

        //change
        float[][] kernel = new float[size][size];
        int half = size / 2;
        float sigmaSqr = 2 * sigma * sigma;
        float total = 0;

        for (int x = -half; x <= half; x++) {
            for (int y = -half; y <= half; y++) {
                kernel[x + half][y + half] = (float) (Math.exp(-(x * x + y * y) / sigmaSqr) / (Math.PI * sigmaSqr));
                total += kernel[x + half][y + half];
            }
        }

        // Normalize the kernel so the values sum to 1.0
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                kernel[i][j] /= total;
            }
        }

        return kernel;

    }
}


