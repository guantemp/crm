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
    public void zero() {
        Balance rmb = Balance.zero(Locale.getDefault());
        assertTrue(rmb == Balance.zero(Locale.CHINESE));
        rmb.pay(FastMoney.of(20, Monetary.getCurrency(Locale.CHINA)));
        rmb.withdrawal(FastMoney.of(120, Monetary.getCurrency(Locale.CHINA)));
        assertTrue(rmb == Balance.zero(Locale.PRC));
        assertTrue(rmb == Balance.zero(Locale.CHINA));
        Balance usd = Balance.zero(Locale.US);
        assertTrue(usd == Balance.zero(Locale.US));
    }

    @Test
    public void trade() {
        Balance rmb = Balance.zero(Locale.getDefault());
        rmb = rmb.credit(FastMoney.of(20, Monetary.getCurrency(Locale.CHINA)));
        assertEquals(rmb.valuable(), FastMoney.of(20, Monetary.getCurrency(Locale.CHINA)));
        assertEquals(rmb.give(), FastMoney.of(0, Monetary.getCurrency(Locale.CHINA)));
        rmb = rmb.credit(FastMoney.of(200, Monetary.getCurrency(Locale.CHINA)), FastMoney.of(20, Monetary.getCurrency(Locale.CHINA)));
        assertEquals(rmb.valuable(), FastMoney.of(220, Monetary.getCurrency(Locale.CHINA)));
        assertEquals(rmb.give(), FastMoney.of(20, Monetary.getCurrency(Locale.CHINA)));
        rmb = rmb.withdrawal(FastMoney.of(150, Monetary.getCurrency(Locale.CHINA)));
        assertEquals(rmb.valuable(), FastMoney.of(70, Monetary.getCurrency(Locale.CHINA)));
        rmb.pay(FastMoney.of(64.50, Monetary.getCurrency(Locale.CHINA)));
        assertEquals(rmb.valuable(), FastMoney.of(5.5, Monetary.getCurrency(Locale.CHINA)));
        rmb = rmb.pay(FastMoney.of(15.50, Monetary.getCurrency(Locale.CHINA)));
        assertEquals(rmb.give(), FastMoney.of(4.5, Monetary.getCurrency(Locale.CHINA)));
        rmb = rmb.pay(FastMoney.of(6, Monetary.getCurrency(Locale.CHINA)));
        assertEquals(rmb.valuable(), FastMoney.of(2.2, Monetary.getCurrency(Locale.CHINA)));
        assertEquals(rmb.give(), FastMoney.of(1.8, Monetary.getCurrency(Locale.CHINA)));
        thrown.expect(InsufficientBalanceException.class);
        rmb = rmb.pay(FastMoney.of(4.00001, Monetary.getCurrency(Locale.CHINA)));
        rmb = rmb.withdrawal(FastMoney.of(2.20001, Monetary.getCurrency(Locale.CHINA)));
    }

}