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

package crm.hoprxi.core.domain.model.bonus.multiplying;

import crm.hoprxi.core.domain.model.bonus.Bonus;
import mi.hoprxi.to.NumberToBigDecimal;

import java.math.BigDecimal;
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-09-09
 */
public class MultiplyingEntry<T> implements Cloneable {
    private T t;
    private Number multiplyingPower;

    public MultiplyingEntry(T t, Number multiplyingPower) {
        this.t = t;
        this.multiplyingPower = multiplyingPower;
    }

    public T t() {
        return t;
    }

    public Number multiplyingPower() {
        return multiplyingPower;
    }

    public Bonus calculation(Bonus bonus) {
        BigDecimal bd = NumberToBigDecimal.to(bonus.toNumber().doubleValue() * multiplyingPower.doubleValue());
        return new Bonus(bd);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MultiplyingEntry)) return false;

        MultiplyingEntry<?> that = (MultiplyingEntry<?>) o;

        return t != null ? t.equals(that.t) : that.t == null;
    }

    @Override
    public int hashCode() {
        return t != null ? t.hashCode() : 0;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MultiplyingEntry.class.getSimpleName() + "[", "]")
                .add("t=" + t)
                .add("multiplyingPower=" + multiplyingPower)
                .toString();
    }
}
