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

package crm.hoprxi.domain.model.collaborator;

import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-08-04
 */
public final class Supplier {
    private String identity;
    private String name;

    public Supplier(String identity, String name) {
        setIdentity(identity);
        setName(name);
    }

    private void setName(String name) {
        Objects.requireNonNull(name, "name require");
        this.name = name;
    }

    private void setIdentity(String identity) {
        Objects.requireNonNull(identity, "identity required");
        this.identity = identity;
    }


    public String identity() {
        return identity;
    }

    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "identity='" + identity + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Supplier supplier = (Supplier) o;

        return identity != null ? identity.equals(supplier.identity) : supplier.identity == null;
    }

    @Override
    public int hashCode() {
        return identity != null ? identity.hashCode() : 0;
    }
}
