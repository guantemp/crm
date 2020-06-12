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

package crm.hoprxi.core.infrastructure.persistence;

import crm.hoprxi.core.domain.model.bonus.consumption.EntryRepository;
import crm.hoprxi.core.domain.model.bonus.consumption.GeneralEntry;
import crm.hoprxi.core.domain.model.bonus.consumption.ItemEntry;
import crm.hoprxi.core.domain.model.bonus.consumption.Ratio;
import crm.hoprxi.core.domain.model.collaborator.Brand;
import crm.hoprxi.core.domain.model.collaborator.Category;
import crm.hoprxi.core.domain.model.collaborator.Item;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-06-11
 */
public class ArangoDBEntryRepositoryTest {
    private static EntryRepository repository = new ArangoDBEntryRepository("crm");

    @BeforeClass
    public void setUpBeforeClass() {
        GeneralEntry generalEntry = new GeneralEntry(Ratio.ONE_TO_ONE);

        Category c1 = new Category("1", "2", "杀虫");
        Category c2 = new Category("1", "3", "清醒");

        Brand b1 = new Brand("234", "彩虹");

        ItemEntry i1 = new ItemEntry(new Item("6655744", "彩虹密文想", c1), Ratio.ONE_TO_ONE);
        repository.save(i1);
        ItemEntry i2 = new ItemEntry(new Item("6655745", "彩虹起源", c1), Ratio.ONE_TO_ONE);
        repository.save(i2);
        ItemEntry i3 = new ItemEntry(new Item("974244", "古亭倾向", c2, b1), new Ratio(4, 1));
        repository.save(i3);
    }

    @Test
    public void testSave() {
    }
}