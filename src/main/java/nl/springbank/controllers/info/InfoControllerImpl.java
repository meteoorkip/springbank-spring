package nl.springbank.controllers.info;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import nl.springbank.bean.AccountBean;
import nl.springbank.bean.CheckingAccountBean;
import nl.springbank.bean.TransactionBean;
import nl.springbank.bean.UserBean;
import nl.springbank.exceptions.InvalidParamValueError;
import nl.springbank.exceptions.NotAuthorizedError;
import nl.springbank.objects.BalanceObject;
import nl.springbank.objects.BankAccountAccessObject;
import nl.springbank.objects.TransactionObject;
import nl.springbank.objects.UserAccessObject;
import nl.springbank.services.AccountService;
import nl.springbank.services.TransactionService;
import nl.springbank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Sven Konings
 */
@Service
@AutoJsonRpcServiceImpl
public class InfoControllerImpl implements InfoController {

    private final AccountService accountService;
    private final UserService userService;
    private final TransactionService transactionService;

    @Autowired
    public InfoControllerImpl(AccountService accountService, UserService userService, TransactionService transactionService) {
        this.accountService = accountService;
        this.userService = userService;
        this.transactionService = transactionService;
    }

    @Override
    public BalanceObject getBalance(String authToken, String iBAN) throws InvalidParamValueError, NotAuthorizedError {
        AccountBean account = accountService.getAccount(iBAN);
        userService.checkAccess(account, authToken);
        return new BalanceObject(account);
    }

    @Override
    public List<TransactionObject> getTransactionsOverview(String authToken, String iBAN, int nrOfTransactions) throws InvalidParamValueError, NotAuthorizedError {
        AccountBean account = accountService.getAccount(iBAN);
        userService.checkAccess(account, authToken);
        List<TransactionBean> transactions = transactionService.getTransactionsBySourceOrTargetAccount(account, account);
        return transactions.stream()
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
}
