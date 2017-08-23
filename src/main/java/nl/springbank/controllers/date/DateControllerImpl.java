package nl.springbank.controllers.date;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import nl.springbank.exceptions.InvalidParamValueError;
import nl.springbank.helper.DateHelper;
import nl.springbank.objects.DateObject;
import nl.springbank.services.TransactionService;
import nl.springbank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Sven Konings
 */
@Service
@AutoJsonRpcServiceImpl
public class DateControllerImpl implements DateController {

    private final UserService userService;
    private final TransactionService transactionService;

    @Autowired

    public DateControllerImpl(UserService userService, TransactionService transactionService) {
        this.userService = userService;
        this.transactionService = transactionService;
    }

    @Override
    public void simulateTime(int nrOfDays) throws InvalidParamValueError {
        DateHelper.addDays(nrOfDays);
    }

    @Override
    public void reset() {
        userService.deleteUsers();
        transactionService.deleteTransactions();
        DateHelper.resetDate();
    }

    @Override
    public DateObject getDate() {
        return new DateObject(DateHelper.getTime());
    }
}
