/*
 * Copyright (c) 2018. www.hoprxi.com rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package crm.hoprxi.domain.model.rmf;

import java.time.temporal.ChronoUnit;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-09-10
 */
public enum Cycle implements Comparable<Cycle> {
    ONE_WEEK(7), ONE_MONTH(30), THERE_MONTHS(90), SIX_MONTHS(180), ONE_YEAR(360);

    private int interval;
    private ChronoUnit unit;

    Cycle(int interval) {
        this.interval = interval;
        unit = ChronoUnit.DAYS;
    }

    public int interval() {
        return interval;
    }

    public ChronoUnit unit() {
        return unit;
    }
}

