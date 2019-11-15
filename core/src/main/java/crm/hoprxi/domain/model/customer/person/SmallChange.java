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

package crm.hoprxi.domain.model.customer.person;

import org.javamoney.moneta.Money;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2019-07-23
 */
public class SmallChange {
    private static final MonetaryAmount MONETARY_ZERO = Money.zero(Monetary.getCurrency(Locale.getDefault()));
    public static final SmallChange ZERO = new SmallChange(MONETARY_ZERO, false);
    private static final MonetaryAmount MONETARY_ONE = Money.of(1, Monetary.getCurrency(Locale.getDefault()));
    private static final int DIVISOR = 1;
    private MonetaryAmount amount;
    private boolean enable;

    public SmallChange(MonetaryAmount amount) {
        this(amount, false);
    }

    public SmallChange(MonetaryAmount amount, boolean enable) {
        setAmount(amount);
        this.enable = enable;
    }

    public SmallChange disable() {
        if (!enable)
            return this;
        return new SmallChange(amount, false);
    }

    public SmallChange enable() {
        if (enable)
            return this;
        return new SmallChange(amount, true);
    }

    /**
     * @param receivables
     * @return
     */
    public MonetaryAmount round(MonetaryAmount receivables) {
        if (!enable)
            throw new IllegalArgumentException("No change wallet function activated");
        Objects.requireNonNull(receivables, "receivables is required");
        if (receivables.isNegative())
            throw new IllegalArgumentException("receivables must larger zero");
        MonetaryAmount remainder = receivables.remainder(DIVISOR);
        if (amount.isZero())
            return receivables.divideToIntegralValue(DIVISOR).add(MONETARY_ONE);
        if (amount.isGreaterThanOrEqualTo(remainder))
            return receivables.divideToIntegralValue(DIVISOR);
        return receivables.divideToIntegralValue(DIVISOR).add(MONETARY_ONE);
    }

    /**
     * @param receivables
     * @return
     */
    public SmallChange access(MonetaryAmount receivables) {
        if (!enable)
            throw new IllegalArgumentException("No change wallet function activated");
        Objects.requireNonNull(receivables, "receivables is required");
        if (receivables.isNegative())
            throw new IllegalArgumentException("receivables must larger zero");
        MonetaryAmount remainder = receivables.remainder(DIVISOR);
        if (amount.isZero())
            return new SmallChange(remainder, enable);
        if (amount.isGreaterThanOrEqualTo(remainder))
            return new SmallChange(amount.subtract(remainder), enable);
        return new SmallChange(amount.add(MONETARY_ONE.subtract(remainder)), enable);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SmallChange smallChange = (SmallChange) o;

        if (enable != smallChange.enable) return false;
        return amount != null ? amount.equals(smallChange.amount) : smallChange.amount == null;
    }

    @Override
    public int hashCode() {
        int result = amount != null ? amount.hashCode() : 0;
        result = 31 * result + (enable ? 1 : 0);
        return result;
    }

    public MonetaryAmount amount() {
        return amount;
    }

    private void setAmount(MonetaryAmount amount) {
        Objects.requireNonNull(amount, "amount required");
        if (amount.isLessThan(MONETARY_ZERO) || amount.isGreaterThan(MONETARY_ONE))
            throw new IllegalArgumentException("amount range is [0-1]");
        this.amount = amount;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SmallChange.class.getSimpleName() + "[", "]")
                .add("amount=" + amount)
                .add("enable=" + enable)
                .toString();
    }

    public boolean isEnable() {
        return enable;
    }
}
