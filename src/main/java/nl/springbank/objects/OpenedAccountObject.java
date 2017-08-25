package nl.springbank.objects;

import nl.springbank.bean.AccountBean;
import nl.springbank.bean.CardBean;

/**
 * @author Tristan de Boer
 * @author Sven Konings
 */
public class OpenedAccountObject extends OpenedCardObject {
    private String iBAN;

    public OpenedAccountObject(AccountBean account, CardBean card) {
        super(card);
        this.iBAN = account.getIban();
    }

    public String getiBAN() {
        return iBAN;
    }

    public void setiBAN(String iBAN) {
        this.iBAN = iBAN;
    }
}
