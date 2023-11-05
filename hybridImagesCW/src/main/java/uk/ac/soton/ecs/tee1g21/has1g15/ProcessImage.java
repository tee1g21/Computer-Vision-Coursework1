package uk.ac.soton.ecs.tee1g21.has1g15;

import org.openimaj.image.FImage;
import org.openimaj.image.MBFImage;
import org.openimaj.image.processing.convolution.Gaussian2D;
import org.openimaj.image.processing.resize.ResizeProcessor;

public class ProcessImage {

    private MBFImage mbf1, mbf2, hybrid1, hybrid2, lowPass1, lowPass2,
            highPass1, highPass2, addHighPass1, addHighPass2;
    private int size;
    private FImage fi1, fi2;
    private MyConvolution mc1, mc2;

    public ProcessImage(int sigma1, int sigma2)
    {
        //This implies the window is +/- 4 sigmas from the centre of the Gaussian
        size = (int) (8.0f * sigma1 + 1.0f);
        //Size must be odd
        if (size % 2 == 0) size++;

        //Create image of 2D Gaussian using size and standard deviation of Gaussian filter
        fi1 = Gaussian2D.createKernelImage(size, sigma1);
        fi2 = Gaussian2D.createKernelImage(size, sigma2);

        //Get 2D float array of images and pass into MyConvolution
        mc1 = new MyConvolution(fi1.pixels);
        mc2 = new MyConvolution(fi2.pixels);
    }

    public void setImageOne(MBFImage img)
    {
        this.mbf1 = img;
    }

    public void setImageTwo(MBFImage img)
    {
        this.mbf2 = img;
    }

    public MBFImage[] makeHighLowPass()
    {
        MBFImage[] highLow = new MBFImage[4];

        //Original images processed with MyConvolution to create low frequency images
        lowPass1 = mbf1.process(mc1);
        lowPass2 = mbf2.process(mc2);

        //High pass filtered images created by subtracting low pass versions of images from originals
        highPass1 = mbf1.subtract(lowPass1);
        highPass2 = mbf2.subtract(lowPass2);

        //Adding 0.5 values to each band in high pass images in order to visualise them
        addHighPass1 = new MBFImage(highPass1.getBand(0).add(0.5f),
                highPass1.getBand(1).add(0.5f),
                highPass1.getBand(2).add(0.5f));

        addHighPass2 = new MBFImage(highPass2.getBand(0).add(0.5f),
                highPass2.getBand(1).add(0.5f),
                highPass2.getBand(2).add(0.5f));

        highLow[0] = lowPass1;
        highLow[1] = lowPass2;
        highLow[2] = addHighPass1;
        highLow[3] = addHighPass2;

        return highLow;
    }

    public MBFImage[] makeHybrids()
    {
        makeHighLowPass();

        MBFImage[] hybrids = new MBFImage[2];

        //Hybrids formed by adding low and high pass images together
        hybrid1 = highPass1.add(lowPass2);
        hybrid2 = highPass2.add(lowPass1);

        hybrids[0] = hybrid1;
        hybrids[1] = hybrid2;

        return hybrids;
    }

    //Scaled hybrids made by utilising ResizeProcessor, image is halved after being drawn to an image then position
    //of next draw point is reset depending on location of previous image
    public MBFImage makeScaledHybrid(int hybOption)
    {
        makeHybrids();
        MBFImage[] scaledMbfs = new MBFImage[5];
        MBFImage temporaryImg;
        MBFImage workingImg;

        if (hybOption == 1)
        {
            temporaryImg = hybrid1;
            workingImg = hybrid1;
        }
        else
        {
            temporaryImg = hybrid2;
            workingImg = hybrid2;
        }

        scaledMbfs[0] = temporaryImg;

        for(int i=1; i<5; i++)
        {
            scaledMbfs[i] = ResizeProcessor.halfSize(temporaryImg);
            temporaryImg = ResizeProcessor.halfSize(temporaryImg);
        }

        int width = 0;

        for(int i = 0; i < 5; i++)
        {
            width = width + scaledMbfs[i].getWidth();
        }

        MBFImage scaledImg = new MBFImage(width, workingImg.getHeight());

        int x = 0;
        int y = 0;

        for(int i = 0; i < 5; i++)
        {
            scaledImg.drawImage(scaledMbfs[i], x, y);
            x = x + ((scaledMbfs[i].getWidth()));
            y = y + ((scaledMbfs[i].getHeight())/2);

        }
        return scaledImg;
    }
}