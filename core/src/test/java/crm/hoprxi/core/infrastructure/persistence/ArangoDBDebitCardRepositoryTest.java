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

import crm.hoprxi.core.domain.model.balance.Balance;
import crm.hoprxi.core.domain.model.balance.SmallChangDenominationEnum;
import crm.hoprxi.core.domain.model.balance.SmallChange;
import crm.hoprxi.core.domain.model.card.DebitCard;
import crm.hoprxi.core.domain.model.card.DebitCardRepository;
import crm.hoprxi.core.domain.model.collaborator.Address;
import crm.hoprxi.core.domain.model.collaborator.Contact;
import crm.hoprxi.core.domain.model.collaborator.Issuer;
import crm.hoprxi.core.domain.model.customer.PostalAddress;
import crm.hoprxi.core.domain.model.customer.person.Person;
import crm.hoprxi.core.domain.model.customer.person.PersonRepository;
import crm.hoprxi.core.domain.model.customer.person.PostalAddressBook;
import crm.hoprxi.core.domain.model.customer.person.certificates.IdentityCard;
import crm.hoprxi.core.domain.model.spss.Spss;
import org.javamoney.moneta.Money;
import org.junit.AfterClass;
import org.junit.Assert;
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
        PostalAddressBook addressBook = new PostalAddressBook();
        PostalAddress address1 = new PostalAddress(Address.chinaAddress("四川省", "泸州市", "龙马潭区", "小市街道", "双井沟38号", "614000"),
                new Contact("富含糖分", "18982455055", "0830-2517218"));
        addressBook = addressBook.add(address1);
        PostalAddress four = new PostalAddress(new Address(Locale.CANADA, "四川", "乐山市", "市中区", "消粑粑街道", "大田路22", "664000"),
                new Contact("符合停机和", "13679692401", "0833-3217589"));
        addressBook = addressBook.addAndSetAcquiescence(four);

        IdentityCard identityCard = new IdentityCard("510107199803073838", "的习惯如",
                new crm.hoprxi.core.domain.model.customer.SimplifyAddress("四川", "乐山市", "附加费", "沙湖路22"));
        Person guan = new Person("18982455055", "hope xi'er", true, Spss.EMPTY_SPSS, null, addressBook, identityCard, MonthDay.of(6, 4));
        personRepository.save(guan);

        DebitCard card1 = new DebitCard(new Issuer("968974548754158X", "小市店"), "18982455055", "667788", "112233", "888888", true, Balance.zero(Locale.CHINESE), SmallChange.zero(Locale.CHINESE), null);
        repository.save(card1);
        DebitCard card2 = new DebitCard(new Issuer("963457MA120486PX", "山岩瑙"), "18982455055", "778899", "123456", "999999", true, Balance.zero(Locale.CHINESE), SmallChange.zero(Locale.CHINESE), null);
        repository.save(card2);
        DebitCard card3 = new DebitCard(new Issuer("98752367MA24158X", "大山平"), "18982455055", "889900", "123456", "618888", true, Balance.zero(Locale.US), SmallChange.zero(Locale.US), null);
        repository.save(card3);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        //repository.remove("667788");
        //repository.remove("778899");
        //repository.remove("889900");
        //personRepository.remove("18982455056");
    }

    @Test
    public void save() {
        DebitCard debitCard = repository.find("667788");
        Assert.assertTrue(debitCard.authenticatePassword("112233"));
        debitCard.changeCardFaceNumber("777777");
        repository.save(debitCard);
        debitCard = repository.find("667788");
        Assert.assertEquals("777777", debitCard.cardFaceNumber());
        debitCard.credit(Money.of(200, "CNY"));
        debitCard.awardRedEnvelope(Money.of(20, "CNY"));
        debitCard.changeSmallChangDenominationEnum(SmallChangDenominationEnum.ONE);
        debitCard.debit(Money.of(62.75, "CNY"));
        repository.save(debitCard);
        DebitCard[] debitCards = repository.findByCardFaceNumber("777777");
        Assert.assertTrue(debitCards.length == 1);
        debitCard = repository.find("889900");
        debitCard.credit(Money.of(50, "USD"));
        repository.save(debitCard);
    }

    @Test
    public void find() {
        DebitCard debitCard = repository.find("667788");
        Assert.assertNotNull(debitCard);
        debitCard = repository.find("778899");
        Assert.assertNotNull(debitCard);
    }


    @Test
    public void findBy() {
        DebitCard[] debitCards = repository.findByCustomer("18982455055");
        Assert.assertEquals(3, debitCards.length);
        debitCards = repository.findByCardFaceNumber("999999");
        Assert.assertEquals(1, debitCards.length);
    }
}