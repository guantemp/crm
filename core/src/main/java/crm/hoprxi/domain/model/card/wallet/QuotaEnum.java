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


import org.javamoney.moneta.FastMoney;

import javax.money.MonetaryAmount;
import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-11-15
 */
public enum QuotaEnum {
    ZERO(0) {
        @Override
        public Rounded round(MonetaryAmount receivables, MonetaryAmount balance) {
            return Rounded.ZERO;
        }
    },

    ONE(1), FIVE(5);

    protected int factor;

    QuotaEnum(int factor) {
        this.factor = factor;
    }

    public Rounded round(MonetaryAmount receivables, MonetaryAmount balance) {
        Objects.requireNonNull(receivables, "receivables required");
        if (receivables.isNegative())
            throw new IllegalArgumentException("receivables must larger zero");
        MonetaryAmount integer = receivables.divideToIntegralValue(factor);
        MonetaryAmount remainder = receivables.remainder(factor);
        if (balance.isGreaterThanOrEqualTo(remainder))
            return new Rounded(integer.multiply(factor), remainder.negate());
        return new Rounded(integer.add(FastMoney.of(1, balance.getCurrency())).multiply(factor), FastMoney.of(factor, balance.getCurrency()).subtract(remainder));
    }
}
