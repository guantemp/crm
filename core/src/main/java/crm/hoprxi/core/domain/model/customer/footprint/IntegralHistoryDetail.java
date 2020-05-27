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

package crm.hoprxi.core.domain.model.customer.footprint;

import crm.hoprxi.core.domain.model.collaborator.Item;

import java.math.BigDecimal;
import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-08-05
 */
public class IntegralHistoryDetail {
    private Item item;
    private BigDecimal value;

    public IntegralHistoryDetail(Item item, BigDecimal value) {
        setItem(item);
        setValue(value);
    }

    private void setValue(BigDecimal value) {
        Objects.requireNonNull(value, "value required");
        this.value = value;
    }

    private void setItem(Item item) {
        Objects.requireNonNull(item, "sku snapshot required");
        this.item = item;
    }

    public Item sku() {
        return item;
    }

    public BigDecimal value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntegralHistoryDetail that = (IntegralHistoryDetail) o;

        if (item != null ? !item.equals(that.item) : that.item != null) return false;
        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        int result = item != null ? item.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IntegralHistoryDetail{" +
                "sku=" + item +
                ", value=" + value +
                '}';
    }
}
