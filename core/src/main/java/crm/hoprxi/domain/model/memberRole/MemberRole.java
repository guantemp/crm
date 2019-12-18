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

package crm.hoprxi.domain.model.memberRole;

import com.arangodb.entity.DocumentField;
import crm.hoprxi.domain.model.card.appearance.Appearance;
import crm.hoprxi.domain.model.customer.Customer;
import crm.hoprxi.domain.model.power.PowerBook;
import crm.hoprxi.domain.model.spss.Data;

import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.2 builder 2019-08-23
 */
public class MemberRole implements Comparable<MemberRole> {
    @DocumentField(DocumentField.Type.KEY)
    private String id;
    private String name;
    private Data lowerLimit;
    private PowerBook powerBook;
    private Appearance cardAppearance;

    public MemberRole(String id, String name, Data lowerLimit, Appearance cardAppearance, PowerBook powerBook) {
        setId(id);
        setName(name);
        setLowerLimit(lowerLimit);
        setPowerBook(powerBook);
        this.cardAppearance = cardAppearance;
    }

    public MemberRole(String id, String name, Data credite) {
        this(id, name, credite, null, null);
    }

    public void rename(String name) {
        Objects.requireNonNull(name, "name required");
        if (!this.name.equals(name)) {
            this.name = name;
        }
    }

    private void setName(String name) {
        this.name = Objects.requireNonNull(name, "name is required");
    }

    public boolean isCurrentMemberRole(Customer customer) {
        return lowerLimit.compareTo(customer.data()) >= 0 ? true : false;
    }

    private void setLowerLimit(Data lowerLimit) {
        if (lowerLimit == null)
            lowerLimit = Data.EMPTY_DATA;
        this.lowerLimit = lowerLimit;
    }

    private void setPowerBook(PowerBook powerBook) {
        if (powerBook == null)
            powerBook = PowerBook.NO_EQUITY;
        this.powerBook = powerBook;
    }

    public PowerBook equityBook() {
        return powerBook;
    }

    public void setId(String id) {
        this.id = Objects.requireNonNull(id, "id is required");
    }

    public Appearance cardAppearance() {
        return cardAppearance;
    }


    @Override
    public int compareTo(MemberRole o) {
        return lowerLimit.compareTo(o.lowerLimit);
    }
}
