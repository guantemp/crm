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

package crm.hoprxi.core.domain.model.bonus.consumption;

import crm.hoprxi.core.domain.model.DomainRegistry;
import crm.hoprxi.core.domain.model.collaborator.Brand;

import java.util.Objects;
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-09-10
 */
public class BrandEntry extends Entry {
    private Brand brand;

    public BrandEntry(Brand brand, Ratio ratio) {
        super(ratio);
        this.brand = brand;
    }

    @Override
    public BrandEntry changeRatio(Ratio newRatio) {
        Objects.requireNonNull(newRatio, "newRatio required");
        if (!ratio.equals(newRatio)) {
            DomainRegistry.domainEventPublisher().publish(new BrandEntryRatioChanged(brand, newRatio));
            return new BrandEntry(brand, newRatio);
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BrandEntry)) return false;

        BrandEntry that = (BrandEntry) o;

        return brand != null ? brand.equals(that.brand) : that.brand == null;
    }

    @Override
    public int hashCode() {
        return brand != null ? brand.hashCode() : 0;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BrandEntry.class.getSimpleName() + "[", "]")
                .add("brand=" + brand)
                .add("ratio=" + ratio)
                .toString();
    }
}
