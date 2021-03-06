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

package crm.hoprxi.core.domain.model.spss;

import javax.money.MonetaryAmount;
import java.time.LocalDateTime;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-09-26
 */
public class ConsumptionRecord implements Comparable<ConsumptionRecord> {
    private String customerId;
    private LocalDateTime occurOn;
    private MonetaryAmount amount;

    public ConsumptionRecord(String customerId, LocalDateTime occurOn, MonetaryAmount amount) {
        this.customerId = customerId;
        this.occurOn = occurOn;
        this.amount = amount;
    }

    public LocalDateTime occurOn() {
        return occurOn;
    }

    public String customerId() {
        return customerId;
    }

    public MonetaryAmount amount() {
        return amount;
    }

    @Override
    public int compareTo(ConsumptionRecord o) {
        return occurOn.compareTo(o.occurOn);
    }
}