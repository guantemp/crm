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
package crm.hoprxi.domain.model.card.balance;

import org.javamoney.moneta.FastMoney;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-11-20
 */
public class Balance {
    private static final Balance RMB_ZERO = new Balance(FastMoney.zero(Monetary.getCurrency(Locale.CHINA)), FastMoney.zero(Monetary.getCurrency(Locale.CHINA))) {
        @Override
        public Balance withdrawal(MonetaryAmount amount) {
            return this;
        }

        @Override
        public Balance withdrawalOfGive(MonetaryAmount amount) {
            return this;
        }

        @Override
        public Balance debit(MonetaryAmount amount, PaymentStrategy strategy) {
            return this;
        }

        @Override
        public Balance debit(MonetaryAmount amount) {
            return this;
        }
    };
    private static final Balance USD_ZERO = new Balance(FastMoney.zero(Monetary.getCurrency(Locale.US)), FastMoney.zero(Monetary.getCurrency(Locale.US)));
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

    public static Balance zero(Locale locale) {
        if (locale == Locale.CHINA || locale == Locale.CHINESE || locale == Locale.SIMPLIFIED_CHINESE || locale == Locale.PRC)
            return RMB_ZERO;
        if (locale == Locale.US)
            return USD_ZERO;
        MonetaryAmount zero = FastMoney.zero(Monetary.getCurrency(locale));
        return new Balance(zero, zero);
    }

    private void setGive(MonetaryAmount give) {
        if (give == null || give.isNegative())
            throw new IllegalArgumentException("give required positive");
        this.give = give;
    }

    private void setValuable(MonetaryAmount valuable) {
        Objects.requireNonNull(valuable, "balance required");
        this.valuable = valuable;
    }

    public MonetaryAmount valuable() {
        return valuable;
    }

    public MonetaryAmount give() {
        return give;
    }

    /**
     * @param valuableAmount must is positive
     * @param giveAmount     must is positive
     */
    Balance credit(MonetaryAmount valuableAmount, MonetaryAmount giveAmount) {
        if ((valuableAmount == null || valuableAmount.isNegativeOrZero()) && (giveAmount == null || giveAmount.isNegativeOrZero()))
            return this;
        if (valuableAmount == null)
            valuableAmount = FastMoney.zero(valuable.getCurrency());
        if (giveAmount == null)
            giveAmount = FastMoney.zero(give.getCurrency());
        MonetaryAmount balanceTemp = valuable.add(valuableAmount);
        MonetaryAmount giveTemp = give.add(giveAmount);
        return new Balance(balanceTemp, giveTemp);
    }

    Balance credit(MonetaryAmount valuableAmount) {
        return credit(valuableAmount, FastMoney.zero(valuable.getCurrency()));
    }

    Balance withdrawal(MonetaryAmount amount) {
        if (amount == null || amount.isNegativeOrZero())
            return this;
        if (amount.isGreaterThan(valuable))
            throw new InsufficientBalanceException("insufficient balance");
        return new Balance(valuable.subtract(amount), give);
    }

    Balance withdrawalOfGive(MonetaryAmount amount) {
        if (amount == null || amount.isNegativeOrZero())
            return this;
        if (amount.isGreaterThan(give))
            throw new InsufficientBalanceException("give amount insufficient balance");
        return new Balance(valuable, give.subtract(amount));
    }

    Balance debit(MonetaryAmount amount, PaymentStrategy strategy) {
        MonetaryAmount available = valuable.add(give);
        if (available.isLessThan(amount))
            throw new InsufficientBalanceException("insufficient balance");
        switch (strategy) {
            case BALANCE_FIRST:
                valuable = valuable.subtract(amount);
                if (valuable.isNegative()) {
                    valuable = FastMoney.zero(valuable.getCurrency());
                    give = give.add(valuable);
                }
                break;
            case RED_ENVELOPES_FIRST:
                MonetaryAmount g = give.subtract(amount);
                if (g.isNegative()) {
                    give = FastMoney.zero(give.getCurrency());
                    valuable = valuable.add(g);
                } else {
                    give = g;
                }
                break;
            case RATIO:
                double d1 = valuable.getNumber().doubleValue();
                double d2 = give.getNumber().doubleValue();
                double d3 = d1 / (d1 + d2);
                MonetaryAmount temp = amount.multiply(d3);
                valuable = valuable.subtract(temp);
                give = give.subtract(amount.subtract(temp));
                break;
        }
        return new Balance(valuable, give);
    }

    /**
     * @param amount
     * @return
     */
    public Balance debit(MonetaryAmount amount) {
        double d1 = valuable.getNumber().doubleValue();
        double d2 = give.getNumber().doubleValue();
        double d3 = d1 / (d1 + d2);
        MonetaryAmount temp = amount.multiply(d3);
        return new Balance(valuable.subtract(temp), give.subtract(amount.subtract(temp)));
    }

    /**
     * @param balance
     * @return
     */
    public Balance subtract(Balance balance) {
        return new Balance(this.valuable.subtract(balance.valuable), give.subtract(balance.give));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Balance balance = (Balance) o;

        if (this.valuable != null ? !this.valuable.equals(balance.valuable) : balance.valuable != null) return false;
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
                .add("balance=" + valuable)
                .add("give=" + give)
                .toString();
    }

    enum PaymentStrategy {
        BALANCE_FIRST, RED_ENVELOPES_FIRST, RATIO
    }
}
