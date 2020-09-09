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


import crm.hoprxi.core.domain.model.collaborator.Referee;

import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-08-22
 */
public class ShareEntry extends MultiplyingEntry {
    private Referee referee;

    public ShareEntry(Number rate, Referee referee) {
        super(rate);
        this.referee = referee;
    }

    public Referee referee() {
        return referee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShareEntry)) return false;
        if (!super.equals(o)) return false;

        ShareEntry that = (ShareEntry) o;

        return referee == that.referee;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (referee != null ? referee.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ShareEntry.class.getSimpleName() + "[", "]")
                .add("referee=" + referee)
                .toString();
    }
}
