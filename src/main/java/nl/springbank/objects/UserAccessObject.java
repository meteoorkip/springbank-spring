package nl.springbank.objects;

import nl.springbank.bean.AccountBean;

/**
 * @author Sven Konings
 */
public class UserAccessObject {

    private String iBAN;
    private String owner;

    public UserAccessObject(AccountBean account) {
        this.iBAN = account.getIban();
        this.owner = account.getHolder().getUsername();
    }

    public String getiBAN() {
        return iBAN;
    }

    public void setiBAN(String iBAN) {
        this.iBAN = iBAN;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
