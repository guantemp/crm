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
public final class Category {
    private String identity;
    private String name;
    private String parentIdentity;

    public Category(String parentIdentity, String identity, String name) {
        setParentIdentity(parentIdentity);
        setIdentity(identity);
        setName(name);
    }

    public String identity() {
        return identity;
    }

    private void setIdentity(String identity) {
        this.identity = identity;
    }

    public String name() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String parentIdentity() {
        return parentIdentity;
    }

    private void setParentIdentity(String parentIdentity) {
        this.parentIdentity = parentIdentity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        return identity != null ? identity.equals(category.identity) : category.identity == null;
    }

    @Override
    public int hashCode() {
        return identity != null ? identity.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Category{" +
                "identity='" + identity + '\'' +
                ", name='" + name + '\'' +
                ", parentIdentity='" + parentIdentity + '\'' +
                '}';
    }
}
