import model.Person;
import model.Transaction;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import service.TransactionValidationService;

@RunWith(SpringRunner.class)
public class TransactionValidationServiceTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private TransactionValidationService transactionValidationService = new TransactionValidationService();

    private Transaction transaction;

    private Person payer;

    private Person payee;

    @Before
    public void init() {
        transaction = new Transaction();
        payer = new Person();
        payee = new Person();
    }

    @Test
    public void test_invalidName() throws Exception {
        exceptionRule.expectMessage("Payer's name cannot be empty!");
        transaction.setPayer(payer);
        transactionValidationService.sendTransactionToPersistence(transaction);
    }

    @Test
    public void test_validateDuplications_duplicateCnp() throws Exception {
        exceptionRule.expectMessage("Payer and Payee cannot have the same CNP!");
        payer.setName("payer");
        payer.setCnp(6010828133126L);
        payee.setName("payee");
        payee.setCnp(6010828133126L);
        transaction.setPayer(payer);
        transaction.setPayee(payee);
        transactionValidationService.sendTransactionToPersistence(transaction);
    }

    @Test
    public void test_validateDuplications_duplicateIban() throws Exception {
        exceptionRule.expectMessage("Payer and Payee cannot have the same IBAN!");
        payer.setName("payer");
        payer.setIban("RO19RZBR8637876275195736");
        payer.setCnp(6010828133126L);
        payee.setName("payee");
        payee.setIban("RO19RZBR8637876275195736");
        payee.setCnp(5010117527057L);
        transaction.setPayer(payer);
        transaction.setPayee(payee);
        transactionValidationService.sendTransactionToPersistence(transaction);
    }

    @Test
    public void test_validateIban_invalidIban() throws Exception {
        exceptionRule.expectMessage("[RO19RZBR8643546556764E43] has invalid check digit: 19, expected check digit is: 08");
        payer.setName("payer");
        payer.setIban("RO19RZBR8643546556764E43");
        payer.setCnp(1900117385586L);
        payee.setName("payee");
        payee.setIban("RO19RZBR8637876275195736");
        payee.setCnp(1980914166775L);
        transaction.setPayer(payer);
        transaction.setPayee(payee);
        transactionValidationService.sendTransactionToPersistence(transaction);
    }
}
