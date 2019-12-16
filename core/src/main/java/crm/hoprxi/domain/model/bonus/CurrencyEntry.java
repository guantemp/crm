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

package crm.hoprxi.domain.model.bonus;

import java.util.Currency;
import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-08-22
 */
public final class CurrencyEntry {
    private Currency currency;
    private Ratio ratio;

    public CurrencyEntry(Currency currency, Ratio ratio) {
        setCurrency(currency);
        setRatio(ratio);
    }

    public Currency currency() {
        return currency;
    }

    private void setCurrency(Currency currency) {
        Objects.requireNonNull(currency, "currency required");
        this.currency = currency;
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

        CurrencyEntry that = (CurrencyEntry) o;

        if (currency != null ? !currency.equals(that.currency) : that.currency != null) return false;
        return ratio != null ? ratio.equals(that.ratio) : that.ratio == null;
    }

    @Override
    public int hashCode() {
        int result = currency != null ? currency.hashCode() : 0;
        result = 31 * result + (ratio != null ? ratio.hashCode() : 0);
        return result;
    }
}
