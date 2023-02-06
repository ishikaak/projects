import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.BasicStroke;
import java.util.Scanner;

public class Animation {
	
	public static final int WIDTH = 800;
	public static final int HEIGHT = 450;
	public static final int FRAME_T = 17; // in ms
	public static final double GRAVITY = 0.33; // in pixels/frame^2

	// Setup DrawingPanel
	public static DrawingPanel panel = new DrawingPanel(WIDTH, HEIGHT);
	public static Graphics2D g = panel.getGraphics();

	public static BufferedImage offscreen = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	public static Graphics2D osg = offscreen.createGraphics();
	
	// main method
	public static void main(String[] args) {
		Scanner console = new Scanner(System.in);

		double x = 0;
		double y = HEIGHT/2;

		// use Scanner object to read in the user's values
		System.out.print("Please type the size, initial velocity, launch angle in degrees, and number of seconds to count down: ");
		double figureSize = console.nextDouble();
		double velocity = console.nextDouble();
		double angle = console.nextDouble();
		double countdown = console.nextDouble();

		// animate the clock
		for (double t = 0; t < countdown; t+= FRAME_T / 1000.0){
			osg.setColor(Color.WHITE);
            osg.fillRect(0, 0, WIDTH, HEIGHT); //clear the image
			drawClock(WIDTH/2, HEIGHT/2, HEIGHT/2, t, countdown);
			g.drawImage(offscreen, 0, 0, null); // copy buffered image to the screen and pause
            panel.sleep(FRAME_T);
		}


		// get x any y components for velocity using the velocity and angle
		// given by the user (hint: use Math.cos and Math.sin)
		double radians = degreesToRadians(angle); 
		double xVel = velocity * Math.cos(radians); 
		double yVel = -velocity * Math.sin(radians);

		// loop for 10 seconds, pausing 17ms at a time
        for (double t = 0; t < 10; t += FRAME_T / 1000.0) {
			// update the y velocity based on gravity. x velocity is constant

			if (y + figureSize > HEIGHT) { // the figure has reached the bottom
				yVel *= -.7; // update the velocity
				if (yVel > -1) {
					yVel = 0; // set velocity to 0 when done bouncing
				}
				y = HEIGHT - figureSize; // set the figure at the base of the panel
			}

			// wraparound when the figure reaches the right edge
			if (x > WIDTH) {
				x = 0;
			}

			// use the current velocity to update the position of the figure
			x += xVel;
			y += yVel;
			yVel += .5 * GRAVITY;
			
            // clear the image
            osg.setColor(Color.WHITE);
            osg.fillRect(0, 0, WIDTH, HEIGHT);

            // draw the figure
            drawFace(x, y, figureSize);

            // copy buffered image to the screen and pause
            g.drawImage(offscreen, 0, 0, null);
            panel.sleep(FRAME_T);
        }

		// close the Scanner object
		console.close();
  }


	// draws the smiley face using 3 ovals and 1 arc
    public static void drawFace(double x, double y, double size) {
        osg.setColor(Color.YELLOW); 
		osg.fillOval((int) (x), (int) (y), (int) (size), (int) (size)); //creates circular face
        osg.setColor(Color.BLACK);
        osg.fillOval((int) (x + 1/5.0 * size), (int) (y + 1/4.0 * size), (int) (1/5.0 * size), (int) (1/4.0 * size)); //creates oval eyes 
        osg.fillOval((int) (x + 3/5.0 * size), (int) (y + 1/4.0 * size), (int) (1/5.0 * size), (int) (1/4.0 * size)); //creates oval eyes
        osg.drawArc((int) (x + 1/5.0 * size), (int) (y + 2/4.0 * size), (int) (3/5.0 * size), (int) (1/4.0 * size), 0, -180); //creates arc mouth
	}
 
	// draws the clock with the hand pointing in the correct direction based on the currentTime
	public static void drawClock(double cx, double cy, double diameter, double currentTime, double totalTime) {
		osg.setColor(Color.BLACK); 
		osg.drawOval(0 + (WIDTH - HEIGHT/2)/2, (int) (cy) * 1/2, (int) (diameter), (int) (diameter)); //creates the circle for the clock
		double theta = (currentTime/totalTime) * 2 * Math.PI; 
		osg.drawLine(( int)(cx), (int) (cy), (int) ((cx) + (diameter/2) * Math.cos(theta - Math.PI/2)), (int) ((cy) + ((diameter)/2) * Math.sin(theta - Math.PI/2))); //creates pointer
	}

	// converts degrees to radians
	// assume degrees only range from 0 to 360 
	// and radians range from 0 to 2pi 
	public static double degreesToRadians(double degrees) {
        return((degrees * (2 * Math.PI))/360);
	}

}
