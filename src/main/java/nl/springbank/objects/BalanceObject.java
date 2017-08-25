package nl.springbank.objects;

import nl.springbank.bean.AccountBean;

/**
 * @author Tristan de Boer
 * @author Sven Konings
 */
public class BalanceObject {
    private double balance;

    public BalanceObject(AccountBean account) {
        this.balance = account.getBalance();
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
