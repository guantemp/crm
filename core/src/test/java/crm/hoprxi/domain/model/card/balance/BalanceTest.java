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

package crm.hoprxi.domain.model.card.balance;

import org.javamoney.moneta.FastMoney;
import org.javamoney.moneta.Money;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.money.Monetary;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-11-22
 */
public class BalanceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void zeroTest() {
        Balance rmb = Balance.zero(Locale.getDefault());
        assertTrue(rmb == Balance.zero(Locale.CHINESE));
        thrown.expect(InsufficientBalanceException.class);
        rmb = rmb.pay(FastMoney.of(20, "CNY"));
        //assertTrue(rmb == Balance.zero(Locale.PRC));
        //assertTrue(rmb == Balance.zero(Locale.CHINA));
        rmb = rmb.withdrawal(FastMoney.of(120, "CNY"));
        //assertTrue(rmb == Balance.zero(Locale.PRC));
        //assertTrue(rmb == Balance.zero(Locale.CHINA));

        Balance usd = Balance.zero(Locale.US);
        thrown.expect(InsufficientBalanceException.class);
        usd.pay(FastMoney.of(20, "USD"));
        usd.withdrawal(FastMoney.of(120, "USD"));
        //assertTrue(usd == Balance.zero(Locale.US));
    }

    @Test
    public void tradeTest() {
        Balance rmb = Balance.zero(Locale.CHINA);
        rmb = rmb.deposit(FastMoney.of(20, "CNY"));
        assertEquals(rmb.valuable(), Money.of(20, "CNY"));
        assertEquals(rmb.give(), Money.of(0, "CNY"));

        rmb = rmb.deposit(FastMoney.of(200, "CNY"), FastMoney.of(20, "CNY"));
        assertEquals(rmb.valuable(), Money.of(220, "CNY"));
        assertEquals(rmb.give(), Money.of(20, "CNY"));

        rmb = rmb.withdrawal(FastMoney.of(150, "CNY"));
        assertEquals(rmb.valuable(), Money.of(70, "CNY"));
        assertEquals(rmb.give(), Money.of(20, "CNY"));

        rmb = rmb.pay(FastMoney.of(64.50, "CNY"));
        System.out.println(rmb);
        assertEquals(rmb.total(), Money.of(25.5, "CNY"));

        rmb = rmb.pay(Money.of(15.50, "CNY"));
        System.out.println(rmb);
        assertEquals(rmb.total(), Money.of(10, "CNY"));

        rmb = rmb.pay(FastMoney.of(10, "CNY"));
        System.out.println(rmb);
        assertTrue(rmb == Balance.zero(Locale.CHINESE));
        assertEquals(rmb.valuable(), Money.of(0, "CNY"));
        assertEquals(rmb.give(), Money.of(0, "CNY"));

        rmb = rmb.overdraw(FastMoney.of(10, "CNY"));
        assertEquals(rmb.valuable(), Money.of(-10, "CNY"));

        rmb = Balance.zero(Locale.CHINA);
        rmb = rmb.deposit(FastMoney.of(20, "CNY"), FastMoney.of(2, "CNY"));
        rmb = rmb.overdraw(FastMoney.of(25, "CNY"));
        assertEquals(rmb.valuable(), Money.of(-3, "CNY"));
        rmb = rmb.overdraw(FastMoney.of(100, "CNY"));
        assertEquals(rmb.valuable(), Money.of(-103, "CNY"));

        rmb = Balance.getInstance(Money.zero(Monetary.getCurrency(Locale.CHINA)), Money.zero(Monetary.getCurrency(Locale.CHINA)));
        assertTrue(rmb == Balance.zero(Locale.CHINESE));

        thrown.expect(InsufficientBalanceException.class);
        rmb.pay(Money.of(0.00000001, "CNY"));
        rmb.withdrawal(Money.of(0.00000001, "CNY"));
    }

}