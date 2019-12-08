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

import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.util.Locale;
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-11-15
 */
public class Rounded {
    private static final MonetaryAmount MONETARY_ZERO = Money.zero(Monetary.getCurrency(Locale.getDefault()));
    public static Rounded ZERO = new Rounded(MONETARY_ZERO, MONETARY_ZERO, false);
    private MonetaryAmount integer;
    private MonetaryAmount remainder;
    private boolean overflow;

    public Rounded(MonetaryAmount integer, MonetaryAmount remainder, boolean overflow) {
        setInteger(integer);
        setRemainder(remainder);
        this.overflow = overflow;
    }

    public static Rounded zero(CurrencyUnit unit) {
        return null;
    }

    private void setRemainder(MonetaryAmount remainder) {
        this.remainder = remainder;
    }

    private void setInteger(MonetaryAmount integer) {
        if (integer.isNegative())
            throw new IllegalArgumentException("");
        MonetaryAmount remainder = integer.remainder(1);
        if (!remainder.isZero())
            throw new IllegalArgumentException("");
        this.integer = integer;
    }

    public MonetaryAmount integer() {
        return integer;
    }

    public MonetaryAmount remainder() {
        return remainder;
    }

    public boolean isOverflow() {
        return overflow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rounded rounded = (Rounded) o;

        if (integer != null ? !integer.equals(rounded.integer) : rounded.integer != null) return false;
        return remainder != null ? remainder.equals(rounded.remainder) : rounded.remainder == null;
    }

    @Override
    public int hashCode() {
        int result = integer != null ? integer.hashCode() : 0;
        result = 31 * result + (remainder != null ? remainder.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Rounded.class.getSimpleName() + "[", "]")
                .add("integer=" + integer)
                .add("remainder=" + remainder)
                .toString();
    }
}
