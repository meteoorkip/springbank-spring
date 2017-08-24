package nl.springbank.controllers.date;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import nl.springbank.bean.BankAccountBean;
import nl.springbank.exceptions.InvalidParamValueError;
import nl.springbank.helper.DateHelper;
import nl.springbank.objects.DateObject;
import nl.springbank.services.BankAccountService;
import nl.springbank.services.TransactionService;
import nl.springbank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

/**
 * @author Sven Konings
 */
@Service
@AutoJsonRpcServiceImpl
public class DateControllerImpl implements DateController {

    private static final double MONTHLY_RATE = 0.00797414043;

    private final UserService userService;
    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;

    @Autowired
    public DateControllerImpl(UserService userService, BankAccountService bankAccountService, TransactionService transactionService) {
        this.userService = userService;
        this.bankAccountService = bankAccountService;
        this.transactionService = transactionService;
    }

    @Override
    public void simulateTime(int nrOfDays) throws InvalidParamValueError {
        if (nrOfDays < 0) {
            throw new InvalidParamValueError("The number of days can't be negative");
        }
        for (int i = 0; i < nrOfDays; i++) {
            simulateDay();
        }
    }

    private void simulateDay() throws InvalidParamValueError {
        Calendar calendar = DateHelper.getSystemCalendar();
        calendar.add(Calendar.DATE, 1);
        if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
            for (BankAccountBean bankAccount : bankAccountService.getBankAccounts()) {
                transactionService.newWithdrawal(bankAccount, bankAccount.getInterest());
                bankAccountService.resetInterest(bankAccount);
            }
        }
        double dailyRate = MONTHLY_RATE / calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        bankAccountService.addDailyInterest(dailyRate);
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
