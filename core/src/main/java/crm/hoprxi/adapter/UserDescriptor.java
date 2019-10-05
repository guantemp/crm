/*
 * @(#}UserDescriptor.java
 *
 * Copyright 2015 www.hoprxi.com rights Reserved.
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
 */
package crm.hoprxi.adapter;

import java.io.Serializable;

/***
 * @author <a href="mailto:myis1000@gmail.com">guan xiangHuan</a>
 * @since JDK7.0
 * @version 0.0.1 2015年12月27日
 */
public final class UserDescriptor implements Serializable {
    public final static UserDescriptor nullDescriptorInstance = new UserDescriptor();
    private static final long serialVersionUID = 1L;
    private String account;
    private long userId;

    private UserDescriptor() {
        super();
    }

    public UserDescriptor(long userId, String account) {
        super();
        this.userId = userId;
        this.account = account;
    }

    public String account() {
        return account;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserDescriptor other = (UserDescriptor) obj;
        if (account == null) {
            if (other.account != null)
                return false;
        } else if (!account.equals(other.account))
            return false;
        if (userId != other.userId)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((account == null) ? 0 : account.hashCode());
        result = prime * result + (int) (userId ^ (userId >>> 32));
        return result;
    }

    public long userId() {
        return userId;
    }
}
