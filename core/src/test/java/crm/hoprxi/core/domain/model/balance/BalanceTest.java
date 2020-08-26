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

package crm.hoprxi.core.domain.model.balance;

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
 * @version 0.0.2 2020-04-02
 */
public class BalanceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void zeroRMBTest() {
        Balance rmb = Balance.rmbZero();
        assertTrue(rmb == Balance.zero(Locale.CHINESE));
        assertTrue(rmb == Balance.zero(Locale.PRC));
        assertTrue(rmb == Balance.zero(Locale.CHINA));
        assertTrue(rmb.isZero());
        thrown.expect(InsufficientBalanceException.class);
        rmb = rmb.pay(FastMoney.of(0.0001, "CNY"));
        thrown.expect(InsufficientBalanceException.class);
        rmb = rmb.withdrawalCash(Money.of(0.000000001, "CNY"));
    }

    @Test
    public void zeroUSDTest() {
        Balance usd = Balance.zero(Locale.US);
        assertTrue(usd == Balance.usdZero());
        assertTrue(usd.isZero());
        thrown.expect(InsufficientBalanceException.class);
        usd.pay(FastMoney.of(0.0001, "USD"));
        thrown.expect(InsufficientBalanceException.class);
        usd.withdrawalCash(Money.of(0.000005, "USD"));
    }

    @Test
    public void tradeTest() {
        Balance rmb = Balance.rmbZero();
        rmb = rmb.deposit(FastMoney.of(20, "CNY"));
        assertEquals(rmb.valuable(), Money.of(20, "CNY"));
        assertEquals(rmb.redEnvelope(), Money.of(0, "CNY"));
        assertEquals(rmb.total(), Money.of(20, "CNY"));

        rmb = rmb.deposit(FastMoney.of(200, "CNY"));
        rmb = rmb.awardRedEnvelope(FastMoney.of(20, "CNY"));
        assertEquals(rmb.valuable(), Money.of(220, "CNY"));
        assertEquals(rmb.redEnvelope(), Money.of(20, "CNY"));
        assertEquals(rmb.total(), Money.of(240, "CNY"));

        rmb = rmb.withdrawalCash(FastMoney.of(150, "CNY"));
        assertEquals(rmb.valuable(), Money.of(70, "CNY"));
        assertEquals(rmb.redEnvelope(), Money.of(20, "CNY"));

        rmb = rmb.pay(FastMoney.of(64.50, "CNY"));
        assertEquals(rmb.valuable(), Money.of(5.5, "CNY"));
        assertEquals(rmb.redEnvelope(), Money.of(20, "CNY"));

        rmb = rmb.pay(Money.of(15.50, "CNY"));
        assertEquals(rmb.valuable(), Money.of(0, "CNY"));
        assertEquals(rmb.redEnvelope(), Money.of(10, "CNY"));

        rmb = rmb.pay(Money.of(10, "CNY"));
        assertTrue(rmb == Balance.rmbZero());
        //overdraw
        rmb = new Balance(FastMoney.of(20, "CNY"), FastMoney.of(20, "CNY"));
        //20,20  -20
        rmb = rmb.overdraw(Money.of(20, "CNY"));
        assertTrue(rmb.valuable().isEqualTo(FastMoney.of(0, "CNY")));
        assertTrue(rmb.redEnvelope().isEqualTo(FastMoney.of(20, "CNY")));
        rmb = new Balance(Money.of(20, "CNY"), Money.of(20, "CNY"));
        //20,20 -30
        rmb = rmb.overdraw(Money.of(30, "CNY"));
        assertTrue(rmb.valuable().isEqualTo(FastMoney.of(0, "CNY")));
        assertTrue(rmb.redEnvelope().isEqualTo(Money.of(10, "CNY")));

        rmb = new Balance(Money.of(0, "CNY"), Money.of(20, "CNY"));
        //0,20 -19.5
        rmb = rmb.overdraw(Money.of(19.5, "CNY"));
        assertTrue(rmb.valuable().isEqualTo(FastMoney.of(0, "CNY")));
        assertTrue(rmb.redEnvelope().isEqualTo(FastMoney.of(0.5, "CNY")));
        rmb = new Balance(Money.of(0, "CNY"), Money.of(20, "CNY"));
        //0.20 -35
        rmb = rmb.overdraw(Money.of(35, "CNY"));
        assertTrue(rmb.valuable().isEqualTo(FastMoney.of(-15, "CNY")));
        assertTrue(rmb.redEnvelope().isEqualTo(FastMoney.of(0, "CNY")));

        rmb = new Balance(FastMoney.of(-20, "CNY"), FastMoney.of(20, "CNY"));
        //-20,20 -16.5
        rmb = rmb.overdraw(Money.of(16.5, "CNY"));
        assertTrue(rmb.valuable().isEqualTo(FastMoney.of(-20, "CNY")));
        assertTrue(rmb.redEnvelope().isEqualTo(FastMoney.of(3.5, "CNY")));
        rmb = new Balance(FastMoney.of(-20, "CNY"), FastMoney.of(20, "CNY"));
        //-20,20 -20.6
        rmb = rmb.overdraw(Money.of(20.6, "CNY"));
        assertTrue(rmb.valuable().isEqualTo(FastMoney.of(-20.6, "CNY")));
        assertTrue(rmb.redEnvelope().isEqualTo(FastMoney.of(0, "CNY")));

        rmb = new Balance(FastMoney.of(-40, "CNY"), FastMoney.of(20, "CNY"));
        //-40,20 -16.55555
        rmb = rmb.overdraw(Money.of(16.55555, "CNY"));
        assertTrue(rmb.valuable().isEqualTo(FastMoney.of(-40, "CNY")));
        assertTrue(rmb.redEnvelope().isEqualTo(FastMoney.of(3.44445, "CNY")));
        rmb = new Balance(FastMoney.of(-40, "CNY"), FastMoney.of(20, "CNY"));
        //-40,20 -38.5
        rmb = rmb.overdraw(Money.of(38.5, "CNY"));
        assertTrue(rmb.valuable().isEqualTo(FastMoney.of(-58.5, "CNY")));
        assertTrue(rmb.redEnvelope().isEqualTo(FastMoney.of(0, "CNY")));


        rmb = Balance.rmbZero();
        rmb = rmb.deposit(Money.of(20, "CNY"));
        rmb = rmb.awardRedEnvelope(Money.of(2, "CNY"));
        rmb = rmb.overdraw(FastMoney.of(21, "CNY"));
        assertTrue(rmb.valuable().isEqualTo(FastMoney.of(0, "CNY")));
        assertTrue(rmb.redEnvelope().isEqualTo(FastMoney.of(1, "CNY")));
        rmb = rmb.overdraw(FastMoney.of(100, "CNY"));
        assertTrue(rmb.valuable().isEqualTo(FastMoney.of(-99, "CNY")));
        assertTrue(rmb.redEnvelope().isEqualTo(FastMoney.of(0, "CNY")));

        rmb = Balance.getInstance(Money.zero(Monetary.getCurrency(Locale.CHINA)), Money.zero(Monetary.getCurrency(Locale.CHINA)));
        assertTrue(rmb == Balance.rmbZero());

        thrown.expect(InsufficientBalanceException.class);
        rmb.pay(Money.of(0.00000001, "CNY"));
        rmb.withdrawalCash(Money.of(0.00000001, "CNY"));
    }

    @Test
    public void redEnvelopeTest() {
        Balance rmb = Balance.rmbZero();
        rmb = rmb.awardRedEnvelope(FastMoney.of(20.5, "CNY"));
        assertTrue(rmb.valuable().isEqualTo(FastMoney.of(0, "CNY")));
        assertTrue(rmb.redEnvelope().isEqualTo(FastMoney.of(20.5, "CNY")));
        rmb = rmb.revokeRedEnvelope(FastMoney.of(10, "CNY"));
        assertTrue(rmb.valuable().isEqualTo(FastMoney.of(0, "CNY")));
        assertTrue(rmb.redEnvelope().isEqualTo(FastMoney.of(10.5, "CNY")));
    }

    @Test
    public void additionTest() {
        Balance rmb1 = new Balance(FastMoney.of(5, "CNY"), FastMoney.of(15, "CNY"));
        Balance rmb2 = new Balance(FastMoney.of(0.5, "CNY"), FastMoney.of(0, "CNY"));
        Balance rmb = rmb1.add(rmb2);
        assertTrue(rmb.valuable().isEqualTo(FastMoney.of(5.5, "CNY")));
        assertTrue(rmb.redEnvelope().isEqualTo(FastMoney.of(15, "CNY")));
    }
}