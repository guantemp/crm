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

import mi.foxtail.to.ByteToHex;

import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-08-22
 */
public final class SkuEntry {
    private String skuId;
    private Ratio ratio;

    public SkuEntry(String skuId, Ratio ratio) {
        setSkuId(skuId);
        setRatio(ratio);
    }

    public String skuId() {
        return skuId;
    }

    private void setSkuId(String skuId) {
        Objects.requireNonNull(skuId, "skuId required");
        if (!ByteToHex.isIdentityHexStr(skuId))
            throw new IllegalArgumentException("illegal skuId characters");
        this.skuId = skuId;
    }

    public Ratio ratio() {
        return ratio;
    }

    private void setRatio(Ratio ratio) {
        this.ratio = ratio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SkuEntry skuEntry = (SkuEntry) o;

        if (skuId != null ? !skuId.equals(skuEntry.skuId) : skuEntry.skuId != null) return false;
        return ratio != null ? ratio.equals(skuEntry.ratio) : skuEntry.ratio == null;
    }

    @Override
    public int hashCode() {
        int result = skuId != null ? skuId.hashCode() : 0;
        result = 31 * result + (ratio != null ? ratio.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SkuEntry{" +
                "skuId='" + skuId + '\'' +
                ", ratio=" + ratio +
                '}';
    }
}
