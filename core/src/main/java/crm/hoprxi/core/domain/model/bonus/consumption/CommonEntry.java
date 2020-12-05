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

import java.util.Objects;
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-09-10
 */
public class CommonEntry extends Entry {
    public static final CommonEntry ONE_TO_ONE = new CommonEntry(Ratio.ONE_TO_ONE);
    public static final CommonEntry ZERO = new CommonEntry(Ratio.ZERO);

    public CommonEntry(Ratio ratio) {
        super(ratio);
    }

    public String name() {
        return "common";
    }

    @Override
    public CommonEntry changeRatio(Ratio newRatio) {
        Objects.requireNonNull(newRatio, "newRatio required");
        if (!ratio.equals(newRatio)) {
            DomainRegistry.domainEventPublisher().publish(new CommonEntryRatioChanged(newRatio));
            return new CommonEntry(newRatio);
        }
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CommonEntry.class.getSimpleName() + "[", "]")
                .add("ratio=" + ratio)
                .toString();
    }
}
