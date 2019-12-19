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

package crm.hoprxi.infrastructure.persistence;

import crm.hoprxi.domain.model.customer.person.Person;
import crm.hoprxi.domain.model.spss.Data;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.MonthDay;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-12-19
 */
public class ArangoDBPersonRepositoryTest {
    private static final ArangoDBPersonRepository repository = new ArangoDBPersonRepository("crm");

    @BeforeClass
    public static void setUpBeforeClass() {
        Person guan = new Person("18982455056", "官相焕", "111220", Data.EMPTY_DATA, null,
                null, null, MonthDay.of(4, 20));
        repository.save(guan);
        Person wang = new Person("18982455062", "王浩", "207896", Data.EMPTY_DATA, null,
                null, null, MonthDay.of(6, 5));
        repository.save(wang);
        Person du = new Person("18982435017", "杜红桃", "", Data.EMPTY_DATA, null,
                null, null, MonthDay.of(11, 12));
        repository.save(du);
    }

    @Test
    public void save() {
    }

    @Test
    public void find() {
    }

    @Test
    public void findAll() {
    }
}