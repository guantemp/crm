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

import com.arangodb.entity.DocumentField;

import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-09-08
 */
public class EntryTemplate implements Cloneable {
    @DocumentField(DocumentField.Type.KEY)
    private String id;
    private String name;
    private Set<ItemEntry> itemEntries;
    private Set<CategoryEntry> categoryEntries;
    private Set<BrandEntry> brandEntries;
    private CommonEntry commonEntry;

    public EntryTemplate(String id, String name, Set<ItemEntry> itemEntries, Set<CategoryEntry> categoryEntries, Set<BrandEntry> brandEntries, CommonEntry commonEntry) {
        setId(id);
        setName(name);
        this.itemEntries = itemEntries;
        this.categoryEntries = categoryEntries;
        this.brandEntries = brandEntries;
        this.commonEntry = commonEntry;
    }

    public EntryTemplate(String id, String name) {
        this(id, name, new HashSet<ItemEntry>(0), new HashSet<CategoryEntry>(0), new HashSet<BrandEntry>(0), CommonEntry.ONE_TO_ONE);
    }

    private void setId(String id) {
        this.id = id;
    }

    private void setName(String name) {
        this.name = name;
    }


    public String id() {
        return id;
    }

    public Set<ItemEntry> itemEntries() {
        return itemEntries;
    }

    public Set<CategoryEntry> categoryEntries() {
        return categoryEntries;
    }

    public Set<BrandEntry> brandEntries() {
        return brandEntries;
    }

    public CommonEntry commonEntry() {
        return commonEntry;
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntryTemplate)) return false;

        EntryTemplate that = (EntryTemplate) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public void add(Entry entry) {
        String className = entry.getClass().getName();
        switch (className) {
            case "crm.hoprxi.core.domain.model.bonus.consumption.ItemEntry":
                itemEntries.add((ItemEntry) entry);
                break;
            case "crm.hoprxi.core.domain.model.bonus.consumption.CategoryEntry":
                categoryEntries.add((CategoryEntry) entry);
                break;
            case "crm.hoprxi.core.domain.model.bonus.consumption.BrandEntry":
                brandEntries.add((BrandEntry) entry);
                break;
            case "crm.hoprxi.core.domain.model.bonus.consumption.CommonEntry":
                this.commonEntry = (CommonEntry) entry;
                break;
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", EntryTemplate.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("name='" + name + "'")
                .add("itemEntries=" + itemEntries)
                .add("categoryEntries=" + categoryEntries)
                .add("brandEntries=" + brandEntries)
                .add("commonEntry=" + commonEntry)
                .toString();
    }
}
