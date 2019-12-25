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

package crm.hoprxi.domain.model.spss;

import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.time.LocalDate;
import java.util.*;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-09-10
 */
public final class Monetary {
    private Map<Consumption, Spss> corresponding;

    public Monetary(Map<Consumption, Spss> corresponding) {
        setCorresponding(corresponding);
    }

    private void setCorresponding(Map<Consumption, Spss> corresponding) {
        Objects.requireNonNull(corresponding, "corresponding required");
        this.corresponding = corresponding;
    }

    public Spss calculate(List<ConsumptionRecord> sample) {
        Collections.sort(sample);
        Consumption[] consumptions = corresponding.keySet().toArray(new Consumption[0]);
        Arrays.sort(consumptions, (Consumption o1, Consumption o2) -> {
            if (o1.cycle.interval() == o2.cycle.interval()) {
                return o1.amount.compareTo(o2.amount);
            } else {
                return o1.cycle.interval() > o2.cycle.interval() ? 1 : -1;
            }
        });
        int numberOfDays = 0;
        for (Consumption consumption : consumptions) {
            if (numberOfDays != consumption.cycle.interval()) {
                LocalDate value = LocalDate.now().minusDays(consumption.cycle.interval());
                MonetaryAmount count = sample.parallelStream().filter(d -> d.occurOn().toLocalDate().isAfter(value)).map(ConsumptionRecord::amount)
                        .reduce(Money.of(0, javax.money.Monetary.getCurrency(Locale.getDefault())), MonetaryAmount::add);
                if (count.isGreaterThanOrEqualTo(consumption.amount))
                    return corresponding.get(consumption);
            }
        }
        return Spss.EMPTY_SPSS;
    }

    public static class Consumption {
        private Cycle cycle;
        private MonetaryAmount amount;

        public Consumption(Cycle cycle, MonetaryAmount amount) {
            this.cycle = cycle;
            this.amount = amount;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Consumption that = (Consumption) o;

            if (cycle != that.cycle) return false;
            return amount != null ? amount.equals(that.amount) : that.amount == null;
        }

        @Override
        public int hashCode() {
            int result = cycle != null ? cycle.hashCode() : 0;
            result = 31 * result + (amount != null ? amount.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Consumption{" +
                    "cycle=" + cycle +
                    ", amount=" + amount +
                    '}';
        }

        public Cycle cycle() {
            return cycle;
        }

        public MonetaryAmount amount() {
            return amount;
        }
    }
}
