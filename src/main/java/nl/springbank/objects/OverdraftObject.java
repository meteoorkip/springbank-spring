package nl.springbank.objects;

import nl.springbank.bean.BankAccountBean;

/**
 * @author Sven Konings
 */
public class OverdraftObject {
    private int overdraftLimit;

    public OverdraftObject(BankAccountBean bankAccountBean) {
        this.overdraftLimit = bankAccountBean.getOverdraftLimit();
    }

    public int getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(int overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }
}
