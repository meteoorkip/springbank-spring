package nl.springbank.controllers.overdraft;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import nl.springbank.bean.BankAccountBean;
import nl.springbank.exceptions.InvalidParamValueError;
import nl.springbank.exceptions.NotAuthorizedError;
import nl.springbank.objects.OverdraftObject;
import nl.springbank.services.BankAccountService;
import nl.springbank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Sven Konings
 */
@Service
@AutoJsonRpcServiceImpl
public class OverdraftControllerImpl implements OverdraftController {

    private final BankAccountService bankAccountService;
    private final UserService userService;

    @Autowired
    public OverdraftControllerImpl(BankAccountService bankAccountService, UserService userService) {
        this.bankAccountService = bankAccountService;
        this.userService = userService;
    }

    @Override
    public void setOverdraftLimit(String authToken, String iBAN, int overdraftLimit) throws InvalidParamValueError, NotAuthorizedError {
        BankAccountBean bankAccount = bankAccountService.getBankAccount(iBAN);
        userService.checkAccess(bankAccount, authToken);
        bankAccountService.setOverdraftLimit(bankAccount, overdraftLimit);
    }

    @Override
    public OverdraftObject getOverdraftLimit(String authToken, String iBAN) throws InvalidParamValueError, NotAuthorizedError {
        BankAccountBean bankAccount = bankAccountService.getBankAccount(iBAN);
        userService.checkAccess(bankAccount, authToken);
        return new OverdraftObject(bankAccount);
    }


}
