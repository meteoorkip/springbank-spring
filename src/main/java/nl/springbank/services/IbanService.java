package nl.springbank.services;

import nl.springbank.bean.BankAccountBean;
import nl.springbank.bean.IbanBean;
import nl.springbank.dao.IbanDao;
import nl.springbank.exceptions.InvalidParamValueError;
import nl.springbank.helper.IbanHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Handles all iBAN requests.
 *
 * @author Tristan de Boer
 * @author Sven Konings
 */
@Service
public class IbanService {

    private final IbanDao ibanDao;

    @Autowired
    public IbanService(IbanDao ibanDao) {
        this.ibanDao = ibanDao;
    }

    /**
     * Get the iban with the given iban id.
     *
     * @param ibanId the given iban id
     * @return the iban
     * @throws InvalidParamValueError if an error occurred or the iban doesn't exist
     */
    public IbanBean getIban(long ibanId) throws InvalidParamValueError {
        IbanBean iban;
        try {
            iban = ibanDao.findOne(ibanId);
            Assert.notNull(iban, "iBAN not found");
        } catch (IllegalArgumentException e) {
            throw new InvalidParamValueError(e);
        }
        return iban;
    }

    /**
     * Get the iban bean with the given iban.
     *
     * @param iban the given iban
     * @return the iban bean
     * @throws InvalidParamValueError if an error occurred or the iban bean doesn't exist
     */
    public IbanBean getIban(String iban) throws InvalidParamValueError {
        IbanBean ibanBean;
        try {
            ibanBean = ibanDao.findByIban(iban);
            Assert.notNull(ibanBean, "iBAN not found");
        } catch (IllegalArgumentException e) {
            throw new InvalidParamValueError(e);
        }
        return ibanBean;
    }

    /**
     * Get all ibans.
     *
     * @return a list of ibans
     */
    public List<IbanBean> getIbans() {
        return ibanDao.findAll();
    }

    /**
     * Create a new iban for the given bank account.
     *
     * @param bankAccount the given bank account
     * @return the new iban
     */
    public synchronized IbanBean newIban(BankAccountBean bankAccount) {
        IbanBean iban = new IbanBean();
        iban.setBankAccount(bankAccount);
        iban.setIban(IbanHelper.generateIban(getIbans()));
        return saveIban(iban);
    }

    /**
     * Save the given iban in the database.
     *
     * @param iban the given iban
     * @return the saved iban
     */
    public IbanBean saveIban(IbanBean iban) {
        return ibanDao.save(iban);
    }

    /**
     * Save the given ibans in the database.
     *
     * @param ibans the given ibans
     * @return the list of saved ibans
     */
    public List<IbanBean> saveIbans(Iterable<IbanBean> ibans) {
        return ibanDao.save(ibans);
    }

    /**
     * Delete the iban with the given id.
     *
     * @param ibanId the given id
     */
    public void deleteIban(long ibanId) {
        ibanDao.delete(ibanId);
    }

    /**
     * Delete the given iban.
     *
     * @param iban the given iban
     */
    public void deleteIban(IbanBean iban) {
        ibanDao.delete(iban);
    }

    /**
     * Delete the given ibans.
     *
     * @param ibans the given ibans
     */
    public void deleteIbans(Iterable<IbanBean> ibans) {
        ibanDao.delete(ibans);
    }

    /**
     * Delete all ibans.
     */
    public void deleteIbans() {
        ibanDao.deleteAll();
    }
}
