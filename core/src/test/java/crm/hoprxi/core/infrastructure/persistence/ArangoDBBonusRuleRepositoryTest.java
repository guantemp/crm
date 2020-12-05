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

import crm.hoprxi.core.domain.model.bonus.BonusRule;
import crm.hoprxi.core.domain.model.bonus.consumption.*;
import crm.hoprxi.core.domain.model.bonus.multiplying.MultiplyingEntryTemplate;
import crm.hoprxi.core.domain.model.collaborator.Brand;
import crm.hoprxi.core.domain.model.collaborator.Category;
import crm.hoprxi.core.domain.model.collaborator.Item;
import org.testng.annotations.Test;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-09-09
 */
public class ArangoDBBonusRuleRepositoryTest {
    private ArangoDBBonusRuleRepository repository = new ArangoDBBonusRuleRepository("crm");

    @Test
    public void testTestSave() {
        EntryTemplate entryTemplate = new EntryTemplate("t1", "测试");
        Category c1 = new Category("ry", "2", "杀虫");
        Category c2 = new Category("bh", "3", "清醒");

        Brand b1 = new Brand("ch", "彩虹");
        Entry itemEntry1 = new ItemEntry(new Item("11", "彩虹灭蚊灯片", c1, b1), Ratio.ONE_TO_ONE);
        entryTemplate.add(itemEntry1);
        Entry itemEntry2 = new ItemEntry(new Item("22", "彩虹灭蚊加热器", c1, b1), new Ratio(2, 1));
        entryTemplate.add(itemEntry2);
        Entry itemEntry3 = new ItemEntry(new Item("33", "保温杯剧", c2, Brand.UNDEFINED), new Ratio(154, 17, true));
        entryTemplate.add(itemEntry3);

        Entry brandEntry1 = new BrandEntry(new Brand("hkws", "海康威视"), new Ratio(1, 2));
        brandEntry1 = brandEntry1.changeRatio(new Ratio(1, 3));
        entryTemplate.add(brandEntry1);

        Entry categoryEntry1 = new CategoryEntry(new Category("sx", "xr", "鲜肉"), new Ratio(5, 1));
        entryTemplate.add(categoryEntry1);
        Entry categoryEntry2 = new CategoryEntry(new Category("sx", "sc", "水产"), new Ratio(5, 2));
        entryTemplate.add(categoryEntry2);
        Entry categoryEntry3 = new CategoryEntry(new Category("sx", "sx", "生鲜"), Ratio.ONE_TO_ONE);
        entryTemplate.add(categoryEntry3);

        System.out.println(entryTemplate);

        MultiplyingEntryTemplate multiplyingEntryTemplate = null;
        //payment,memberRole,currency,Reference,ConsumptionInterval
        BonusRule bonusRule = new BonusRule("123", "命名1", entryTemplate, multiplyingEntryTemplate);
        repository.save(bonusRule);
    }

    @Test
    public void testTestFindAll() {
    }
}