package crm.hoprxi.domain.model.coinPurse;

import org.javamoney.moneta.Money;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.util.Locale;
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-11-15
 */
public class Round {
    private static final MonetaryAmount MONETARY_ZERO = Money.zero(Monetary.getCurrency(Locale.getDefault()));
    public static Round ZERO = new Round(MONETARY_ZERO, MONETARY_ZERO);
    private MonetaryAmount integer;
    private MonetaryAmount decimal;

    public Round(MonetaryAmount integer, MonetaryAmount decimal) {
        setInteger(integer);
        setDecimal(decimal);
    }

    private void setDecimal(MonetaryAmount decimal) {
        this.decimal = decimal;
    }

    private void setInteger(MonetaryAmount integer) {
        if (integer.isNegative())
            throw new IllegalArgumentException("");
        MonetaryAmount remainder = integer.remainder(1);
        if (!remainder.isZero())
            throw new IllegalArgumentException("");
        this.integer = integer;
    }

    public MonetaryAmount neat() {
        return integer;
    }

    public MonetaryAmount fragment() {
        return decimal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Round round = (Round) o;

        if (integer != null ? !integer.equals(round.integer) : round.integer != null) return false;
        return decimal != null ? decimal.equals(round.decimal) : round.decimal == null;
    }

    @Override
    public int hashCode() {
        int result = integer != null ? integer.hashCode() : 0;
        result = 31 * result + (decimal != null ? decimal.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Round.class.getSimpleName() + "[", "]")
                .add("neat=" + integer)
                .add("fragment=" + decimal)
                .toString();
    }
}
