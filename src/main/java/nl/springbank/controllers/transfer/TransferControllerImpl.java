package nl.springbank.controllers.transfer;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import nl.springbank.bean.AccountBean;
import nl.springbank.exceptions.InvalidPINError;
import nl.springbank.exceptions.InvalidParamValueError;
import nl.springbank.exceptions.NotAuthorizedError;
import nl.springbank.helper.UserHelper;
import nl.springbank.services.AccountService;
import nl.springbank.services.CardService;
import nl.springbank.services.TransactionService;
import nl.springbank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Sven Konings
 */
@Service
@AutoJsonRpcServiceImpl
public class TransferControllerImpl implements TransferController {

    private final AccountService accountService;
    private final UserService userService;
    private final CardService cardService;
    private final TransactionService transactionService;

    @Autowired
    public TransferControllerImpl(AccountService accountService, UserService userService, CardService cardService, TransactionService transactionService) {
        this.accountService = accountService;
        this.userService = userService;
        this.cardService = cardService;
        this.transactionService = transactionService;
    }

    @Override
    public void depositIntoAccount(String iBAN, String pinCard, String pinCode, double amount) throws InvalidParamValueError, InvalidPINError {
        AccountBean bankAccount = accountService.getCheckingAccount(iBAN);
        cardService.checkPin(bankAccount, pinCard, pinCode);
        transactionService.newDeposit(bankAccount, "ATM deposit", amount, "Deposited money through an ATM");
    }

    @Override
    public void payFromAccount(String sourceIBAN, String targetIBAN, String pinCard, String pinCode, double amount) throws InvalidParamValueError, InvalidPINError {
        AccountBean sourceAccount = accountService.getCheckingAccount(sourceIBAN);
        cardService.checkPin(sourceAccount, pinCard, pinCode);
        AccountBean targetAccount = accountService.getAccount(targetIBAN);
        String targetName = UserHelper.getDisplayName(targetAccount.getHolder());
        transactionService.newTransaction(sourceAccount, targetAccount, targetName, amount, "PIN transaction");
    }

    @Override
    public void transferMoney(String authToken, String sourceIBAN, String targetIBAN, String targetName, double amount, String description) throws InvalidParamValueError, NotAuthorizedError {
        AccountBean sourceAccount = accountService.getAccount(sourceIBAN);
        userService.checkAccess(sourceAccount, authToken);
        AccountBean targetAccount = accountService.getAccount(targetIBAN);
        transactionService.newTransaction(sourceAccount, targetAccount, targetName, amount, description);
    }
}
