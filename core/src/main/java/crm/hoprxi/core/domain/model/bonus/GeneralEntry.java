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

import java.util.Objects;
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.2 builder 2020-05-31
 */
public class GeneralEntry extends BasicsEntry {
    public static final GeneralEntry ONE_TO_ONE = new GeneralEntry(Ratio.ONE_TO_ONE);

    public GeneralEntry(Ratio ratio) {
        super(ratio);
    }

    public GeneralEntry of(Ratio ratio) {
        if (ratio == Ratio.ONE_TO_ONE)
            return ONE_TO_ONE;
        return new GeneralEntry(ratio);
    }

    public GeneralEntry changeRatio(Ratio newRatio) {
        Objects.requireNonNull(newRatio, "newRatio required");
        if (ratio.equals(newRatio))
            return this;
        return new GeneralEntry(newRatio);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GeneralEntry.class.getSimpleName() + "[", "]")
                .add("ratio=" + ratio)
                .toString();
    }
}
