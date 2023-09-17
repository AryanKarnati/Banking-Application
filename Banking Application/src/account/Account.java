package account;

//importts data class for money transfers between accounts
import database.Data;

//Begining of Account Class 
public class Account {

  //balance field
  public double balance;

  //mutator method which sets the balance
  public void setBalance(double b){
    this.balance = b;
  }

  //accessor method that gets the balance
  public double getBalance(){
    return this.balance;
  }

  //constructor that set the balance of an account to 0
  public Account(){
    this.balance = 0;
  }

  //overloading constructor that takes in a double for balance which must be greator than 0
  public Account(double b){
    if(b > 0){
      balance = b;
    }else{
      throw new IllegalArgumentException("You cannot have a negative balance");
    }

  }

    //instance method deposit which updates the balance
    public void deposit(double d){
    balance += d;
  }
  
    //instance method withdraw which updates the balance
    public void withdraw(double d){
    balance -= d;
  }

  //instance method that transfers money in between accounts one user has
  public void transfer(String email, int accountID1, int accountID2, double amount) {
    Data data = new Data();
    data.withdraw(email, accountID1, amount);
    data.deposit(email, accountID2, amount);
  }
    
}//end of class