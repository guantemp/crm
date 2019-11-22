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

import org.javamoney.moneta.Money;
import org.junit.Assert;
import org.junit.Test;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-11-18
 */
public class ChangeWalletTest {

    @Test
    public void balance() {
        ChangeWallet changeWallet = new ChangeWallet(Money.of(0.55, "CNY"), QuotaEnum.ONE);
        Rounded rounded = changeWallet.round(Money.of(5.25, "CNY"));
        Assert.assertTrue(Money.of(5, "CNY").isEqualTo(rounded.integer()));
        Assert.assertTrue(Money.of(-0.25, "CNY").isEqualTo(rounded.remainder()));
        changeWallet = changeWallet.pay(rounded.remainder().negate());
        rounded = changeWallet.round(Money.of(4.75, "CNY"));
        Assert.assertTrue(Money.of(5, "CNY").isEqualTo(rounded.integer()));
        Assert.assertTrue(Money.of(0.25, "CNY").isEqualTo(rounded.remainder()));
        changeWallet = changeWallet.deposit(rounded.remainder());
        Assert.assertTrue(Money.of(0.55, "CNY").isEqualTo(changeWallet.balance()));

        changeWallet = new ChangeWallet(Money.of(4.55, "CNY"), QuotaEnum.FIVE);
        rounded = changeWallet.round(Money.of(4.75, "CNY"));
        Assert.assertTrue(Money.of(5, "CNY").isEqualTo(rounded.integer()));
        Assert.assertTrue(Money.of(0.25, "CNY").isEqualTo(rounded.remainder()));
        changeWallet = changeWallet.deposit(rounded.remainder());

        rounded = changeWallet.round(Money.of(6.73, "CNY"));
        Assert.assertTrue(Money.of(5, "CNY").isEqualTo(rounded.integer()));
        Assert.assertTrue(Money.of(-1.73, "CNY").isEqualTo(rounded.remainder()));
        changeWallet = changeWallet.pay(rounded.remainder().negate());

        rounded = changeWallet.round(Money.of(3.02, "CNY"));
        Assert.assertTrue(Money.of(0, "CNY").isEqualTo(rounded.integer()));
        Assert.assertTrue(Money.of(-3.02, "CNY").isEqualTo(rounded.remainder()));
        changeWallet = changeWallet.pay(rounded.remainder().negate());

        changeWallet = changeWallet.changeQuota(QuotaEnum.ONE);
        rounded = changeWallet.round(Money.of(6.73, "CNY"));
        Assert.assertTrue(Money.of(7, "CNY").isEqualTo(rounded.integer()));
        Assert.assertTrue(Money.of(0.27, "CNY").isEqualTo(rounded.remainder()));
        changeWallet = changeWallet.deposit(rounded.remainder());

        rounded = changeWallet.round(Money.of(11.32, "CNY"));
        Assert.assertTrue(Money.of(11, "CNY").isEqualTo(rounded.integer()));
        Assert.assertTrue(Money.of(-0.32, "CNY").isEqualTo(rounded.remainder()));
        changeWallet = changeWallet.pay(rounded.remainder().negate());
        Assert.assertTrue(Money.of(0, "CNY").isEqualTo(changeWallet.balance()));

        changeWallet = new ChangeWallet(Money.of(0, "CNY"), QuotaEnum.ZERO);
        rounded = changeWallet.round(Money.of(3.28, "CNY"));
        Assert.assertTrue(rounded.remainder().isZero());
        Assert.assertTrue(rounded.integer().isZero());
        rounded = changeWallet.round(Money.of(7.89, "CNY"));
        Assert.assertTrue(rounded.remainder().isZero());
        Assert.assertTrue(rounded.integer().isZero());
    }
}