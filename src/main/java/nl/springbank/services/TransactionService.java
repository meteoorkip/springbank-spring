package nl.springbank.services;

import nl.springbank.bean.BankAccountBean;
import nl.springbank.bean.IbanBean;
import nl.springbank.bean.TransactionBean;
import nl.springbank.dao.BankAccountDao;
import nl.springbank.dao.IbanDao;
import nl.springbank.dao.TransactionDao;
import nl.springbank.exceptions.TransactionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * Service that does all operation regarding Transactions.
 *
 * @author Tristan de Boer.
 */
@Service
public class TransactionService {

    private final TransactionDao transactionDao;

    private final IbanDao ibanDao;

    private final BankAccountDao bankAccountDao;

    /**
     * Autowire <code>TransactionDao</code>
     */
    public TransactionService(TransactionDao transactionDao, IbanDao ibanDao, BankAccountDao bankAccountDao) {
        this.transactionDao = transactionDao;
        this.ibanDao = ibanDao;
        this.bankAccountDao = bankAccountDao;
    }

    /**
     * Returns a list of all transactions.
     *
     * @return
     */
    public Iterable<TransactionBean> getAllTransactions() {
        return transactionDao.findAll();
    }

    /**
     * Returns a list of all transactions based on IBAN.
     */
    public Iterable<TransactionBean> getTransactionsByIban(String iban) {
        IbanBean ibanBeanByIban = ibanDao.findByIban(iban);
        return transactionDao.findBySourceBankAccountOrTargetBankAccountOrSourceBankAccountIbanOrTargetBankAccountIban(
                ibanBeanByIban.getBankAccountId(), ibanBeanByIban.getBankAccountId(), iban, iban
        );
    }

    /**
     * Returns a list of all transactions based on accountId.
     */
    public Iterable<TransactionBean> getTransactionsById(long accountId) {
        return transactionDao.findBySourceBankAccountOrTargetBankAccount(accountId, accountId);
    }

    /**
     * Makes a transaction.
     * @param transactionBean
     * @return
     */
    @Transactional
    public void makeTransaction(TransactionBean transactionBean) throws TransactionException {
        BankAccountBean sourceAccount = bankAccountDao.findByIbanBean_Iban(transactionBean.getSourceBankAccountIban());
        BankAccountBean targetAccount = bankAccountDao.findByIbanBean_Iban(transactionBean.getTargetBankAccountIban());
        double amount = transactionBean.getAmount();
        if (!(sourceAccount.getBalance() > amount)) {
            throw new TransactionException();
        }
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        targetAccount.setBalance(targetAccount.getBalance() + amount);
        bankAccountDao.save(Arrays.asList(sourceAccount, targetAccount));
        transactionDao.save(transactionBean);
    }
}
