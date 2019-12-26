/*
 * Copyright (c) 2019. www.hoprxi.com All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package crm.hoprxi.domain.model.balance;

import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-11-20
 */
public class Balance {
    private static final Balance RMB_ZERO = new Balance(Money.zero(Monetary.getCurrency(Locale.CHINA)), Money.zero(Monetary.getCurrency(Locale.CHINA)));
    private static final Balance USD_ZERO = new Balance(Money.zero(Monetary.getCurrency(Locale.US)), Money.zero(Monetary.getCurrency(Locale.US)));
    private MonetaryAmount valuable;
    private MonetaryAmount give;

    /**
     * @param valuable
     * @param give     required positive
     */
    public Balance(MonetaryAmount valuable, MonetaryAmount give) {
        setValuable(valuable);
        setGive(give);
    }

    /**
     * @param valuable
     * @param give
     * @return
     */
    public static Balance getInstance(MonetaryAmount valuable, MonetaryAmount give) {
        if (valuable.isZero() && give.isZero())
            return zero(valuable.getCurrency());
        return new Balance(valuable, give);
    }

    /**
     * @param locale
     * @return
     */
    public static Balance zero(Locale locale) {
        if (locale == Locale.CHINA || locale == Locale.CHINESE || locale == Locale.SIMPLIFIED_CHINESE || locale == Locale.PRC)
            return RMB_ZERO;
        if (locale == Locale.US)
            return USD_ZERO;
        MonetaryAmount zero = Money.zero(Monetary.getCurrency(locale));
        return new Balance(zero, zero);
    }

    public static Balance zero(CurrencyUnit currencyUnit) {
        if (currencyUnit.getNumericCode() == 156)
            return RMB_ZERO;
        if (currencyUnit.getNumericCode() == 840)
            return USD_ZERO;
        MonetaryAmount zero = Money.zero(currencyUnit);
        return new Balance(zero, zero);
    }

    private void setValuable(MonetaryAmount valuable) {
        Objects.requireNonNull(valuable, "valuable required");
        this.valuable = valuable;
    }

    private void setGive(MonetaryAmount give) {
        if (give == null || give.isNegative())
            throw new IllegalArgumentException("give required positive");
        if (!give.getCurrency().equals(valuable.getCurrency()))
            throw new IllegalArgumentException("give currency required equal valuable currency");
        this.give = give;
    }

    public MonetaryAmount valuable() {
        return valuable;
    }

    public MonetaryAmount give() {
        return give;
    }

    public MonetaryAmount total() {
        return valuable.add(give);
    }

    /**
     * @param valuable
     * @param give     must is positive
     */
    public Balance deposit(MonetaryAmount valuable, MonetaryAmount give) {
        if (valuable == null && (give == null || give.isNegativeOrZero()))
            return this;
        if (valuable == null)
            valuable = Money.zero(this.valuable.getCurrency());
        if (give == null)
            give = Money.zero(this.give.getCurrency());
        return new Balance(this.valuable.add(valuable), this.give.add(give));
    }

    /**
     * @param valuable
     * @return
     */
    public Balance deposit(MonetaryAmount valuable) {
        return deposit(valuable, Money.zero(this.valuable.getCurrency()));
    }


    Balance pay(MonetaryAmount amount, PaymentStrategy strategy) {
        MonetaryAmount available = valuable.add(give);
        if (available.isLessThan(amount))
            throw new InsufficientBalanceException("insufficient balance");
        switch (strategy) {
            case BALANCE_FIRST:
                valuable = valuable.subtract(amount);
                if (valuable.isNegative()) {
                    valuable = Money.zero(valuable.getCurrency());
                    give = give.add(valuable);
                }
                break;
            case RED_ENVELOPES_FIRST:
                MonetaryAmount g = give.subtract(amount);
                if (g.isNegative()) {
                    give = Money.zero(give.getCurrency());
                    valuable = valuable.add(g);
                } else {
                    give = g;
                }
                break;
            case RATIO:
                double d1 = valuable.getNumber().doubleValue();
                double d2 = give.getNumber().doubleValue();
                double d3 = d2 / (d1 + d2);
                MonetaryAmount temp = amount.multiply(d3);
                valuable = valuable.subtract(amount.subtract(temp));
                give = give.subtract(temp);
                break;
        }
        return new Balance(valuable, give);
    }

    public Balance pay(MonetaryAmount amount) {
        return pay(amount, 5);
    }

    /**
     * @param amount
     * @param scale
     * @return
     */
    public Balance pay(MonetaryAmount amount, int scale) {
        MonetaryAmount available = valuable.add(give);
        if (available.isLessThan(amount))
            throw new InsufficientBalanceException("insufficient balance");
        if (amount.isEqualTo(available))
            return zero(valuable.getCurrency());
/*
        BigDecimal big1 = valuable.getNumber().numberValue(BigDecimal.class);
        BigDecimal big2 = give.getNumber().numberValue(BigDecimal.class);
        BigDecimal big3 = big1.add(big2);
        System.out.println(big1+" "+big2+" "+big3);
        System.out.println(big2.divide(big3,20, BigDecimal.ROUND_HALF_EVEN));
 */
        double d1 = valuable.getNumber().doubleValue();
        double d2 = give.getNumber().doubleValue();
        double d3 = d2 / (d1 + d2);
        MonetaryAmount temp = amount.multiply(BigDecimal.valueOf(d3).setScale(scale, BigDecimal.ROUND_HALF_EVEN));
        return new Balance(valuable.subtract(amount.subtract(temp)), give.subtract(temp));
    }

    /**
     * @param amount
     * @return
     */
    public Balance overdraw(MonetaryAmount amount) {
        Objects.requireNonNull(amount, "amount required");
        if (valuable.isNegativeOrZero() && amount.isLessThanOrEqualTo(give))
            return new Balance(valuable, give.subtract(amount));
        MonetaryAmount total = valuable.add(give);
        if (total.isGreaterThanOrEqualTo(amount))
            return pay(amount);
        MonetaryAmount surplus = amount.subtract(give);
        return new Balance(valuable.subtract(surplus), Money.zero(give.getCurrency()));
    }


    /**
     * @param amount
     * @return
     */
    public Balance withdrawal(MonetaryAmount amount) {
        Objects.requireNonNull(amount, "amount required");
        if (valuable.isLessThan(amount))
            throw new InsufficientBalanceException("insufficient balance");
        return new Balance(valuable.subtract(amount), give);
    }

    public boolean isZero() {
        return valuable.isZero() && give.isZero();
    }

    /**
     * @param balance
     * @return
     */
    public Balance subtract(Balance balance) {
        return new Balance(valuable.subtract(balance.valuable), give.subtract(balance.give));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Balance balance = (Balance) o;

        if (valuable != null ? !valuable.equals(balance.valuable) : balance.valuable != null) return false;
        return give != null ? give.equals(balance.give) : balance.give == null;
    }

    @Override
    public int hashCode() {
        int result = valuable != null ? valuable.hashCode() : 0;
        result = 31 * result + (give != null ? give.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Balance.class.getSimpleName() + "[", "]")
                .add("valuable=" + valuable)
                .add("give=" + give)
                .toString();
    }

    enum PaymentStrategy {
        BALANCE_FIRST, RED_ENVELOPES_FIRST, RATIO
    }
}
