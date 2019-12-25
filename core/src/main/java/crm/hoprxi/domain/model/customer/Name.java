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

package crm.hoprxi.domain.model.customer;

import java.util.Objects;
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2019-12-25
 */
public class Name {
    private static final int NAME_MAX_LENGHT = 256;
    private String name;
    private String nickName;

    /**
     * @param name
     * @param nickName equal name if null
     * @throws IllegalArgumentException if name is <code>NULL</code>
     */
    public Name(String name, String nickName) {
        setName(name);
        setNickName(nickName);
    }

    public String nickName() {
        return nickName;
    }

    private void setNickName(String nickName) {
        if (nickName != null && nickName.length() > NAME_MAX_LENGHT)
            throw new IllegalArgumentException("nickName length rang is [1-255]");
        this.nickName = nickName;
    }

    public String name() {
        return name;
    }

    private void setName(String name) {
        name = Objects.requireNonNull(name, "name required").trim();
        if (name.length() > NAME_MAX_LENGHT)
            throw new IllegalArgumentException("name length rang is [1-255]");
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Name name1 = (Name) o;

        if (name != null ? !name.equals(name1.name) : name1.name != null) return false;
        return nickName != null ? nickName.equals(name1.nickName) : name1.nickName == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (nickName != null ? nickName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Name.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("nickName='" + nickName + "'")
                .toString();
    }
}