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
import crm.hoprxi.domain.model.spss.Spss;
import org.junit.Assert;
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

        IdentityCard identityCard = new IdentityCard("510107199803073838", "官相焕",
                new crm.hoprxi.domain.model.customer.person.certificates.Address("四川", "乐山市", "市中区", "沙湖路22"));
        Person guan = new Person("18982455056", "官相焕", "111222", Spss.EMPTY_SPSS, null,
                book, identityCard, MonthDay.of(4, 20));
        repository.save(guan);

        Person wang = new Person("18982455062", "王浩", "207896", Spss.EMPTY_SPSS, null,
                new PostalAddressBook(new PostalAddress(new Address(Locale.getDefault(), "四川省", "泸州市", "江阳区", "喝咖啡", "酒谷大道3段3号", "614000"),
                        new Contact("王浩", "18982455062", null))), null, MonthDay.of(6, 5));
        repository.save(wang);
        Person du = new Person("18982435017", "杜红桃", "657895", Spss.EMPTY_SPSS, null,
                null, null, MonthDay.of(11, 12));
        repository.save(du);

        Person yang = new Person("13618514021", "杨安顺", "975421", Spss.EMPTY_SPSS, null,
                new PostalAddressBook(new PostalAddress(new Address(Locale.getDefault(), "贵州", "贵阳市", "南明区", "喝咖啡", "宝山南路208号", "325897"),
                        new Contact("杨安顺", "13618514021", null))), null, MonthDay.of(1, 12));
        repository.save(yang);
    }

    /*
    @AfterClass
    public static void teardown() {
        repository.remove("13618514021");
        repository.remove("18982435017");
        repository.remove("18982455062");
        repository.remove("18982455056");
        repository.remove("18982455056");
    }
*/
    @Test
    public void find() {
        Person guan = repository.find("18982455056");
        Assert.assertNotNull(guan);
        Assert.assertTrue(guan.authenticateTransactionPassword("111222"));
        Person wang = repository.find("18982455062");
        Assert.assertNotNull(wang);
        Assert.assertTrue(wang.authenticateTransactionPassword("207896"));
        Person yang = repository.find("13618514021");
        Assert.assertNotNull(yang);
        yang.changeTransactionPassword("975421", "204316");
        repository.save(yang);
        yang = repository.find("13618514021");
        Assert.assertTrue(yang.authenticateTransactionPassword("204316"));
        Person person = repository.find("18982435016");
        Assert.assertNull(person);
    }

    @Test
    public void findAll() {
        Person[] people = repository.findAll(0, 1);
        Assert.assertTrue(people.length == 1);
        people = repository.findAll(2, 1);
        Assert.assertTrue(people.length == 1);
        people = repository.findAll(0, 5);
        Assert.assertTrue(people.length == 4);
        people = repository.findAll(4, 5);
        Assert.assertTrue(people.length == 0);
    }
}