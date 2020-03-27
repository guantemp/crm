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

package crm.hoprxi.domain.model.card;

import javax.money.MonetaryAmount;
import java.time.LocalDate;
import java.util.Objects;
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.2 2020-03-27
 */
public class LineOfCredit {
    private MonetaryAmount quota;
    private LocalDate repaymentDate;
    private int billDate;

    public LineOfCredit(MonetaryAmount quota, int billDate) {
        this(quota, billDate, LocalDate.now().minusDays(billDate));
    }

    private LineOfCredit(MonetaryAmount quota, int billDate, LocalDate repaymentDate) {
        setQuota(quota);
        setBillDate(billDate);
        this.repaymentDate = repaymentDate;
    }

    private void setBillDate(int billDate) {
        if (billDate < 3 || billDate > 28)
            throw new IllegalArgumentException("Bill date will is 4-28");
        this.billDate = billDate;
    }

    private void setQuota(MonetaryAmount quota) {
        Objects.requireNonNull(quota, "quota required");
        if (quota.isNegativeOrZero())
            throw new IllegalArgumentException("quota must larger zero");
        this.quota = quota;
    }

    /**
     * @param quota
     * @return this if quota is negative or zero or greater old quota
     */
    public LineOfCredit reductionQuota(MonetaryAmount quota) {
        Objects.requireNonNull(quota, "quota required");
        if (quota.isNegativeOrZero() || quota.isGreaterThan(this.quota))
            return this;
        return new LineOfCredit(quota, billDate, repaymentDate);
    }

    /**
     * @param quota
     * @return this if quota is negative or zero or less old quota
     */
    public LineOfCredit upgradeQuota(MonetaryAmount quota) {
        Objects.requireNonNull(quota, "quota required");
        if (quota.isNegativeOrZero() || quota.isLessThan(this.quota))
            return this;
        return new LineOfCredit(quota, billDate, repaymentDate);
    }

    public LocalDate repaymentDate() {
        return repaymentDate;
    }

    public LineOfCredit nextRepaymentDate() {
        return new LineOfCredit(quota, billDate, LocalDate.now().minusDays(billDate));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LineOfCredit that = (LineOfCredit) o;

        if (billDate != that.billDate) return false;
        if (quota != null ? !quota.equals(that.quota) : that.quota != null) return false;
        return repaymentDate != null ? repaymentDate.equals(that.repaymentDate) : that.repaymentDate == null;
    }

    @Override
    public int hashCode() {
        int result = quota != null ? quota.hashCode() : 0;
        result = 31 * result + (repaymentDate != null ? repaymentDate.hashCode() : 0);
        result = 31 * result + billDate;
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LineOfCredit.class.getSimpleName() + "[", "]")
                .add("quota=" + quota)
                .add("repaymentDate=" + repaymentDate)
                .add("billDate=" + billDate)
                .toString();
    }
}
