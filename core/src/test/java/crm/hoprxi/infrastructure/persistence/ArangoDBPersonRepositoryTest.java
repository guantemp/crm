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

package crm.hoprxi.infrastructure.persistence;

import crm.hoprxi.domain.model.collaborator.Address;
import crm.hoprxi.domain.model.collaborator.Contact;
import crm.hoprxi.domain.model.customer.PostalAddress;
import crm.hoprxi.domain.model.customer.person.Person;
import crm.hoprxi.domain.model.customer.person.PostalAddressBook;
import crm.hoprxi.domain.model.customer.person.certificates.IdentityCard;
import crm.hoprxi.domain.model.spss.Data;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.MonthDay;
import java.util.Locale;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-12-19
 */
public class ArangoDBPersonRepositoryTest {
    private static final ArangoDBPersonRepository repository = new ArangoDBPersonRepository("crm");

    @BeforeClass
    public static void setUpBeforeClass() {
        PostalAddressBook book = new PostalAddressBook();
        PostalAddress address1 = new PostalAddress(Address.chinaAddress("四川省", "泸州市", "龙马潭区", "小市街道", "双井沟38号", "614000"),
                Contact.withMobilePhone("官相焕", "18982455056"));
        book = book.add(address1);
        PostalAddress address2 = new PostalAddress(Address.chinaAddress("四川省", "泸州市", "龙马潭区", "小市街道", "双井沟38号", "614000"),
                Contact.withTelephone("官相焕", "0830-2517210"));
        book = book.add(address2);
        PostalAddress four = new PostalAddress(new Address(Locale.CANADA, "四川", "泸州", "龙马潭区", "鱼塘街道", "沙湖路22", "614000"),
                new Contact("库电话", "13679692401", "0830-3217589"));
        book = book.addAndSetAcquiescence(four);

        IdentityCard identityCard = new IdentityCard("510107197606240057", "官相焕",
                new crm.hoprxi.domain.model.customer.person.certificates.Address("四川", "乐山市", "市中区", "沙湖路22"));
        Person guan = new Person("18982455056", "官相焕", "111220", Data.EMPTY_DATA, null,
                book, identityCard, MonthDay.of(4, 20));
        repository.save(guan);

        Person wang = new Person("18982455062", "王浩", "207896", Data.EMPTY_DATA, null,
                null, null, MonthDay.of(6, 5));
        repository.save(wang);
        Person du = new Person("18982435017", "杜红桃", "", Data.EMPTY_DATA, null,
                null, null, MonthDay.of(11, 12));
        repository.save(du);
    }

    @Test
    public void save() {
    }

    @Test
    public void find() {
    }

    @Test
    public void findAll() {
    }
}