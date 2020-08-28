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

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDate;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.2 2020-06-13
 */
public class ValidityPeriodTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test() {
        ValidityPeriod permanent = ValidityPeriod.PERMANENCE;
        Assert.assertTrue(permanent.isValidityPeriod());
        ValidityPeriod permanent1 = permanent.broughtForwardExpiryDate(LocalDate.now().plusDays(2));
        Assert.assertTrue(permanent1 == permanent);
        permanent1 = permanent.postponeExpiryDate(LocalDate.now().plusDays(20));
        Assert.assertTrue(permanent1 == permanent);

        ValidityPeriod validityPeriod = new ValidityPeriod(LocalDate.now(), LocalDate.now());
        Assert.assertTrue(validityPeriod.isValidityPeriod());

        validityPeriod = new ValidityPeriod(LocalDate.now(), LocalDate.now().plusDays(30));
        Assert.assertTrue(validityPeriod.isValidityPeriod());

        validityPeriod = validityPeriod.postponeExpiryDate(LocalDate.now().plusDays(60));
        Assert.assertTrue(validityPeriod.isValidityPeriod());
        Assert.assertEquals(validityPeriod.expiryDate(), LocalDate.now().plusDays(60));
        //do nothing
        Assert.assertTrue(validityPeriod == validityPeriod.postponeExpiryDate(LocalDate.now().plusDays(50)));

        validityPeriod = validityPeriod.broughtForwardExpiryDate(LocalDate.now().plusDays(45));
        Assert.assertTrue(validityPeriod.isValidityPeriod());
        Assert.assertEquals(validityPeriod.expiryDate(), LocalDate.now().plusDays(45));
        //do nothing
        Assert.assertTrue(validityPeriod == validityPeriod.broughtForwardExpiryDate(LocalDate.now().plusDays(50)));

        validityPeriod = validityPeriod.postponeStartDate(LocalDate.now().plusDays(30));
        Assert.assertFalse(validityPeriod.isValidityPeriod());
        Assert.assertEquals(validityPeriod.startDate(), LocalDate.now().plusDays(30));
        validityPeriod = validityPeriod.broughtForwardStartDate(LocalDate.now().plusDays(5));
        Assert.assertFalse(validityPeriod.isValidityPeriod());
        Assert.assertEquals(validityPeriod.startDate(), LocalDate.now().plusDays(5));

        validityPeriod = ValidityPeriod.getInstance(LocalDate.of(2015, 3, 26), LocalDate.of(2015, 3, 26));
        Assert.assertTrue(validityPeriod == ValidityPeriod.PERMANENCE);

        ValidityPeriod threeYears = ValidityPeriod.threeYears();
        System.out.println(threeYears.startDate());
        System.out.println(threeYears.expiryDate());

        thrown.expect(IllegalArgumentException.class);
        new ValidityPeriod(LocalDate.now(), LocalDate.now().minusDays(1));
    }

}