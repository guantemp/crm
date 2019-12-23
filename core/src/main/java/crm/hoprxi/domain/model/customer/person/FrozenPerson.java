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
import crm.hoprxi.domain.model.customer.person.certificates.IdentityCard;
import crm.hoprxi.domain.model.spss.Data;

import java.net.URI;
import java.time.MonthDay;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2019-12-23
 */
public class FrozenPerson extends Customer {
    private PostalAddressBook postalAddressBook;
    private IdentityCard identityCard;
    private MonthDay birthday;

    private FrozenPerson(String id, String name, Data data, URI headPortrait,
                         PostalAddressBook postalAddressBook, IdentityCard identityCard, MonthDay birthday) {
        super(id, name, data, headPortrait);
        this.postalAddressBook = postalAddressBook;
        this.identityCard = identityCard;
        this.birthday = birthday;
    }

    protected FrozenPerson(String id, String name, String transactionPassword, Data data, URI headPortrait,
                           PostalAddressBook postalAddressBook, IdentityCard identityCard, MonthDay birthday) {
        super(id, name, data, headPortrait);
        super.transactionPassword = transactionPassword;
        this.postalAddressBook = postalAddressBook;
        this.identityCard = identityCard;
        this.birthday = birthday;
    }


    public PostalAddressBook postalAddressBook() {
        return postalAddressBook;
    }

    public IdentityCard idCard() {
        return identityCard;
    }

    public MonthDay birthday() {
        return birthday;
    }

    public Person unfreeze() {
        return new Person(id(), name(), super.transactionPassword, data(), headPortrait(), postalAddressBook, identityCard, birthday);
    }
}
