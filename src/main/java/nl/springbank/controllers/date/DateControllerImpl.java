package nl.springbank.controllers.date;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import nl.springbank.bean.CheckingAccountBean;
import nl.springbank.bean.SavingsAccountBean;
import nl.springbank.exceptions.InvalidParamValueError;
import nl.springbank.helper.DateHelper;
import nl.springbank.objects.DateObject;
import nl.springbank.services.AccountService;
import nl.springbank.services.TransactionService;
import nl.springbank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

import static java.util.Calendar.*;

/**
 * @author Sven Konings
 */
@Service
@AutoJsonRpcServiceImpl
public class DateControllerImpl implements DateController {

    private static final double MONTHLY_CHECKING_RATE = 0.00797414043;
    private static final double YEARLY_SAVINGS_RATE_LOW = 0.15;
    private static final double YEARLY_SAVINGS_RATE_MED = 0.15;
    private static final double YEARLY_SAVINGS_RATE_HIGH = 0.20;

    private final UserService userService;
    private final AccountService accountService;
    private final TransactionService transactionService;

    @Autowired
    public DateControllerImpl(UserService userService, AccountService accountService, TransactionService transactionService) {
        this.userService = userService;
        this.accountService = accountService;
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
        calendar.add(DATE, 1);
        if (calendar.get(DAY_OF_YEAR) == 1) {
            processSavingsAccountInterest();
        }
        if (calendar.get(DAY_OF_MONTH) == 1) {
            processCheckingAccountInterest();
        }
        calculateDailyCheckingAccountInterest(calendar);
        calculateDailySavingsAccountInterest(calendar);
    }

    private void processCheckingAccountInterest() throws InvalidParamValueError {
        List<CheckingAccountBean> checkingAccounts = accountService.getCheckingAccounts();
        for (CheckingAccountBean checkingAccount : checkingAccounts) {
            transactionService.newWithdrawal(checkingAccount, "Interest", checkingAccount.getInterest(), "Payed interest because of negative balance");
            checkingAccount.setInterest(0);
        }
        accountService.saveCheckingAccounts(checkingAccounts);
    }

    private void processSavingsAccountInterest() throws InvalidParamValueError {
        List<SavingsAccountBean> savingsAccounts = accountService.getSavingsAccounts();
        for (SavingsAccountBean savingsAccount : savingsAccounts) {
            transactionService.newDeposit(savingsAccount, "Interest", savingsAccount.getInterest(), "Received interest because of savings");
            savingsAccount.setInterest(0);
        }
        accountService.saveSavingsAccounts(savingsAccounts);
    }

    private void calculateDailyCheckingAccountInterest(Calendar calendar) {
        double dailyCheckingRate = MONTHLY_CHECKING_RATE / calendar.getActualMaximum(DAY_OF_MONTH);
        List<CheckingAccountBean> checkingAccounts = accountService.getCheckingAccounts();
        for (CheckingAccountBean checkingAccount : checkingAccounts) {
            if (checkingAccount.getMinimumBalance() < 0) {
                double dailyInterest = Math.abs(checkingAccount.getMinimumBalance()) * dailyCheckingRate;
                checkingAccount.setInterest(checkingAccount.getInterest() + dailyInterest);
            }
            checkingAccount.setMinimumBalance(checkingAccount.getBalance());
        }
        accountService.saveCheckingAccounts(checkingAccounts);
    }

    private void calculateDailySavingsAccountInterest(Calendar calendar) {
        int daysOfYear = calendar.getActualMaximum(DAY_OF_YEAR);
        double dailySavingsRateLow = YEARLY_SAVINGS_RATE_LOW / daysOfYear;
        double dailySavingsRateMed = YEARLY_SAVINGS_RATE_MED / daysOfYear;
        double dailySavingsRateHigh = YEARLY_SAVINGS_RATE_HIGH / daysOfYear;
        List<SavingsAccountBean> savingsAccounts = accountService.getSavingsAccounts();
        for (SavingsAccountBean savingsAccount : savingsAccounts) {
            double dailyInterest = 0;
            double minimumBalance = savingsAccount.getMinimumBalance();
            if (minimumBalance < 25000) {
                dailyInterest = minimumBalance * dailySavingsRateLow;
            } else if (minimumBalance < 75000) {
                dailyInterest = minimumBalance * dailySavingsRateMed;
            } else if (minimumBalance < 1000000) {
                dailyInterest = minimumBalance * dailySavingsRateHigh;
            }
            savingsAccount.setInterest(savingsAccount.getInterest() + dailyInterest);
            savingsAccount.setMinimumBalance(savingsAccount.getBalance());
        }
        accountService.saveSavingsAccounts(savingsAccounts);
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
