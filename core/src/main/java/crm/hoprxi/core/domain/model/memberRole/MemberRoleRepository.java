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
package crm.hoprxi.core.domain.model.memberRole;

import java.util.Collection;

/***
 * @author <a href="mailto:myis1000@126.com">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.2 builder 20170303
 */
public interface MemberRoleRepository {

    /**
     * @return
     */
    Collection<MemberRole> allOf();

    /**
     * @param memberRole
     */
    void remove(MemberRole memberRole);

    /**
     * @param memberRole
     */
    void save(MemberRole memberRole);
}
