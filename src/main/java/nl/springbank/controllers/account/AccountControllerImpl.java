package nl.springbank.controllers.account;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import nl.springbank.bean.AccountBean;
import nl.springbank.bean.CardBean;
import nl.springbank.bean.UserBean;
import nl.springbank.exceptions.InvalidParamValueError;
import nl.springbank.exceptions.NotAuthorizedError;
import nl.springbank.objects.OpenedAccountObject;
import nl.springbank.services.AccountService;
import nl.springbank.services.CardService;
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

    @Autowired
    public AccountControllerImpl(UserService userService, AccountService accountService, CardService cardService) {
        this.userService = userService;
        this.accountService = accountService;
        this.cardService = cardService;
    }

    @Override
    public OpenedAccountObject openAccount(String name, String surname, String initials, String dob, String ssn, String address, String telephoneNumber, String email, String username, String password) throws InvalidParamValueError {
        UserBean user = userService.newUser(name, surname, initials, dob, ssn, address, telephoneNumber, email, username, password);
        try {
            return openAccount(user);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidParamValueError(e);
        }
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
        AccountBean account = accountService.getAccount(iBAN);
        UserBean user = userService.getUserByAuth(authToken);
        userService.checkHolder(account, user);
        accountService.closeCheckingAccount(account);
        user = userService.getUserByAuth(authToken); // refresh user bean
        if (user.getHolderAccounts().isEmpty()) {
            userService.deleteUser(user);
        }
    }
}
