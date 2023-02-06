//*******************************************************************
//      This program prints out a structure that resembles a rocket. 
//
//*******************************************************************

public class DrawRocket {
    public static void main(String[] args) 
    {
        drawRocket(3);
        System.out.println();
        drawRocket(5);
    }
    
    public static void repeat(int times, String p) 
    {
         for (int i = 1; i <= times; i++)
            System.out.print(p);
    } 

    public static void drawRocket(int scale) //calls the methods in order
    {
        cap(scale);
        line(scale);
        top(scale);
        bottom(scale);
        line(scale);
        bottom(scale);
        top(scale);
        line(scale);
        cap(scale);
    }

    public static void cap(int scale) //creates the trapezoid on the top and bottom of the rocket
    {
        for (int row = 1; row < scale*2; row++){
            //print spaces
            repeat(scale*2 - row, " "); 
            //prints foward slashes
            repeat(row, "/");
            //prints characters in between
            System.out.print("**");
            //prints back slashes
           repeat(row, "\\");
            //move onto new line
            System.out.println();
        }
    }

    public static void line(int scale) //creates this type of line: +=*=*=*=*=*=*+ 
    {
        System.out.print("+");
        for (int i = 1; i <= scale * 2; i++) {
            System.out.print("=*");
        }
        System.out.println("+");        
    } 

     public static void top(int scale) //creates the top part of the pattern in the middle of the rocket
    {
        for (int row = 1; row <= scale; row++){
            System.out.print("|");
            for (int i = 0; i < 2; i++) {
                repeat(scale - row, "."); 
                repeat(row, "/\\");
                repeat(scale - row, ".");
            }
            System.out.println("|");
        }        
    } 

    public static void bottom(int scale) //creates the bottom part of the pattern in the middle of the rocket
    {
        for (int row = scale; row > 0; row--){
            System.out.print("|");
            for (int i = 0; i < 2; i++) {
                repeat(scale - row, "."); 
                repeat(row, "\\/");
                repeat(scale - row, ".");
            }
            System.out.println("|");
        }        
    }   
}

