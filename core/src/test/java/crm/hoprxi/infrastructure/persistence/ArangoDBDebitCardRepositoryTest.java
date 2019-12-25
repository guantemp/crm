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

import crm.hoprxi.domain.model.balance.Balance;
import crm.hoprxi.domain.model.balance.SmallChange;
import crm.hoprxi.domain.model.card.DebitCard;
import crm.hoprxi.domain.model.card.DebitCardRepository;
import crm.hoprxi.domain.model.card.TermOfValidity;
import crm.hoprxi.domain.model.collaborator.Address;
import crm.hoprxi.domain.model.collaborator.Contact;
import crm.hoprxi.domain.model.collaborator.Issuer;
import crm.hoprxi.domain.model.customer.PostalAddress;
import crm.hoprxi.domain.model.customer.person.Person;
import crm.hoprxi.domain.model.customer.person.PersonRepository;
import crm.hoprxi.domain.model.customer.person.PostalAddressBook;
import crm.hoprxi.domain.model.customer.person.certificates.IdentityCard;
import crm.hoprxi.domain.model.spss.Spss;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.MonthDay;
import java.util.Locale;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-12-24
 */
public class ArangoDBDebitCardRepositoryTest {
    private static final DebitCardRepository repository = new ArangoDBDebitCardRepository("crm");
    private static final PersonRepository personRepository = new ArangoDBPersonRepository("crm");

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Issuer issuer = new Issuer("9634578512046PX", "小市店");

        PostalAddressBook book = new PostalAddressBook();
        PostalAddress address1 = new PostalAddress(Address.chinaAddress("四川省", "泸州市", "龙马潭区", "小市街道", "双井沟38号", "614000"),
                new Contact("官相焕", "18982455056", "0830-2517218"));
        book = book.add(address1);
        PostalAddress four = new PostalAddress(new Address(Locale.CANADA, "四川", "泸州", "龙马潭区", "鱼塘街道", "沙湖路22", "614000"),
                new Contact("库电话", "13679692401", "0830-3217589"));
        book = book.addAndSetAcquiescence(four);

        IdentityCard identityCard = new IdentityCard("510107199803073838", "官相焕",
                new crm.hoprxi.domain.model.customer.person.certificates.Address("四川", "乐山市", "市中区", "沙湖路22"));
        Person guan = new Person("18982455056", "hope xi'er", "333222", true, Spss.EMPTY_SPSS, null,
                book, identityCard, MonthDay.of(6, 4));
        personRepository.save(guan);

        DebitCard card1 = new DebitCard(issuer, "18982455056", "668888", "975426", "888888", TermOfValidity.PERMANENCE, Balance.zero(Locale.CHINESE), SmallChange.zero(Locale.CHINESE), null);
        repository.save(card1);
        DebitCard card2 = new DebitCard(issuer, "18982455056", "668889", "123456", "999999", TermOfValidity.PERMANENCE, Balance.zero(Locale.CHINESE), SmallChange.zero(Locale.CHINESE), null);
        repository.save(card2);
    }

    @Test
    public void save() {
    }

    @Test
    public void authenticCredentials() {
    }

    @Test
    public void remove() {
    }

    @Test
    public void find() {
    }

    @Test
    public void findAll() {
    }

    @Test
    public void findByCustomer() {
    }

    @Test
    public void findByCardFaceNumber() {
    }
}