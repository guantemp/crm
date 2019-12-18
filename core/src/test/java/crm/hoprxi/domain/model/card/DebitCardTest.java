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

import org.javamoney.moneta.Money;
import org.junit.Rule;
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
/*
    @Test
    public void all() {
        DebitCard card = new DebitCard("600256", "宜宾总店", "327885095", "123456", zero, zero, zero);
        card.prepay(three_hundred);
        Assert.assertTrue(three_hundred.isEqualTo(card.totalBalance()));
        Assert.assertTrue(three_hundred.isEqualTo(card.availableBalance()));
        card.prepay(three_hundred, thirty);
        Assert.assertTrue(thirty.isEqualTo(card.give()));

        card.frozen(two_hundred);
        Assert.assertTrue(Money.of(630, "CNY").isEqualTo(card.totalBalance()));
        Assert.assertTrue(Money.of(430, "CNY").isEqualTo(card.availableBalance()));
        Assert.assertTrue(Money.of(200, "CNY").isEqualTo(card.freeze()));
        card.thaw(one_hundred);
        Assert.assertTrue(Money.of(630, "CNY").isEqualTo(card.totalBalance()));
        Assert.assertTrue(Money.of(530, "CNY").isEqualTo(card.availableBalance()));
        Assert.assertTrue(Money.of(100, "CNY").isEqualTo(card.freeze()));

        card.withdrawal(one_hundred);
        Assert.assertTrue(Money.of(530, "CNY").isEqualTo(card.totalBalance()));
        Assert.assertTrue(Money.of(500, "CNY").isEqualTo(card.principal()));
        Assert.assertTrue(Money.of(430, "CNY").isEqualTo(card.availableBalance()));
        card.withdrawal(two_hundred);
        Assert.assertTrue(Money.of(330, "CNY").isEqualTo(card.totalBalance()));
        Assert.assertTrue(Money.of(300, "CNY").isEqualTo(card.principal()));
        Assert.assertTrue(Money.of(230, "CNY").isEqualTo(card.availableBalance()));

        card.frozen(Money.of(530, "CNY"));
        Assert.assertTrue(Money.of(630, "CNY").isEqualTo(card.freeze()));
        Assert.assertTrue(Money.of(300, "CNY").isEqualTo(card.principal()));
        Assert.assertTrue(Money.of(0, "CNY").isEqualTo(card.availableBalance()));
        card.thaw(Money.of(900, "CNY"));

        card.pay(one_hundred, DebitCard.Usage.PRINCIPAL_FIRST);
        Assert.assertTrue(Money.of(230, "CNY").isEqualTo(card.totalBalance()));
        Assert.assertTrue(Money.of(200, "CNY").isEqualTo(card.principal()));
        Assert.assertTrue(Money.of(230, "CNY").isEqualTo(card.availableBalance()));
        Assert.assertTrue(Money.of(30, "CNY").isEqualTo(card.give()));
        card.pay(Money.of(10, "CNY"), DebitCard.Usage.GIVE_FIRST);
        Assert.assertTrue(Money.of(220, "CNY").isEqualTo(card.totalBalance()));
        Assert.assertTrue(Money.of(200, "CNY").isEqualTo(card.principal()));
        Assert.assertTrue(Money.of(220, "CNY").isEqualTo(card.availableBalance()));
        Assert.assertTrue(Money.of(20, "CNY").isEqualTo(card.give()));
        card.pay(Money.of(150, "CNY"));
        Assert.assertTrue(Money.of(70, "CNY").isEqualTo(card.totalBalance()));
        Assert.assertTrue(Money.of(70, "CNY").isEqualTo(card.availableBalance()));
        card.pay(Money.of(50, "CNY"));
        Assert.assertTrue(Money.of(20, "CNY").isEqualTo(card.availableBalance()));
        System.out.println(card);
    }

    @Test
    public void testException() {
        DebitCard card = new DebitCard("600156", "泸州看画城", "327885095", "123465", zero, zero, zero);
        System.out.println(card);
    }

 */
}