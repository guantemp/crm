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

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-08-22
 */
public final class CategoryEntry {
    private Ratio ratio;
    private String categoryId;
    private boolean includeSubclasses;

    public CategoryEntry(String categoryId, Ratio ratio, boolean includeSubclasses) {
        this.ratio = ratio;
        this.categoryId = categoryId;
        this.includeSubclasses = includeSubclasses;
    }

    public CategoryEntry(String categoryId, Ratio ratio) {
        this(categoryId, ratio, true);
    }

    public Ratio ratio() {
        return ratio;
    }

    private void setRatio(Ratio ratio) {
        this.ratio = ratio;
    }

    public String categoryId() {
        return categoryId;
    }

    private void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isIncludeSubclasses() {
        return includeSubclasses;
    }

    private void setIncludeSubclasses(boolean includeSubclasses) {
        this.includeSubclasses = includeSubclasses;
    }

    public boolean isInclude(String skuId) {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryEntry that = (CategoryEntry) o;

        if (includeSubclasses != that.includeSubclasses) return false;
        if (ratio != null ? !ratio.equals(that.ratio) : that.ratio != null) return false;
        return categoryId != null ? categoryId.equals(that.categoryId) : that.categoryId == null;
    }

    @Override
    public int hashCode() {
        int result = ratio != null ? ratio.hashCode() : 0;
        result = 31 * result + (categoryId != null ? categoryId.hashCode() : 0);
        result = 31 * result + (includeSubclasses ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CategoryEntry{" +
                "ratio=" + ratio +
                ", categoryId='" + categoryId + '\'' +
                ", includeSubclasses=" + includeSubclasses +
                '}';
    }
}
