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

import mi.hoprxi.to.ByteToHex;

import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2019-08-22
 */
public class ItemEntry {
    private String itemId;
    private Ratio ratio;

    public ItemEntry(String itemId, Ratio ratio) {
        setItemId(itemId);
        setRatio(ratio);
    }

    public String itemId() {
        return itemId;
    }

    private void setItemId(String itemId) {
        Objects.requireNonNull(itemId, "skuId required");
        if (!ByteToHex.isIdentityHexStr(itemId))
            throw new IllegalArgumentException("illegal skuId characters");
        this.itemId = itemId;
    }

    public Ratio ratio() {
        return ratio;
    }

    private void setRatio(Ratio ratio) {
        this.ratio = ratio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemEntry itemEntry = (ItemEntry) o;

        if (itemId != null ? !itemId.equals(itemEntry.itemId) : itemEntry.itemId != null) return false;
        return ratio != null ? ratio.equals(itemEntry.ratio) : itemEntry.ratio == null;
    }

    @Override
    public int hashCode() {
        int result = itemId != null ? itemId.hashCode() : 0;
        result = 31 * result + (ratio != null ? ratio.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SkuEntry{" +
                "skuId='" + itemId + '\'' +
                ", ratio=" + ratio +
                '}';
    }
}
