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
package crm.hoprxi.domain.model.integral;

import mi.hoprxi.to.NumberToBigDecimal;

import java.math.BigDecimal;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2019-07-22
 */
public class Integral implements Comparable<Integral> {
    public static final Integral ZERO = new Integral(0);
    public static final Integral ONE = new Integral(1);
    private BigDecimal value;

    public Integral(BigDecimal value) {
        setValue(value);
    }

    public Integral(Number value) {
        setValue(NumberToBigDecimal.to(value));
    }

    private void setValue(BigDecimal value) {
        if (value == null)
            value = BigDecimal.ZERO;
        this.value = value;
    }

    public Integral add(Integral integral) {
        return new Integral(value.add(integral.value));
    }

    public Integral subtract(Integral integral) {
        return new Integral(value.subtract(integral.value));
    }

    public BigDecimal value() {
        return value;
    }

    public boolean isNegative() {
        return value.compareTo(BigDecimal.ZERO) < 0 ? true : false;
    }

    @Override
    public int compareTo(Integral o) {
        return value.compareTo(o.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Integral integral = (Integral) o;

        return value != null ? value.equals(integral.value) : integral.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Integral{" +
                "value=" + value +
                '}';
    }
}
