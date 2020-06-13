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

import crm.hoprxi.core.domain.model.bonus.consumption.*;
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
        repository.save(CommonEntry.ONE_TO_ONE);

        Category c1 = new Category("ry", "2", "杀虫");
        Category c2 = new Category("bh", "3", "清醒");

        Brand b1 = new Brand("ch", "彩虹");

        ItemEntry i1 = new ItemEntry(new Item("11", "彩虹灭蚊灯片", c1, b1), Ratio.ONE_TO_ONE);
        repository.save(i1);
        ItemEntry i2 = new ItemEntry(new Item("22", "彩虹灭蚊加热器", c1, b1), new Ratio(2, 1));
        repository.save(i2);
        ItemEntry i3 = new ItemEntry(new Item("33", "保温杯剧", c2, Brand.UNDEFINED), new Ratio(154, 17, true));
        repository.save(i3);

        BrandEntry be1 = new BrandEntry(new Brand("hkws", "海康威视"), new Ratio(1, 2));
        repository.save(be1);
        BrandEntry be2 = new BrandEntry(b1, new Ratio(3, 2));
        repository.save(be2);

        CategoryEntry ce1 = new CategoryEntry(new Category("sx", "xr", "鲜肉"), new Ratio(5, 1));
        repository.save(ce1);
        CategoryEntry ce2 = new CategoryEntry(new Category("sx", "sc", "水产"), new Ratio(5, 2));
        repository.save(ce2);
        CategoryEntry ce3 = new CategoryEntry(new Category("sx", "sx", "生鲜"), Ratio.ONE_TO_ONE);
        repository.save(ce3);
    }

    @Test
    public void testSave() {
    }
}