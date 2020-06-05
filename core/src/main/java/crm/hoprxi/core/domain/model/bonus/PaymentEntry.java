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
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-08-22
 */
public class PaymentEntry extends SuperpositionEntry {
    private Payment payment;

    public PaymentEntry(double ratio, Payment payment) {
        super(ratio);
        setPayment(payment);
    }

    public Payment payment() {
        return payment;
    }

    private void setPayment(Payment payment) {
        Objects.requireNonNull(payment, "payment required");
        this.payment = payment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentEntry)) return false;
        if (!super.equals(o)) return false;

        PaymentEntry that = (PaymentEntry) o;

        return payment == that.payment;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (payment != null ? payment.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PaymentEntry.class.getSimpleName() + "[", "]")
                .add("payment=" + payment)
                .add("ratio=" + rate)
                .toString();
    }
}
