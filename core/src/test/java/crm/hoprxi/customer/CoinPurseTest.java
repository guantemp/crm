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

package crm.hoprxi.customer;

import crm.hoprxi.domain.model.customer.person.SmallChange;
import org.javamoney.moneta.Money;
import org.junit.Assert;
import org.junit.Test;

import javax.money.MonetaryAmount;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-08-22
 */
public class CoinPurseTest {

    @Test
    public void round() {
        SmallChange smallChange = SmallChange.ZERO;
        smallChange = smallChange.enable();

        MonetaryAmount temp = smallChange.round(Money.of(4.55, "CNY"));
        Assert.assertTrue(Money.of(5, "CNY").isEqualTo(temp));
        smallChange = smallChange.access(Money.of(4.55, "CNY"));
        Assert.assertTrue(Money.of(0.55, "CNY").isEqualTo(smallChange.amount()));

        temp = smallChange.round(Money.of(1.9, "CNY"));
        Assert.assertTrue(Money.of(2, "CNY").isEqualTo(temp));
        smallChange = smallChange.access(Money.of(1.9, "CNY"));
        Assert.assertTrue(Money.of(0.65, "CNY").isEqualTo(smallChange.amount()));

        temp = smallChange.round(Money.of(2151.8, "CNY"));
        Assert.assertTrue(Money.of(2152, "CNY").isEqualTo(temp));
        smallChange = smallChange.access(Money.of(2151.8, "CNY"));
        Assert.assertTrue(Money.of(0.85, "CNY").isEqualTo(smallChange.amount()));

        temp = smallChange.round(Money.of(41.7, "CNY"));
        Assert.assertTrue(Money.of(41, "CNY").isEqualTo(temp));
        smallChange = smallChange.access(Money.of(41.7, "CNY"));
        Assert.assertTrue(Money.of(0.15, "CNY").isEqualTo(smallChange.amount()));

        temp = smallChange.round(Money.of(22, "CNY"));
        Assert.assertTrue(Money.of(22, "CNY").isEqualTo(temp));
        smallChange = smallChange.access(Money.of(22, "CNY"));
        Assert.assertTrue(Money.of(0.15, "CNY").isEqualTo(smallChange.amount()));

        temp = smallChange.round(Money.of(22.7, "CNY"));
        Assert.assertTrue(Money.of(23, "CNY").isEqualTo(temp));
        smallChange = smallChange.access(Money.of(22.7, "CNY"));
        Assert.assertTrue(Money.of(0.45, "CNY").isEqualTo(smallChange.amount()));

        temp = smallChange.round(Money.of(22.45, "CNY"));
        Assert.assertTrue(Money.of(22, "CNY").isEqualTo(temp));
        smallChange = smallChange.access(Money.of(22.45, "CNY"));
        temp = smallChange.round(Money.of(2.415, "CNY"));
        Assert.assertTrue(Money.of(3, "CNY").isEqualTo(temp));
        smallChange = smallChange.access(Money.of(2.415, "CNY"));
        Assert.assertTrue(Money.of(0.415, "CNY").isEqualTo(smallChange.amount()));
    }
}