/*
 * Copyright (c) 2018. www.hoprxi.com rights Reserved.
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
 *
 *
 */

package crm.hoprxi.domain.model.integral;

import mi.hoprxi.to.ByteToHex;

import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-08-22
 */
public final class SupplierEntry {
    private Ratio ratio;
    private String supplierId;

    public SupplierEntry(String supplierId, Ratio ratio) {
        setSupplierId(supplierId);
        setRatio(ratio);
    }

    public Ratio ratio() {
        return ratio;
    }

    private void setRatio(Ratio ratio) {
        this.ratio = ratio;
    }

    public String supplierId() {
        return supplierId;
    }

    private void setSupplierId(String supplierId) {
        Objects.requireNonNull(supplierId, "supplierId required");
        if (!ByteToHex.isIdentityHexStr(supplierId))
            throw new IllegalArgumentException("Illegal supplierId");
        this.supplierId = supplierId;
    }

    public boolean isInclude(String skuId) {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SupplierEntry that = (SupplierEntry) o;

        if (ratio != null ? !ratio.equals(that.ratio) : that.ratio != null) return false;
        return supplierId != null ? supplierId.equals(that.supplierId) : that.supplierId == null;
    }

    @Override
    public int hashCode() {
        int result = ratio != null ? ratio.hashCode() : 0;
        result = 31 * result + (supplierId != null ? supplierId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SupplierEntry{" +
                "ratio=" + ratio +
                ", supplierId='" + supplierId + '\'' +
                '}';
    }
}
