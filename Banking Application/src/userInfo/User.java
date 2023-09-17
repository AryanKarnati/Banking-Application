package userInfo;

import java.text.DecimalFormat;
import database.Data;

public class User {
    //declare fields
    private Data data;
    private String email;
    private String[] user;
    private String[][] accounts, transactions;

    //uvi start
    //overloaded constructor
    public User(String email) {
        this.data = new Data();
        this.email = email;
        this.user = data.getUser(this.email);
    }

    //return first name
    public String getFirstName() {
        return user[0];
    }

    //return last name
    public String getLastName() {
        return user[1];
    }

    //return password
    public String getPassword() {
        return user[2];
    }

    //return date
    public String getDate() {
        return user[3];
    }

    //print all accounts nicely
    public void printAccounts() {
        DecimalFormat df = new DecimalFormat("0.00");
        this.accounts = data.getAccounts(this.email);
        for (int i = 0; i < accounts.length; i++)
            System.out.println(String.format("%s(%s)\t\t$%s", accounts[i][1], accounts[i][0], df.format(Double.parseDouble(accounts[i][2]))));
    }
    //uvi end

    //aryan start (needed an array)
    //print all the transactions out in a nice format in the order of most recent to oldest
    public void printTransactions(int accountID) {
        DecimalFormat df = new DecimalFormat("0.00");
        transactions = data.getTransactions(this.email, accountID);
        for (int i = transactions.length-1; i >= 0; i--) {
            if (!transactions[i][1].substring(0, 1).equals("-"))
                System.out.println(String.format("%s\t%s\t $%s", transactions[i][2], transactions[i][0], df.format(Double.parseDouble(transactions[i][1]))));
            else
                System.out.println(String.format("%s\t%s\t-$%s", transactions[i][2], transactions[i][0], df.format(Math.abs(Double.parseDouble(transactions[i][1])))));
        }
    }
    //aryan end

}
