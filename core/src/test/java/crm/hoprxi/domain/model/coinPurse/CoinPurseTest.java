package crm.hoprxi.domain.model.coinPurse;

import org.javamoney.moneta.Money;
import org.junit.Assert;
import org.junit.Test;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-11-18
 */
public class CoinPurseTest {

    @Test
    public void balance() {
        CoinPurse coinPurse = new CoinPurse(Money.of(0.55, "CNY"), Quota.ONE);
        Round round = coinPurse.round(Money.of(5.25, "CNY"));
        Assert.assertTrue(Money.of(5, "CNY").isEqualTo(round.integer()));
        Assert.assertTrue(Money.of(-0.25, "CNY").isEqualTo(round.remainder()));
        round = coinPurse.round(Money.of(4.75, "CNY"));
        Assert.assertTrue(Money.of(5, "CNY").isEqualTo(round.integer()));
        Assert.assertTrue(Money.of(0.25, "CNY").isEqualTo(round.remainder()));

        coinPurse = new CoinPurse(Money.of(4.55, "CNY"), Quota.FIVE);
        round = coinPurse.round(Money.of(4.75, "CNY"));
        Assert.assertTrue(Money.of(5, "CNY").isEqualTo(round.integer()));
        Assert.assertTrue(Money.of(0.25, "CNY").isEqualTo(round.remainder()));

        round = coinPurse.round(Money.of(6.73, "CNY"));
        Assert.assertTrue(Money.of(5, "CNY").isEqualTo(round.integer()));
        Assert.assertTrue(Money.of(-1.73, "CNY").isEqualTo(round.remainder()));

        round = coinPurse.round(Money.of(3.28, "CNY"));
        Assert.assertTrue(Money.of(0, "CNY").isEqualTo(round.integer()));
        Assert.assertTrue(Money.of(-3.28, "CNY").isEqualTo(round.remainder()));

        coinPurse = new CoinPurse(Money.of(0, "CNY"), Quota.ZERO);
        round = coinPurse.round(Money.of(3.28, "CNY"));
        Assert.assertTrue(round.remainder().isZero());
        Assert.assertTrue(round.integer().isZero());
        round = coinPurse.round(Money.of(7.89, "CNY"));
        Assert.assertTrue(round.remainder().isZero());
        Assert.assertTrue(round.integer().isZero());
    }
}