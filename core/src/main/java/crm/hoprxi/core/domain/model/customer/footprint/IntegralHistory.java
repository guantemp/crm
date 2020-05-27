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

import crm.hoprxi.core.domain.model.bonus.Bonus;
import crm.hoprxi.core.domain.model.collaborator.Issuer;
import crm.hoprxi.core.domain.model.collaborator.Operator;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-08-04
 */
public final class IntegralHistory {
    private LocalDateTime occurOn;
    private Issuer issuer;
    private Operator operator;
    private String cardId;
    private Bonus bonus;
    private List<IntegralHistoryDetail> datails = new ArrayList<>(0);
    private URL cover;
    private URL herf;
    private String synopsis;

    public IntegralHistory(LocalDateTime occurOn, Issuer issuer, Operator operator, String cardId, Bonus bonus) {
        this.occurOn = occurOn;
        this.issuer = issuer;
        this.operator = operator;
        this.cardId = cardId;
        this.bonus = bonus;
    }

    public IntegralHistory(LocalDateTime occurOn, Issuer issuer, Operator operator, String cardId, Bonus bonus, List<IntegralHistoryDetail> datails, URL cover, URL herf, String synopsis) {
        setOccurOn(occurOn);
        setIssuer(issuer);
        setOperator(operator);
        setCardId(cardId);
        this.bonus = bonus;
        this.datails = datails;
        this.cover = cover;
        this.herf = herf;
        this.synopsis = synopsis;
    }

    private void setCardId(String cardId) {
        cardId = Objects.requireNonNull(cardId, "cardId required").trim();
        this.cardId = cardId;
    }

    private void setOperator(Operator operator) {
        Objects.requireNonNull(operator, "operator required");
        this.operator = operator;
    }

    private void setOccurOn(LocalDateTime occurOn) {
        Objects.requireNonNull(occurOn, "occurOn required");
        this.occurOn = occurOn;
    }

    private void setIssuer(Issuer issuer) {
        Objects.requireNonNull(issuer, "issuer required");
        this.issuer = issuer;
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

    public Bonus integral() {
        return bonus;
    }

    public List<IntegralHistoryDetail> datails() {
        return datails;
    }

    public URL cover() {
        return cover;
    }

    public URL herf() {
        return herf;
    }

    public String synopsis() {
        return synopsis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntegralHistory that = (IntegralHistory) o;

        if (occurOn != null ? !occurOn.equals(that.occurOn) : that.occurOn != null) return false;
        if (issuer != null ? !issuer.equals(that.issuer) : that.issuer != null) return false;
        if (operator != null ? !operator.equals(that.operator) : that.operator != null) return false;
        if (cardId != null ? !cardId.equals(that.cardId) : that.cardId != null) return false;
        if (bonus != null ? !bonus.equals(that.bonus) : that.bonus != null) return false;
        if (datails != null ? !datails.equals(that.datails) : that.datails != null) return false;
        if (cover != null ? !cover.equals(that.cover) : that.cover != null) return false;
        if (herf != null ? !herf.equals(that.herf) : that.herf != null) return false;
        return synopsis != null ? synopsis.equals(that.synopsis) : that.synopsis == null;
    }

    @Override
    public int hashCode() {
        int result = occurOn != null ? occurOn.hashCode() : 0;
        result = 31 * result + (issuer != null ? issuer.hashCode() : 0);
        result = 31 * result + (operator != null ? operator.hashCode() : 0);
        result = 31 * result + (cardId != null ? cardId.hashCode() : 0);
        result = 31 * result + (bonus != null ? bonus.hashCode() : 0);
        result = 31 * result + (datails != null ? datails.hashCode() : 0);
        result = 31 * result + (cover != null ? cover.hashCode() : 0);
        result = 31 * result + (herf != null ? herf.hashCode() : 0);
        result = 31 * result + (synopsis != null ? synopsis.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IntegralHistory{" +
                "occurOn=" + occurOn +
                ", issuer=" + issuer +
                ", operator=" + operator +
                ", cardId='" + cardId + '\'' +
                ", rule=" + bonus +
                ", datails=" + datails +
                ", cover=" + cover +
                ", herf=" + herf +
                ", synopsis='" + synopsis + '\'' +
                '}';
    }
}
