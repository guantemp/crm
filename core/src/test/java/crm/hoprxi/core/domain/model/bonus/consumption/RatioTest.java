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

package crm.hoprxi.core.domain.model.bonus.consumption;

import org.testng.Assert;
import org.testng.annotations.Test;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-06-03
 */
public class RatioTest {

    @Test(invocationCount = 1, threadPoolSize = 1)
    public void testCalculation() {
        Ratio ratio = Ratio.ONE_TO_ONE;
        Assert.assertEquals(ratio.calculation(1), 1.0);
        Assert.assertEquals(ratio.calculation(0.001), 0.001);
        Assert.assertEquals(ratio.calculation(-977436534), 0);

        ratio = Ratio.ZERO;
        Assert.assertEquals(ratio.calculation(1), 0);
        Assert.assertEquals(ratio.calculation(-1000), 0);
        Assert.assertEquals(ratio.calculation(41.25680), 0);

        ratio = new Ratio(5, 2);
        Assert.assertEquals(ratio.calculation(10), 4.0);
        Assert.assertEquals(ratio.calculation(8), 3.2);
        Assert.assertEquals(ratio.calculation(2), 0.8);
        Assert.assertEquals(ratio.calculation(0), 0);

        ratio = new Ratio(3, 2);
        System.out.println(ratio.calculation(10));
        Assert.assertEquals(ratio.calculation(10), 6.666666666666667);
        Assert.assertEquals(ratio.calculation(10).intValue(), 6);
        Assert.assertEquals(ratio.calculation(9).intValue(), 6);
        Assert.assertEquals(ratio.calculation(9).doubleValue(), 6.0);
        Assert.assertEquals(ratio.calculation(-0.00025), 0);
        Assert.assertEquals(ratio.calculation(-0.0), 0);

        ratio = new Ratio(213, 5, true);
        Assert.assertEquals(ratio.calculation(45635454), 5);
        Assert.assertEquals(ratio.calculation(0), 5);
        Assert.assertEquals(ratio.calculation(-0), 5);
    }
}