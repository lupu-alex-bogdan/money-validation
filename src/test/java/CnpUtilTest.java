import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import util.CnpUtil;

@RunWith(SpringRunner.class)
public class CnpUtilTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void test_invalidLength() {
        exceptionRule.expectMessage("CNP must have exactly 13 digits!");
        CnpUtil.validate("123");
    }

    @Test
    public void test_invalidBirthDate() {
        exceptionRule.expectMessage("CNP has invalid birth date!");
        CnpUtil.validate("1901312386785");
    }

    @Test
    public void test_invalidGender_before2000mismatch() {
        exceptionRule.expectMessage("Gender is incorrect!");
        CnpUtil.validate("5901212386785");
    }

    @Test
    public void test_invalidGender_invalidValue() {
        exceptionRule.expectMessage("Gender is incorrect!");
        CnpUtil.validate("3901212386785");
    }

    @Test
    public void test_invalidCounty() {
        exceptionRule.expectMessage("County code is incorrect!");
        CnpUtil.validate("1901212536785");
    }

    @Test
    public void test_invalidControlDigit() {
        exceptionRule.expectMessage("Control digit (last) is invalid!");
        CnpUtil.validate("1901212526784");
    }
}
