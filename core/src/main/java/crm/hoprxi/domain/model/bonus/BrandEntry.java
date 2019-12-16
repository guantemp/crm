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

import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2019-08-22
 */
public class BrandEntry {
    private String brandId;
    private Ratio ratio;

    /**
     * @param brandId
     * @param ratio
     */
    public BrandEntry(String brandId, Ratio ratio) {
        setBrandId(brandId);
        setRatio(ratio);
    }

    public final static BrandEntry createOneToOneBrandEntry(String brandId) {
        return new BrandEntry(brandId, Ratio.ONE_TO_ONE);
    }

    public String brandId() {
        return brandId;
    }

    private void setBrandId(String brandId) {
        Objects.requireNonNull(brandId, "brandId required");
        this.brandId = brandId;
    }

    public Ratio ratio() {
        return ratio;
    }

    private void setRatio(Ratio ratio) {
        Objects.requireNonNull(ratio, "ratio require");
        this.ratio = ratio;
    }

    public boolean isIncluded(String itemId) {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BrandEntry that = (BrandEntry) o;

        if (brandId != null ? !brandId.equals(that.brandId) : that.brandId != null) return false;
        return ratio != null ? ratio.equals(that.ratio) : that.ratio == null;
    }

    @Override
    public int hashCode() {
        int result = brandId != null ? brandId.hashCode() : 0;
        result = 31 * result + (ratio != null ? ratio.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BrandEntry{" +
                "brandId=" + brandId +
                ", ratio=" + ratio +
                '}';
    }
}
