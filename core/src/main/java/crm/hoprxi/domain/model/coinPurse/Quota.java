package crm.hoprxi.domain.model.coinPurse;


import org.javamoney.moneta.FastMoney;

import javax.money.MonetaryAmount;
import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-11-15
 */
public enum Quota {
    ZERO(0) {
        @Override
        public Round round(MonetaryAmount receivables, MonetaryAmount balance) {
            return Round.ZERO;
        }
    },

    ONE(1), FIVE(5);

    protected int factor;

    Quota(int factor) {
        this.factor = factor;
    }

    public Round round(MonetaryAmount receivables, MonetaryAmount balance) {
        Objects.requireNonNull(receivables, "receivables required");
        if (receivables.isNegative())
            throw new IllegalArgumentException("receivables must larger zero");
        MonetaryAmount integer = receivables.divideToIntegralValue(factor);
        MonetaryAmount remainder = receivables.remainder(factor);
        if (balance.isGreaterThanOrEqualTo(remainder))
            return new Round(integer.multiply(factor), remainder.negate());
        return new Round(integer.add(FastMoney.of(1, balance.getCurrency())).multiply(factor), FastMoney.of(factor, balance.getCurrency()).subtract(remainder));
    }

    public int factor() {
        return factor;
    }
}
