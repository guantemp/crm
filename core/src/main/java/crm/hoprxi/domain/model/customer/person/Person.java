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

package crm.hoprxi.domain.model.customer.person;

import crm.hoprxi.domain.model.customer.Customer;
import crm.hoprxi.domain.model.customer.PostalAddress;
import crm.hoprxi.domain.model.customer.person.certificates.IdentityCard;
import crm.hoprxi.domain.model.spss.Data;

import java.net.URI;
import java.time.MonthDay;
import java.util.Objects;
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-11-26
 */
public class Person extends Customer {
    private PostalAddressBook postalAddressBook;
    private IdentityCard identityCard;
    private MonthDay birthday;
    private boolean freeze;

    public Person(String id, String name) {
        this(id, name, "", Data.EMPTY_DATA, null, null, null, null);
    }

    private Person(String id, String name, Data data, URI headPortrait,
                   PostalAddressBook postalAddressBook, IdentityCard identityCard, MonthDay birthday) {
        super(id, name, data, headPortrait);
        this.postalAddressBook = postalAddressBook;
        this.identityCard = identityCard;
        this.birthday = birthday;
    }

    /**
     * @param id
     * @param name
     * @param transactionPassword
     * @param data
     * @param headPortrait
     * @param postalAddressBook
     * @param identityCard
     * @param birthday
     */
    public Person(String id, String name, String transactionPassword, Data data, URI headPortrait,
                  PostalAddressBook postalAddressBook, IdentityCard identityCard, MonthDay birthday) {
        super(id, name, transactionPassword, data, headPortrait);
        this.postalAddressBook = postalAddressBook;
        this.identityCard = identityCard;
        this.birthday = birthday;
    }


    public PostalAddressBook postalAddressBook() {
        return postalAddressBook;
    }

    public void addPostalAddress(PostalAddress address) {
        Objects.requireNonNull(address, "address required");
        PostalAddressBook temp = postalAddressBook.add(address);
        if (temp != postalAddressBook) {
            postalAddressBook = temp;
        }
    }

    public void removePostalAddress(PostalAddress address) {
        Objects.requireNonNull(address, "address required");
        PostalAddressBook temp = postalAddressBook.remove(address);
        if (temp != postalAddressBook) {
            postalAddressBook = temp;
        }
    }

    public void changeAcquiescencePostalAddress(PostalAddress address) {
        Objects.requireNonNull(address, "address required");
        PostalAddressBook temp = postalAddressBook.changeAcquiescencePostalAddress(address);
        if (temp != postalAddressBook) {
            postalAddressBook = temp;
        }
    }

    public IdentityCard identityCard() {
        return identityCard;
    }

    public void changeIdentityCard(IdentityCard newIdentityCard) {
        Objects.requireNonNull(newIdentityCard, "newIdentityCard required");
        if (!identityCard.equals(newIdentityCard)) {
            identityCard = newIdentityCard;
        }
    }

    public MonthDay birthday() {
        return birthday;
    }

    public void changeBirthday(MonthDay newBirthday) {
        Objects.requireNonNull(newBirthday, "newBirthday required");
        if (!birthday.equals(newBirthday)) {
            this.birthday = newBirthday;
        }
    }

    public boolean isTodayBirthday() {
        return MonthDay.now().compareTo(birthday) == 0 ? true : false;
    }

    public boolean isFreeze() {
        return freeze;
    }

    public void freeze() {
        freeze = true;
    }

    public void unfreeze() {
        freeze = false;
    }

    public PersonSnapshot toSnapshot() {
        return null;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Person.class.getSimpleName() + "[", "]")
                .add("super=" + super.toString())
                .add("postalAddressBook=" + postalAddressBook)
                .add("identityCard=" + identityCard)
                .add("birthday=" + birthday)
                .toString();
    }
}
