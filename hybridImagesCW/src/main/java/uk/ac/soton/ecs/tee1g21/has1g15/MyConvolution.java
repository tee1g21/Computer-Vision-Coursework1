package uk.ac.soton.ecs.tee1g21.has1g15;

import org.openimaj.image.FImage;
import org.openimaj.image.processor.SinglebandImageProcessor;

public class MyConvolution implements SinglebandImageProcessor<Float, FImage> {

    private float[][] kernel;

    public MyConvolution(float[][] kernel)
    {
        //Construct template
        this.kernel = kernel;
    }

    @Override
    public void processImage(FImage image)
    {
        //Image dimensions
        int rows = image.height;
        int columns = image.width;

        //Template dimensions
        int templateRows = kernel.length;
        int templateColumns = kernel[0].length;

        //Set black temporary image
        FImage temp  = new FImage(columns, rows);
        temp.fill(0f);

        //Halve template rows and columns
        int halfTemplateRows = (int) Math.floor(templateRows/2);
        int halfTemplateColumns = (int) Math.floor(templateColumns/2);

        //convolute template
        /*
        for (int x = halfTemplateRows + 1; x < columns - halfTemplateRows; x++)
        {
            for (int y = halfTemplateColumns + 1; y < rows - halfTemplateColumns; y++)
            {
                float sum = 0;

                for (int i = 1; i < templateRows; i++)
                {
                    for (int j = 1; j < templateColumns; j++)
                    {
                        //convolve image with kernel and store result back in image
                        sum = sum + image.getPixel(x + i - halfTemplateRows - 1,
                                y + j - halfTemplateColumns -1) * kernel[j][i];
                    }
                }
                temp.setPixel(x, y, sum);
            }
        }


         */
        //temp = temp.normalise();

        //Using FImage#internalAssign(FImage) to set the contents of temporary buffer
        //image to the image
        image.internalAssign(temp);

    }
}