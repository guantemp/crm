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

import org.javamoney.moneta.Money;
import org.junit.Assert;
import org.junit.Test;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-11-18
 */
public class SmallChangeBalanceTest {

    @Test
    public void balance() {
        SmallChangeBalance smallChangeBalance = new SmallChangeBalance(Money.of(0.55, "CNY"), ChangDenominationEnum.ONE);
        Rounded rounded = smallChangeBalance.round(Money.of(5.25, "CNY"));
        Assert.assertTrue(Money.of(5, "CNY").isEqualTo(rounded.integer()));
        Assert.assertTrue(Money.of(-0.25, "CNY").isEqualTo(rounded.remainder()));
        smallChangeBalance = smallChangeBalance.pay(rounded.remainder().negate());
        rounded = smallChangeBalance.round(Money.of(4.75, "CNY"));
        Assert.assertTrue(Money.of(5, "CNY").isEqualTo(rounded.integer()));
        Assert.assertTrue(Money.of(0.25, "CNY").isEqualTo(rounded.remainder()));
        smallChangeBalance = smallChangeBalance.deposit(rounded.remainder());
        Assert.assertTrue(Money.of(0.55, "CNY").isEqualTo(smallChangeBalance.balance()));

        smallChangeBalance = new SmallChangeBalance(Money.of(4.55, "CNY"), ChangDenominationEnum.FIVE);
        rounded = smallChangeBalance.round(Money.of(4.75, "CNY"));
        Assert.assertTrue(Money.of(5, "CNY").isEqualTo(rounded.integer()));
        Assert.assertTrue(Money.of(0.25, "CNY").isEqualTo(rounded.remainder()));
        smallChangeBalance = smallChangeBalance.deposit(rounded.remainder());

        rounded = smallChangeBalance.round(Money.of(6.73, "CNY"));
        Assert.assertTrue(Money.of(5, "CNY").isEqualTo(rounded.integer()));
        Assert.assertTrue(Money.of(-1.73, "CNY").isEqualTo(rounded.remainder()));
        smallChangeBalance = smallChangeBalance.pay(rounded.remainder().negate());

        rounded = smallChangeBalance.round(Money.of(3.02, "CNY"));
        Assert.assertTrue(Money.of(0, "CNY").isEqualTo(rounded.integer()));
        Assert.assertTrue(Money.of(-3.02, "CNY").isEqualTo(rounded.remainder()));
        smallChangeBalance = smallChangeBalance.pay(rounded.remainder().negate());

        smallChangeBalance = smallChangeBalance.changeChangDenominationEnum(ChangDenominationEnum.ONE);
        rounded = smallChangeBalance.round(Money.of(6.73, "CNY"));
        Assert.assertTrue(Money.of(7, "CNY").isEqualTo(rounded.integer()));
        Assert.assertTrue(Money.of(0.27, "CNY").isEqualTo(rounded.remainder()));
        smallChangeBalance = smallChangeBalance.deposit(rounded.remainder());

        rounded = smallChangeBalance.round(Money.of(11.32, "CNY"));
        Assert.assertTrue(Money.of(11, "CNY").isEqualTo(rounded.integer()));
        Assert.assertTrue(Money.of(-0.32, "CNY").isEqualTo(rounded.remainder()));
        smallChangeBalance = smallChangeBalance.pay(rounded.remainder().negate());
        Assert.assertTrue(Money.of(0, "CNY").isEqualTo(smallChangeBalance.balance()));

        smallChangeBalance = new SmallChangeBalance(Money.of(0, "CNY"), ChangDenominationEnum.ZERO);
        rounded = smallChangeBalance.round(Money.of(3.28, "CNY"));
        Assert.assertTrue(rounded.remainder().isZero());
        Assert.assertTrue(rounded.integer().isZero());
        rounded = smallChangeBalance.round(Money.of(7.89, "CNY"));
        Assert.assertTrue(rounded.remainder().isZero());
        Assert.assertTrue(rounded.integer().isZero());
    }
}