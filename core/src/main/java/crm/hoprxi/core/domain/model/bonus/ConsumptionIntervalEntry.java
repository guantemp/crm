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

import javax.money.MonetaryAmount;
import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-08-22
 */
public class ConsumptionIntervalEntry extends SuperpositionEntry {
    private MonetaryAmount lowerLimit;

    public ConsumptionIntervalEntry(double ratio, MonetaryAmount lowerLimit) {
        super(ratio);
        setLowerLimit(lowerLimit);
    }

    public double ratio() {
        return rate;
    }

    private void setRatio(double ratio) {
        Objects.requireNonNull(ratio, "ratio required");
        this.rate = ratio;
    }

    public MonetaryAmount lowerLimit() {
        return lowerLimit;
    }

    private void setLowerLimit(MonetaryAmount lowerLimit) {
        Objects.requireNonNull(lowerLimit, "lowerLimit required");
        //if (lowerLimit.compareTo(Member.class) < 0)
        //throw new IllegalArgumentException("lowerLimit grant than zero");
        this.lowerLimit = lowerLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConsumptionIntervalEntry that = (ConsumptionIntervalEntry) o;

        if (rate != null ? !rate.equals(that.rate) : that.rate != null) return false;
        return lowerLimit != null ? lowerLimit.equals(that.lowerLimit) : that.lowerLimit == null;
    }

    @Override
    public int hashCode() {
        int result = rate != null ? rate.hashCode() : 0;
        result = 31 * result + (lowerLimit != null ? lowerLimit.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ConsumptionIntervalEntry{" +
                "ratio=" + rate +
                ", lowerLimit=" + lowerLimit +
                '}';
    }
}
