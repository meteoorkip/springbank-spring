package nl.springbank.controllers.overdraft;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import nl.springbank.bean.AccountBean;
import nl.springbank.exceptions.InvalidParamValueError;
import nl.springbank.exceptions.NotAuthorizedError;
import nl.springbank.objects.OverdraftLimitObject;
import nl.springbank.services.AccountService;
import nl.springbank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Sven Konings
 */
@Service
@AutoJsonRpcServiceImpl
public class OverdraftControllerImpl implements OverdraftController {

    private final AccountService accountService;
    private final UserService userService;

    @Autowired
    public OverdraftControllerImpl(AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    @Override
    public void setOverdraftLimit(String authToken, String iBAN, int overdraftLimit) throws InvalidParamValueError, NotAuthorizedError {
        AccountBean account = accountService.getCheckingAccount(iBAN);
        userService.checkAccess(account, authToken);
        accountService.setOverdraftLimit(account, overdraftLimit);
    }

    @Override
    public OverdraftLimitObject getOverdraftLimit(String authToken, String iBAN) throws InvalidParamValueError, NotAuthorizedError {
        AccountBean account = accountService.getAccount(iBAN);
        userService.checkAccess(account, authToken);
        return new OverdraftLimitObject(account);
    }
}
