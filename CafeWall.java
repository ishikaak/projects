
//*******************************************************************
//    This program produces a graphic of the Cafe Wall illusion.
//
//*******************************************************************
import java.awt.*;

public class CafeWall {
    final static int MORTAR = 2;
    final static int WIDTH = 650;
    final static int HEIGHT = 400;

    // set up panel and graphics objects
    static DrawingPanel panel = new DrawingPanel(WIDTH, HEIGHT);
    static Graphics2D g = panel.getGraphics();

    public static void main(String[] args) {

        // Change from Color.GRAY to change background color.
        panel.setBackground(Color.GRAY);

        drawRow(0, 2, 4, 20);
        drawRow(50, 70, 5, 30);

        drawGrid(10, 339, 4, 25, 0);

        drawGrid(250, 335, 3, 25, 10);

        drawGrid(425, 378, 5, 20, 10);

        drawGrid(400, 131, 2, 35, 35);
    }

    // Draw a row of squares, the total number of squares is pairs * 2
    // (x, y) is the top-left corner of the first box
    public static void drawRow(int x, int y, int pairs, int size) { 
        for (int i = 0; i < pairs; i++)
        {
            g.setColor(Color.BLACK); 
            g.fillRect((size * i) * 2 + x, y, size, size); //create black squares
            g.setColor(Color.BLUE);
            g.drawLine((size * i) * 2 + x, y, (size * i) * 2 + x + size, y + size); //creates diagonal line
            g.drawLine((size * i) * 2 + x, y + size, (size * i) * 2 + x + size, y); //creates second diagonal line to form the X
            g.setColor(Color.WHITE); 
            g.fillRect((size * i) * 2 + x + size, y, size, size); //creates white squares
        }
        
    }

    // Draw a grid of 2 * pairs rows
    public static void drawGrid(int x, int y, int pairs, int size, int offset) { 
        for (int i = 0; i < pairs; i++)
        {
            drawRow(x, y - 2 * i * (size + MORTAR), pairs, size); 
            drawRow(x + offset, y - (2* i + 1) * (size + MORTAR), pairs, size);
        }
    }

}
