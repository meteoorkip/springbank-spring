package nl.springbank.bean;

import org.junit.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertThat;

public class BeanTest {
    @Test
    public void testBankAccountBean() {
        testBean(CheckingAccountBean.class);
    }

    @Test
    public void testCardBean() {
        testBean(CardBean.class);
    }

    @Test
    public void testTransactionBean() {
        testBean(TransactionBean.class);
    }

    @Test
    public void testUserBean() {
        testBean(UserBean.class);
    }

    private void testBean(Class<?> beanClass) {
        assertThat(beanClass, allOf(
                hasValidBeanConstructor(),
                hasValidGettersAndSetters()
        ));
    }
}
