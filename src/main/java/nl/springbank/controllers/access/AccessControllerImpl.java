package nl.springbank.controllers.access;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import nl.springbank.bean.AccountBean;
import nl.springbank.bean.CardBean;
import nl.springbank.bean.UserBean;
import nl.springbank.exceptions.InvalidParamValueError;
import nl.springbank.exceptions.NoEffectError;
import nl.springbank.exceptions.NotAuthorizedError;
import nl.springbank.objects.OpenedCardObject;
import nl.springbank.services.AccountService;
import nl.springbank.services.CardService;
import nl.springbank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Authentication controller that enables a user to get access and revoke access.
 *
 * @author Tristan de Boer
 * @author Sven Konings
 */
@Service
@AutoJsonRpcServiceImpl
public class AccessControllerImpl implements AccessController {

    private final AccountService accountService;
    private final UserService userService;
    private final CardService cardService;

    @Autowired
    public AccessControllerImpl(AccountService accountService, UserService userService, CardService cardService) {
        this.accountService = accountService;
        this.userService = userService;
        this.cardService = cardService;
    }

    @Override
    public OpenedCardObject provideAccess(String authToken, String iBAN, String username) throws InvalidParamValueError, NotAuthorizedError, NoEffectError {
        AccountBean account = accountService.getCheckingAccount(iBAN);
        userService.checkHolder(account, authToken);
        UserBean user = userService.getUser(username);
        Set<UserBean> accessUsers = account.getAccessUsers();
        if (accessUsers.contains(user)) {
            throw new NoEffectError("User already has access");
        }
        accessUsers.add(user);
        account = accountService.saveAccount(account);
        CardBean card = cardService.newCard(account, user);
        return new OpenedCardObject(card);
    }

    @Override
    public void revokeAccess(String authToken, String iBAN) throws InvalidParamValueError, NotAuthorizedError, NoEffectError {
        AccountBean account = accountService.getAccount(iBAN);
        UserBean user = userService.getUserByAuth(authToken);
        if (account.getHolder().equals(user)) {
            throw new InvalidParamValueError("You can't revoke access to your own account, use close account instead");
        }
        userService.checkAccess(account, user);
        revokeAccess(account, user);
    }

    @Override
    public void revokeAccess(String authToken, String iBAN, String username) throws InvalidParamValueError, NotAuthorizedError, NoEffectError {
        AccountBean account = accountService.getAccount(iBAN);
        UserBean holder = userService.getUserByAuth(authToken);
        userService.checkHolder(account, holder);
        UserBean user = userService.getUser(username);
        if (holder.equals(user)) {
            throw new InvalidParamValueError("You can't revoke access to your own account, use close account instead");
        }
        revokeAccess(account, user);
    }

    private void revokeAccess(AccountBean account, UserBean user) throws NoEffectError {
        Set<UserBean> accessUsers = account.getAccessUsers();
        if (!accessUsers.contains(user)) {
            throw new NoEffectError("The username did not have access");
        }
        accessUsers.remove(user);
        accountService.saveAccount(account);
    }
}
