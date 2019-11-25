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

package crm.hoprxi.domain.model;

import javax.money.MonetaryAmount;
import java.time.LocalDate;
import java.util.Objects;
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-11-25
 */
public class LineOfCredit {
    private MonetaryAmount quota;
    private LocalDate billDate;
    private int billingCycleDays;

    public LineOfCredit(MonetaryAmount quota, int billingCycleDays) {
        setQuota(quota);
        setBillingCycleDays(billingCycleDays);
        billDate = LocalDate.now().minusDays(billingCycleDays);
    }

    public LineOfCredit(MonetaryAmount quota, int billingCycleDays, LocalDate billDate) {
        setQuota(quota);
        setBillingCycleDays(billingCycleDays);
        setBillDate(billDate);
    }

    private void setBillingCycleDays(int billingCycleDays) {
        if (billingCycleDays <= 0)
            throw new IllegalArgumentException("billingCycleDays will larger zero");
        this.billingCycleDays = billingCycleDays;
    }

    private void setQuota(MonetaryAmount quota) {
        this.quota = quota;
    }

    /**
     * @param quota
     * @return this if quota is negative or zero or greater old quota
     */
    public LineOfCredit reductionTo(MonetaryAmount quota) {
        Objects.requireNonNull(quota, "quota required");
        if (quota.isNegativeOrZero() || quota.isGreaterThan(this.quota))
            return this;
        return new LineOfCredit(quota, billingCycleDays, billDate);
    }

    /**
     * @param quota
     * @return this if quota is negative or zero or less old quota
     */
    public LineOfCredit IncreaseTo(MonetaryAmount quota) {
        Objects.requireNonNull(quota, "quota required");
        if (quota.isNegativeOrZero() || quota.isLessThan(this.quota))
            return this;
        return new LineOfCredit(quota, billingCycleDays, billDate);
    }

    public boolean isBillDate() {
        return LocalDate.now().isBefore(billDate);
    }

    private void setBillDate(LocalDate billDate) {
        Objects.requireNonNull(billDate, "billDate required");
        if (billDate.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Billing date must be later than now");
        this.billDate = billDate;
    }

    public LineOfCredit updateBillDate() {
        return new LineOfCredit(quota, billingCycleDays, LocalDate.now().minusDays(billingCycleDays));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LineOfCredit that = (LineOfCredit) o;

        if (billingCycleDays != that.billingCycleDays) return false;
        if (quota != null ? !quota.equals(that.quota) : that.quota != null) return false;
        return billDate != null ? billDate.equals(that.billDate) : that.billDate == null;
    }

    @Override
    public int hashCode() {
        int result = quota != null ? quota.hashCode() : 0;
        result = 31 * result + (billDate != null ? billDate.hashCode() : 0);
        result = 31 * result + billingCycleDays;
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LineOfCredit.class.getSimpleName() + "[", "]")
                .add("quota=" + quota)
                .add("billDate=" + billDate)
                .add("billingCycleDays=" + billingCycleDays)
                .toString();
    }
}
