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
package crm.hoprxi.domain.model.card;


import com.arangodb.entity.DocumentField;
import crm.hoprxi.domain.model.card.appearance.Appearance;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.util.Locale;
import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2019-08-12
 */
public abstract class Card {
    protected static final MonetaryAmount MONETARY_ZERO = Money.of(0, Monetary.getCurrency(Locale.getDefault()));

    private static final Logger LOGGER = LoggerFactory.getLogger(Card.class);
    @DocumentField(DocumentField.Type.KEY)
    private String id;
    private String issuerId;
    protected TermOfValidity termOfValidity;
    private Appearance appearance;
    private String cardFaceNumber;

    public Card(String id, String issuerId, String cardFaceNumber) {
        this(id, issuerId, cardFaceNumber, TermOfValidity.PERMANENCE, null);
    }

    /**
     * @param id
     * @param issuerId
     * @param cardFaceNumber
     * @param termOfValidity
     * @param appearance
     */
    public Card(String id, String issuerId, String cardFaceNumber, TermOfValidity termOfValidity, Appearance appearance) {
        setId(id);
        setIssuerId(issuerId);
        setTermOfValidity(termOfValidity);
        setAppearance(appearance);
        setCardFaceNumber(cardFaceNumber);
    }

    private void setCardFaceNumber(String cardFaceNumber) {
        this.cardFaceNumber = cardFaceNumber;
    }

    private void setAppearance(Appearance appearance) {
        this.appearance = appearance;
    }

    public String issuerId() {
        return issuerId;
    }

    public String id() {
        return id;
    }

    public TermOfValidity termOfValidity() {
        return termOfValidity;
    }

    private void setIssuerId(String issuerId) {
        issuerId = Objects.requireNonNull(issuerId, "issuerId required").trim();
        if (issuerId.isEmpty())
            throw new IllegalArgumentException("Must provide a issuer id.");
        this.issuerId = issuerId;
    }

    private void setTermOfValidity(TermOfValidity termOfValidity) {
        if (termOfValidity == null)
            termOfValidity = TermOfValidity.PERMANENCE;
        this.termOfValidity = termOfValidity;
    }


    private void setId(String id) {
        id = Objects.requireNonNull(id, "id required").trim();
        if (id.isEmpty())
            throw new IllegalArgumentException("Must provide a card id.");
        this.id = id;
    }

    public Appearance appearance() {
        return appearance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        return id != null ? id.equals(card.id) : card.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
