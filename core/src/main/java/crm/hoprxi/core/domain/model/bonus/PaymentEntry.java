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


import crm.hoprxi.core.domain.model.collaborator.Payment;

import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-08-22
 */
public final class PaymentEntry {
    private Payment payment;
    private Ratio ratio;

    public PaymentEntry(Payment payment, Ratio ratio) {
        setPayment(payment);
        setRatio(ratio);
    }

    public Payment payment() {
        return payment;
    }

    private void setPayment(Payment payment) {
        Objects.requireNonNull(payment, "payment required");
        this.payment = payment;
    }

    public Ratio ratio() {
        return ratio;
    }

    private void setRatio(Ratio ratio) {
        if (ratio == null)
            ratio = Ratio.ONE_TO_ONE;
        this.ratio = ratio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaymentEntry that = (PaymentEntry) o;

        if (payment != that.payment) return false;
        return ratio != null ? ratio.equals(that.ratio) : that.ratio == null;
    }

    @Override
    public int hashCode() {
        int result = payment != null ? payment.hashCode() : 0;
        result = 31 * result + (ratio != null ? ratio.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PaymentEntry{" +
                "payment=" + payment +
                ", ratio=" + ratio +
                '}';
    }
}
