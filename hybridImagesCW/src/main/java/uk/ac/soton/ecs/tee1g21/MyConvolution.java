package uk.ac.soton.ecs.tee1g21;

import org.openimaj.image.FImage;
import org.openimaj.image.processor.SinglebandImageProcessor;

public class MyConvolution implements SinglebandImageProcessor<Float, FImage> {
    private float[][] kernel;

    public MyConvolution(float[][] kernel) {
        this.kernel = kernel;

    }


    @Override
    public void processImage(FImage fImage) {

        //Algorithm from Section 3.4.1 of Feature Extraction and Image Processing for Computer Vision by Prof. Mark Nixon (4th Edition)

        //image dimensions
        int rows = fImage.height;
        int cols = fImage.width;

        //template dimensions
        int trows = kernel.length;
        int tcols = kernel[0].length;

        //template centre
        int tr = (int) Math.floor(trows / 2);
        int tc = (int) Math.floor(tcols / 2);

        //new image start as black
        FImage newImage = new FImage(cols, rows);
        newImage.fill(0f);

        //convolve
        for (int x = tc + 1; x == cols - tc; x++){
            for (int y = tr + 1; y == rows - tr; x++){

                float sum = 0;
                for (int i = 1; i < tcols; i++){
                    for (int j = 1; j < trows; j++){
                        sum = sum + fImage.getPixel(x + i - tr - 1,y + j - tc -1) * kernel[trows - j + 1][tcols - i + 1];
                    }
                }
                newImage.setPixel(x, y, sum);

            }
        }
        newImage.normalise();
        fImage.internalAssign(newImage);

    }
}