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

package crm.hoprxi.core.domain.model.bonus.consumption;

import crm.hoprxi.core.domain.model.DomainRegistry;
import crm.hoprxi.core.domain.model.bonus.Bonus;

import java.util.Objects;
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-09-09
 */

public class Entry<T> {
    private T t;
    private Ratio ratio;

    public Entry(T t, Ratio ratio) {
        this.t = Objects.requireNonNull(t, "t is required");
        setRatio(ratio);
    }

    public T t() {
        return t;
    }

    public Ratio ratio() {
        return ratio;
    }

    private void setRatio(Ratio ratio) {
        if (ratio == null)
            ratio = Ratio.ZERO;
        this.ratio = ratio;
    }

    public Bonus calculation(Number consumption) {
        Number number = ratio.calculation(consumption);
        return new Bonus(number);
    }

    public Entry<T> changeRatio(Ratio newRatio) {
        Objects.requireNonNull(newRatio, "newRatio required");
        if (!ratio.equals(newRatio)) {
            ratio = newRatio;
            DomainRegistry.domainEventPublisher().publish(new EntryRatioChanged<T>(t, newRatio));
            return new Entry<T>(t, newRatio);
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entry)) return false;

        Entry<?> entry = (Entry<?>) o;

        return t != null ? t.equals(entry.t) : entry.t == null;
    }

    @Override
    public int hashCode() {
        return t != null ? t.hashCode() : 0;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Entry.class.getSimpleName() + "[", "]")
                .add("t=" + t)
                .add("ratio=" + ratio)
                .toString();
    }
}
