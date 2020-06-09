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

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-06-03
 */
public abstract class SuperpositionEntry {
    protected double rate;

    public SuperpositionEntry(double rate) {
        if (Double.compare(rate, 0.0) <= 0)
            throw new IllegalArgumentException("");
        this.rate = rate;
    }

    public Bonus calculation(Bonus bonus, int scale, RoundingMode roundingMode) {
        BigDecimal bd = NumberToBigDecimal.to(bonus.toNumber().doubleValue() * rate);
        bd = bd.setScale(scale, roundingMode);
        return new Bonus(bd);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SuperpositionEntry)) return false;

        SuperpositionEntry that = (SuperpositionEntry) o;

        return Double.compare(that.rate, rate) == 0;
    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(rate);
        return (int) (temp ^ (temp >>> 32));
    }
}
