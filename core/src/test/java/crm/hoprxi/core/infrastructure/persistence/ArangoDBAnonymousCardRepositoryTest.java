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
import crm.hoprxi.core.domain.model.bonus.Bonus;
import crm.hoprxi.core.domain.model.card.AnonymousCard;
import crm.hoprxi.core.domain.model.card.AnonymousCardRepository;
import crm.hoprxi.core.domain.model.card.TermOfValidity;
import crm.hoprxi.core.domain.model.collaborator.Issuer;
import org.javamoney.moneta.Money;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Locale;


/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-06-11
 */
public class ArangoDBAnonymousCardRepositoryTest {
    private static AnonymousCardRepository repository = new ArangoDBAnonymousCardRepository("crm");

    @BeforeClass
    public void setUpBeforeClass() {
        Issuer issuer = new Issuer("9678512046PX", "总公司");
        AnonymousCard a1 = new AnonymousCard(issuer, "a1", "22156789", TermOfValidity.PERMANENCE, Balance.zero(Locale.CHINESE), SmallChange.zero(Locale.CHINESE), Bonus.ZERO, null);
        repository.save(a1);
        AnonymousCard a2 = new AnonymousCard(issuer, "a2", "22156790", TermOfValidity.PERMANENCE, Balance.zero(Locale.CHINESE), SmallChange.zero(Locale.CHINESE), Bonus.ZERO, null);
        repository.save(a2);
        issuer = new Issuer("9678512046PX", "self");
        AnonymousCard a3 = new AnonymousCard(issuer, "a3", "22156791", TermOfValidity.PERMANENCE, Balance.zero(Locale.CHINESE), SmallChange.zero(Locale.CHINESE), Bonus.ZERO, null);
        repository.save(a3);
    }

    @AfterClass
    public void tearDownAfterClass() throws Exception {
        //repository.remove("a1");
        //repository.remove("a2");
        //repository.remove("a3");
    }


    @Test
    public void testSave() {
        AnonymousCard a = repository.find("a2");
        Assert.assertNotNull(a);
        a.credit(Money.of(200, "CNY"));
        a.awardRedEnvelope(Money.of(20, "CNY"));
        repository.save(a);
        a = repository.find("a2");
        a.changeSmallChangDenominationEnum(SmallChangDenominationEnum.ONE);
        a.debit(Money.of(67.52, "CNY"));
        a.debit(Money.of(67.45, "CNY"));
        a.debit(Money.of(67.45, "CNY"));
        repository.save(a);
    }

    @Test(invocationCount = 100, threadPoolSize = 4)
    public void testSize() {
        int size = repository.size();
        Assert.assertEquals(3, size);
    }

    @Test(invocationCount = 100, threadPoolSize = 4)
    public void testFindAll() {
        AnonymousCard[] anonymousCards = repository.findAll(0, 3);
        Assert.assertEquals(3, anonymousCards.length);
        anonymousCards = repository.findAll(0, 2);
        Assert.assertEquals(2, anonymousCards.length);
        anonymousCards = repository.findAll(1, 2);
        Assert.assertEquals(2, anonymousCards.length);
    }

    @Test
    public void testFindByCardFaceNumber() {
        AnonymousCard[] a3 = repository.findByCardFaceNumber("22156791");
        Assert.assertEquals(a3.length, 1);
    }
}