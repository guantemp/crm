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

package crm.hoprxi.core.domain.model.customer.enterprise;

import mi.hoprxi.id.LongId;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-05-29
 */
public interface EnterpriseRepository {
    default String nextIdentity() {
        return String.valueOf(LongId.generate());
    }

    /**
     * @param enterprise
     */
    void save(Enterprise enterprise);

    /**
     * @param id
     * @return
     */
    Enterprise find(String id);

    /**
     * @param id
     */
    void remove(String id);

    /**
     * @param offset
     * @param limit
     * @return
     */
    Enterprise[] findAll(int offset, int limit);
}
