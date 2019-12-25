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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-09-10
 */
public final class Frequency {
    private Map<Rate, Spss> corresponding;

    public Frequency(Map<Rate, Spss> corresponding) {
        setCorresponding(corresponding);
    }

    public static void main(String[] args) {
        Map<Rate, Spss> map = new HashMap<>();
        map.put(new Rate(Cycle.ONE_MONTH, (short) 15), new Spss(400));
        map.put(new Rate(Cycle.ONE_MONTH, (short) 3), new Spss(100));
        map.put(new Rate(Cycle.ONE_WEEK, (short) 7), new Spss(700));
        map.put(new Rate(Cycle.ONE_WEEK, (short) 3), new Spss(300));
        map.put(new Rate(Cycle.ONE_MONTH, (short) 7), new Spss(200));
        map.put(new Rate(Cycle.ONE_WEEK, (short) 1), new Spss(100));
        map.put(new Rate(Cycle.ONE_MONTH, (short) 10), new Spss(300));
        Frequency frequency = new Frequency(map);

        LocalDateTime[] sample = new LocalDateTime[]{
                LocalDateTime.of(2018, 9, 15, 12, 0, 0),
                LocalDateTime.of(2018, 9, 14, 13, 0, 0),
                LocalDateTime.of(2018, 9, 14, 10, 0, 0),
                LocalDateTime.of(2018, 4, 15, 13, 34, 56),
                LocalDateTime.of(2018, 4, 12, 20, 20, 34),
                LocalDateTime.of(2018, 4, 9, 23, 15, 45),
                LocalDateTime.of(2018, 4, 10, 1, 2, 3),
                LocalDateTime.of(2018, 4, 10, 17, 23, 12),
                LocalDateTime.of(2018, 4, 12, 8, 0, 23),
                LocalDateTime.of(2018, 8, 22, 9, 30, 2),
                LocalDateTime.of(2018, 8, 20, 11, 11, 11),
                LocalDateTime.of(2018, 8, 18, 14, 45, 59),
                LocalDateTime.of(2018, 8, 15, 13, 23, 34),
                LocalDateTime.of(2018, 8, 12, 7, 9, 12),
                LocalDateTime.of(2018, 7, 10, 5, 45, 43),
                LocalDateTime.of(2018, 6, 30, 4, 21, 12),
                LocalDateTime.of(2018, 6, 10, 22, 45, 23),
                LocalDateTime.of(2018, 6, 10, 21, 34, 45),
                LocalDateTime.of(2018, 6, 20, 20, 43, 23),
                LocalDateTime.of(2018, 6, 4, 19, 19, 19),
                LocalDateTime.of(2018, 5, 4, 18, 59, 12),
                LocalDateTime.of(2018, 5, 4, 17, 33, 54),
        };
        System.out.println(frequency.calculate(sample));
        System.out.println(frequency.calculate(Arrays.asList(sample)));
    }

    private void setCorresponding(Map<Rate, Spss> corresponding) {
        Objects.requireNonNull(corresponding, "corresponding required");
        this.corresponding = corresponding;
    }

    /**
     * @param sample
     * @return
     */
    public Spss calculate(LocalDateTime[] sample) {
        Arrays.parallelSort(sample, (o1, o2) -> o2.compareTo(o1));
        Rate[] rates = corresponding.keySet().toArray(new Rate[0]);
        Arrays.sort(rates, (Rate o1, Rate o2) -> {
            if (o1.cycle.interval() == o2.cycle.interval()) {
                return o1.number == o2.number ? 0 : o1.number < o2.number ? 1 : -1;
            } else {
                return o1.cycle.interval() > o2.cycle.interval() ? 1 : -1;
            }
        });
        int numberOfDays = 0;
        for (Rate rate : rates) {
            long numberOfTimes = 0l;
            if (numberOfDays != rate.cycle.interval()) {
                LocalDate value = LocalDate.now().minusDays(rate.cycle.interval());
                numberOfTimes = Arrays.stream(sample).filter(d -> d.toLocalDate().isAfter(value)).count();
                if (numberOfTimes >= rate.number)
                    return corresponding.get(rate);
            }
        }
        return Spss.EMPTY_SPSS;
    }

    /**
     * @param sample Sort according to the time of occurrence.
     * @return
     */
    public Spss calculate(List<LocalDateTime> sample) {
        Collections.sort(sample);
        Rate[] rates = corresponding.keySet().toArray(new Rate[0]);
        Arrays.sort(rates, (Rate o1, Rate o2) -> {
            if (o1.cycle.interval() == o2.cycle.interval()) {
                return o1.number == o2.number ? 0 : o1.number < o2.number ? 1 : -1;
            } else {
                return o1.cycle.interval() > o2.cycle.interval() ? 1 : -1;
            }
        });
        int numberOfDays = 0;
        for (Rate rate : rates) {
            long numberOfTimes = 0l;
            if (numberOfDays != rate.cycle.interval()) {
                LocalDate value = LocalDate.now().minusDays(rate.cycle.interval());
                numberOfTimes = sample.parallelStream().filter(d -> d.toLocalDate().isAfter(value)).count();
                if (numberOfTimes >= rate.number)
                    return corresponding.get(rate);
            }
        }
        return Spss.EMPTY_SPSS;
    }

    public static class Rate {
        private Cycle cycle;
        private short number;

        public Rate(Cycle cycle, short number) {
            this.cycle = cycle;
            this.number = number;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Rate rate = (Rate) o;

            if (number != rate.number) return false;
            return cycle == rate.cycle;
        }

        @Override
        public int hashCode() {
            int result = cycle != null ? cycle.hashCode() : 0;
            result = 31 * result + (int) number;
            return result;
        }

        @Override
        public String toString() {
            return "Rate{" +
                    "cycle=" + cycle +
                    ", number=" + number +
                    '}';
        }
    }
}
