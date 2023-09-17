package account;

//Begining of class SavingsAccount which inherits Account class
public class SavingsAccount extends Account {

    //interest rate field
    final static double interestRate = 0.02;

    //Calculates the total interest using balance, rate, the amount of days for daily interest
    public double calculateInterestTotal(double principal, double rate, long days) {
        return principal * Math.pow((1 + rate), days) - principal;
    }

    //To string method that returns the total balance
    public String toString(){
        return "" + ((this.balance * interestRate) + balance);
    }

}//end of class
