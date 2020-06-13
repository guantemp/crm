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
 * @version 0.0.1 2019-12-05
 */
public class TermOfValidityTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test() {
        TermOfValidity permanent = TermOfValidity.PERMANENCE;
        Assert.assertTrue(permanent.isValidityPeriod());
        TermOfValidity permanent1 = permanent.broughtForwardExpiryDate(LocalDate.now().plusDays(2));
        Assert.assertTrue(permanent1 == permanent);
        permanent1 = permanent.postponeExpiryDate(LocalDate.now().plusDays(20));
        Assert.assertTrue(permanent1 == permanent);

        TermOfValidity termOfValidity = new TermOfValidity(LocalDate.now(), LocalDate.now());
        Assert.assertTrue(termOfValidity.isValidityPeriod());

        termOfValidity = new TermOfValidity(LocalDate.now(), LocalDate.now().plusDays(30));
        Assert.assertTrue(termOfValidity.isValidityPeriod());

        termOfValidity = termOfValidity.postponeExpiryDate(LocalDate.now().plusDays(60));
        Assert.assertTrue(termOfValidity.isValidityPeriod());
        Assert.assertEquals(termOfValidity.expiryDate(), LocalDate.now().plusDays(60));

        termOfValidity = termOfValidity.broughtForwardExpiryDate(LocalDate.now().plusDays(45));
        Assert.assertTrue(termOfValidity.isValidityPeriod());
        Assert.assertEquals(termOfValidity.expiryDate(), LocalDate.now().plusDays(45));

        termOfValidity = termOfValidity.postponeStartDate(LocalDate.now().plusDays(30));
        Assert.assertFalse(termOfValidity.isValidityPeriod());
        Assert.assertEquals(termOfValidity.startDate(), LocalDate.now().plusDays(30));

        termOfValidity = termOfValidity.broughtForwardStartDate(LocalDate.now().plusDays(5));
        Assert.assertFalse(termOfValidity.isValidityPeriod());
        Assert.assertEquals(termOfValidity.startDate(), LocalDate.now().plusDays(5));

        termOfValidity = TermOfValidity.getInstance(LocalDate.of(2015, 3, 26), LocalDate.of(2015, 3, 26));
        Assert.assertTrue(termOfValidity == TermOfValidity.PERMANENCE);

        thrown.expect(IllegalArgumentException.class);
        new TermOfValidity(LocalDate.now(), LocalDate.now().minusDays(1));
    }

}