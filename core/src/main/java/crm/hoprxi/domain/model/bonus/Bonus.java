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
package crm.hoprxi.domain.model.bonus;

import mi.hoprxi.to.NumberToBigDecimal;

import java.math.BigDecimal;
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2019-07-22
 */
public class Bonus implements Comparable<Bonus> {
    public static final Bonus ZERO = new Bonus(0);
    private static int SCALE = 2;
    private long value;

    public Bonus(Number value) {
        setValue(value);
    }

    public static Bonus of(Number value) {
        if (value.doubleValue() == 0.0)
            return ZERO;
        return new Bonus(value);
    }

    private void setValue(Number value) {
        BigDecimal bd = NumberToBigDecimal.to(value);
        if (bd.scale() > SCALE)
            throw new IllegalArgumentException(value + " can not be represented by this class, scale > " + SCALE);
        if (bd.compareTo(BigDecimal.ZERO) == -1)
            throw new IllegalArgumentException("value required positive");
        this.value = bd.movePointRight(SCALE).longValue();
    }

    public int scale() {
        return SCALE;
    }

    public Bonus add(Bonus bonus) {
        long temp = value + bonus.value;
        if (temp == 0)
            return ZERO;
        return new Bonus(value + bonus.value);
    }

    public Bonus subtract(Bonus bonus) {
        if (bonus.value > value)
            throw new BonusDeficiencyException("integral deficiency");
        if (bonus.value == value)
            return ZERO;
        return new Bonus(value - bonus.value);
    }

    public Number value() {
        return value / Math.pow(10, SCALE);
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
                .add("value=" + value)
                .toString();
    }
}
