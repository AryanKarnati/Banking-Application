package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
import java.text.DecimalFormat;

import login.Login;

public class Data {
    //declare fields
    private Connection connection;
    private Statement statement;
    private ResultSet result;
    
    //overiding constructor: establish Java Database Connection (JDBC)
    public Data() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:/E:\\Banking Application\\data.db");
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Error connecting to SQLite database");
            e.printStackTrace();
        }
    }

    //reset the database
    public void reset() {
        try {
            statement.executeUpdate("drop table if exists users");
            statement.executeUpdate("drop table if exists accounts");
            statement.executeUpdate("drop table if exists transactions");
            statement.executeUpdate("create table users (email text primary key, firstName text, lastName text, password text, date text);");
            statement.executeUpdate("create table accounts (email text, accountID integer, accountType text, balance decimal, interest decimal);");
            statement.executeUpdate("create table transactions (email text, accountID integer, description text, amount decimal, date text);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    // USERS TABLE



    //return true if user exists in database
    public boolean userExists(String email) {
        boolean exists = false;
        try {
            this.result = statement.executeQuery("select email from users");
            while (result.next()) {
                if (email.equalsIgnoreCase(this.result.getString("email")))
                    exists = true;
            }
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exists;
    }

    //adds new user to database
    public void createUser(String email, String firstName, String lastName, String password, String date) {
        email = email.toLowerCase();
        if (!userExists(email)) {
            try {
                statement.executeUpdate(String.format("insert into users values ('%s', '%s', '%s', '%s', '%s');", email, firstName, lastName, password, date));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else
            throw new IllegalArgumentException("Email already exists");
    }

    //gets user info in array [first name, last name, password, date]
    public String[] getUser(String email) {
        String[] user = new String[4];
        try {
            this.result = statement.executeQuery("select * from users where email = '" + email + "'");
            if (result.next()) {
                user[0] = this.result.getString("firstName");
                user[1] = this.result.getString("lastName");
                user[2] = this.result.getString("password");
                user[3] = this.result.getString("date");
            }
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
    
    //change password of user
    public void changePassword(String email, String password) {
        try {
            statement.executeUpdate(String.format("update users set password = '%s' where email = '%s", password, email));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //update user date
    public void updateDate(String email, String date) {
        try {
            statement.executeUpdate(String.format("update users set date = '%s' where email = '%s'", date, email));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    
    // ACCOUNT TABLE



    //generate a new account ID (account ID's cannot repeat for the same user but two users having the same account ID doesn't matter)
    public int generateAccountID(String email) {
        ArrayList<Integer> accountIDs = new ArrayList<Integer>();
        Random rand = new Random();
        int random = rand.nextInt(9000) + 1000;
        boolean exists = false;
        try {
            this.result = statement.executeQuery("select accountID from accounts where email = '" + email + "'");
            while (result.next()) {
                accountIDs.add(Integer.parseInt(result.getString("accountID")));
                do {
                    exists = false;
                    random = rand.nextInt(9000) + 1000;
                    for (int i : accountIDs) {
                        if (random == i)
                            exists = true;
                    }
                } while (exists);
            }
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return random;
    }

    //create new account (no interest)
    public void createAccount(String email, String accountType) {
        try {
            statement.executeUpdate(String.format("insert into accounts values ('%s', %s, '%s', 0, 0)", email, generateAccountID(email), accountType));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //create new account (with interest)
    public void createAccount(String email, String accountType, double interest) {
        try {
            statement.executeUpdate(String.format("insert into accounts values ('%s', %s, '%s', 0, %s)", email, generateAccountID(email), accountType, interest));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //return the amount of accounts a user has
    public int getAccountLength(String email) {
        int len = 0;
        try {
            result = statement.executeQuery(String.format("select count(*) as len from accounts where email = '%s'", email));
            if (result.next())
                len = Integer.parseInt(result.getString("len"));
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return len;
    }

    //get all account info in 2D Array [account ID, account type, balance, interest], ...
    public String[][] getAccounts(String email) {
        String[][] accounts;
        int len = getAccountLength(email);
        try {
            accounts = new String[len][5];
            this.result = statement.executeQuery("select * from accounts where email = '" + email + "'");
            for (int i = 0; i < len; i++) {
                result.next();
                accounts[i][0] = result.getString("accountID");
                accounts[i][1] = result.getString("accountType");
                accounts[i][2] = result.getString("balance");
                accounts[i][3] = result.getString("interest");
            }
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
            accounts = new String[0][0];
        }

        return accounts;
    }

    //get balance of user account
    public double getBalance(String email, int accountID) {
        double balance = 0;
        try {
            result = statement.executeQuery(String.format("select balance from accounts where email = '%s' and accountID = %s", email, accountID));
            if (result.next())
                balance = Double.parseDouble(result.getString("balance"));
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return balance;
    }

    //update money in an account
    public void updateBalance(String email, int accountID, double balance) {
        try {
            statement.executeUpdate(String.format("update accounts set balance = %s where email = '%s' and accountID = %s", balance, email, accountID));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //return the type of account (Chequings/Savings)
    public String getAccountType(String email, int accountID) {
        String type = "";
        try {
            result = statement.executeQuery(String.format("select accountType from accounts where email = '%s' and accountID = %s", email, accountID));
            if (result.next())
                type = result.getString("accountType");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return type;
    }

    //check if accout exists using account ID
    public boolean accountExists(String email, int accountID) {
        int len = 0;
        try {
            result = statement.executeQuery(String.format("select count(*) as len from accounts where email = '%s' and accountID = %s", email, accountID));
            if (result.next())
                len = Integer.parseInt(result.getString("len"));
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return len == 1;
    }

    //remove an account along with its transactions
    public void closeAccount(String email, int accountID) {
        try {
            statement.executeUpdate(String.format("delete from accounts where email = '%s' and accountID = %s", email, accountID));
            statement.executeUpdate(String.format("delete from transactions where email = '%s' and accountID = %s", email, accountID));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    


    // TRANSACTIONS TABLE



    //add a new transaction
    public void addTransaction(String email, int accountID, String description, double amount, String date) {
        try {
            statement.executeUpdate(String.format("insert into transactions values ('%s', %s, '%s', %s, '%s')", email, accountID, description, amount, date));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //return amount of transactions in an account
    public int getTransactionLength(String email, int accountID) {
        int len = 0;
        try {
            this.result = statement.executeQuery(String.format("select count(*) as len from transactions where email = '%s' and accountID = %s", email, accountID));
            if (result.next())
                len = Integer.parseInt(result.getString("len"));
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return len;
    }

    //get users transaction record in a 2D Array [description, amount, date], ...
    public String[][] getTransactions(String email, int accountID) {
        String[][] transactions;
        int len = getTransactionLength(email, accountID);
        try {
            transactions = new String[len][4];
            this.result = statement.executeQuery(String.format("select * from transactions where email = '%s' and accountID = %s", email, accountID));
            for (int i = 0; i < len; i++) {
                result.next();
                transactions[i][0] = result.getString("description");
                transactions[i][1] = result.getString("amount");
                transactions[i][2] = result.getString("date");
            }
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
            transactions = new String[0][0];
        }

        return transactions;
    }

    //return amount of withdraws made in an account today
    public int withdrawLength(String email, int accountID) {
        String now = new Login().currentDate();
        int len = 0;
        try {
            result = statement.executeQuery(String.format("select count(*) as len from transactions where email = '%s' and accountID = %s and description = 'Withdraw' and date = '%s'", email, accountID, now));
            if (result.next())
                len = Integer.parseInt(result.getString("len"));
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return len;
    }

    //return $ amount of withdraws made in an account today
    public double withdrawAmount(String email, int accountID) {
        String now = new Login().currentDate();
        double total = 0;
        String check;
        try {
            result = statement.executeQuery(String.format("select sum(amount) as total from transactions where email = '%s' and accountID = %s and description = 'Withdraw' and date = '%s'", email, accountID, now));
            result.next();
            check = result.getString("total");
            if (check != null)
                total = Math.abs(Double.parseDouble(check));
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }



    // MULTIPLE TABLES



    //deposit into an account
    public void deposit(String email, int accountID, double amount) {
        if (amount > 0) {
            String date = new Login().currentDate();
            double balance = getBalance(email, accountID);
            amount = round(amount);
            updateBalance(email, accountID, balance + amount);
            addTransaction(email, accountID, "Deposit ", amount, date);
        }
    }

    //withdraw from an account
    public void withdraw(String email, int accountID, double amount) {
        if (amount > 0) {
            String date = new Login().currentDate();
            double balance = getBalance(email, accountID);
            amount = -round(amount);
            updateBalance(email, accountID, balance + amount);
            addTransaction(email, accountID, "Withdraw", amount, date);
        }
    }

    //round to two decimal places
    public double round(double num) {
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(num));
    }

}
