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

package crm.hoprxi.core.domain.model.power;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-10-20
 */
public class PowerBook {
    public static PowerBook NO_EQUITY = new PowerBook(new HashSet<>(0));
    private Set<Power> powers;

    public PowerBook(Set<Power> powers) {
        this.powers = Objects.requireNonNull(powers, "powers required");
    }


    public boolean add(Power power) {
        return powers.add(power);
    }

    public boolean remove(Power power) {
        return powers.remove(power);
    }

    public Iterator<Power> equites() {
        return powers.iterator();
    }
}
