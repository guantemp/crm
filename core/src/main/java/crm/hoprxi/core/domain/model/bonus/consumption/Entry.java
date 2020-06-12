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

import com.arangodb.entity.DocumentField;
import crm.hoprxi.core.domain.model.bonus.Bonus;

import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-06-03
 */
public abstract class Entry {
    @DocumentField(DocumentField.Type.KEY)
    protected String id;
    protected Ratio ratio;

    public Entry(Ratio ratio) {
        setRatio(ratio);
    }

    private void setRatio(Ratio ratio) {
        if (ratio == null)
            ratio = Ratio.ZERO;
        this.ratio = ratio;
    }

    public String id() {
        return id;
    }

    public Ratio ratio() {
        return ratio;
    }

    public void changeRatio(Ratio newRatio) {
        Objects.requireNonNull(newRatio, "newRatio required");
        if (!ratio.equals(newRatio))
            ratio = newRatio;
    }

    public Bonus calculation(Number consumption) {
        Number number = ratio.calculation(consumption);
        return new Bonus(number);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entry)) return false;

        Entry entry = (Entry) o;

        return id != null ? id.equals(entry.id) : entry.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
