package nl.springbank.objects;

import nl.springbank.bean.CheckingAccountBean;

/**
 * @author Tristan de Boer
 * @author Sven Konings
 */
public class BalanceObject {
    private double balance;
    private double savingAccountBalance;

    public BalanceObject(CheckingAccountBean checkingAccount) {
        this.balance = checkingAccount.getBalance();
        if (checkingAccount.getSavingsAccount() != null) {
            this.savingAccountBalance = checkingAccount.getSavingsAccount().getBalance();
        }
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getSavingAccountBalance() {
        return savingAccountBalance;
    }

    public void setSavingAccountBalance(double savingAccountBalance) {
        this.savingAccountBalance = savingAccountBalance;
    }
}
