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
package crm.hoprxi.core.domain.model.bonus;

import mi.hoprxi.to.NumberToBigDecimal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.2 builder 2020-09-07
 */
public class Bonus implements Comparable<Bonus> {
    private static final int SCALE = 2;
    public static final Bonus ZERO = new Bonus(0);
    private long value;

    /**
     * this is for rebuild
     *
     * @param value
     */
    private Bonus(long value) {
        this.value = value;
    }

    public Bonus(Number value) {
        this(NumberToBigDecimal.to(value));
    }

    public Bonus(BigDecimal value) {
        value = value.setScale(SCALE, RoundingMode.HALF_EVEN);
        this.value = value.movePointRight(SCALE).longValue();
    }

    public static Bonus of(Number value) {
        if (value.longValue() == 0l)
            return ZERO;
        if (value.doubleValue() == 0.0)
            return ZERO;
        return new Bonus(value);
    }

    public Bonus add(Bonus bonus) {
        if (bonus == null || bonus == Bonus.ZERO || bonus.value == 0l)
            return this;
        return new Bonus(value + bonus.value);
    }

    public Bonus subtract(Bonus bonus) {
        if (bonus == null || bonus == Bonus.ZERO || bonus.value == 0l)
            return this;
        if (value == bonus.value)
            return ZERO;
        return new Bonus(value - bonus.value);
    }

    public Bonus multiply(Number multiplicand) {
        if (multiplicand.doubleValue() == 0.0)
            return ZERO;
        BigDecimal bd = NumberToBigDecimal.to(multiplicand);
        if (bd.scale() == 0 && bd.longValueExact() == 1L)
            return this;
        return new Bonus(NumberToBigDecimal.to(value / Math.pow(10, SCALE)).multiply(bd));
    }

    public Number toNumber() {
        return value / Math.pow(10, SCALE);
    }

    public BigDecimal toBigDecimal() {
        return BigDecimal.valueOf(value / Math.pow(10, SCALE));
    }

    @Override
    public int compareTo(Bonus o) {
        return value == o.value ? 0 : value > o.value ? 1 : -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bonus bonus = (Bonus) o;

        return value == bonus.value;
    }

    @Override
    public int hashCode() {
        return (int) (value ^ (value >>> 32));
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Bonus.class.getSimpleName() + "[", "]")
                .add("value=" + value / Math.pow(10, SCALE))
                .toString();
    }
}
