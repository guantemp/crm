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

package crm.hoprxi.domain.model.card;

import crm.hoprxi.domain.model.balance.SmallChangDenominationEnum;
import crm.hoprxi.domain.model.collaborator.Issuer;
import org.javamoney.moneta.Money;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.util.Locale;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-08-15
 */
public class DebitCardTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    Locale locale = Locale.getDefault();
    MonetaryAmount three_hundred = Money.of(300, "CNY");
    MonetaryAmount zero = Money.of(0, "CNY");
    MonetaryAmount thirty = Money.of(30, "CNY");
    MonetaryAmount two_hundred = Money.of(200, Monetary.getCurrency(locale));
    MonetaryAmount one_hundred = Money.of(100, Monetary.getCurrency(locale));

    @Test
    public void all() {
        DebitCard card = new DebitCard(new Issuer("600256", "宜宾总店"), "327885095", "123456", "2002123456");
        card.credit(three_hundred);
        Assert.assertTrue(three_hundred.isEqualTo(card.balance().total()));
        card.credit(three_hundred);
        card.giveRedPackets(thirty);
        Assert.assertTrue(thirty.isEqualTo(card.balance().redPackets()));

        card.cashWithdrawal(one_hundred);
        Assert.assertTrue(Money.of(530, "CNY").isEqualTo(card.balance().total()));
        Assert.assertTrue(Money.of(500, "CNY").isEqualTo(card.balance().valuable()));
        Assert.assertTrue(Money.of(30, "CNY").isEqualTo(card.balance().redPackets()));

        card.cashWithdrawal(two_hundred);
        Assert.assertTrue(Money.of(330, "CNY").isEqualTo(card.balance.total()));
        Assert.assertTrue(Money.of(300, "CNY").isEqualTo(card.balance.valuable()));
        Assert.assertTrue(Money.of(30, "CNY").isEqualTo(card.balance.redPackets()));

        card.debit(one_hundred);
        Assert.assertTrue(Money.of(230, "CNY").isEqualTo(card.balance().total()));
        Assert.assertTrue(Money.of(200, "CNY").isEqualTo(card.balance().valuable()));
        Assert.assertTrue(Money.of(30, "CNY").isEqualTo(card.balance().redPackets()));

        card.changeSmallChangDenominationEnum(SmallChangDenominationEnum.ONE);
        card.debit(Money.of(99.5, Monetary.getCurrency(locale)));
        Assert.assertTrue(Money.of(100, "CNY").isEqualTo(card.balance().valuable()));
        Assert.assertTrue(Money.of(30, "CNY").isEqualTo(card.balance().redPackets()));
        Assert.assertTrue(Money.of(0.5, "CNY").isEqualTo(card.smallChange().amount()));

        card.debit(Money.of(10.6, Monetary.getCurrency(locale)));
        Assert.assertTrue(Money.of(89, "CNY").isEqualTo(card.balance().valuable()));
        Assert.assertTrue(Money.of(30, "CNY").isEqualTo(card.balance().redPackets()));
        Assert.assertTrue(Money.of(0.9, "CNY").isEqualTo(card.smallChange().amount()));

    }

    @Test
    public void testException() {
       DebitCard card = new DebitCard(new Issuer("600156", "泸州看画城"), "52275427", "123465","2002123456");
       //System.out.println(card);
    }

}