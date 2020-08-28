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

package crm.hoprxi.core.domain.model.card;

import crm.hoprxi.core.domain.model.balance.Balance;
import crm.hoprxi.core.domain.model.balance.SmallChange;
import crm.hoprxi.core.domain.model.collaborator.Issuer;
import org.javamoney.moneta.FastMoney;
import org.javamoney.moneta.Money;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Locale;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-06-10
 */
public class CreditCardTest {
    public static LineOfCredit lineOfCredit = new LineOfCredit(FastMoney.of(100, "CNY"), 30);
    public static CreditCard card = new CreditCard(new Issuer("968974548754158X", "小市店"), "18982455066", "20281405", "112233", "81405",
            true, lineOfCredit, Balance.zero(Locale.CHINESE), SmallChange.zero(Locale.CHINESE), null);

    @Test(priority = 0)
    public void testPassword() {
        Assert.assertTrue(card.authenticatePassword("112233"));
        Assert.assertFalse(card.authenticatePassword("122233"));
    }

    @Test(priority = 0)
    public void testTransaction() {
        card.debit(FastMoney.of(45.56, "CNY"));
        card.debit(Money.of(54.44, "CNY"));
        card.credit(Money.of(20, "CNY"));
        System.out.println(card);
        card.debit(Money.of(15.00001, "CNY"));
        System.out.println(card);

    }

    @Test(expectedExceptions = ExceedQuotaException.class, priority = 1)
    public void testExceedQuotaException() {
        card.debit(Money.of(5, "CNY"));
    }
}