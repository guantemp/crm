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

import crm.hoprxi.core.domain.model.bonus.consumption.Ratio;
import org.testng.Assert;
import org.testng.annotations.Test;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-06-03
 */
public class RatioTest {

    @Test
    public void testCalculation() {
        Ratio ratio = Ratio.ONE_TO_ONE;
        Assert.assertEquals(ratio.calculation(1).intValue(), 1);
        Assert.assertEquals(ratio.calculation(0.001), 0.001);

        ratio = Ratio.ZERO;
        Assert.assertEquals(ratio.calculation(1).intValue(), 0);
        Assert.assertEquals(ratio.calculation(1000).intValue(), 0);
        Assert.assertEquals(ratio.calculation(41.25680).intValue(), 0);
        ratio = new Ratio(5, 2);
        Assert.assertEquals(ratio.calculation(10), 4.0);
        Assert.assertEquals(ratio.calculation(8), 3.2);
        Assert.assertEquals(ratio.calculation(2), 0.8);

        ratio = new Ratio(3, 2);
        Assert.assertEquals(ratio.calculation(10), 6.666666666666667);
        Assert.assertEquals(ratio.calculation(-0.00025), 0.0);
        Assert.assertEquals(ratio.calculation(-0.0), 0.0);
    }
}