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

package crm.hoprxi.domain.model.rmf;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-09-10
 */
public final class Recency {
    private EnumMap<Cycle, Credit> corresponding;

    public Recency(EnumMap<Cycle, Credit> corresponding) {
        setCorresponding(corresponding);
    }

    public static void main(String[] args) {
        EnumMap<Cycle, Credit> corresponding = new EnumMap<Cycle, Credit>(Cycle.class);
        corresponding.put(Cycle.ONE_WEEK, new Credit(200));
        corresponding.put(Cycle.ONE_MONTH, new Credit(100));
        corresponding.put(Cycle.THERE_MONTHS, new Credit(0));
        corresponding.put(Cycle.SIX_MONTHS, new Credit(-50));
        corresponding.put(Cycle.ONE_YEAR, new Credit(-200));
        Recency recency = new Recency(corresponding);
        System.out.println(recency.calculate(LocalDate.of(2017, 1, 28)));
        System.out.println(recency.calculate(LocalDate.now().minusDays(1)));
    }

    private void setCorresponding(EnumMap<Cycle, Credit> corresponding) {
        Objects.requireNonNull(corresponding, "corresponding required");
        this.corresponding = corresponding;
    }

    public Credit calculate(LocalDate lastDate) {
        Cycle temp = null;
        long days = ChronoUnit.DAYS.between(lastDate, LocalDate.now());
        for (Cycle cycle : Cycle.values()) {
            if (days >= cycle.interval() && corresponding.get(cycle) != null) {
                if (temp == null)
                    temp = cycle;
                else if (temp.interval() <= cycle.interval())
                    temp = cycle;
            }
        }
        return corresponding.get(temp);
    }

    public Credit calculate(LocalDateTime lastDateTime) {
        return calculate(lastDateTime.toLocalDate());
    }

    /**
     * @param sample this sample must sort by occerOn
     * @return
     */
    public Credit calculate(List<ConsumptionRecord> sample) {
        if (sample.isEmpty())
            return Credit.NO_CREDIT;
        return calculate(sample.get(1).occurOn());
    }
}
