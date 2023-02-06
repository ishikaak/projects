//*******************************************************************
//      This program simulates the motion of N particles, or planets, in a plane, or the universe.
//
//*******************************************************************
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.management.ValueExp;

import java.io.IOException;

public class NBody {

    // set up panel
    static final int SIZE = 500;
    static DrawingPanel panel = new DrawingPanel(SIZE, SIZE); 
    static Graphics2D g = panel.getGraphics();

    // enable double buffering
    static BufferedImage offscreen = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB);
    static Graphics2D osg = offscreen.createGraphics();

    // slider position
    public static final int slideStart = 100;
    public static final int slideEnd = 400;

    public static int slideX = 250;
    public static final int slideY = 450;
    public static final int circleRad = 10;

    // animation pause (in miliseconds)
    public static final int DELAY = 100;

    // music (2001 theme)
    public static final String MUSIC = "2001theme.wav";

    // background image
    public static final String BACKGROUND = "starfield.jpg";
    public static BufferedImage bgImage;

    // gravitational constant (N m^2 / kg^2)
    public static final double G = 6.67e-11;

    // parameters from command line
    public static double simDuration; // simulate from time 0 to simDuration (s)
    public static double baseTimeStep; // time quantum given by user input
    public static double timeStep; // time quantum used in simulation. can be updated by speed slider

    // parameters from .txt file
    public static int numBodies; // number of bodies (N)
    public static double universeRadius; // radius of universe (R)

    public static double[] rx; // x position (m)
    public static double[] ry; // y position (m)
    public static double[] vx; // x velocity (m/s)
    public static double[] vy; // y velocity (m/s)
    public static double[] mass; // mass (kg)
    public static String[] imageNames; // image file names
    public static BufferedImage[] images; // image objects

    public static void main(String[] args) {

        // check for number of arguments, give usage string
        if (args.length < 3) {
            printUsage();
            System.exit(1);
        }

        simDuration = Double.parseDouble(args[0]);

        baseTimeStep = Double.parseDouble(args[1]);
        timeStep = baseTimeStep;

        String universeFileName = args[2]; 

        // load BACKGROUND image
        loadBG();

        // load planets from file specified in the command line
        loadPlanets(universeFileName);

        // play audio file
        StdAudio.play(MUSIC);

        // Set up mouse listener for slider
        panel.onDrag((x, y) -> dragUpdate(x, y));

        // Run simulation
        runSimulation();

        // print final state of universe to standard output
        System.out.printf("%d\n", numBodies);
        System.out.printf("%.2e\n", universeRadius);
        for (int i = 0; i < numBodies; i++) {
            System.out.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n", //prints the values in the arrays
                    rx[i], ry[i], vx[i], vy[i], mass[i], imageNames[i]);
        }
    }

    public static void printUsage() {
        System.out.println("Usage:");
        System.out.println("java NBody <duration> <time step> <universe file>");
    }

    // load BACKGROUND image into the variable bgImage
    public static void loadBG() {
        try {
        bgImage = ImageIO.read(new File(BACKGROUND)); 
        } catch (IOException e){
            System.out.println("Error: Unable to find image.");
        }
    }

    // Read the planet file, create the parallel arrays, and load
    // their values from the file.
    public static void loadPlanets(String planetFileName) {
        
        try {
            File file = new File(planetFileName);  //uploads files
            Scanner scan = new Scanner(file); //scans files

            numBodies = scan.nextInt(); //scans the first line of each file to get the numBodies
            universeRadius = scan.nextDouble(); //scans the second line of each file to get the numBodies

            //initializing the arrays
            rx = new double[numBodies]; 
            ry = new double[numBodies];
            vx = new double[numBodies];
            vy = new double[numBodies];
            mass = new double[numBodies];
            imageNames = new String[numBodies];
            images = new BufferedImage[numBodies];

            for (int i = 0; i < numBodies; i++) { //taking values from the file and putting them in the designated array
                rx[i] = scan.nextDouble();
                ry[i] = scan.nextDouble();
                vx[i] = scan.nextDouble();
                vy[i] = scan.nextDouble();
                mass[i] = scan.nextDouble();
                imageNames[i] = scan.next();

            }

            for (int i = 0; i < numBodies; i++) {
                try { //checks if a image is in the folder
                    //if the image is in the folder, then the code uploads the image into the images array 
                    images[i] = ImageIO.read(new File(imageNames[i])); 
                } catch (IOException e) { //returns an error if image is not found in the folder
                    System.out.println("Error: Unable to find image.");
                } // end of catch		
            
            }
            scan.close();	
        } catch (FileNotFoundException e) { //returns an error if the file cannot be found
            System.out.println("Error: Unable to read file.");
        }	
    }

    public static void runSimulation() {
        // run numerical simulation from 0 to simDuration
        // speed may vary if the slider updates timeStep
        for (double t = 0.0; t < simDuration; t += timeStep) { 

            // the x- and y-components of force
            double[] fx = new double[numBodies];
            double[] fy = new double[numBodies];

            // calculate forces on each object
            // loop to set up fx and fy
            for (int i = 0; i < numBodies; i++) {
                fx[i] = 0;
                fy[i] = 0;
                for (int j = 0; j < numBodies; j++){
                    double deltaX = rx[j] - rx[i]; //change in x
                    double deltaY = ry[j] - ry[i]; //change in y
                    double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)); 
                    double netForce = (G * mass[i] * mass[j])/Math.pow(distance, 2);
                    if ( i != j) {
                        fx[i] = fx[i] + netForce * (deltaX/distance); //the sum of the x forces
                        fy[i] = fy[i] + netForce * (deltaY/distance); //the sum of the y forces
                    }
                }
            }

            double[] ax = new double[numBodies];
            double[] ay = new double[numBodies];

            for (int i = 0; i < numBodies; i++) {

                ax[i] = fx[i]/mass[i]; //calculating acceleration for the x component 
                ay[i] = fy[i]/mass[i]; //calculating acceleration for the y component 
                    
                vx[i] = vx[i] + timeStep * ax[i]; //calculating velocity for the x component
                vy[i] = vy[i] + timeStep * ay[i]; //calculating acceleration for the y component

                rx[i] = rx[i] + timeStep * vx[i]; //calculating position for the x component
                ry[i] = ry[i] + timeStep * vy[i]; //calculating position for the y component
            }

            // draw background
            osg.drawImage(bgImage, 0, 0, null);
            
            // draw each planet
            for (int i = 0; i < numBodies; i++) {
                osg.drawImage(images[i], scaleToPanel(rx[i]), scaleToPanel(ry[i]), null); 
            }

            // draw slider (when you are ready)
            drawSlider(); 

            // copy from offscreen buffer to panel
            g.drawImage(offscreen, 0, 0, null);

            // pause
            panel.sleep(DELAY);
        }
    }

    // general scaling method
    public static double scale(double oldValue, double oldMin, double oldMax, double newMin, double newMax) {
        double newValue = (oldValue - oldMin)/(oldMax - oldMin) * (newMax - newMin) + newMin;
        return newValue;
    }

    // helper function since we have multiple places where you will scale the 
    // universe to panel size
    public static int scaleToPanel(double oldValue) {
        return (int) (scale(oldValue, -universeRadius, universeRadius, 0, SIZE));
    }

    // handle mouse events
    public static void dragUpdate(int x, int y) {
        //checks if the cursor is within the circle
        if (x >= slideX - circleRad && x <= slideX + circleRad && y >= slideY - circleRad && y <= slideY + circleRad) {
            slideX = x; 
            if (x < slideStart){ //this if statement prevents the slider from going beyond first slider dash line
                slideX = slideStart;
            }
            if (x > slideEnd){ //this if statement prevents the slider from going beyond the last slider dash line
                slideX = slideEnd;
            }
            if (x >= slideStart && x <= slideEnd){ 
                timeStep = baseTimeStep * scale(slideX, slideStart, slideEnd, 0, 2); //updating and changing the timeStep
            }
        }
    }

    // draw the slider for this frame
    public static void drawSlider() {

        osg.setColor(Color.GRAY);

        // dashed slider line
        osg.drawLine(slideStart, slideY, slideEnd, slideY);
        for (int dashX = slideStart; dashX <= slideEnd; dashX += (slideEnd - slideStart) / 12) {
            osg.drawLine(dashX, slideY - 5, dashX, slideY + 5);
        }

        for (int dashX = slideStart; dashX <= slideEnd; dashX += (slideEnd - slideStart) / 4) {
            osg.drawLine(dashX, slideY - 10, dashX, slideY + 10);
        }

        // outlined slider "button"
        osg.fillOval(slideX - circleRad, slideY - circleRad, 2 * circleRad, 2 * circleRad);
        osg.setColor(Color.WHITE);
        osg.drawOval(slideX - circleRad, slideY - circleRad, 2 * circleRad, 2 * circleRad);
    }

}