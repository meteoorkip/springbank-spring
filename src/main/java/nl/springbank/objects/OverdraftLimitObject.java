package nl.springbank.objects;

import nl.springbank.bean.AccountBean;

/**
 * @author Sven Konings
 */
public class OverdraftLimitObject {
    private int overdraftLimit;

    public OverdraftLimitObject(AccountBean account) {
        this.overdraftLimit = account.getOverdraftLimit();
    }

    public int getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(int overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }
}
