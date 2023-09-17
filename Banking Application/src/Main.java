import java.util.Scanner;
import java.io.Console;
import java.util.Arrays;
import java.util.InputMismatchException;

import account.*;
import database.Data;
import email.EmailVerification;
import login.*;
import userInfo.User;
import webscrape.Currency;

public class Main {
    public static void main(String[] args) {
        //create objects
        Scanner sc = new Scanner(System.in);
        Console console = System.console();
        Data data = new Data();
        Login login = new Login();
        Register register = new Register();
        EmailVerification emails = new EmailVerification();
        User user;
        Currency currency = new Currency();
        ChequingAccount chequing = new ChequingAccount();
        
        //declare and initialize variables
        char[] passwordChars;
        String registerLogin, email = "", password = "", confPassword, firstName, lastName, yesNo, withdrawDeposit, chequingSavings, fromCurrency, toCurrency;
        int accountID, accountNum, count = 0, code, option, accountOption;
        double amount = 0;
        boolean userExists, validEmail, accountExists, exit, moneyLeft;


        //print welcome message
        System.out.println("""
                ==================== DEVOPS FINANCIAL ====================

                Welcome to DevOps Financial! Our bank is made from a highly advanced system with a multitude of features.
                Just keep a few things in mind while you explore our banking application:

                    1. Don't be alarmed when you dont see your password on the screen as you type it out.
                       We're just trying to keep it hidden!
                    2. Remember to keep an eye on your Chequing account daily limit of $100 and only 3 transactions
                    3. Savings accounts only come with 2% daily interest
                    4. And finally, make smart financial decisions!
                
                """);

        //uvi start
        //loop while user doesn't input valid option
        do {
            System.out.print("[R]egister or [L]ogin: ");
            registerLogin = sc.nextLine();
        } while (!(registerLogin.equalsIgnoreCase("r") || registerLogin.equalsIgnoreCase("l")));
        

        //if they register
        if (registerLogin.equalsIgnoreCase("r")) {
            
            //keep asking for valid name
            do {
                System.out.print("First Name: ");
                firstName = sc.nextLine().trim();
            } while (register.invalidName(firstName));

            //keep asking for valid name
            do {
                System.out.print("Last Name: ");
                lastName = sc.nextLine().trim();
            } while (register.invalidName(lastName));
            //uvi end

            //samee start
            //loop while the user wants to enter a new wmail to use
            do {
                exit = false;

                //keep asking for valid email
                do {
                    System.out.print("Email: ");
                    email = sc.nextLine().trim();
                    userExists = data.userExists(email);
                    validEmail = emails.validateEmail(email);
                    if (!validEmail)
                        System.out.println("Invalid Email");

                    if (userExists)
                        System.out.println("Email in use");

                } while (userExists || !validEmail);

                //keep asking for valid password and confirmation password
                do {
                    //keep asking for a valid password
                    do {
                        //make password not show in the console
                        register.passwordRequirements(password);
                        passwordChars = console.readPassword("Password: ");
                        password = new String(passwordChars).trim();
                    } while (register.invalidPassword(password));

                    passwordChars = console.readPassword("Confirm Password: ");
                    confPassword = new String(passwordChars).trim();
                } while (!password.equals(confPassword));
                
                //clear the password
                Arrays.fill(passwordChars, ' ');
                
                //send verification code through email
                emails.sendEmail(email);
                System.out.println("A 4-digit code has been sent to you through email");

                //keep asking for rigth verification code
                do {
                    code = intInput("Enter 4-Digit Code: ");
                    count++;

                    //if they don't enter the right code 3 times
                    if (count == 3) {
                        //ask user if they want to try a different email
                        do {
                            System.out.print("Would you like to try a different email: y/n ");
                            yesNo = sc.nextLine();
                        } while (!(yesNo.equalsIgnoreCase("y") || yesNo.equalsIgnoreCase("n")));

                        if (yesNo.equalsIgnoreCase("y")) {
                            exit = true;
                            count = 0;
                            password = "";
                        }
                        else {
                            exit = false;
                            count = 0;
                        }
                        
                    }
                } while (code != emails.getCode() && !exit);

            } while (exit);
            //samee end

            //uvi start
            //create new user with inputs taken
            register.createUser(email, firstName, lastName, password);
            System.out.println("Account Created");
        }

        //if logging in
        else if (registerLogin.equalsIgnoreCase("l")) {

            //keep asking for valid email
            do {
                System.out.print("Email: ");
                email = sc.nextLine().trim();
            } while (!data.userExists(email));
            //uvi end

            //samee start
            //keep asking for valid password
            do {
                //make password not show in the console
                passwordChars = console.readPassword("Password: ");
                password = new String(passwordChars).trim();
            } while (!login.isPassword(email, password));

            //clear the password
            Arrays.fill(passwordChars, ' ');
            
            //update interest in users account 
            login.updateInterest(email);
        }
        //samee end

        //uvi start
        //create new user object
        user = new User(email);
        //output welcome message
        System.out.println("\nWelcome " + user.getFirstName() + " " + user.getLastName() + "!\n");
           
        //loop while option entered isn't between 1-4
        do {
            System.out.println("""
                    ========== HOME PAGE ==========
                    1. View Accounts
                    2. Open Account
                    3. Currency Converter
                    4. Exit
                    """);
            
            //ask for option
            do {
                option = intInput("Enter Option: ");
            } while (!(option >= 1 && option <= 4));

            //get amount of accounts user has
            System.out.println();
            accountNum = data.getAccountLength(email);
            //uvi end

            //aryan start
            //if they chose to view accounts while they have none
            if (option == 1 && accountNum == 0) {
                System.out.println("It looks like you don't have any accounts with us at this time");
                //ask them if they would want to open an account
                do {
                    System.out.print("Would you like to open an account: y/n ");
                    yesNo = sc.nextLine();
                } while (!(yesNo.equalsIgnoreCase("y") || yesNo.equalsIgnoreCase("n")));

                //if they do direct them to the opening account page
                if (yesNo.equalsIgnoreCase("y"))
                    option = 2;
                //if not take them to main menu
                else
                    option = 0;
            }
            //aryan end

            //samee start
            //if they chose to view accounts
            else if (option == 1) {
                //print their accounts with their accountID's and balances
                do {
                    System.out.println("---------- ACCOUNTS ----------");
                    user.printAccounts();
                    System.out.println("""

                            ========== ACCOUNT PAGE ==========
                            1. Make Transaction
                            2. View Transactions
                            3. Close Account
                            4. Go Back
                            """);

                    //ask for option to enter
                    do {
                        accountOption = intInput("Enter Option: ");
                    } while (!(accountOption >= 1 && accountOption <= 4));


                    //if they want to make a transaction
                    if (accountOption == 1) {
                        //keep asking for valid accountID
                        do {
                            accountID = intInput("Enter the ID of the account you want to use: ");
                        } while (!data.accountExists(email, accountID));

                        //ask if they want to withdraw or deposit
                        do {
                            System.out.print("[W]ithdraw or [D]eposit: ");
                            withdrawDeposit = sc.nextLine();
                        } while (!(withdrawDeposit.equalsIgnoreCase("w") || withdrawDeposit.equalsIgnoreCase("d")));

                        //if they want to withdraw
                        if (withdrawDeposit.equalsIgnoreCase("w")) {
                            //if the account they chose is a chequing account
                            if (data.getAccountType(email, accountID).equals("Chequing")) {
                                //keep asking for amount to be withdrawn while not negative, transaction larger than balance, they hit their limit of 3 transactions and $100 per day
                                do {
                                    amount = doubleInput("Enter Amount: $");
                                } while (amount < 0 || data.getBalance(email, accountID) < amount || amount != 0 && (chequing.hitAmountLimit(email, accountID, amount) || chequing.hitWithdrawLimit(email, accountID)));
                            }

                            //if the account they chose is a savings account
                            if (data.getAccountType(email, accountID).equals("Saving")) {
                                //keep asking for ammount while not negative or transaction larger than balance
                                do {
                                    amount = doubleInput("Enter Amount: $");
                                } while (amount < 0 || data.getBalance(email, accountID) < amount);
                            }

                            //withdraw amount
                            data.withdraw(email, accountID, amount);
                        }

                        //if they want to deposit
                        if (withdrawDeposit.equalsIgnoreCase("d")) {
                            //ask for amount as long as it's not negative
                            do {
                                amount = doubleInput("Enter Amount: $");
                            } while (amount < 0);

                            //deposite amount
                            data.deposit(email, accountID, amount);
                        }

                        //pause before continuing to main menu
                        System.out.print("Enter anything to continue ==> ");
                        sc.nextLine();
                        System.out.println();
                        accountOption = 0;
                    }


                    //if they chose to view their transactions
                    if (accountOption == 2) {
                        //ask for the account ID they want to view if it exists
                        do {
                            accountID = intInput("Enter the ID of the account you want to use: ");
                        } while (!data.accountExists(email, accountID));

                        System.out.println();
                        
                        //if they have no transactions output message
                        if (data.getTransactionLength(email, accountID) == 0)
                            System.out.println("It looks like you don't have any transactions at the moment");
                        //if they have them print all their transactions for that account
                        else {
                            System.out.println("---------- TRANSACTIONS ----------");
                            user.printTransactions(accountID);
                            System.out.println();
                        }

                        //pause before continuing to main menu
                        System.out.print("Enter anything to continue ==> ");
                        sc.nextLine();
                        System.out.println();
                        accountOption = 0;
                    }
                    

                    //if they want to close an account
                    if (accountOption == 3) {
                        //let them know they can't remove an account with money in it
                        System.out.println("\nPlease make sure you withdraw all the money out of the account you wish to close.");
                        
                        //loop while they want to try more accounts and theres money left or the account doesnt exist
                        do {
                            //ask for account to close
                            accountID = intInput("Enter the account ID you want to close: ");

                            //check if money in the account or the account exists
                            moneyLeft = data.getBalance(email, accountID) > 0;
                            accountExists = data.accountExists(email, accountID);
                            yesNo = "n";
                            
                            //if there's money left or the account doesn't exist
                            if (moneyLeft || !accountExists) {
                                System.out.println("Couldn't close account");
                                //ask if they want to return to account page
                                do {
                                    System.out.print("Would you like to return to the Account Page: y/n ");
                                    yesNo = sc.nextLine();
                                } while (!(yesNo.equalsIgnoreCase("y") || yesNo.equalsIgnoreCase("n")));
                            }

                            //if not close account
                            else {
                                data.closeAccount(email, accountID);
                                System.out.println("Account Closed Successfully\n");
                            }

                        } while (yesNo.equalsIgnoreCase("n") && (moneyLeft || !accountExists));
                        //return to account page
                        accountOption = 0;
                    }


                    //if they want to go back return to home page
                    if (accountOption == 4) {
                        System.out.println();
                        option = 0;
                    }

                } while (!(accountOption > 0));
            }
            //samee end


            //aryan start
            //if they want to open an account
            if (option == 2) {
                System.out.println("========== OPEN ACCOUNT ==========\n");

                //ask if they want to open a chequing or savings account
                do {
                    System.out.print("Would you like to open a [C]hequings or [S]avings Account: ");
                    chequingSavings = sc.nextLine();
                } while (!(chequingSavings.equalsIgnoreCase("c") || chequingSavings.equalsIgnoreCase("s")));

                //if they want to open a chequings account create one
                if (chequingSavings.equalsIgnoreCase("c")) {
                    data.createAccount(email, "Chequing");
                    System.out.println("Account Created");
                }
                //if not create a savings account
                else {
                    data.createAccount(email, "Saving", 0.02);
                    System.out.println("Account Created");
                }

                //pause before returning to home page
                System.out.print("Enter anything to continue ==> ");
                sc.nextLine();
                System.out.println();
                option = 0;
            }
            //aryan end

            //samee start
            //if they want to use our currency conversion calculator
            if (option == 3) {
                System.out.println("========== CURRENCY CONVERSION ==========");

                System.out.println("\nCurrency Being Converted");
                //ask for real currency code
                do {
                    System.out.print("Currency Code: ");
                    fromCurrency = sc.nextLine();
                } while (currency.notCode(fromCurrency));
                currency.setFromCurrency(fromCurrency);

                System.out.println("\nCurrency Being Converted To");
                //ask for real currency code
                do {
                    System.out.print("Currency Code: ");
                    toCurrency = sc.nextLine();
                } while (currency.notCode(toCurrency));
                currency.setToCurrency(toCurrency);

                //ask for ammount to be converted
                System.out.print(String.format("\nEnter Amount(%s): ", fromCurrency.toUpperCase()));
                amount = sc.nextDouble();

                //print conversion rate and calculation
                System.out.println("\n" + currency.toString(amount));

                //pause before returning to home page
                System.out.print("\nEnter anything to continue ==> ");
                sc.nextLine();
                sc.nextLine();
                System.out.println();
                option = 0;
            }
            //samee end

        } while (!(option > 0));
        
        //close Scanner object
        sc.close();
    } //end of main method



    //start of intInput method
    public static int intInput(String text) {
        //declare object and variables
        Scanner sc = new Scanner(System.in);
        boolean invalid;
        int option = 0;

        //take validated integer input
        do {
            try {
                System.out.print(text);
                option = sc.nextInt();
                invalid = false;
            } catch (InputMismatchException e) {
                sc.nextLine();
                invalid = true;
            }
        } while (invalid);

        //return validated integer input
        return option;
    } //end of intInput method

    //start of doubleInput method
    public static double doubleInput(String text) {
        //declare object and variables
        Scanner sc = new Scanner(System.in);
        boolean invalid;
        double option = 0;

        //take validated double input
        do {
            try {
                System.out.print(text);
                option = sc.nextDouble();
                invalid = false;
            } catch (InputMismatchException e) {
                sc.nextLine();
                invalid = true;
            }
        } while (invalid);

        //return validated double input
        return option;
    } //end of doubleInput method


}
