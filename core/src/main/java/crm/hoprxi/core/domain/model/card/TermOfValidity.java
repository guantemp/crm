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

import java.time.LocalDate;
import java.util.Objects;
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.2 builder 2020-03-27
 */
public class TermOfValidity {
    private static final LocalDate DAY_OF_INFAMY = LocalDate.of(2015, 3, 26);
    public static final TermOfValidity PERMANENCE = new TermOfValidity(DAY_OF_INFAMY,
            DAY_OF_INFAMY) {
        public boolean isValidityPeriod() {
            return true;
        }

        @Override
        public TermOfValidity postponeExpiryDate(LocalDate newExpiryDate) {
            return this;
        }

        @Override
        public TermOfValidity broughtForwardExpiryDate(LocalDate newExpiryDate) {
            return this;
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
     * @return
     */
    public static TermOfValidity getInstance(LocalDate startDate, LocalDate expiryDate) {
        if (startDate.isEqual(DAY_OF_INFAMY) && expiryDate.isEqual(DAY_OF_INFAMY))
            return PERMANENCE;
        return new TermOfValidity(startDate, expiryDate);
    }

    /**
     * @param startDate
     * @param expiryDate
     */
    public TermOfValidity(LocalDate startDate, LocalDate expiryDate) {
        setStartDate(startDate);
        setExpiryDate(expiryDate);
    }

    public TermOfValidity(LocalDate expiryDate) {
        this(LocalDate.now(), expiryDate);
    }

    private void setExpiryDate(LocalDate expiryDate) {
        Objects.requireNonNull(expiryDate, "expiryDate required");
        if (expiryDate.isBefore(startDate))
            throw new IllegalArgumentException(
                    "startDate must be consistent with expiryDate or before the expiryDate,now is:[" + startDate + " -> " + expiryDate + "]");
        this.expiryDate = expiryDate;
    }

    private void setStartDate(LocalDate startDate) {
        Objects.requireNonNull(startDate, "startDate required");
        if (startDate.isBefore(LocalDate.now()) && !startDate.isEqual(DAY_OF_INFAMY))
            throw new IllegalArgumentException("startDate need to be today or later");
        this.startDate = startDate;
    }

    public LocalDate expiryDate() {
        return expiryDate;
    }

    public TermOfValidity postponeExpiryDate(LocalDate newExpiryDate) {
        if (newExpiryDate.isBefore(expiryDate))
            return this;
        return new TermOfValidity(startDate, newExpiryDate);
    }

    public TermOfValidity postponeStartDate(LocalDate newStartDate) {
        if (newStartDate.isBefore(startDate) || newStartDate.isAfter(expiryDate))
            return this;
        return new TermOfValidity(newStartDate, expiryDate);
    }

    public TermOfValidity broughtForwardExpiryDate(LocalDate newExpiryDate) {
        if (newExpiryDate.isAfter(expiryDate) || newExpiryDate.isBefore(startDate))
            return this;
        return new TermOfValidity(startDate, newExpiryDate);
    }

    public TermOfValidity broughtForwardStartDate(LocalDate newStartDate) {
        if (newStartDate.isBefore(LocalDate.now()) || newStartDate.isAfter(startDate))
            return this;
        return new TermOfValidity(newStartDate, expiryDate);
    }

    public boolean isValidityPeriod() {
        LocalDate now = LocalDate.now();
        if ((now.isAfter(startDate) && now.isBefore(expiryDate)) || now.isEqual(startDate) || now.isEqual(expiryDate))
            return true;
        return false;
    }

    public LocalDate startDate() {
        return startDate;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TermOfValidity.class.getSimpleName() + "[", "]")
                .add("startDate=" + startDate)
                .add("expiryDate=" + expiryDate)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TermOfValidity that = (TermOfValidity) o;

        if (expiryDate != null ? !expiryDate.equals(that.expiryDate) : that.expiryDate != null) return false;
        return startDate != null ? startDate.equals(that.startDate) : that.startDate == null;
    }

    @Override
    public int hashCode() {
        int result = expiryDate != null ? expiryDate.hashCode() : 0;
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        return result;
    }
}
