package nl.springbank.controllers.account;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import nl.springbank.bean.BankAccountBean;
import nl.springbank.bean.CardBean;
import nl.springbank.bean.IbanBean;
import nl.springbank.bean.UserBean;
import nl.springbank.exceptions.InvalidParamValueError;
import nl.springbank.exceptions.NotAuthorizedError;
import nl.springbank.objects.OpenedAccountObject;
import nl.springbank.objects.OpenedCardObject;
import nl.springbank.services.*;
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
    private final BankAccountService bankAccountService;
    private final IbanService ibanService;
    private final CardService cardService;
    private final TransactionService transactionService;

    @Autowired
    public AccountControllerImpl(UserService userService, BankAccountService bankAccountService, IbanService ibanService, CardService cardService, TransactionService transactionService) {
        this.userService = userService;
        this.bankAccountService = bankAccountService;
        this.ibanService = ibanService;
        this.cardService = cardService;
        this.transactionService = transactionService;
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
        BankAccountBean bankAccount = bankAccountService.newBankAccount(user);
        IbanBean iban = ibanService.newIban(bankAccount);
        CardBean card = cardService.newCard(bankAccount, user);
        return new OpenedAccountObject(iban, card);
    }

    @Override
    public void closeAccount(String authToken, String iBAN) throws InvalidParamValueError, NotAuthorizedError {
        UserBean user = userService.getUserByAuth(authToken);
        BankAccountBean bankAccount = bankAccountService.getBankAccount(iBAN);
        userService.checkHolder(bankAccount, user);
        bankAccountService.deleteBankAccount(bankAccount);
        user = userService.getUserByAuth(authToken);
        if (user.getHolderAccounts().isEmpty()) {
            userService.deleteUser(user);
        }
    }

    @Override
    public OpenedCardObject invalidateCard(String authToken, String iBAN, String pinCard, boolean newPin) throws InvalidParamValueError, NotAuthorizedError {
        BankAccountBean bankAccount = bankAccountService.getBankAccount(iBAN);
        CardBean card = cardService.getCard(bankAccount, pinCard);
        userService.checkCardOwner(card, authToken);
        transactionService.newWithdrawal(bankAccount, 7.5);
        CardBean newCard = cardService.invalidateCard(card, newPin);
        return new OpenedCardObject(newCard, newPin);
    }
}
