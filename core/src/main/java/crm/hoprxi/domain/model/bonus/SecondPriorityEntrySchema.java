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

import java.util.Set;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-09-07
 */
public class SecondPriorityEntrySchema {
    private Sort[] sorts = new Sort[3];
    private Set<BrandEntry> brandEntries;
    private Set<CategoryEntry> categoryEntries;
    private Set<SupplierEntry> supplierEntries;

    public SecondPriorityEntrySchema(Set<BrandEntry> brandEntries, Set<CategoryEntry> categoryEntries, Set<SupplierEntry> supplierEntries) {
        this.brandEntries = brandEntries;
        this.categoryEntries = categoryEntries;
        this.supplierEntries = supplierEntries;
    }

    public Bonus calculation() {
        return null;
    }

    public enum Sort {
        BRAND, CATEGORY, SUPPLIER
    }
}
