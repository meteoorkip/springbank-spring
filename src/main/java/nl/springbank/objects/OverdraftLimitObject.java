package nl.springbank.objects;

import nl.springbank.bean.AccountBean;

/**
 * @author Sven Konings
 */
public class OverdraftLimitObject {
    private Integer overdraftLimit;

    public OverdraftLimitObject(AccountBean account) {
        this.overdraftLimit = account.getOverdraftLimit();
    }

    public Integer getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(Integer overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }
}
