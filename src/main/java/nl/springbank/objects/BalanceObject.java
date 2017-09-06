package nl.springbank.objects;

import nl.springbank.bean.CheckingAccountBean;

/**
 * @author Tristan de Boer
 * @author Sven Konings
 */
public class BalanceObject {
    private Double balance;
    private Double savingAccountBalance;

    public BalanceObject(CheckingAccountBean checkingAccount) {
        this.balance = checkingAccount.getBalance();
        if (checkingAccount.getSavingsAccount() != null) {
            this.savingAccountBalance = checkingAccount.getSavingsAccount().getBalance();
        }
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getSavingAccountBalance() {
        return savingAccountBalance;
    }

    public void setSavingAccountBalance(Double savingAccountBalance) {
        this.savingAccountBalance = savingAccountBalance;
    }
}
