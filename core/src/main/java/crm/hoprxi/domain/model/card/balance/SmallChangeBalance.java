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
import org.javamoney.moneta.Money;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.util.Locale;
import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-11-14
 */
public class SmallChangeBalance {
    private static final SmallChangeBalance RMB_ZERO = new SmallChangeBalance(Money.zero(Monetary.getCurrency(Locale.CHINA)), SmallChangDenominationEnum.ZERO) {
        @Override
        public SmallChangeBalance pay(MonetaryAmount amount) {
            return this;
        }
    };
    private static final SmallChangeBalance USD_ZERO = new SmallChangeBalance(Money.zero(Monetary.getCurrency(Locale.US)), SmallChangDenominationEnum.ZERO) {
        @Override
        public SmallChangeBalance pay(MonetaryAmount amount) {
            return this;
        }
    };
    private MonetaryAmount balance;
    private SmallChangDenominationEnum smallChangDenominationEnum;

    public SmallChangeBalance(MonetaryAmount balance, SmallChangDenominationEnum smallChangDenominationEnum) {
        setBalance(balance);
        setSmallChangDenominationEnum(smallChangDenominationEnum);
    }

    public static SmallChangeBalance zero(Locale locale) {
        if (locale == Locale.CHINA || locale == Locale.CHINESE || locale == Locale.SIMPLIFIED_CHINESE || locale == Locale.PRC)
            return RMB_ZERO;
        if (locale == Locale.US)
            return USD_ZERO;
        return new SmallChangeBalance(FastMoney.zero(Monetary.getCurrency(locale)), SmallChangDenominationEnum.ZERO);
    }

    private void setSmallChangDenominationEnum(SmallChangDenominationEnum smallChangDenominationEnum) {
        if (smallChangDenominationEnum == null)
            smallChangDenominationEnum = SmallChangDenominationEnum.ZERO;
        this.smallChangDenominationEnum = smallChangDenominationEnum;
    }

    private void setBalance(MonetaryAmount balance) {
        Objects.requireNonNull(balance, "balance required");
        if (balance.isNegative())
            throw new IllegalArgumentException("balance must large or equal zero");
        this.balance = balance;
    }

    public MonetaryAmount balance() {
        return balance;
    }

    public SmallChangDenominationEnum changDenomination() {
        return smallChangDenominationEnum;
    }

    public Rounded round(MonetaryAmount receivables) {
        return smallChangDenominationEnum.round(receivables, balance);
    }

    public SmallChangeBalance pay(MonetaryAmount amount) {
        Objects.requireNonNull(amount, "newBalance required");
        if (amount.isNegativeOrZero())
            throw new IllegalArgumentException("pay amount must large zero");
        if (amount.isGreaterThan(balance))
            throw new InsufficientBalanceException("Sorry, your credit is running low");
        return new SmallChangeBalance(balance.subtract(amount), smallChangDenominationEnum);
    }

    public SmallChangeBalance deposit(MonetaryAmount amount) {
        Objects.requireNonNull(amount, "amount required");
        if (amount.isNegativeOrZero())
            throw new IllegalArgumentException("deposit amount must large zero");
        return new SmallChangeBalance(balance.add(amount), smallChangDenominationEnum);
    }

    public SmallChangeBalance changeChangDenominationEnum(SmallChangDenominationEnum newSmallChangDenominationEnum) {
        Objects.requireNonNull(newSmallChangDenominationEnum, "newQuota required");
        if (smallChangDenominationEnum != newSmallChangDenominationEnum)
            return new SmallChangeBalance(balance, newSmallChangDenominationEnum);
        return this;
    }
}
