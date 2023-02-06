//==========================================================
// This program calculates an estimation of pi, by generating 
// random numbers, and determining which ones fall inside a 
// circle of radius 0.5, which is contained inside a 1 x 1 square.
// The program asks the user for the number of points to drop, and
// then estimates pi by: 
//
//==========================================================

import java.util.Scanner;
import java.awt.Graphics2D;

public class MonteCarloPi {

    // Enforce a minimal of # of points to drop
    static final int MIN_N = 5;

    // A common practice is to define messages as class constants
    static final String PROMPT = "Please input the number of points to drop: ";

    // Specific configuration
    static final double RADIUS = 0.5;

    static final double CIRCLE_CENTER_X = 0.5;
    static final double CIRCLE_CENTER_Y = 0.5;

	// width and height of the screen
	static final int WIDTH = 800;
	static final int HEIGHT = 300;

	// set up panel. no need for double buffering in this case
	public static DrawingPanel panel = new DrawingPanel(WIDTH, HEIGHT);
	public static Graphics2D g = panel.getGraphics();
    public static void main(String[] args) { // loop and output to terminal and drawing panel 
        int totalPoints = getN();
        int pointsInCircle = 0;
        double previousPi = 0;
        int previousIteration = 0;
        double currentPi = 0;
        int currentIteration = 0;
        for (int i = 1; i <= totalPoints; i++){
            double x = Math.random(); //gets random x value
            double y = Math.random(); //gets random y value
            if (isInCircle(x, y)){ //checks if the randomly generated point is in the circle
                pointsInCircle++; //counts the number of points in the circle
            }
            if(i%5 == 0 && i < 50 || i%50 == 0 && i < 1000 || i%1000 == 0){ //finds pi for certain iterations
                currentPi = estimatePi(pointsInCircle, i);
                System.out.println(i + "\t" + currentPi);
                currentIteration = i; 
                g.drawLine(previousIteration, (int) (previousPi), currentIteration, (int) ( HEIGHT -currentPi/5 * HEIGHT)); //draws the graph plotting the pi estimations
                previousIteration = currentIteration;
                previousPi = HEIGHT - (currentPi/5 * HEIGHT);  
            }
        }       
    } // end of method main

    // Get N from user
    public static int getN() {
        Scanner console = new Scanner(System.in);
        System.out.print(PROMPT);
	    int numberOfN = console.nextInt(); //gets user input
        if (numberOfN >= MIN_N){
            return numberOfN;
        }
        else{ // if the inputted number of points is less than 5
            System.out.print("Error: the inputted number of points to drop is less than 5."); //error message
            System.exit(1); //error
            return 0;
        }
    }

    // Determine if new point (x, y) is in circle
    public static boolean isInCircle(double x, double y) {
        double circleEquation = (Math.pow((x - CIRCLE_CENTER_X), 2) + Math.pow((y - CIRCLE_CENTER_Y), 2));
        if (circleEquation <= Math.pow(RADIUS, 2)){ //uses circle equation to check if the point is in the circle
            return true;
        }
        else{
            return false; 
        }
    }

    // Compute estimate of Pi given pointsInCircle and pointsDropped
    public static double estimatePi(int pointsInCircle, int pointsDropped) {
        double pi = (4.0 * pointsInCircle)/pointsDropped;  //calculates pi
        return pi;
    }
} // end of class
