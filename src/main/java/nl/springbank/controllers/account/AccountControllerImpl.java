package nl.springbank.controllers.account;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import nl.springbank.bean.*;
import nl.springbank.exceptions.InvalidParamValueError;
import nl.springbank.exceptions.NotAuthorizedError;
import nl.springbank.objects.OpenedAccountObject;
import nl.springbank.services.AccountService;
import nl.springbank.services.CardService;
import nl.springbank.services.TransactionService;
import nl.springbank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 * @author Tristan de Boer
 * @author Sven Konings
 */
@Service
@AutoJsonRpcServiceImpl
public class AccountControllerImpl implements AccountController {

    private final UserService userService;
    private final AccountService accountService;
    private final CardService cardService;
    private final TransactionService transactionService;

    @Autowired
    public AccountControllerImpl(UserService userService, AccountService accountService, CardService cardService, TransactionService transactionService) {
        this.userService = userService;
        this.accountService = accountService;
        this.cardService = cardService;
        this.transactionService = transactionService;
    }

    @Override
    public OpenedAccountObject openAccount(String name, String surname, String initials, String dob, String ssn, String address, String telephoneNumber, String email, String username, String password) throws InvalidParamValueError {
        UserBean user;
        try {
            user = userService.newUser(name, surname, initials, dob, ssn, address, telephoneNumber, email, username, password);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidParamValueError(e);
        }
        return openAccount(user);
    }

    @Override
    public OpenedAccountObject openAdditionalAccount(String authToken) throws NotAuthorizedError {
        UserBean user = userService.getUserByAuth(authToken);
        return openAccount(user);
    }

    private OpenedAccountObject openAccount(UserBean user) {
        AccountBean account = accountService.newCheckingAccount(user);
        CardBean card = cardService.newCard(account, user);
        return new OpenedAccountObject(account, card);
    }

    @Override
    public void closeAccount(String authToken, String iBAN) throws InvalidParamValueError, NotAuthorizedError {
        CheckingAccountBean checkingAccount = accountService.getCheckingAccount(iBAN);
        userService.checkHolder(checkingAccount, authToken);
        transactionService.clearAccountTransactions(checkingAccount);
        accountService.closeCheckingAccount(checkingAccount);
        UserBean user = userService.getUserByAuth(authToken);
        if (user.getHolderAccounts().isEmpty()) {
            userService.deleteUser(user);
        }
    }

    @Override
    public void openSavingsAccount(String authToken, String iBAN) throws InvalidParamValueError, NotAuthorizedError {
        CheckingAccountBean checkingAccount = accountService.getCheckingAccount(iBAN);
        userService.checkHolder(checkingAccount, authToken);
        accountService.newSavingsAccount(checkingAccount);
    }

    @Override
    public void closeSavingsAccount(String authToken, String iBAN) throws InvalidParamValueError, NotAuthorizedError {
        SavingsAccountBean savingsAccount = accountService.getSavingsAccount(iBAN);
        userService.checkHolder(savingsAccount, authToken);
        transactionService.newTransaction(savingsAccount, savingsAccount.getCheckingAccount(), "Closed savings account", savingsAccount.getBalance(), "Money left on savings account");
        transactionService.clearAccountTransactions(savingsAccount);
        accountService.closeSavingsAccount(savingsAccount);
    }
}
