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


import crm.hoprxi.core.domain.model.collaborator.Referee;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-08-22
 */
public class ShareEntry {
    private Referee referee;
    private Ratio ratio;

    public ShareEntry(Referee referee, Ratio ratio) {
        setReferee(referee);
        setRatio(ratio);
    }

    public Referee referee() {
        return referee;
    }

    private void setReferee(Referee referee) {
        this.referee = referee;
    }

    public Ratio ratio() {
        return ratio;
    }

    private void setRatio(Ratio ratio) {
        this.ratio = ratio;
    }

    @Override
    public String toString() {
        return "CommissionEntry{" +
                "referee=" + referee +
                ", ratio=" + ratio +
                '}';
    }
}
