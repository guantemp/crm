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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-07-13
 */
public class Contact {
    private static final Pattern CHINA_MOBILE_PHONE = Pattern.compile("^[1](([3][0-9])|([4][5,7,9])|([5][^4,6,9])|([6][6])|([7][3,5,6,7,8])|([8][0-9])|([9][8,9]))[0-9]{8}$");
    private static final Pattern CHINA_TELEPHONE = Pattern.compile("^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$");
    private String telephone;
    private String mobilePhone;
    private String fullName;

    public Contact(String fullName, String mobilePhone, String telephone) {
        setFullName(fullName);
        setPhone(mobilePhone, telephone);
    }

    private void setFullName(String fullName) {
        this.fullName = Objects.requireNonNull(fullName, "fullName required");
    }

    private void setPhone(String mobilePhone, String telephone) {
        if (mobilePhone == null && telephone == null)
            throw new IllegalArgumentException("mobilePhone or telephone needs at least one");
        if (mobilePhone != null)
            setMobilePhone(mobilePhone);
        if (telephone != null)
            setTelephone(telephone);
    }

    private void setMobilePhone(String mobilePhone) {
        Matcher matcher = CHINA_MOBILE_PHONE.matcher(mobilePhone);
        if (!matcher.matches())
            throw new IllegalArgumentException("Not a valid cell phone number!");
        this.mobilePhone = mobilePhone;
    }

    private void setTelephone(String telephone) {
        Matcher matcher = CHINA_TELEPHONE.matcher(telephone);
        if (!matcher.matches())
            throw new IllegalArgumentException("Not a valid telephone number!");
        this.telephone = telephone;
    }

    public String telephone() {
        return telephone;
    }

    public String mobilePhone() {
        return mobilePhone;
    }

    public String fullName() {
        return fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (telephone != null ? !telephone.equals(contact.telephone) : contact.telephone != null) return false;
        if (mobilePhone != null ? !mobilePhone.equals(contact.mobilePhone) : contact.mobilePhone != null) return false;
        return fullName != null ? fullName.equals(contact.fullName) : contact.fullName == null;
    }

    @Override
    public int hashCode() {
        int result = telephone != null ? telephone.hashCode() : 0;
        result = 31 * result + (mobilePhone != null ? mobilePhone.hashCode() : 0);
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "telephone='" + telephone + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
