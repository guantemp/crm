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

package crm.hoprxi.domain.model.bonus;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-12-17
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
        Bonus b2 = b1.add(Bonus.of(45));
        Assert.assertTrue(b2.compareTo(b1) == 1);
        Assert.assertTrue(b2.value().intValue() == 45);
        Bonus b3 = b2.subtract(new Bonus(25));
        Assert.assertTrue(b3.compareTo(b2) == -1);
        Assert.assertTrue(b3.value().intValue() == 20);
        Assert.assertTrue(b3.compareTo(Bonus.of(20)) == 0);
        thrown.expect(BonusDeficiencyException.class);
        b3.subtract(new Bonus(20.01));
        thrown.expect(BonusDeficiencyException.class);
        Bonus.ZERO.subtract(Bonus.of(0.01));
    }
}