/*
 * Copyright (c) 2018 www.hoprxi.com rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package crm.hoprxi.domain.model.card;

import java.time.LocalDate;
import java.util.Objects;
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-07-20
 */
public class TermOfValidity {
    public static final LocalDate DAY_OF_INFAMY = LocalDate.of(2015, 3, 26);
    public static final TermOfValidity PERMANENCE = new TermOfValidity(DAY_OF_INFAMY,
            DAY_OF_INFAMY) {
        public boolean isNowValid() {
            return true;
        }

        @Override
        public String toString() {
            return "Permanent validity";
        }
    };
    private LocalDate expiryDate;
    private LocalDate startDate;

    /**
     * @param startDate
     * @param expiryDate
     */
    public TermOfValidity(LocalDate startDate, LocalDate expiryDate) {
        super();
        setStartDate(startDate);
        setExpiryDate(expiryDate);
    }

    public LocalDate expiryDate() {
        return expiryDate;
    }

    public TermOfValidity postponeTo(LocalDate newExpiryDate) {
        if (newExpiryDate.isBefore(expiryDate))
            return this;
        return new TermOfValidity(startDate, newExpiryDate);
    }

    public TermOfValidity cutDownTo(LocalDate newExpiryDate) {
        if (newExpiryDate.isAfter(expiryDate))
            return this;
        return new TermOfValidity(startDate, newExpiryDate);
    }

    public boolean isNowValid() {
        LocalDate now = LocalDate.now();
        if (startDate.isEqual(expiryDate) || now.isEqual(startDate) || now.isEqual(expiryDate)
                || (now.isAfter(startDate) && now.isBefore(expiryDate)))
            return true;
        return false;
    }

    private void setExpiryDate(LocalDate expiryDate) {
        Objects.requireNonNull(expiryDate, "expiry date required");
        if (expiryDate.isBefore(startDate))
            throw new IllegalArgumentException(
                    "Start date must be consistent with expiryDate or before the expiryDate.");
        this.expiryDate = expiryDate;
    }

    private void setStartDate(LocalDate startDate) {
        this.startDate = Objects.requireNonNull(startDate, "start date required");
    }

    public LocalDate startDate() {
        return startDate;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TermOfValidity.class.getSimpleName() + "[", "]")
                .add("expiryDate=" + expiryDate)
                .add("startDate=" + startDate)
                .toString();
    }
}
