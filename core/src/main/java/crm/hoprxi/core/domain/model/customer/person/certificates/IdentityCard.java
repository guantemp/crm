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
package crm.hoprxi.core.domain.model.customer.person.certificates;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;
import java.util.regex.Pattern;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2019-07-19
 */
public class IdentityCard {
    private static final Pattern NUMBER = Pattern.compile("^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|31)|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}([0-9]|x|X)$");
    private String number;
    private String name;
    private SimplifyAddress simplifyAddress;

    public IdentityCard(String number, String name, SimplifyAddress simplifyAddress) {
        this.number = number;
        this.name = name;
        this.simplifyAddress = simplifyAddress;
    }

    public Sex sex() {
        String sCardNum = number.substring(16, 17);
        if (Integer.parseInt(sCardNum) % 2 != 0) {
            return Sex.MAN;
        } else {
            return Sex.WOMEN;
        }
    }


    public MonthDay birthday() {
        LocalDate localDate = LocalDate.parse(number.substring(6, 14), DateTimeFormatter.ISO_LOCAL_DATE);
        return MonthDay.of(localDate.getMonth(), localDate.getDayOfMonth());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IdentityCard that = (IdentityCard) o;

        return number != null ? number.equals(that.number) : that.number == null;
    }

    @Override
    public int hashCode() {
        return number != null ? number.hashCode() : 0;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", IdentityCard.class.getSimpleName() + "[", "]")
                .add("number='" + number + "'")
                .add("name='" + name + "'")
                .add("address=" + simplifyAddress)
                .toString();
    }
}
