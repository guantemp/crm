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

package crm.hoprxi.domain.model.customer.footprint;

import crm.hoprxi.domain.model.collaborator.Sku;

import java.math.BigDecimal;
import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-08-05
 */
public class IntegralHistoryDetail {
    private Sku sku;
    private BigDecimal value;

    public IntegralHistoryDetail(Sku sku, BigDecimal value) {
        setSku(sku);
        setValue(value);
    }

    private void setValue(BigDecimal value) {
        Objects.requireNonNull(value, "value required");
        this.value = value;
    }

    private void setSku(Sku sku) {
        Objects.requireNonNull(sku, "sku snapshot required");
        this.sku = sku;
    }

    public Sku sku() {
        return sku;
    }

    public BigDecimal value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntegralHistoryDetail that = (IntegralHistoryDetail) o;

        if (sku != null ? !sku.equals(that.sku) : that.sku != null) return false;
        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        int result = sku != null ? sku.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IntegralHistoryDetail{" +
                "sku=" + sku +
                ", value=" + value +
                '}';
    }
}
