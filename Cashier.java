//*******************************************************************
//      This program reads in unit price, quantity, sales tax, and paid 
//      amount. It then determines the amount of change, if any, that 
//      would be received; and prints the numbers of 50 dollars, 20 dollars, 
//      10 dollars, 5 dollars, 1 dollars, quarters, dimes, nickels, and 
//      pennies that should be returned. 
//
//*******************************************************************
import java.util.Scanner;
import java.lang.Math; 

public class Cashier {
    public static void main(String[] args) {
        System.out.println("Welcome to Cashier!");
        System.out.println();

        Scanner console = new Scanner(System.in);
        System.out.print("Enter the unit price: "); 
		double unitPrice = console.nextDouble(); //take user input for unit price
        System.out.print("Enter the quantity: ");
		int quantity = console.nextInt(); //take user input for quantity
        System.out.print("Enter the sales tax rate: ");
		double taxRate = console.nextDouble(); //take user input for sales tax rate

        double amountOwedWithoutTax = (unitPrice * quantity); //calculates amount for money owed without sales tax
        double amountOwed = (unitPrice * quantity) * (1 + taxRate/100); //calculates amount for money owed with sales tax
        System.out.printf("The total owed amount is $%.2f ($%.2f plus %.2f%% tax) \n", amountOwed, amountOwedWithoutTax, taxRate); 

        System.out.print("Enter the paid amount: ");
		double paidAmount = console.nextDouble(); //take user input for amount paid

        String paid = String.format("%.2f", paidAmount);
        String owed = String.format("%.2f", amountOwed);
        double roundedPaid = Double.parseDouble(paid);
        double roundedOwed = Double.parseDouble(owed);

        if (roundedPaid < roundedOwed){ 
            double change = amountOwed - paidAmount; 
            System.out.printf("You still owe $%.2f. \n", change); 
        }
        if (roundedPaid > roundedOwed){
            System.out.println(getChange(amountOwed, paidAmount));
        }
        if (roundedPaid == roundedOwed){
            System.out.println("Thank you for paying the exact amount!");
        }
    }

    public static int getMoney(double typeOfMoney, double change){ //this method finds how much of each type of money is needed in change
        int amountOfMoney =  (int) (change / (typeOfMoney * 100));
        return(amountOfMoney);
    }

    public static String getChange(double owed, double paid){ //this method returns the change seperated into specific money and coins
        double change = ((paid - owed) * 100); 
        int fiftyOwed = getMoney(50, change);
        change = change - (fiftyOwed * 50 * 100);

        int twentyOwed = getMoney(20, change);
        change = change - (twentyOwed * 20 * 100);
      
        int tenOwed = getMoney(10, change);
        change = change - (tenOwed * 10 * 100);

        int fiveOwed = getMoney(5, change);
        change = change - (fiveOwed * 5 * 100);
          
        int oneOwed = getMoney(1, change);
        change = Math.round(change - (oneOwed * 1 * 100));

        int quarterOwed = getMoney(0.25, change);
        change = Math.round(change - (quarterOwed * 0.25 * 100));
                                
        int dimeOwed = getMoney(0.10, change);
        change = Math.round(change - (dimeOwed * 0.10 * 100));
                                    
        int nickelOwed = getMoney(0.05, change);
        change = Math.round(change - (nickelOwed * 0.05 * 100));
                                        
        int pennyOwed = getMoney(0.01, change);;
        change = Math.round(change - (pennyOwed * 0.01 * 100)); 
        
        return String.format("Your change of $%.2f is given as: \n %d 50 dollars \n %d 20 dollars \n %d 10 dollars \n %d 5 dollars \n %d 1 dollars \n %d quarters \n %d dimes \n %d nickels \n %d pennies", paid - owed, fiftyOwed, twentyOwed, tenOwed, fiveOwed, oneOwed, quarterOwed, dimeOwed, nickelOwed, pennyOwed);
    }
}

