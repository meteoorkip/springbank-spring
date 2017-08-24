package nl.springbank.controllers.card;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import nl.springbank.bean.BankAccountBean;
import nl.springbank.bean.CardBean;
import nl.springbank.exceptions.InvalidParamValueError;
import nl.springbank.exceptions.NoEffectError;
import nl.springbank.exceptions.NotAuthorizedError;
import nl.springbank.objects.OpenedCardObject;
import nl.springbank.services.BankAccountService;
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
public class CardControllerImpl implements CardController {

    private final UserService userService;
    private final BankAccountService bankAccountService;
    private final CardService cardService;
    private final TransactionService transactionService;

    @Autowired
    public CardControllerImpl(UserService userService, BankAccountService bankAccountService, CardService cardService, TransactionService transactionService) {
        this.userService = userService;
        this.bankAccountService = bankAccountService;
        this.cardService = cardService;
        this.transactionService = transactionService;
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

    @Override
    public void unblockCard(String authToken, String iBAN, String pinCard) throws InvalidParamValueError, NotAuthorizedError, NoEffectError {
        BankAccountBean bankAccount = bankAccountService.getBankAccount(iBAN);
        CardBean card = cardService.getCard(bankAccount, pinCard);
        userService.checkCardOwner(card, authToken);
        cardService.unblockCard(card);
    }
}
