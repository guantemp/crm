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
import crm.hoprxi.core.domain.model.collaborator.Category;

import java.util.Objects;
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-09-10
 */
public class CategoryEntry extends Entry {
    private Category category;

    public CategoryEntry(Category category, Ratio ratio) {
        super(ratio);
        setCategory(category);
    }

    @Override
    public CategoryEntry changeRatio(Ratio newRatio) {
        Objects.requireNonNull(newRatio, "newRatio required");
        if (!ratio.equals(newRatio)) {
            DomainRegistry.domainEventPublisher().publish(new CategoryEntryRatioChanged(category, newRatio));
            return new CategoryEntry(category, newRatio);
        }
        return this;
    }

    public Category category() {
        return category;
    }

    private void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryEntry)) return false;

        CategoryEntry that = (CategoryEntry) o;

        return category != null ? category.equals(that.category) : that.category == null;
    }

    @Override
    public int hashCode() {
        return category != null ? category.hashCode() : 0;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CategoryEntry.class.getSimpleName() + "[", "]")
                .add("category=" + category)
                .add("ratio=" + ratio)
                .toString();
    }
}
