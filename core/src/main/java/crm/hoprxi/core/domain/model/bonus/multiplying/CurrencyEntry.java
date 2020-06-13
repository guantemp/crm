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

package crm.hoprxi.core.domain.model.bonus.multiplying;

import java.util.Currency;
import java.util.Objects;
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-08-22
 */
public class CurrencyEntry extends MultiplyingEntry {
    private Currency currency;

    public CurrencyEntry(double ratio, Currency currency) {
        super(ratio);
        setCurrency(currency);
    }

    public Currency currency() {
        return currency;
    }

    private void setCurrency(Currency currency) {
        Objects.requireNonNull(currency, "currency required");
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CurrencyEntry)) return false;
        if (!super.equals(o)) return false;

        CurrencyEntry that = (CurrencyEntry) o;

        return currency != null ? currency.equals(that.currency) : that.currency == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CurrencyEntry.class.getSimpleName() + "[", "]")
                .add("currency=" + currency)
                .add("ratio=" + rate)
                .toString();
    }
}
