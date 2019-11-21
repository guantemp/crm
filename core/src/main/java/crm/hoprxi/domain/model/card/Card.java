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
import crm.hoprxi.domain.model.DomainRegistry;
import crm.hoprxi.domain.model.card.appearance.Appearance;
import crm.hoprxi.domain.model.card.wallet.CoinWallet;
import crm.hoprxi.domain.model.card.wallet.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2019-08-12
 */
public abstract class Card {
    private static final Logger LOGGER = LoggerFactory.getLogger(Card.class);
    @DocumentField(DocumentField.Type.KEY)
    private String id;
    private String issuerId;
    protected TermOfValidity termOfValidity;
    private Appearance appearance;
    private String cardFaceNumber;
    private Wallet wallet;
    private CoinWallet coinWallet;

    public Card(String id, String issuerId, String cardFaceNumber) {
        this(id, issuerId, cardFaceNumber, TermOfValidity.PERMANENCE, null);
    }


    /**
     * @param id
     * @param issuerId
     * @param cardFaceNumber
     * @param termOfValidity
     * @param appearance
     * @throws IllegalArgumentException if id is null or empty
     * @throws IllegalArgumentException if Issuer does not exist
     */
    public Card(String id, String issuerId, String cardFaceNumber, TermOfValidity termOfValidity, Appearance appearance) {
        setId(id);
        setIssuerId(issuerId);
        setTermOfValidity(termOfValidity);
        setCardFaceNumber(cardFaceNumber);
        setAppearance(appearance);
    }

    private void setId(String id) {
        id = Objects.requireNonNull(id, "id required").trim();
        if (id.isEmpty())
            throw new IllegalArgumentException("Must provide a card id.");
        this.id = id;
    }

    private void setIssuerId(String issuerId) {
        issuerId = Objects.requireNonNull(issuerId, "issuerId required").trim();
        if (!DomainRegistry.validIssuerId(issuerId))
            throw new IllegalArgumentException("issuerId not valid");
        this.issuerId = issuerId;
    }

    private void setCardFaceNumber(String cardFaceNumber) {
        cardFaceNumber = Objects.requireNonNull(cardFaceNumber, "cardFaceNumber").trim();
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

    private void setTermOfValidity(TermOfValidity termOfValidity) {
        if (termOfValidity == null)
            termOfValidity = TermOfValidity.PERMANENCE;
        this.termOfValidity = termOfValidity;
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
