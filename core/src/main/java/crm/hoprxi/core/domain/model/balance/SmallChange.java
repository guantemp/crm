/*
 * Copyright (c) 2020. www.hoprxi.com All Rights Reserved.
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
package crm.hoprxi.core.domain.model.balance;

import org.javamoney.moneta.FastMoney;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.2 2020-04-02
 */
public class SmallChange {
    private static final SmallChange RMB_ZERO = new SmallChange(FastMoney.zero(Monetary.getCurrency(Locale.CHINA)), SmallChangDenominationEnum.ZERO);
    private static final SmallChange USD_ZERO = new SmallChange(FastMoney.zero(Monetary.getCurrency(Locale.US)), SmallChangDenominationEnum.ZERO);
    private MonetaryAmount amount;
    private SmallChangDenominationEnum smallChangDenominationEnum;

    public SmallChange(MonetaryAmount amount, SmallChangDenominationEnum smallChangDenominationEnum) {
        setAmount(amount);
        setSmallChangDenominationEnum(smallChangDenominationEnum);
    }

    /**
     * SmallChangDenominationEnum will set ZERO
     *
     * @param amount
     */
    public SmallChange(MonetaryAmount amount) {
        this(amount, SmallChangDenominationEnum.ZERO);
    }

    public static SmallChange zero(Locale locale) {
        if (locale == Locale.CHINA || locale == Locale.CHINESE || locale == Locale.SIMPLIFIED_CHINESE || locale == Locale.PRC)
            return RMB_ZERO;
        if (locale == Locale.US)
            return USD_ZERO;
        return new SmallChange(FastMoney.zero(Monetary.getCurrency(locale)), SmallChangDenominationEnum.ZERO);
    }

    public static SmallChange zero(CurrencyUnit currencyUnit) {
        if (currencyUnit.getNumericCode() == 156)
            return RMB_ZERO;
        if (currencyUnit.getNumericCode() == 840)
            return USD_ZERO;
        return new SmallChange(FastMoney.zero(currencyUnit), SmallChangDenominationEnum.ZERO);
    }

    public static SmallChange rmbZero() {
        return RMB_ZERO;
    }

    public static SmallChange usdZero() {
        return USD_ZERO;
    }

    private void setSmallChangDenominationEnum(SmallChangDenominationEnum smallChangDenominationEnum) {
        Objects.requireNonNull(smallChangDenominationEnum, "smallChangDenominationEnum required");
        this.smallChangDenominationEnum = smallChangDenominationEnum;
    }

    private void setAmount(MonetaryAmount amount) {
        Objects.requireNonNull(amount, "amount required");
        if (amount.isNegative())
            throw new IllegalArgumentException("amount must large or equal zero");
        this.amount = amount;
    }

    public MonetaryAmount amount() {
        return amount;
    }

    public SmallChangDenominationEnum smallChangDenominationEnum() {
        return smallChangDenominationEnum;
    }

    public Rounded round(MonetaryAmount receivables) {
        return smallChangDenominationEnum.round(receivables, amount);
    }

    public boolean isZero() {
        return amount.isZero();
    }

    /**
     * @param amount
     * @return
     */
    public SmallChange pay(MonetaryAmount amount) {
        Objects.requireNonNull(amount, "amount required");
        if (amount.isNegative())
            throw new IllegalArgumentException("amount must is positive");
        if (!this.amount.getCurrency().equals(amount.getCurrency()))
            throw new IllegalArgumentException("amount currency must is:" + this.amount.getCurrency());
        if (this.amount.isLessThan(amount))
            throw new InsufficientBalanceException("Insufficient balance");
        if (amount.isZero())
            return this;
        if (amount.isEqualTo(this.amount))
            return zero(this.amount.getCurrency());
        return new SmallChange(this.amount.subtract(amount), smallChangDenominationEnum);
    }

    /**
     * @param amount
     * @return
     */
    public SmallChange deposit(MonetaryAmount amount) {
        Objects.requireNonNull(amount, "amount required");
        if (amount.isNegative())
            throw new IllegalArgumentException("amount must is positive");
        if (amount.isZero())
            return this;
        return new SmallChange(this.amount.add(amount), smallChangDenominationEnum);
    }

    public SmallChange changeSmallChangDenominationEnum(SmallChangDenominationEnum newSmallChangDenominationEnum) {
        Objects.requireNonNull(newSmallChangDenominationEnum, "newSmallChangDenominationEnum required");
        if (smallChangDenominationEnum == newSmallChangDenominationEnum)
            return this;
        return new SmallChange(amount, newSmallChangDenominationEnum);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SmallChange that = (SmallChange) o;

        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        return smallChangDenominationEnum == that.smallChangDenominationEnum;
    }

    @Override
    public int hashCode() {
        int result = amount != null ? amount.hashCode() : 0;
        result = 31 * result + (smallChangDenominationEnum != null ? smallChangDenominationEnum.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SmallChange.class.getSimpleName() + "[", "]")
                .add("amount=" + amount)
                .add("smallChangDenominationEnum=" + smallChangDenominationEnum)
                .toString();
    }
}
