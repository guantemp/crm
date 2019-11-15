package crm.hoprxi.domain.model.coinPurse;


import javax.money.MonetaryAmount;
import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-11-15
 */
public enum Quota {
    ONE_YUAN(1) {
        @Override
        public Round round(MonetaryAmount receivables, MonetaryAmount balance) {
            Objects.requireNonNull(receivables, "receivables required");
            if (receivables.isNegative())
                throw new IllegalArgumentException("receivables must larger zero");
            MonetaryAmount remainder = receivables.remainder(divisor);
            if (balance.isGreaterThanOrEqualTo(remainder))
                return new Round(receivables.divideToIntegralValue(divisor), remainder.negate());
            return new Round(receivables.divideToIntegralValue(divisor), remainder);
        }
    }, FIVE_YUAN(5) {
        @Override
        public Round round(MonetaryAmount receivables, MonetaryAmount balance) {
            Objects.requireNonNull(receivables, "receivables required");
            if (receivables.isNegative())
                throw new IllegalArgumentException("receivables must larger zero");
            MonetaryAmount remainder = receivables.remainder(divisor);
            if (balance.isGreaterThanOrEqualTo(remainder))
                return new Round(receivables.divideToIntegralValue(divisor), balance.subtract(remainder));
            return new Round(receivables.divideToIntegralValue(divisor), remainder);
        }
    };

    protected int divisor;

    Quota(int divisor) {
        this.divisor = divisor;
    }

    public Round round(MonetaryAmount receivables, MonetaryAmount balance) {
        return Round.ZERO;
    }
    //CoinPurse access(MonetaryAmount receivables, MonetaryAmount balance);
}
