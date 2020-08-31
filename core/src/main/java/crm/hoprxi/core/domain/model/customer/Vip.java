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

package crm.hoprxi.core.domain.model.customer;

import java.time.LocalDate;
import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-05-28
 */
public class Vip {
    public static Vip NOT = new Vip(LocalDate.MIN) {
        @Override
        public boolean isWithinThePeriodOfValidity() {
            return false;
        }

        @Override
        public Vip delay(int days) {
            return this;
        }
    };
    private LocalDate validUntil;

    public Vip(LocalDate validUntil) {
        Objects.requireNonNull(validUntil, "validUntil required.");
        if (validUntil != LocalDate.MIN && validUntil.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Valid for at least 1 day");
        this.validUntil = validUntil;
    }

    public LocalDate validUntil() {
        return validUntil;
    }

    public boolean isWithinThePeriodOfValidity() {
        return LocalDate.now().isBefore(validUntil) || LocalDate.now().isEqual(validUntil);
    }

    public Vip delay(int days) {
        if (LocalDate.now().isAfter(validUntil))
            return new Vip(LocalDate.now().plusDays(days));
        return new Vip(validUntil.plusDays(days));
    }

    public Vip cancel() {
        return Vip.NOT;
    }
}
