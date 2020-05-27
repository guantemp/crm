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
    private EnumMap<Cycle, Spss> corresponding;

    public Recency(EnumMap<Cycle, Spss> corresponding) {
        setCorresponding(corresponding);
    }

    public static void main(String[] args) {
        EnumMap<Cycle, Spss> corresponding = new EnumMap<Cycle, Spss>(Cycle.class);
        corresponding.put(Cycle.ONE_WEEK, new Spss(200));
        corresponding.put(Cycle.ONE_MONTH, new Spss(100));
        corresponding.put(Cycle.THERE_MONTHS, new Spss(0));
        corresponding.put(Cycle.SIX_MONTHS, new Spss(-50));
        corresponding.put(Cycle.ONE_YEAR, new Spss(-200));
        Recency recency = new Recency(corresponding);
        System.out.println(recency.calculate(LocalDate.of(2017, 1, 28)));
        System.out.println(recency.calculate(LocalDate.now().minusDays(1)));
    }

    private void setCorresponding(EnumMap<Cycle, Spss> corresponding) {
        Objects.requireNonNull(corresponding, "corresponding required");
        this.corresponding = corresponding;
    }

    public Spss calculate(LocalDate lastDate) {
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

    public Spss calculate(LocalDateTime lastDateTime) {
        return calculate(lastDateTime.toLocalDate());
    }

    /**
     * @param sample this sample must sort by occerOn
     * @return
     */
    public Spss calculate(List<ConsumptionRecord> sample) {
        if (sample.isEmpty())
            return Spss.EMPTY_SPSS;
        return calculate(sample.get(1).occurOn());
    }
}
