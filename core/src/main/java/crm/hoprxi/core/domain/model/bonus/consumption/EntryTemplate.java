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

import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-09-08
 */
public class EntryTemplate implements Cloneable {
    private String name;
    private Set<Entry<?>> entries;

    public EntryTemplate(String name) {
        this.name = name;
    }

    public EntryTemplate(String name, Set<Entry<?>> entries) {
        this.name = name;
        this.entries = entries;
    }

    public <T> EntryTemplate add(Entry<T> entry) {
        if (entries == null)
            entries = new HashSet<>();
        entries.add(entry);
        return new EntryTemplate(name, entries);
    }

    public <T> void remove(Entry<T> entry) {
        if (entries == null)
            return;
        entries.remove(entry);
    }

    public String name() {
        return name;
    }

    public Set<Entry<?>> getEntries() {
        return entries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntryTemplate)) return false;

        EntryTemplate that = (EntryTemplate) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", EntryTemplate.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("entries=" + entries)
                .toString();
    }
}
