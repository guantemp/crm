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
package crm.hoprxi.domain.model.card.coinWallet;

import crm.hoprxi.domain.model.card.InsufficientBalanceException;
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
public class CoinWallet {
    private static final CoinWallet RMB_ZERO = new CoinWallet(Money.zero(Monetary.getCurrency(Locale.CHINA)), Quota.ZERO);
    private static final CoinWallet USD_ZERO = new CoinWallet(Money.zero(Monetary.getCurrency(Locale.US)), Quota.ZERO);
    private MonetaryAmount balance;
    private Quota quota;

    public CoinWallet(MonetaryAmount balance, Quota quota) {
        setBalance(balance);
        setQuota(quota);
    }

    public static CoinWallet zero(Locale locale) {
        if (locale == Locale.CHINA || locale == Locale.CHINESE || locale == Locale.SIMPLIFIED_CHINESE || locale == Locale.PRC)
            return RMB_ZERO;
        if (locale == Locale.US)
            return USD_ZERO;
        return new CoinWallet(FastMoney.zero(Monetary.getCurrency(locale)), Quota.ZERO);
    }

    private void setQuota(Quota quota) {
        if (quota == null)
            quota = Quota.ZERO;
        this.quota = quota;
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

    public Quota quota() {
        return quota;
    }

    public Rounded round(MonetaryAmount receivables) {
        return quota.round(receivables, balance);
    }

    public CoinWallet pay(MonetaryAmount amount) {
        Objects.requireNonNull(amount, "newBalance required");
        if (amount.isNegativeOrZero())
            throw new IllegalArgumentException("pay amount must large zero");
        if (amount.isGreaterThan(balance))
            throw new InsufficientBalanceException("Sorry, your credit is running low");
        return new CoinWallet(balance.subtract(amount), quota);
    }

    public CoinWallet deposit(MonetaryAmount amount) {
        Objects.requireNonNull(amount, "amount required");
        if (amount.isNegativeOrZero())
            throw new IllegalArgumentException("deposit amount must large zero");
        return new CoinWallet(balance.add(amount), quota);
    }

    public CoinWallet changeQuota(Quota newQuota) {
        Objects.requireNonNull(newQuota, "newQuota required");
        if (quota != newQuota)
            return new CoinWallet(balance, newQuota);
        return this;
    }
}
