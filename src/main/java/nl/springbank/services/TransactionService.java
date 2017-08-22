package nl.springbank.services;

import nl.springbank.bean.BankAccountBean;
import nl.springbank.bean.TransactionBean;
import nl.springbank.dao.TransactionDao;
import nl.springbank.exceptions.InvalidParamValueError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.time.Instant;
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
     * Get the transactions with the given source or target bank account.
     *
     * @param sourceAccount the given source bank account
     * @param targetAccount the given target bank account
     * @return the list of transactions
     * @throws InvalidParamValueError if an error occurred
     */
    public List<TransactionBean> getTransactionsBySourceOrTargetAccount(BankAccountBean sourceAccount, BankAccountBean targetAccount) throws InvalidParamValueError {
        List<TransactionBean> transactions;
        try {
            transactions = transactionDao.findBySourceBankAccountOrTargetBankAccountOrderByDateDesc(sourceAccount, targetAccount);
        } catch (IllegalArgumentException e) {
            throw new InvalidParamValueError(e);
        }
        return transactions;
    }

    /**
     * Get the transactions with the given source and target bank account.
     *
     * @param sourceAccount the given source bank account
     * @param targetAccount the given target bank account
     * @return the list of transactions
     * @throws InvalidParamValueError if an error occurred
     */
    public List<TransactionBean> getTransactionsBySourceAndTargetAccount(BankAccountBean sourceAccount, BankAccountBean targetAccount) throws InvalidParamValueError {
        List<TransactionBean> transactions;
        try {
            transactions = transactionDao.findBySourceBankAccountAndTargetBankAccountOrderByDateDesc(sourceAccount, targetAccount);
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
     * @param bankAccount the target bank account
     * @param amount      the amount
     */
    public synchronized void newDeposit(BankAccountBean bankAccount, double amount) {
        TransactionBean transaction = new TransactionBean();
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        transaction.setTargetBankAccount(bankAccount);
        transaction.setAmount(amount);
        transaction.setDate(Timestamp.from(Instant.now()));
        saveTransaction(transaction);
    }

    /**
     * Makes a new withdrawal
     *
     * @param bankAccount the source bank account
     * @param amount      the amount
     * @throws InvalidParamValueError if the amount is less than zero or the source bank account dus not have enough
     *                                money
     */
    public synchronized void newWithdrawal(BankAccountBean bankAccount, double amount) throws InvalidParamValueError {
        checkAmount(bankAccount, amount);
        TransactionBean transaction = new TransactionBean();
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        transaction.setSourceBankAccount(bankAccount);
        transaction.setAmount(amount);
        transaction.setDate(Timestamp.from(Instant.now()));
        saveTransaction(transaction);
    }

    /**
     * Makes a new transaction.
     *
     * @param sourceAccount the source bank account
     * @param targetAccount the target bank account
     * @param targetName    the target name
     * @param amount        the amount
     * @param description   the description
     * @throws InvalidParamValueError if the amount is less than zero or the source bank account dus not have enough
     *                                money
     */
    public synchronized void newTransaction(BankAccountBean sourceAccount, BankAccountBean targetAccount, String targetName, double amount, String description) throws InvalidParamValueError {
        checkAmount(sourceAccount, amount);
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        targetAccount.setBalance(targetAccount.getBalance() + amount);

        TransactionBean transaction = new TransactionBean();
        transaction.setSourceBankAccount(sourceAccount);
        transaction.setTargetBankAccount(targetAccount);
        transaction.setTargetName(targetName);
        transaction.setAmount(amount);
        transaction.setMessage(description);
        transaction.setDate(Timestamp.from(Instant.now()));
        saveTransaction(transaction);
    }

    /**
     * Checks if the amount is valid for the given bank account.
     *
     * @param bankAccount the given bank account
     * @param amount      the amount
     * @throws InvalidParamValueError if the amount is less than zero or the bank account dus not have enough money
     */
    private void checkAmount(BankAccountBean bankAccount, double amount) throws InvalidParamValueError {
        if (amount < 0) {
            throw new InvalidParamValueError("Amount less than zero: " + amount);
        } else if (amount > bankAccount.getBalance()) {
            throw new InvalidParamValueError("Amount more than account balance: " + amount);
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
}
