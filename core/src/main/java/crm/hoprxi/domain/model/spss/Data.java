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

package crm.hoprxi.domain.model.spss;

import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-07-15
 */
public class Data implements Comparable<Data> {
    public static final Data EMPTY_DATA = new Data(0);
    private int value;

    public Data(int value) {
        setValue(value);
    }

    private void setValue(int value) {
        if (value < 0)
            value = 0;
        this.value = value;
    }

    @Override
    public int compareTo(Data o) {
        return value == o.value ? 0 : value > o.value ? 1 : -1;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Data.class.getSimpleName() + "[", "]")
                .add("value=" + value)
                .toString();
    }
}
