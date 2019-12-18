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

package crm.hoprxi.domain.model.collaborator;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-08-07
 */
public class Category {
    private String id;
    private String name;
    private String parentId;

    public Category(String parentId, String id, String name) {
        setParentId(parentId);
        setId(id);
        setName(name);
    }

    public String identity() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String name() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String parentIdentity() {
        return parentId;
    }

    private void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        return id != null ? id.equals(category.id) : category.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Category{" +
                "identity='" + id + '\'' +
                ", name='" + name + '\'' +
                ", parentIdentity='" + parentId + '\'' +
                '}';
    }
}
