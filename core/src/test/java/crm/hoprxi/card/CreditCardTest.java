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

package crm.hoprxi.card;

import org.javamoney.moneta.Money;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import javax.money.MonetaryAmount;
import java.util.Locale;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-08-15
 */
public class CreditCardTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    Locale locale = Locale.getDefault();
    MonetaryAmount zero = Money.of(0, "CNY");
/*
    @Test
    public void all() {
        CreditCard card = new CreditCard("001", "214", "136796", Money.of(1500, "CNY"), zero);
        card.pay(Money.of(500, "CNY"));
        Assert.assertTrue(Money.of(-500, "CNY").isEqualTo(card.balance()));
        card.pay(Money.of(1000, "CNY"));
        Assert.assertTrue(Money.of(-1500, "CNY").isEqualTo(card.balance()));
        card.pay(zero);
        Assert.assertTrue(Money.of(-1500, "CNY").isEqualTo(card.balance()));
        card.pay(Money.of(-500, "CNY"));
        Assert.assertTrue(Money.of(-1500, "CNY").isEqualTo(card.balance()));

        card.quotaAdjustTo(Money.of(2000, "CNY"));
        Assert.assertTrue(Money.of(2000, "CNY").isEqualTo(card.quota()));
        card.quotaAdjustTo(Money.of(-1, "CNY"));
        Assert.assertTrue(Money.of(2000, "CNY").isEqualTo(card.quota()));

        card.pay(Money.of(300, "CNY"));
        Assert.assertTrue(Money.of(-1800, "CNY").isEqualTo(card.balance()));

        card.repayment(Money.of(900, "CNY"));
        Assert.assertTrue(Money.of(-900, "CNY").isEqualTo(card.balance()));
        card.repayment(Money.of(1000, "CNY"));
        Assert.assertTrue(Money.of(100, "CNY").isEqualTo(card.balance()));

        card.quotaAdjustTo(Money.of(0, "CNY"));
        Assert.assertTrue(Money.of(0, "CNY").isEqualTo(card.quota()));

        thrown.expect(InsufficientBalanceException.class);
        //thrown.expectMessage("\"Insufficient available quota\"");
        card.pay(Money.of(0.01, "CNY"));
    }

 */
}