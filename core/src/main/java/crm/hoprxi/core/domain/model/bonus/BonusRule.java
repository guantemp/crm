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

import com.arangodb.entity.DocumentField;
import crm.hoprxi.core.domain.model.bonus.consumption.Entry;
import crm.hoprxi.core.domain.model.bonus.consumption.EntryTemplate;
import crm.hoprxi.core.domain.model.bonus.multiplying.MultiplyingEntryTemplate;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-09-08
 */
public class BonusRule {
    @DocumentField(DocumentField.Type.KEY)
    private String id;
    private String name;
    private EntryTemplate entryTemplate;
    private MultiplyingEntryTemplate multiplyingEntryTemplate;

    public BonusRule(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public BonusRule(String id, String name, EntryTemplate entryTemplate, MultiplyingEntryTemplate multiplyingEntryTemplate) {
        this.id = id;
        this.name = name;
        this.entryTemplate = entryTemplate;
        this.multiplyingEntryTemplate = multiplyingEntryTemplate;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public <T> void addEntry(Entry entry) {
        entryTemplate.add(entry);
    }

    public EntryTemplate entryTemplate() {
        return entryTemplate;
    }

    public MultiplyingEntryTemplate multiplyingEntryTemplate() {
        return multiplyingEntryTemplate;
    }
}
