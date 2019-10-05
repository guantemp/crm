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
 */

package crm.hoprxi.domain.model.card;


import mi.hoprxi.id.ObjectId;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-08-04
 */
public interface CardRepository {
    void save(Card card);

    /**
     * @param offset
     * @param limit
     * @return
     */
    Card[] findAll(long offset, int limit);

    /**
     * @param identity
     * @return
     */
    Card find(String identity);

    /**
     * @param telephone
     * @return
     */
    Card findByTelephone(String telephone);

    /**
     * @param number
     * @return
     */
    Card findByNumber(String number);

    /**
     * @return
     */
    default String nextIdentity() {
        return new ObjectId().id();
    }

    /**
     * @param number
     * @param password
     * @return
     */
    Card authenticCredentials(String number, String password);

    /**
     * @param identity
     */
    void remove(String identity);
}
