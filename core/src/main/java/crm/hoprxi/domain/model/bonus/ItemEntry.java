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

package crm.hoprxi.domain.model.bonus;

import crm.hoprxi.domain.model.collaborator.Item;

import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2019-08-22
 */
public class ItemEntry {
    private Item item;
    private Ratio ratio;

    public ItemEntry(Item item, Ratio ratio) {
        setItem(item);
        setRatio(ratio);
    }

    public Item item() {
        return item;
    }

    private void setItem(Item item) {
        Objects.requireNonNull(item, "skuId required");
        //if (!ByteToHex.isIdentityHexStr(item))
        //    throw new IllegalArgumentException("illegal skuId characters");
        this.item = item;
    }

    public Ratio ratio() {
        return ratio;
    }

    private void setRatio(Ratio ratio) {
        this.ratio = ratio;
    }

}
