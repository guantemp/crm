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

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-12-16
 */
public class BonusDeficiencyException extends RuntimeException {
    /**
     * 仅包含message, 没有cause, 不记录栈异常, 性能最高
     *
     * @param message
     */
    public BonusDeficiencyException(String message) {
        super(message, null, false, false);
    }

    public BonusDeficiencyException(String message, Throwable cause) {
        super(message, cause, false, true);
    }
}
