package crm.hoprxi.card;

import crm.hoprxi.domain.model.card.CreditCard;
import crm.hoprxi.domain.model.card.InsufficientBalanceException;
import org.javamoney.moneta.Money;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.money.MonetaryAmount;
import java.util.Locale;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-08-15
 */
public class CreditCardTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    Locale locale = Locale.getDefault();
    MonetaryAmount zero = Money.of(0, "CNY");

    @Test
    public void all() {
        CreditCard card = new CreditCard("001", "32", "214", "136796", Money.of(1500, "CNY"), zero);
        card.pay(Money.of(500, "CNY"));
        Assert.assertTrue(Money.of(-500, "CNY").isEqualTo(card.balance()));
        card.pay(Money.of(1000, "CNY"));
        Assert.assertTrue(Money.of(-1500, "CNY").isEqualTo(card.balance()));
        card.pay(zero);
        Assert.assertTrue(Money.of(-1500, "CNY").isEqualTo(card.balance()));
        card.pay(Money.of(-500, "CNY"));
        Assert.assertTrue(Money.of(-1500, "CNY").isEqualTo(card.balance()));

        card.quotaAdjustTo(Money.of(2000, "CNY"));
        Assert.assertTrue(Money.of(2000, "CNY").isEqualTo(card.quota()));
        card.quotaAdjustTo(Money.of(-1, "CNY"));
        Assert.assertTrue(Money.of(2000, "CNY").isEqualTo(card.quota()));

        card.pay(Money.of(300, "CNY"));
        Assert.assertTrue(Money.of(-1800, "CNY").isEqualTo(card.balance()));

        card.repayment(Money.of(900, "CNY"));
        Assert.assertTrue(Money.of(-900, "CNY").isEqualTo(card.balance()));
        card.repayment(Money.of(1000, "CNY"));
        Assert.assertTrue(Money.of(100, "CNY").isEqualTo(card.balance()));

        card.quotaAdjustTo(Money.of(0, "CNY"));
        Assert.assertTrue(Money.of(0, "CNY").isEqualTo(card.quota()));

        thrown.expect(InsufficientBalanceException.class);
        //thrown.expectMessage("\"Insufficient available quota\"");
        card.pay(Money.of(0.01, "CNY"));
    }
}