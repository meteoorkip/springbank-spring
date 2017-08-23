package nl.springbank.services;

import nl.springbank.bean.BankAccountBean;
import nl.springbank.bean.UserBean;
import nl.springbank.dao.BankAccountDao;
import nl.springbank.exceptions.InvalidParamValueError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

/**
 * Service that does all operations regarding BankAccounts.
 *
 * @author Tristan de Boer
 * @author Sven Konings
 */
@Service
public class BankAccountService {

    private final BankAccountDao bankAccountDao;

    @Autowired
    public BankAccountService(BankAccountDao bankAccountDao) {
        this.bankAccountDao = bankAccountDao;
    }

    /**
     * Get the bank account with the given bank account id.
     *
     * @param bankAccountId the given bank account id
     * @return the bank account
     * @throws InvalidParamValueError if an error occurred or the account doesn't exist
     */
    public BankAccountBean getBankAccount(long bankAccountId) throws InvalidParamValueError {
        BankAccountBean bankAccount;
        try {
            bankAccount = bankAccountDao.findOne(bankAccountId);
            Assert.notNull(bankAccount, "Bank account not found");
        } catch (IllegalArgumentException e) {
            throw new InvalidParamValueError(e);
        }
        return bankAccount;
    }

    /**
     * Get the bank account with the given iban.
     *
     * @param iban the given iban
     * @return the bank account
     * @throws InvalidParamValueError if an error occurred or the account doesn't exist
     */
    public BankAccountBean getBankAccount(String iban) throws InvalidParamValueError {
        BankAccountBean bankAccount;
        try {
            bankAccount = bankAccountDao.findByIban_Iban(iban);
            Assert.notNull(bankAccount, "Bank account not found");
        } catch (IllegalArgumentException e) {
            throw new InvalidParamValueError(e);
        }
        return bankAccount;
    }

    /**
     * Get all bank accounts.
     *
     * @return the list of bank accounts
     */
    public List<BankAccountBean> getBankAccounts() {
        return bankAccountDao.findAll();
    }

    /**
     * Creates a new bank account for the given user.
     *
     * @param user the given user
     * @return the created bank account
     */
    public BankAccountBean newBankAccount(UserBean user) {
        BankAccountBean bankAccount = new BankAccountBean();
        bankAccount.setHolder(user);
        bankAccount.setAccessUsers(Collections.singleton(user));
        bankAccount.setBalance(0.0);
        return saveBankAccount(bankAccount);
    }

    /**
     * Save the given bank account in the database.
     *
     * @param bankAccount the given bank account
     * @return the saved bank account
     */
    public BankAccountBean saveBankAccount(BankAccountBean bankAccount) {
        return bankAccountDao.save(bankAccount);
    }

    /**
     * Save the given bank accounts in the database.
     *
     * @param bankAccounts the given bank accounts
     * @return the list of saved bank accounts
     */
    public List<BankAccountBean> saveBankAccounts(Iterable<BankAccountBean> bankAccounts) {
        return bankAccountDao.save(bankAccounts);
    }

    /**
     * Delete the bank account with the given id.
     *
     * @param bankAccountId the given id
     */
    public void deleteBankAccount(long bankAccountId) {
        bankAccountDao.delete(bankAccountId);
    }

    /**
     * Delete the given bank account.
     *
     * @param bankAccount the given bank account
     */
    public void deleteBankAccount(BankAccountBean bankAccount) {
        bankAccountDao.delete(bankAccount);
    }

    /**
     * Delete the given bank accounts.
     *
     * @param bankAccounts the given bank accounts
     */
    public void deleteBankAccounts(Iterable<BankAccountBean> bankAccounts) {
        bankAccountDao.delete(bankAccounts);
    }

    /**
     * Delete all bank accounts.
     */
    public void deleteBankAccounts() {
        bankAccountDao.deleteAll();
    }
}
