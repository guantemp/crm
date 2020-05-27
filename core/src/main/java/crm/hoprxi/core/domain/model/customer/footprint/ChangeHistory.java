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

package crm.hoprxi.core.domain.model.customer.footprint;

import crm.hoprxi.core.domain.model.balance.SmallChange;
import crm.hoprxi.core.domain.model.collaborator.Issuer;
import crm.hoprxi.core.domain.model.collaborator.Operator;

import java.net.URL;
import java.time.LocalDateTime;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-08-07
 */
public class ChangeHistory {
    private LocalDateTime occurOn;
    private Issuer issuer;
    private Operator operator;
    private String cardId;
    private SmallChange smallChange;
    private URL cover;
    private URL herf;

    public ChangeHistory(LocalDateTime occurOn, Issuer issuer, Operator operator, String cardId, SmallChange smallChange, URL cover, URL herf) {
        this.occurOn = occurOn;
        this.issuer = issuer;
        this.operator = operator;
        this.cardId = cardId;
        this.smallChange = smallChange;
        this.cover = cover;
        this.herf = herf;
    }

    public LocalDateTime occurOn() {
        return occurOn;
    }

    public Issuer issuer() {
        return issuer;
    }

    public Operator operator() {
        return operator;
    }

    public String cardId() {
        return cardId;
    }

    public SmallChange change() {
        return smallChange;
    }

    public URL cover() {
        return cover;
    }

    public URL herf() {
        return herf;
    }

    @Override
    public String toString() {
        return "ChangeHistory{" +
                "occurOn=" + occurOn +
                ", issuer=" + issuer +
                ", operator=" + operator +
                ", cardId='" + cardId + '\'' +
                ", change=" + smallChange +
                ", cover=" + cover +
                ", herf=" + herf +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChangeHistory that = (ChangeHistory) o;

        if (occurOn != null ? !occurOn.equals(that.occurOn) : that.occurOn != null) return false;
        if (issuer != null ? !issuer.equals(that.issuer) : that.issuer != null) return false;
        if (operator != null ? !operator.equals(that.operator) : that.operator != null) return false;
        if (cardId != null ? !cardId.equals(that.cardId) : that.cardId != null) return false;
        if (smallChange != null ? !smallChange.equals(that.smallChange) : that.smallChange != null) return false;
        if (cover != null ? !cover.equals(that.cover) : that.cover != null) return false;
        return herf != null ? herf.equals(that.herf) : that.herf == null;
    }

    @Override
    public int hashCode() {
        int result = occurOn != null ? occurOn.hashCode() : 0;
        result = 31 * result + (issuer != null ? issuer.hashCode() : 0);
        result = 31 * result + (operator != null ? operator.hashCode() : 0);
        result = 31 * result + (cardId != null ? cardId.hashCode() : 0);
        result = 31 * result + (smallChange != null ? smallChange.hashCode() : 0);
        result = 31 * result + (cover != null ? cover.hashCode() : 0);
        result = 31 * result + (herf != null ? herf.hashCode() : 0);
        return result;
    }
}
