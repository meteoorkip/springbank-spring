package nl.springbank.objects;

import nl.springbank.bean.CardBean;
import nl.springbank.bean.IbanBean;

/**
 * @author Tristan de Boer.
 */
public class OpenedAccount extends OpenedCard {
    private String iBAN;

    public OpenedAccount(IbanBean ibanBean, CardBean cardBean) {
        super(cardBean);
        this.iBAN = ibanBean.getIban();
    }

    public String getiBAN() {
        return iBAN;
    }

    public void setiBAN(String iBAN) {
        this.iBAN = iBAN;
    }
}
