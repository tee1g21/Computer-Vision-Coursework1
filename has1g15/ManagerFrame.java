package uk.ac.soton.ecs.tee1g21.has1g15;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;

public class ManagerFrame extends JFrame{

    private ProcessImage processImage;
    private JPanel optPanel;
    private JComboBox<String> viewOptions;
    private JComboBox<String> imgOptions;
    private String view;

    public ManagerFrame(int width, final int height)
    {
        this.setSize(width, height);
        this.setTitle("Hybrid Images");
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        contentPane.setBounds(0, 0, width, height);
        this.setContentPane(contentPane);
        optPanel = new JPanel();
        optPanel.setSize(width, 100);

        processImage = new ProcessImage(3, 3);

        //Populate options in combo boxes
        String[] views = new String[]{"Hybrid",
                "Scaled Hybrid Option 1",
                "Scaled Hybrid Option 2",
                "High/Low Pass Filtering Option 1",
                "High/Low Pass Filtering Option 2"};

        String[] images = new String[]{"Bicycle & Motorcycle",
                "Bird & Plane",
                "Cat & Dog",
                "Einstein & Marilyn",
                "Fish & Submarine"};

        viewOptions = new JComboBox<String>(views);
        imgOptions = new JComboBox<String>(images);

        optPanel.add(viewOptions);
        optPanel.add(imgOptions);

        //Initialise options with default
        setImgs("bicycle", "motorcycle");
        view = "Hybrid";

        viewOptions.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                view = (String)viewOptions.getSelectedItem();
            }
        });

        imgOptions.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                String img = (String)imgOptions.getSelectedItem();
                switch (img)
                {
                    //When image combination selected, view and image names are passed to display image method
                    case "Bicycle & Motorcycle":
                        setImgs("bicycle", "motorcycle");
                        dispImgs(view, (img.split(" & "))[0], (img.split(" & "))[1]);
                        break;
                    case "Bird & Plane":
                        setImgs("bird", "plane");
                        dispImgs(view, (img.split(" & "))[0], (img.split(" & "))[1]);
                        break;
                    case "Cat & Dog":
                        setImgs("cat", "dog");
                        dispImgs(view, (img.split(" & "))[0], (img.split(" & "))[1]);
                        break;
                    case "Einstein & Marilyn":
                        setImgs("einstein", "marilyn");
                        dispImgs(view, (img.split(" & "))[0], (img.split(" & "))[1]);
                        break;
                    case "Fish & Submarine":
                        setImgs("fish", "submarine");
                        dispImgs(view, (img.split(" & "))[0], (img.split(" & "))[1]);
                        break;
                    default:
                        break;
                }
            }
        });

        contentPane.add(optPanel);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    public void setImgs(String img1, String img2)
    {
        try
        {
            //Load requested images from hybrid-images folder and set as images in ProcessImage
            processImage.setImageOne(ImageUtilities.readMBF(new File("src/hybrid-images/" + img1 + ".bmp")));
            processImage.setImageTwo(ImageUtilities.readMBF(new File("src/hybrid-images/" + img2 + ".bmp")));
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
    }

    //Depending on the view selected, either hybrids, a scaled hybrid or high and low frequency are created
    //All windows are named using the passed name variables
    public void dispImgs(String view, String name1, String name2)
    {
        this.view = view;
        switch (view)
        {
            case "Hybrid":
                DisplayUtilities.display(processImage.makeHybrids()[0], name1 + " & " + name2 + " Hybrid");
                DisplayUtilities.display(processImage.makeHybrids()[1], name2 + " & " + name1 + " Hybrid");
                break;
            case "Scaled Hybrid Option 1":
                DisplayUtilities.display(processImage.makeScaledHybrid(1),
                        name1 + " & " + name2 + " Scaled Hybrid");
                break;
            case "Scaled Hybrid Option 2":
                DisplayUtilities.display(processImage.makeScaledHybrid(2),
                        name2 + " & " + name1 + " Scaled Hybrid");
                break;
            case "High/Low Pass Filtering Option 1":
                DisplayUtilities.display(processImage.makeHighLowPass()[0], name1 + " Low Pass");
                DisplayUtilities.display(processImage.makeHighLowPass()[3], name2 + " High Pass");
                break;
            case "High/Low Pass Filtering Option 2":
                DisplayUtilities.display(processImage.makeHighLowPass()[1], name2 + " Low Pass");
                DisplayUtilities.display(processImage.makeHighLowPass()[2], name1 + " High Pass");
                break;
            default:
                break;
        }
    }
}