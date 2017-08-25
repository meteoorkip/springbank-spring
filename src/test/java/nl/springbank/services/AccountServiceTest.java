package nl.springbank.services;

import nl.springbank.bean.CheckingAccountBean;
import nl.springbank.bean.UserBean;
import nl.springbank.exceptions.InvalidParamValueError;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.*;


/**
 * Tests BankAccountService
 * <p>
 * Service is tested on profile 'test', which means 'application-test.properties' is used as configuration.
 * A test is annotated Transactional if changes are made to the database. This ensures a rollback.
 *
 * @author Tristan de Boer
 * @author Sven Konings
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class BankAccountServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private BankAccountService bankAccountService;

    @Test
    public void testGetBankAccount() throws Exception {
        assertNotNull(bankAccountService.getBankAccount("NL83SPRI0114480386"));
    }

    @Test(expected = InvalidParamValueError.class)
    public void testGetInvalidBankAccount() throws Exception {
        bankAccountService.getBankAccount("NL58INGB8290060130");
    }

    @Test
    public void testNewBankAccount() throws Exception {
        UserBean user = userService.getUser(1);
        CheckingAccountBean bankAccount = bankAccountService.newBankAccount(user);
        assertNotNull(bankAccount);
        assertEquals(bankAccount.getHolder(), user);
        assertEquals(bankAccount.getBalance(), 0, 0);
        assertNull(bankAccount.getIban());
        assertEquals(bankAccount.getAccessUsers().size(), 1);
        assertTrue(bankAccount.getAccessUsers().contains(user));
        assertTrue(bankAccount.getCards().isEmpty());
        assertTrue(bankAccount.getSourceTransactions().isEmpty());
        assertTrue(bankAccount.getTargetTransactions().isEmpty());
        assertEquals(bankAccount, bankAccountService.getBankAccount(bankAccount.getBankAccountId()));
    }

    @Test
    public void testDeleteBankAccount() throws Exception {
        CheckingAccountBean bankAccount = bankAccountService.getBankAccount("NL83SPRI0114480386");
        bankAccountService.deleteBankAccount(bankAccount);
        try {
            bankAccountService.getBankAccount("NL83SPRI0114480386");
            fail();
        } catch (InvalidParamValueError ignored) {
        }
    }
}
