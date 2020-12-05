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

package crm.hoprxi.core.infrastructure.persistence;

import crm.hoprxi.core.domain.model.collaborator.Address;
import crm.hoprxi.core.domain.model.collaborator.Contact;
import crm.hoprxi.core.domain.model.customer.PostalAddress;
import crm.hoprxi.core.domain.model.customer.person.Person;
import crm.hoprxi.core.domain.model.customer.person.PostalAddressBook;
import crm.hoprxi.core.domain.model.customer.person.certificates.IdentityCard;
import crm.hoprxi.core.domain.model.spss.Spss;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.MonthDay;
import java.util.Locale;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-12-25
 */
public class ArangoDBPersonRepositoryTest {
    private static final ArangoDBPersonRepository repository = new ArangoDBPersonRepository("crm");

    @BeforeClass
    public static void setUpBeforeClass() {
        PostalAddressBook book = new PostalAddressBook();
        PostalAddress address1 = new PostalAddress(Address.chinaAddress("四川省", "泸州市", "龙马潭区", "抵抗力", "还得快", "614200"),
                Contact.withMobilePhone("官相焕", "18982455855"));
        book = book.add(address1);
        PostalAddress address2 = new PostalAddress(Address.chinaAddress("四川省", "泸州市", "龙马潭区", "到黄昏", "规划局", "614300"),
                Contact.withTelephone("官相焕", "0830-2588210"));
        book = book.add(address2);
        PostalAddress four = new PostalAddress(new Address(Locale.CANADA, "四川", "泸州", "龙马潭区", "鱼塘街道", "沙湖路22", "614070"),
                new Contact("库电话", "13679692481", "0830-3217888"));
        book = book.addAndSetAcquiescence(four);

        IdentityCard identityCard = new IdentityCard("510107199803073838", "官相焕",
                new crm.hoprxi.core.domain.model.customer.SimplifyAddress("四川", "乐山市", "市中区", "沙湖路22"));
        Person guan = new Person("18982455855", "官相焕", false, Spss.EMPTY_SPSS, null,
                book, identityCard, MonthDay.of(4, 20));
        repository.save(guan);

        Person wang = new Person("18982455866", "王浩", true, Spss.EMPTY_SPSS, null,
                new PostalAddressBook(new PostalAddress(new Address(Locale.getDefault(), "四川省", "泸州市", "江阳区", "喝咖啡", "酒谷大道3段3号", "616000"),
                        new Contact("王浩", "18982455866", null))), null, MonthDay.of(6, 5));
        repository.save(wang);
        Person du = new Person("18982435835", "杜红桃", false, Spss.EMPTY_SPSS, null,
                null, null, MonthDay.of(11, 12));
        repository.save(du);

        Person yang = new Person("13618514821", "杨安顺", false, Spss.EMPTY_SPSS, null,
                new PostalAddressBook(new PostalAddress(new Address(Locale.getDefault(), "贵州", "贵阳市", "南明区", "电视台", "宝山南路208号", "325897"),
                        new Contact("杨安顺", "13618514821", null))), null, MonthDay.of(1, 12));
        repository.save(yang);
    }

    /*
        @AfterClass
        public static void teardownAfterClass() {
            repository.remove("13618514821");
            repository.remove("18982435835");
            repository.remove("18982455866");
            repository.remove("18982455855");
            repository.remove("18982455855");
        }
    */
    @Test
    public void find() {
        Person guan = repository.find("18982455855");
        Assert.assertNotNull(guan);
        System.out.println(guan);
        Person wang = repository.find("18982455866");
        Assert.assertNotNull(wang);
        Person yang = repository.find("13618514821");
        Assert.assertNotNull(yang);
        repository.save(yang);
        yang = repository.find("13618514821");
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
        Assert.assertTrue(people.length == 5);
        people = repository.findAll(4, 5);
        Assert.assertTrue(people.length == 1);
    }
}