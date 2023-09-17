package login;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import database.Data;
import userInfo.User;
import account.SavingsAccount;

public class Login {
    //declare fields
    private String email, password;

    //overiding constructor
    public Login() {
        this.email = "";
        this.password = "";
    }

    //overloading constructor
    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }

    //returns boolean if password is correct
    public boolean isPassword(String email, String password) {
        return new Register().hash(password).equals(new User(email).getPassword());
    }

    //updates all users accounts for interest based on the time they last logged in and now
    public void updateInterest(String email) {
        Data data = new Data();
        SavingsAccount savingsAccount = new SavingsAccount();
        String[][] accounts = data.getAccounts(email);
        long days = numDays(new User(email).getDate());
        double total, principal, rate;
        int accountID;

        data.updateDate(email, currentDate());
        
        for (int i = 0; i < accounts.length; i++) {
            accountID = Integer.parseInt(accounts[i][0]);
            principal = Double.parseDouble(accounts[i][2]);
            rate = Double.parseDouble(accounts[i][3]);

            if (rate > 0 && days > 0) {
                total = savingsAccount.calculateInterestTotal(principal, rate, days);
                data.deposit(email, accountID, total);
            }
        }

    }

    //returns current date with yyy-MM-dd format
    public String currentDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return now.format(formatter);
    }

    //calculates difference between two dates
    public long numDays(String date) {
        return ChronoUnit.DAYS.between(LocalDate.parse(date), LocalDate.now());
    }

}
