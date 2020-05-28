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
import crm.hoprxi.core.domain.model.card.CreditCard;
import crm.hoprxi.core.domain.model.card.CreditCardRepository;
import crm.hoprxi.core.domain.model.card.LineOfCredit;
import crm.hoprxi.core.domain.model.card.TermOfValidity;
import crm.hoprxi.core.domain.model.collaborator.Contact;
import crm.hoprxi.core.domain.model.collaborator.Issuer;
import crm.hoprxi.core.domain.model.collaborator.SimplifyAddress;
import crm.hoprxi.core.domain.model.customer.PostalAddress;
import crm.hoprxi.core.domain.model.customer.person.Person;
import crm.hoprxi.core.domain.model.customer.person.PersonRepository;
import crm.hoprxi.core.domain.model.customer.person.PostalAddressBook;
import crm.hoprxi.core.domain.model.customer.person.certificates.IdentityCard;
import crm.hoprxi.core.domain.model.spss.Spss;
import org.javamoney.moneta.FastMoney;
import org.javamoney.moneta.Money;
import org.junit.Assert;

import java.time.MonthDay;
import java.util.Locale;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-05-23
 */
public class ArangoDBCreditCardRepositoryTest {
    private static final CreditCardRepository repository = new ArangoDBCreditCardRepository("crm");
    private static final PersonRepository personRepository = new ArangoDBPersonRepository("crm");

    @org.testng.annotations.BeforeMethod
    public void setUp() {
        PostalAddressBook book = new PostalAddressBook();
        PostalAddress address1 = new PostalAddress(SimplifyAddress.chinaAddress("四川省", "泸州市", "龙马潭区", "茜草街道", "碧桂园5栋四单元902", "614000"),
                new Contact("五人读过", "18982455062", "0830-2581218"));
        book = book.add(address1);
        PostalAddress four = new PostalAddress(new SimplifyAddress(Locale.CANADA, "甘肃省", "临夏直至区", "政和县", "撒旦镇", "阿萨是发", "515000"),
                new Contact("杨的合同", "13679695687", "0852-3217589"));
        book = book.addAndSetAcquiescence(four);

        IdentityCard identityCard = new IdentityCard("510107199803073838", "杨史蒂夫",
                new crm.hoprxi.core.domain.model.customer.SimplifyAddress("四川", "临夏直至区", "政和县", "撒旦镇"));
        Person wang = new Person("18982455066", "不晓得都是富人的干扰", true, Spss.EMPTY_SPSS, null, book, identityCard, MonthDay.of(4, 20));
        personRepository.save(wang);

        LineOfCredit lineOfCredit = new LineOfCredit(FastMoney.of(1000, "CNY"), 30);
        CreditCard card1 = new CreditCard(new Issuer("968974548754158X", "小市店"), "18982455066", "20281405", "112233", "81405",
                true, TermOfValidity.PERMANENCE, lineOfCredit, Balance.zero(Locale.CHINESE), SmallChange.zero(Locale.CHINESE), null);
        repository.save(card1);
        lineOfCredit = new LineOfCredit(Money.of(997853544515180.565574579, "CNY"), 7);
        CreditCard card2 = new CreditCard(new Issuer("963457MA120486PX", "山岩瑙"), "18982455066", "202801654", "123456", "801654",
                true, TermOfValidity.PERMANENCE, lineOfCredit, Balance.zero(Locale.CHINESE), SmallChange.zero(Locale.CHINESE), null);
        repository.save(card2);
        lineOfCredit = new LineOfCredit(FastMoney.of(368547.05, "CNY"), 60);
        CreditCard card3 = new CreditCard(new Issuer("98752367MA24158X", "大山平"), "18982455066", "202601402", "123456", "801402",
                true, TermOfValidity.PERMANENCE, lineOfCredit, Balance.zero(Locale.US), SmallChange.zero(Locale.US), null);
        repository.save(card3);
    }

    @org.testng.annotations.AfterMethod
    public void tearDown() {
        //repository.remove("202601402");
        //repository.remove("202801654");
        //repository.remove("20281405");
        //personRepository.remove("18982455066");
    }

    @org.testng.annotations.Test
    public void testSave() {
        LineOfCredit lineOfCredit = new LineOfCredit(FastMoney.of(250, "CNY"), 60);
        CreditCard card = new CreditCard(new Issuer("968974548754158X", "小市店"), "18982455066", "20281725", "112233", "81725",
                false, TermOfValidity.PERMANENCE, lineOfCredit, new Balance(Money.of(-79.85, "CNY"), Money.of(15.25, "CNY")),
                new SmallChange(FastMoney.of(0.64, "CNY"), SmallChangDenominationEnum.ONE), null);
        repository.save(card);
    }

    @org.testng.annotations.Test
    public void testFind() {
        CreditCard creditCard = repository.find("202601402");
        Assert.assertNotNull(creditCard);
        creditCard = repository.find("202801654");
        Assert.assertNotNull(creditCard);
        creditCard = repository.find("20280154");
        Assert.assertNull(creditCard);
        creditCard = repository.find("20281725");
        Assert.assertNotNull(creditCard);
        System.out.println(creditCard);
    }

    @org.testng.annotations.Test
    public void testFindByCardFaceNumber() {
        CreditCard[] creditCards = repository.findByCardFaceNumber("^801");
        Assert.assertEquals(2, creditCards.length);
        creditCards = repository.findByCardFaceNumber("1402");
        Assert.assertEquals(1, creditCards.length);
        creditCards = repository.findByCardFaceNumber("40");
        Assert.assertEquals(2, creditCards.length);
    }

    @org.testng.annotations.Test
    public void testFindByCustomer() {
        CreditCard[] creditCards = repository.findByCustomer("18982455066");
        Assert.assertEquals(4, creditCards.length);
        creditCards = repository.findByCustomer("18982455065");
        Assert.assertEquals(0, creditCards.length);
    }

    @org.testng.annotations.Test
    public void testFindAll() {
        CreditCard[] creditCards = repository.findAll(0, 3);
        Assert.assertEquals(3, creditCards.length);
        creditCards = repository.findAll(0, 2);
        Assert.assertEquals(2, creditCards.length);
        creditCards = repository.findAll(1, 2);
        Assert.assertEquals(2, creditCards.length);
    }

    @org.testng.annotations.Test
    public void testSize() {
        int size = repository.size();
        Assert.assertEquals(4, size);
    }
}