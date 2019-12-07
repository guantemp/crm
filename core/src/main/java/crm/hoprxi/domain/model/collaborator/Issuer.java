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

import java.util.Objects;
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2019-12-05
 */
public class Issuer {
    private String id;
    private String name;

    public Issuer(String id, String name) {
        setId(id);
        setName(name);
    }

    private void setName(String name) {
        Objects.requireNonNull(name, "name required");
        this.name = name;
    }

    private void setId(String id) {
        Objects.requireNonNull(id, "id required");
        this.id = id;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Issuer.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("name='" + name + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Issuer issuer = (Issuer) o;

        if (id != null ? !id.equals(issuer.id) : issuer.id != null) return false;
        return name != null ? name.equals(issuer.name) : issuer.name == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
