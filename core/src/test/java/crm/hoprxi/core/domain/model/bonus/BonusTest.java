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

package crm.hoprxi.core.domain.model.bonus;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2020-05-17
 */
public class BonusTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void of() {
        Assert.assertTrue(Bonus.of(0) == Bonus.ZERO);
        Assert.assertTrue(Bonus.of(0.0000) == Bonus.ZERO);
        Assert.assertTrue(Bonus.of(0l) == Bonus.ZERO);
    }

    @Test
    public void addAndSubtract() {
        Bonus b1 = Bonus.ZERO;
        Bonus b2 = b1.add(new Bonus(145.5265));
        Assert.assertTrue(b2.compareTo(b1) == 1);
        Assert.assertTrue(b2.toNumber().doubleValue() == 145.53);

        Bonus b3 = b2.subtract(new Bonus(25.07));
        Assert.assertTrue(b3.compareTo(b2) == -1);
        Assert.assertTrue(b3.toNumber().doubleValue() == 120.46);
        Assert.assertTrue(b3.compareTo(Bonus.of(120.456)) == 0);
        Bonus b7 = b3.subtract(new Bonus(120.53));
        Assert.assertTrue(b7 == b3);

        Bonus b4 = Bonus.of(Double.valueOf("167.865"));
        System.out.println(b4);
        b4 = b4.multiply(1.24);
        System.out.println(b4);
        Assert.assertTrue(b4.compareTo(Bonus.of(208.15)) == 0);
        b4 = b4.multiply(0.65);
        System.out.println(b4);
        Assert.assertTrue(b4.compareTo(Bonus.of(135.30)) == 0);


        //thrown.expect(BonusDeficiencyException.class);
        //Bonus.ZERO.subtract(Bonus.of(0.01));
    }
}