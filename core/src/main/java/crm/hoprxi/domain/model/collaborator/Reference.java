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

import mi.foxtail.to.ByteToHex;

import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-08-25
 */
public final class Reference {
    private String identity;
    private String name;

    public Reference(String identity, String name) {
        setIdentity(identity);
        setName(name);
    }

    private void setIdentity(String identity) {
        Objects.requireNonNull(identity, "identity required");
        if (!ByteToHex.isIdentityHexStr(identity))
            throw new IllegalArgumentException("Illegal identity");
        this.identity = identity;
    }

    private void setName(String name) {
        Objects.requireNonNull(name, "name required");
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reference reference = (Reference) o;

        return identity != null ? identity.equals(reference.identity) : reference.identity == null;
    }

    @Override
    public int hashCode() {
        return identity != null ? identity.hashCode() : 0;
    }
}
