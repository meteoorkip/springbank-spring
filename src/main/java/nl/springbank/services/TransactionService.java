package nl.springbank.services;

import nl.springbank.bean.AccountBean;
import nl.springbank.bean.SavingsAccountBean;
import nl.springbank.bean.TransactionBean;
import nl.springbank.dao.TransactionDao;
import nl.springbank.exceptions.InvalidParamValueError;
import nl.springbank.helper.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Service that does all operation regarding Transactions.
 *
 * @author Tristan de Boer
 * @author Sven Konings
 */
@Service
public class TransactionService {

    private final TransactionDao transactionDao;

    @Autowired
    public TransactionService(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    /**
     * Get the transaction with the given transaction id.
     *
     * @param transactionId the given transaction id
     * @return the transaction
     * @throws InvalidParamValueError if an error occurred or the transaction doesn't exist
     */
    public TransactionBean getTransaction(long transactionId) throws InvalidParamValueError {
        TransactionBean transaction;
        try {
            transaction = transactionDao.findOne(transactionId);
            Assert.notNull(transaction, "Transaction not found");
        } catch (IllegalArgumentException e) {
            throw new InvalidParamValueError(e);
        }
        return transaction;
    }

    /**
     * Get the transactions with the given source or target account.
     *
     * @param sourceAccount the given source account
     * @param targetAccount the given target account
     * @return the list of transactions
     * @throws InvalidParamValueError if an error occurred
     */
    public List<TransactionBean> getTransactionsBySourceOrTargetAccount(AccountBean sourceAccount, AccountBean targetAccount) throws InvalidParamValueError {
        List<TransactionBean> transactions;
        try {
            transactions = transactionDao.findBySourceAccountOrTargetAccountOrderByDateDesc(sourceAccount, targetAccount);
        } catch (IllegalArgumentException e) {
            throw new InvalidParamValueError(e);
        }
        return transactions;
    }

    /**
     * Get the transactions with the given source and target account.
     *
     * @param sourceAccount the given source account
     * @param targetAccount the given target account
     * @return the list of transactions
     * @throws InvalidParamValueError if an error occurred
     */
    public List<TransactionBean> getTransactionsBySourceAndTargetAccount(AccountBean sourceAccount, AccountBean targetAccount) throws InvalidParamValueError {
        List<TransactionBean> transactions;
        try {
            transactions = transactionDao.findBySourceAccountAndTargetAccountOrderByDateDesc(sourceAccount, targetAccount);
        } catch (IllegalArgumentException e) {
            throw new InvalidParamValueError(e);
        }
        return transactions;
    }

    /**
     * Get all transactions.
     *
     * @return the list of transactions
     */
    public List<TransactionBean> getTransactions() {
        return transactionDao.findAll();
    }

    /**
     * Makes a new deposit.
     *
     * @param account the target account
     * @param amount  the amount
     */
    public synchronized void newDeposit(AccountBean account, String targetName, double amount, String description) throws InvalidParamValueError {
        if (amount == 0) {
            return;
        }
        checkAmount(amount);
        TransactionBean transaction = new TransactionBean();
        account.setBalance(account.getBalance() + amount);
        transaction.setTargetAccount(account);
        transaction.setTargetName(targetName);
        transaction.setAmount(amount);
        transaction.setMessage(description);
        transaction.setDate(DateHelper.getTime());
        saveTransaction(transaction);
    }

    /**
     * Makes a new withdrawal
     *
     * @param account the source account
     * @param amount  the amount
     * @throws InvalidParamValueError if the amount is less than zero or the source account dus not have enough
     *                                money
     */
    public synchronized void newWithdrawal(AccountBean account, String targetName, double amount, String description) throws InvalidParamValueError {
        if (amount == 0) {
            return;
        }
        checkAmount(account, amount);
        TransactionBean transaction = new TransactionBean();
        account.setBalance(account.getBalance() - amount);
        transaction.setSourceAccount(account);
        transaction.setTargetName(targetName);
        transaction.setAmount(amount);
        transaction.setMessage(description);
        transaction.setDate(DateHelper.getTime());
        saveTransaction(transaction);
    }

    /**
     * Makes a new transaction.
     *
     * @param sourceAccount the source account
     * @param targetAccount the target account
     * @param targetName    the target name
     * @param amount        the amount
     * @param description   the description
     * @throws InvalidParamValueError if the amount is less than zero or the source account dus not have enough
     *                                money
     */
    public synchronized void newTransaction(AccountBean sourceAccount, AccountBean targetAccount, String targetName, double amount, String description) throws InvalidParamValueError {
        if (amount == 0) {
            return;
        }
        checkTransaction(sourceAccount, targetAccount);
        checkAmount(sourceAccount, amount);
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        targetAccount.setBalance(targetAccount.getBalance() + amount);

        TransactionBean transaction = new TransactionBean();
        transaction.setSourceAccount(sourceAccount);
        transaction.setTargetAccount(targetAccount);
        transaction.setTargetName(targetName);
        transaction.setAmount(amount);
        transaction.setMessage(description);
        transaction.setDate(DateHelper.getTime());
        saveTransaction(transaction);
    }

    /**
     * Check if the transactions between the given source and target account is valid.
     *
     * @param sourceAccount the given source account
     * @param targetAccount the given target account
     * @throws InvalidParamValueError if the transaction is invalid
     */
    private void checkTransaction(AccountBean sourceAccount, AccountBean targetAccount) throws InvalidParamValueError {
        if (sourceAccount instanceof SavingsAccountBean && !((SavingsAccountBean) sourceAccount).getCheckingAccount().equals(targetAccount)) {
            throw new InvalidParamValueError("Transactions from savings accounts can only be made to the corresponding checkings account");
        } else if (targetAccount instanceof SavingsAccountBean && !((SavingsAccountBean) targetAccount).getCheckingAccount().equals(sourceAccount)) {
            throw new InvalidParamValueError("Transactions to savings accounts can only be made from the corresponding checkings account");
        }
    }

    /**
     * Checks if the amount is valid for the given account.
     *
     * @param account the given account
     * @param amount  the amount
     * @throws InvalidParamValueError if the amount is less than zero or the account dus not have enough money
     */
    private void checkAmount(AccountBean account, double amount) throws InvalidParamValueError {
        checkAmount(amount);
        if (amount > account.getBalance() + account.getOverdraftLimit()) {
            throw new InvalidParamValueError("Amount more than available money");
        }
    }

    /**
     * Checks if the amount is valid
     *
     * @param amount the amount
     * @throws InvalidParamValueError if the amount is less than zero
     */
    private void checkAmount(double amount) throws InvalidParamValueError {
        if (amount < 0) {
            throw new InvalidParamValueError("Amount less than zero: " + amount);
        }
    }

    /**
     * Save the given transaction.
     *
     * @param transaction the given transaction
     * @return the saved transaction
     */
    public TransactionBean saveTransaction(TransactionBean transaction) {
        return transactionDao.save(transaction);
    }

    /**
     * Save the given transactions.
     *
     * @param transactions the given transactions
     * @return the list of saved transactions
     */
    public List<TransactionBean> saveTransactions(Iterable<TransactionBean> transactions) {
        return transactionDao.save(transactions);
    }

    /**
     * Delete the transaction with the given id.
     *
     * @param transactionId the given id
     */
    public void deleteTransaction(long transactionId) {
        transactionDao.delete(transactionId);
    }

    /**
     * Delete the given transaction.
     *
     * @param transaction the given transaction
     */
    public void deleteTransaction(TransactionBean transaction) {
        transactionDao.delete(transaction);
    }

    /**
     * Delete the given transactions.
     *
     * @param transactions the given transactions
     */
    public void deleteTransactions(Iterable<TransactionBean> transactions) {
        transactionDao.delete(transactions);
    }

    /**
     * Delete all transactions.
     */
    public void deleteTransactions() {
        transactionDao.deleteAll();
    }
}
