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

import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2019-08-22
 */
public class GeneralEntry {
    public static final GeneralEntry ONE_TO_ONE = new GeneralEntry(Ratio.ONE_TO_ONE);
    private Ratio ratio;

    public GeneralEntry(Ratio ratio) {
        setRatio(ratio);
    }

    public GeneralEntry valueOf(Ratio ratio) {
        if (ratio == Ratio.ONE_TO_ONE)
            return ONE_TO_ONE;
        return new GeneralEntry(ratio);
    }

    private void setRatio(Ratio ratio) {
        Objects.requireNonNull(ratio, "ratio required");
        this.ratio = ratio;
    }

    public Ratio ratio() {
        return ratio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeneralEntry that = (GeneralEntry) o;

        return ratio != null ? ratio.equals(that.ratio) : that.ratio == null;
    }

    @Override
    public int hashCode() {
        return ratio != null ? ratio.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "GeneralEntry{" +
                "ratio=" + ratio +
                '}';
    }
}
