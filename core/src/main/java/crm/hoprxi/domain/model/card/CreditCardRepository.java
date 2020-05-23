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

package crm.hoprxi.domain.model.card;

import mi.hoprxi.id.LongId;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2020-03-27
 */
public interface CreditCardRepository {
    /**
     * @return
     */
    default String nextIdentity() {
        return String.valueOf(LongId.generate());
    }

    /**
     * @param creditCard
     */
    void save(CreditCard creditCard);

    /**
     * @param id
     */
    void remove(String id);

    /**
     * @param id
     * @return
     */
    CreditCard find(String id);

    /**
     * @param cardFaceNumber is support regular
     * @return
     */
    CreditCard[] findByCardFaceNumber(String cardFaceNumber);

    /**
     * @param customerId
     * @return
     */
    CreditCard[] findByCustomer(String customerId);

    /**
     * @param offset
     * @param limit
     * @return
     */
    CreditCard[] findAll(int offset, int limit);

    int size();
}
