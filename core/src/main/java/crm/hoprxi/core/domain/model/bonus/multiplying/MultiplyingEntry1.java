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

import com.arangodb.entity.DocumentField;
import crm.hoprxi.core.domain.model.bonus.Bonus;
import crm.hoprxi.core.domain.model.card.ValidityPeriod;
import mi.hoprxi.to.NumberToBigDecimal;

import java.math.BigDecimal;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-06-03
 */
public abstract class MultiplyingEntry1 {
    @DocumentField(DocumentField.Type.KEY)
    protected String id;
    protected Number rate;
    protected ValidityPeriod validityPeriod;

    public MultiplyingEntry1(Number rate) {
        if (Double.compare(rate.doubleValue(), 0.0) <= 0)
            throw new IllegalArgumentException("rate lager zero");
        this.rate = rate;
    }

    public String id() {
        return id;
    }

    public Number rate() {
        return rate;
    }

    public Bonus calculation(Bonus bonus) {
        BigDecimal bd = NumberToBigDecimal.to(bonus.toNumber().doubleValue() * rate.doubleValue());
        return new Bonus(bd);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MultiplyingEntry1)) return false;

        MultiplyingEntry1 that = (MultiplyingEntry1) o;

        return rate != null ? rate.equals(that.rate) : that.rate == null;
    }

    @Override
    public int hashCode() {
        return rate != null ? rate.hashCode() : 0;
    }
}
