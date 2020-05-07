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
package crm.hoprxi.domain.model.balance;


import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-11-15
 */
public enum SmallChangDenominationEnum {
    ZERO(0) {
        @Override
        public Rounded round(MonetaryAmount receivables, MonetaryAmount smallChang) {
            CurrencyUnit currencyUnit = smallChang.getCurrency();
            return new Rounded(receivables, Money.zero(currencyUnit));
        }
    },

    ONE(1), FIVE(5);

    private int factor;

    SmallChangDenominationEnum(int factor) {
        this.factor = factor;
    }

    public Rounded round(MonetaryAmount receivables, MonetaryAmount smallChang) {
        Objects.requireNonNull(receivables, "receivables required");
        CurrencyUnit currencyUnit = smallChang.getCurrency();
        if (!currencyUnit.equals(receivables.getCurrency()))
            throw new IllegalArgumentException("receivables currency required equal smallChang currency");
        if (receivables.isNegative())
            throw new IllegalArgumentException("receivables must larger zero");
        MonetaryAmount integer = receivables.divideToIntegralValue(factor);
        MonetaryAmount remainder = receivables.remainder(factor);
        if (smallChang.isGreaterThanOrEqualTo(remainder))
            return new Rounded(integer.multiply(factor), remainder.negate());
        return new Rounded(integer.add(Money.of(1, currencyUnit)).multiply(factor), Money.of(factor, currencyUnit).subtract(remainder));
    }
/*
    public static SmallChangDenominationEnum of(String s) {
        for (SmallChangDenominationEnum smallChang : values()) {
            if (smallChang.toString().equals(s))
                return smallChang;
        }
        return SmallChangDenominationEnum.ZERO;
    }
 */
}
