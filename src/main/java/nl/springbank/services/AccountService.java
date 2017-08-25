package nl.springbank.services;

import nl.springbank.bean.AccountBean;
import nl.springbank.bean.CheckingAccountBean;
import nl.springbank.bean.SavingsAccountBean;
import nl.springbank.bean.UserBean;
import nl.springbank.dao.AccountDao;
import nl.springbank.dao.CheckingAccountDao;
import nl.springbank.dao.SavingsAccountDao;
import nl.springbank.exceptions.InvalidParamValueError;
import nl.springbank.helper.IbanHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

/**
 * Service that does all operations regarding accounts.
 *
 * @author Tristan de Boer
 * @author Sven Konings
 */
@Service
public class AccountService {

    private final AccountDao accountDao;
    private final CheckingAccountDao checkingAccountDao;
    private final SavingsAccountDao savingsAccountDao;

    @Autowired
    public AccountService(AccountDao accountDao, CheckingAccountDao checkingAccountDao, SavingsAccountDao savingsAccountDao) {
        this.accountDao = accountDao;
        this.checkingAccountDao = checkingAccountDao;
        this.savingsAccountDao = savingsAccountDao;
    }

    /**
     * Get the account with the given account id.
     *
     * @param accountId the given account id
     * @return the account
     * @throws InvalidParamValueError if an error occurred or the account doesn't exist
     */
    public AccountBean getAccount(long accountId) throws InvalidParamValueError {
        AccountBean account;
        try {
            account = accountDao.findOne(accountId);
            Assert.notNull(account, "Account not found");
        } catch (IllegalArgumentException e) {
            throw new InvalidParamValueError(e);
        }
        return account;
    }

    /**
     * Get the account with the given iban.
     *
     * @param iban the given iban
     * @return the account
     * @throws InvalidParamValueError if an error occurred or the account doesn't exist
     */
    public AccountBean getAccount(String iban) throws InvalidParamValueError {
        AccountBean account;
        try {
            account = accountDao.findByIban(iban);
            Assert.notNull(account, "Account not found");
        } catch (IllegalArgumentException e) {
            throw new InvalidParamValueError(e);
        }
        return account;
    }

    /**
     * Get the checking account with the given iban.
     *
     * @param iban the given iban
     * @return the checking account
     * @throws InvalidParamValueError if an error occurred or the checking account doesn't exist
     */
    public CheckingAccountBean getCheckingAccount(String iban) throws InvalidParamValueError {
        CheckingAccountBean checkingAccount;
        try {
            checkingAccount = checkingAccountDao.findByIban(iban);
            Assert.notNull(checkingAccount, "Account not found");
        } catch (IllegalArgumentException e) {
            throw new InvalidParamValueError(e);
        }
        return checkingAccount;
    }

    /**
     * Get the savings account with the given iban.
     *
     * @param iban the given iban
     * @return the savings account
     * @throws InvalidParamValueError if an error occurred or the savings account doesn't exist
     */
    public SavingsAccountBean getSavingsAccount(String iban) throws InvalidParamValueError {
        if (!iban.endsWith("S")) {
            iban += "S";
        }
        SavingsAccountBean savingsAccount;
        try {
            savingsAccount = savingsAccountDao.findByIban(iban);
            Assert.notNull(savingsAccount, "Account not found");
        } catch (IllegalArgumentException e) {
            throw new InvalidParamValueError(e);
        }
        return savingsAccount;
    }

    /**
     * Get all accounts.
     *
     * @return the list of accounts
     */
    public List<AccountBean> getAccounts() {
        return accountDao.findAll();
    }

    /**
     * Get all checking accounts.
     *
     * @return the list of checking accounts
     */
    public List<CheckingAccountBean> getCheckingAccounts() {
        return checkingAccountDao.findAll();
    }

    /**
     * Get all savings accounts.
     *
     * @return the list of savings accounts
     */
    public List<SavingsAccountBean> getSavingsAccounts() {
        return savingsAccountDao.findAll();
    }

    /**
     * Creates a new account for the given user.
     *
     * @param user the given user
     * @return the created account
     */
    public CheckingAccountBean newCheckingAccount(UserBean user) {
        CheckingAccountBean checkingAccount = new CheckingAccountBean();
        checkingAccount.setIban(IbanHelper.generateIban(getAccounts()));
        checkingAccount.setHolder(user);
        checkingAccount.setAccessUsers(Collections.singleton(user));
        return saveCheckingAccount(checkingAccount);
    }

    /**
     * Create a new savings account for the given checking account.
     *
     * @param checkingAccount the given checking account
     * @return the created account
     */
    public SavingsAccountBean newSavingsAccount(CheckingAccountBean checkingAccount) {
        SavingsAccountBean savingsAccount = new SavingsAccountBean();
        savingsAccount.setCheckingAccount(checkingAccount);
        savingsAccount.setIban(checkingAccount.getIban() + "S");
        return saveSavingsAccount(savingsAccount);
    }

