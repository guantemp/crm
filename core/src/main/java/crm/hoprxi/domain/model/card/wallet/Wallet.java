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
package crm.hoprxi.domain.model.card.wallet;

import crm.hoprxi.domain.model.card.PaymentStrategy;
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
public class Wallet {
    private static final Wallet RMB_ZERO = new Wallet(FastMoney.zero(Monetary.getCurrency(Locale.CHINA)), FastMoney.zero(Monetary.getCurrency(Locale.CHINA))) {
        @Override
        public Wallet withdrawal(MonetaryAmount amount) {
            return this;
        }

        @Override
        public Wallet withdrawalOfGive(MonetaryAmount amount) {
            return this;
        }

        @Override
        public Wallet pay(MonetaryAmount amount, PaymentStrategy strategy) {
            return this;
        }

        @Override
        public Wallet pay(MonetaryAmount amount) {
            return this;
        }
    };
    private static final Wallet USD_ZERO = new Wallet(FastMoney.zero(Monetary.getCurrency(Locale.US)), FastMoney.zero(Monetary.getCurrency(Locale.US)));
    private MonetaryAmount balance;
    private MonetaryAmount give;

    /**
     * @param balance
     * @param give    required positive
     */
    public Wallet(MonetaryAmount balance, MonetaryAmount give) {
        setBalance(balance);
        setGive(give);
    }

    public static Wallet zero(Locale locale) {
        if (locale == Locale.CHINA || locale == Locale.CHINESE || locale == Locale.SIMPLIFIED_CHINESE || locale == Locale.PRC)
            return RMB_ZERO;
        if (locale == Locale.US)
            return USD_ZERO;
        MonetaryAmount zero = FastMoney.zero(Monetary.getCurrency(locale));
        return new Wallet(zero, zero);
    }

    private void setGive(MonetaryAmount give) {
        if (give == null || give.isNegative())
            throw new IllegalArgumentException("give required positive");
        this.give = give;
    }

    private void setBalance(MonetaryAmount balance) {
        Objects.requireNonNull(balance, "balance required");
        this.balance = balance;
    }

    public MonetaryAmount balance() {
        return balance;
    }

    public MonetaryAmount give() {
        return give;
    }

    /**
     * @param balanceAmount must is positive
     * @param giveAmount    must is positive
     * @throws ExceedQuotaException if balance or give is greater limit(when the limit is positive)
     */
    public Wallet prepay(MonetaryAmount balanceAmount, MonetaryAmount giveAmount) {
        if ((balanceAmount == null || balanceAmount.isNegativeOrZero()) && (giveAmount == null || giveAmount.isNegativeOrZero()))
            return this;
        if (balanceAmount == null)
            balanceAmount = FastMoney.zero(balance.getCurrency());
        if (giveAmount == null)
            giveAmount = FastMoney.zero(give.getCurrency());
        MonetaryAmount balanceTemp = balance.add(balanceAmount);
        MonetaryAmount giveTemp = give.add(giveAmount);
        return new Wallet(balanceTemp, giveTemp);
    }

    public Wallet prepay(MonetaryAmount balanceAmount) {
        return prepay(balanceAmount, FastMoney.zero(balance.getCurrency()));
    }

    public Wallet withdrawal(MonetaryAmount amount) {
        if (amount == null || amount.isNegativeOrZero())
            return this;
        if (amount.isGreaterThan(balance))
            throw new InsufficientBalanceException("insufficient balance");
        return new Wallet(balance.subtract(amount), give);
    }

    public Wallet withdrawalOfGive(MonetaryAmount amount) {
        if (amount == null || amount.isNegativeOrZero())
            return this;
        if (amount.isGreaterThan(give))
            throw new InsufficientBalanceException("give amount insufficient balance");
        return new Wallet(balance, give.subtract(amount));
    }

    public Wallet pay(MonetaryAmount amount, PaymentStrategy strategy) {
        MonetaryAmount available = balance.add(give);
        if (available.isLessThan(amount))
            throw new InsufficientBalanceException("insufficient balance");
        switch (strategy) {
            case BALANCE_FIRST:
                balance = balance.subtract(amount);
                if (balance.isNegative()) {
                    balance = FastMoney.zero(balance.getCurrency());
                    give = give.add(balance);
                }
                break;
            case RED_ENVELOPES_FIRST:
                MonetaryAmount g = give.subtract(amount);
                if (g.isNegative()) {
                    give = FastMoney.zero(give.getCurrency());
                    balance = balance.add(g);
                } else {
                    give = g;
                }
                break;
            case RATIO:
                double d1 = balance.getNumber().doubleValue();
                double d2 = give.getNumber().doubleValue();
                double d3 = d1 / (d1 + d2);
                MonetaryAmount temp = amount.multiply(d3);
                balance = balance.subtract(temp);
                give = give.subtract(amount.subtract(temp));
                break;
        }
        return new Wallet(balance, give);
    }

    public Wallet pay(MonetaryAmount amount) {
        return pay(amount, PaymentStrategy.BALANCE_FIRST);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Wallet wallet = (Wallet) o;

        if (balance != null ? !balance.equals(wallet.balance) : wallet.balance != null) return false;
        return give != null ? give.equals(wallet.give) : wallet.give == null;
    }

    @Override
    public int hashCode() {
        int result = balance != null ? balance.hashCode() : 0;
        result = 31 * result + (give != null ? give.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Wallet.class.getSimpleName() + "[", "]")
                .add("balance=" + balance)
                .add("give=" + give)
                .toString();
    }
}
