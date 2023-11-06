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

        //padding for input image ALSO equal to kernel centre
        int rowPadding = trows / 2;
        int colPadding = tcols / 2;

        FImage padImage = new FImage(cols + 2 * colPadding, rows + 2 * rowPadding);
        padImage.fill(0f);

        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                padImage.setPixel(x + colPadding, y + rowPadding, fImage.getPixel(x,y));
            }
        }

        //new image start as black
        FImage convImage  = new FImage(cols, rows);
        convImage.fill(0f);

        for (int x = colPadding; x < cols + colPadding; x++) {
            for (int y = rowPadding; y < rows + rowPadding; y++) {
                float sum = 0;
                for (int i = 0; i < trows; i++) {
                    for (int j = 0; j < tcols; j++) {
                        sum = sum + padImage.getPixel(x + j - colPadding, y + i - rowPadding) * kernel[i][j];

                    }
                }
                convImage.setPixel(x - colPadding,y - rowPadding, sum);
            }
        }

        fImage.internalAssign(convImage);

    }
}