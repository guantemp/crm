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

import crm.hoprxi.domain.model.card.AnonymousCard;
import crm.hoprxi.domain.model.card.AnonymousCardRepository;
import crm.hoprxi.domain.model.card.TermOfValidity;
import crm.hoprxi.domain.model.card.balance.Balance;
import crm.hoprxi.domain.model.card.balance.SmallChangDenominationEnum;
import crm.hoprxi.domain.model.card.balance.SmallChange;
import crm.hoprxi.domain.model.collaborator.Issuer;
import crm.hoprxi.domain.model.integral.Integral;
import org.javamoney.moneta.Money;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Locale;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-12-06
 */
public class ArangoDBAnonymousCardRepositoryTest {
    private static AnonymousCardRepository repository = new ArangoDBAnonymousCardRepository("crm");

    @BeforeClass
    public static void setUpBeforeClass() {
        AnonymousCard a1 = new AnonymousCard("a1", new Issuer("9678512046PX", "自己"), "22156789", TermOfValidity.PERMANENCE, Balance.zero(Locale.CHINESE), SmallChange.zero(Locale.CHINESE), Integral.ZERO, null);
        repository.save(a1);
        AnonymousCard a2 = new AnonymousCard("a2", new Issuer("9678512046PX", "self"), "22156790", TermOfValidity.PERMANENCE, Balance.zero(Locale.CHINESE), SmallChange.zero(Locale.CHINESE), Integral.ZERO, null);
        repository.save(a2);
        AnonymousCard a3 = new AnonymousCard("a3", new Issuer("9678512046PX", "self"), "22156791", TermOfValidity.PERMANENCE, Balance.zero(Locale.CHINESE), SmallChange.zero(Locale.CHINESE), Integral.ZERO, null);
        repository.save(a3);
    }

    @Test
    public void save() {
        AnonymousCard a = repository.find("a2");
        Assert.assertNotNull(a);
        a.credit(Money.of(200, "CNY"), Money.of(20, "CNY"));
        repository.save(a);
        a = repository.find("a2");
        a.changeSmallChangDenominationEnum(SmallChangDenominationEnum.ONE);
        repository.save(a);
        System.out.println(a);
    }

    @Test
    public void size() {
    }

    @Test
    public void findAll() {
    }

    @Test
    public void findByCardFaceNumber() {
    }

    @Test
    public void find() {
    }

    @Test
    public void remove() {
    }
}