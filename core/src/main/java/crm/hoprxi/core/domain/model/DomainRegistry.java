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
package crm.hoprxi.core.domain.model;


import event.hoprxi.domain.model.DomainEventPublisher;
import event.hoprxi.infrastruture.simple.SimpleDomainEventPublisher;
import mi.hoprxi.crypto.HashService;
import mi.hoprxi.crypto.SM3Hash;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 2019-10-24
 */
public class DomainRegistry {
    public static DomainEventPublisher domainEventPublisher() {
        return SimpleDomainEventPublisher.instance();
    }

    /*
        public static boolean validIssuerId(Issuer issuerId) {
            if (issuerId == null || issuerId.isEmpty())
                return false;
            return true;
        }
    */
    public static boolean validCustomerId(String customerId) {
        if (customerId == null || customerId.isEmpty())
            return false;
        return true;
    }

    public static boolean validCategoryId(String categoryId) {
        if (categoryId == null || categoryId.isEmpty())
            return false;
        return true;
    }

    public static HashService getHashService() {
        return new SM3Hash();
    }
}