    /**
     * Closes the given checking account.
     *
     * @param checkingAccount the given checking account
     * @throws InvalidParamValueError if the amount is negative
     */
    public void closeCheckingAccount(CheckingAccountBean checkingAccount) throws InvalidParamValueError {
        if (checkingAccount.getBalance() < 0) {
            throw new InvalidParamValueError("The specified account has a negative amount");
        }
        deleteAccount(checkingAccount);
    }

    /**
     * Closes the given savings account.
     *
     * @param savingsAccount the given savings account
     */
    public void closeSavingsAccount(SavingsAccountBean savingsAccount) {
        assert savingsAccount.getBalance() == 0;
        deleteAccount(savingsAccount);
    }

    /**
     * Sets the overdraft limit of the given account.
     *
     * @param account        the given account
     * @param overdraftLimit the overdraft limit
     * @throws InvalidParamValueError if the overdraft limit is invalid
     */
    public void setOverdraftLimit(AccountBean account, int overdraftLimit) throws InvalidParamValueError {
        if (!(account instanceof CheckingAccountBean)) {
            throw new InvalidParamValueError("Only checking accounts can have overdraft");
        } else if (overdraftLimit < 0) {
            throw new InvalidParamValueError("Overdraft can't be negative");
        } else if (overdraftLimit > 5000) {
            throw new InvalidParamValueError("The maximum overdraft is 5000");
        } else {
            account.setOverdraftLimit(overdraftLimit);
            saveAccount(account);
        }
    }

    /**
     * Add the daily interest based on the daily rate.
     *
     * @param dailyRate the daily rate
     */
    public void addDailyInterest(double dailyRate) {
        List<AccountBean> accounts = getAccounts();
        for (AccountBean account : accounts) {
            if (account.getMinimumBalance() < 0) {
                double dailyInterest = Math.abs(account.getMinimumBalance()) * dailyRate;
                account.setInterest(account.getInterest() + dailyInterest);
            }
            account.setMinimumBalance(account.getBalance());
        }
        saveAccounts(accounts);
    }

    /**
     * Reset the interest of the given account.
     *
     * @param account the given account
     */
    public void resetInterest(AccountBean account) {
        account.setInterest(0.0);
        saveAccount(account);
    }

    /**
     * Save the given account in the database.
     *
     * @param account the given account
     * @return the saved account
     */
    public AccountBean saveAccount(AccountBean account) {
        return accountDao.save(account);
    }

    /**
     * Save the given checking account in the database.
     *
     * @param checkingAccount the given checking account
     * @return the saved checking account
     */
    public CheckingAccountBean saveCheckingAccount(CheckingAccountBean checkingAccount) {
        return checkingAccountDao.save(checkingAccount);
    }

    /**
     * Save the given savings account in the database.
     *
     * @param savingsAccount the given savings account
     * @return the saved savings account
     */
    public SavingsAccountBean saveSavingsAccount(SavingsAccountBean savingsAccount) {
        return savingsAccountDao.save(savingsAccount);
    }

    /**
     * Save the given accounts in the database.
     *
     * @param accounts the given accounts
     * @return the list of saved accounts
     */
    public List<AccountBean> saveAccounts(Iterable<AccountBean> accounts) {
        return accountDao.save(accounts);
    }

    /**
     * Save the given checking accounts in the database.
     *
     * @param checkingAccounts the given checking accounts
     * @return the list of saved checking accounts
     */
    public List<CheckingAccountBean> saveCheckingAccounts(Iterable<CheckingAccountBean> checkingAccounts) {
        return checkingAccountDao.save(checkingAccounts);
    }

    /**
     * Save the given savings accounts in the database.
     *
     * @param savingsAccounts the given savings accounts
     * @return the list of saved savings accounts
     */
    public List<SavingsAccountBean> saveSavingsAccounts(Iterable<SavingsAccountBean> savingsAccounts) {
        return savingsAccountDao.save(savingsAccounts);
    }

    /**
     * Delete the account with the given id.
     *
     * @param accountId the given id
     */
    public void deleteAccount(long accountId) {
        accountDao.delete(accountId);
    }

    /**
     * Delete the given account.
     *
     * @param account the given account
     */
    public void deleteAccount(AccountBean account) {
        accountDao.delete(account);
    }

    /**
     * Delete the given accounts.
     *
     * @param accounts the given accounts
     */
    public void deleteAccounts(Iterable<AccountBean> accounts) {
        accountDao.delete(accounts);
    }

    /**
     * Delete all accounts.
     */
    public void deleteAccounts() {
        accountDao.deleteAll();
    }
}
