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

package crm.hoprxi.domain.model.card.wallet;

import crm.hoprxi.domain.model.card.PaymentStrategy;
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
public class WalletTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void zero() {
        Wallet rmb = Wallet.zero(Locale.getDefault());
        assertTrue(rmb == Wallet.zero(Locale.CHINESE));
        rmb.pay(FastMoney.of(20, Monetary.getCurrency(Locale.CHINA)));
        rmb.withdrawal(FastMoney.of(120, Monetary.getCurrency(Locale.CHINA)));
        assertTrue(rmb == Wallet.zero(Locale.PRC));
        assertTrue(rmb == Wallet.zero(Locale.CHINA));
        Wallet usd = Wallet.zero(Locale.US);
        assertTrue(usd == Wallet.zero(Locale.US));
    }

    @Test
    public void trade() {
        Wallet rmb = Wallet.zero(Locale.getDefault());
        rmb = rmb.prepay(FastMoney.of(20, Monetary.getCurrency(Locale.CHINA)));
        assertEquals(rmb.balance(), FastMoney.of(20, Monetary.getCurrency(Locale.CHINA)));
        assertEquals(rmb.give(), FastMoney.of(0, Monetary.getCurrency(Locale.CHINA)));
        rmb = rmb.prepay(FastMoney.of(200, Monetary.getCurrency(Locale.CHINA)), FastMoney.of(20, Monetary.getCurrency(Locale.CHINA)));
        assertEquals(rmb.balance(), FastMoney.of(220, Monetary.getCurrency(Locale.CHINA)));
        assertEquals(rmb.give(), FastMoney.of(20, Monetary.getCurrency(Locale.CHINA)));
        rmb = rmb.withdrawal(FastMoney.of(150, Monetary.getCurrency(Locale.CHINA)));
        assertEquals(rmb.balance(), FastMoney.of(70, Monetary.getCurrency(Locale.CHINA)));
        rmb.pay(FastMoney.of(64.50, Monetary.getCurrency(Locale.CHINA)));
        assertEquals(rmb.balance(), FastMoney.of(5.5, Monetary.getCurrency(Locale.CHINA)));
        rmb = rmb.pay(FastMoney.of(15.50, Monetary.getCurrency(Locale.CHINA)), PaymentStrategy.RED_ENVELOPES_FIRST);
        assertEquals(rmb.give(), FastMoney.of(4.5, Monetary.getCurrency(Locale.CHINA)));
        thrown.expect(InsufficientBalanceException.class);
        rmb = rmb.pay(FastMoney.of(10.1, Monetary.getCurrency(Locale.CHINA)));
    }

}