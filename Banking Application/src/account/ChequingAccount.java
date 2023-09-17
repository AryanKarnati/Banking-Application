package account;

import database.Data;

public class ChequingAccount extends Account {

    //return boolean of if the amount of withdrawals is 3
    public boolean hitWithdrawLimit(String email, int accountID) {
        Data data = new Data();
        return data.withdrawLength(email, accountID) == 3;
    }

    //return boolean of is the total withdrawls is greater than 100
    public boolean hitAmountLimit(String email, int accountID, double amount) {
        Data data = new Data();
        return data.withdrawAmount(email, accountID) + amount >= 100;
    }

}
