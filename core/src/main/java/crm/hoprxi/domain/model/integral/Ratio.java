/*
 * Copyright (c) 2018 www.hoprxi.com rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package crm.hoprxi.domain.model.integral;


import mi.foxtail.to.NumberToBigDecimal;

import java.math.BigDecimal;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-07-25
 */
public class Ratio {
    public static final Ratio ONE_TO_ONE = new Ratio((short) 1, (short) 1);
    public static final Ratio ONE_TO_TWO = new Ratio((short) 1, (short) 2);
    private short denominator;
    private short numerator = (short) 1;

    public Ratio(short denominator, short numerator) {
        setDenominator(denominator);
        setNumerator(numerator);
    }

    public static Ratio createFixedRatio(short numerator) {
        return new Ratio((short) 1, numerator) {
            @Override
            public Integral calculation(BigDecimal amount, int scale, int roundingMode) {
                return new Integral(numerator);
            }
        };
    }

    private void setNumerator(short numerator) {
        if (numerator <= 0)
            throw new IllegalArgumentException("numerator must greater than zero");
        this.numerator = numerator;
    }

    private void setDenominator(short denominator) {
        if (denominator <= 0)
            throw new IllegalArgumentException("denominator must greater than zero");
        this.denominator = denominator;
    }

    public Integral calculation(BigDecimal amount, int scale, int roundingMode) {
        amount = amount.multiply(NumberToBigDecimal.to(numerator)).divide(NumberToBigDecimal.to(denominator));
        return new Integral(amount.setScale(scale, roundingMode));
    }
}
