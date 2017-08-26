package nl.springbank.controllers.info;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import nl.springbank.bean.*;
import nl.springbank.exceptions.InvalidParamValueError;
import nl.springbank.exceptions.NotAuthorizedError;
import nl.springbank.helper.DateHelper;
import nl.springbank.objects.*;
import nl.springbank.services.AccountService;
import nl.springbank.services.LogService;
import nl.springbank.services.TransactionService;
import nl.springbank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Sven Konings
 */
@Service
@AutoJsonRpcServiceImpl
public class InfoControllerImpl implements InfoController {

    private final AccountService accountService;
    private final UserService userService;
    private final TransactionService transactionService;
    private final LogService logService;

    @Autowired
    public InfoControllerImpl(AccountService accountService, UserService userService, TransactionService transactionService, LogService logService) {
        this.accountService = accountService;
        this.userService = userService;
        this.transactionService = transactionService;
        this.logService = logService;
    }

    @Override
    public BalanceObject getBalance(String authToken, String iBAN) throws InvalidParamValueError, NotAuthorizedError {
        CheckingAccountBean checkingAccount = accountService.getCheckingAccount(iBAN);
        userService.checkAccess(checkingAccount, authToken);
        return new BalanceObject(checkingAccount);
    }

    @Override
    public List<TransactionObject> getTransactionsOverview(String authToken, String iBAN, int nrOfTransactions) throws InvalidParamValueError, NotAuthorizedError {
        CheckingAccountBean checkingAccount = accountService.getCheckingAccount(iBAN);
        userService.checkAccess(checkingAccount, authToken);
        Stream<TransactionBean> transactions = transactionService.getTransactionsBySourceOrTargetAccount(checkingAccount, checkingAccount).stream();
        SavingsAccountBean savingsAccount = checkingAccount.getSavingsAccount();
        if (savingsAccount != null) {
            Stream<TransactionBean> savingsTransactions = transactionService.getTransactionsBySourceOrTargetAccount(savingsAccount, savingsAccount).stream();
            transactions = Stream.concat(transactions, savingsTransactions).distinct().sorted();
        }
        return transactions
                .limit(nrOfTransactions)
                .map(TransactionObject::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserAccessObject> getUserAccess(String authToken) throws InvalidParamValueError, NotAuthorizedError {
        UserBean user = userService.getUserByAuth(authToken);
        Set<CheckingAccountBean> accessAccounts = user.getAccessAccounts();
        return accessAccounts.stream()
                .map(UserAccessObject::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BankAccountAccessObject> getBankAccountAccess(String authToken, String iBAN) throws InvalidParamValueError, NotAuthorizedError {
        AccountBean account = accountService.getAccount(iBAN);
        userService.checkHolder(account, authToken);
        Set<UserBean> accessUsers = account.getAccessUsers();
        return accessUsers.stream()
                .map(BankAccountAccessObject::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventLogObject> getEventLogs(String beginDate, String endDate) throws InvalidParamValueError {
        List<LogBean> logs = logService.getLogs(DateHelper.getDateFromString(beginDate), DateHelper.getDateFromString(endDate));
        return logs.stream()
                .map(EventLogObject::new)
                .collect(Collectors.toList());
    }
}
