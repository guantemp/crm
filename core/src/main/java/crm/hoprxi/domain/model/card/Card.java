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
import crm.hoprxi.domain.model.card.appearance.AppearanceFactory;
import crm.hoprxi.domain.model.card.wallet.ChangeWallet;
import crm.hoprxi.domain.model.card.wallet.Wallet;

import javax.money.MonetaryAmount;
import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2019-08-12
 */
public abstract class Card {
    @DocumentField(DocumentField.Type.KEY)
    private String id;
    private String issuerId;
    private TermOfValidity termOfValidity;
    private Appearance appearance;
    private String cardFaceNumber;
    private Wallet wallet;
    private ChangeWallet changeWallet;

    /**
     * @param id
     * @param issuerId
     * @param cardFaceNumber
     * @param termOfValidity
     * @param wallet
     * @param changeWallet
     * @param appearance
     * @throws IllegalArgumentException if id is null or empty
     * @throws IllegalArgumentException if issuer does not exist
     * @throws IllegalArgumentException if issuer does not exist
     */
    public Card(String id, String issuerId, String cardFaceNumber, TermOfValidity termOfValidity, Wallet wallet, ChangeWallet changeWallet, Appearance appearance) {
        setId(id);
        setIssuerId(issuerId);
        setCardFaceNumber(cardFaceNumber);
        setTermOfValidity(termOfValidity);
        setWallet(wallet);
        setChangeWallet(changeWallet);
        setAppearance(appearance);
    }

    public Card(String id, String issuerId) {
        this(id, issuerId, id, TermOfValidity.PERMANENCE, Wallet.zero(Locale.getDefault()), ChangeWallet.zero(Locale.getDefault()), AppearanceFactory.getDefault());
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
        if (cardFaceNumber == null)
            cardFaceNumber = id;
        this.cardFaceNumber = cardFaceNumber;
    }

    private void setTermOfValidity(TermOfValidity termOfValidity) {
        if (termOfValidity == null)
            termOfValidity = TermOfValidity.PERMANENCE;
        this.termOfValidity = termOfValidity;
    }

    private void setChangeWallet(ChangeWallet changeWallet) {
        if (changeWallet == null)
            changeWallet = ChangeWallet.zero(Locale.getDefault());
        this.changeWallet = changeWallet;
    }

    private void setWallet(Wallet wallet) {
        if (wallet == null)
            wallet = Wallet.zero(Locale.getDefault());
        this.wallet = wallet;
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

    public boolean isLimitedPeriod() {
        return termOfValidity.isLimitedPeriod();
    }

    public Appearance appearance() {
        return appearance;
    }

    public String cardFaceNumber() {
        return cardFaceNumber;
    }

    public Wallet wallet() {
        return wallet;
    }

    public void debit(MonetaryAmount amount) {
        if (isLimitedPeriod()) {
            wallet.debit(amount);
        }
    }

    public void credit(MonetaryAmount amount, MonetaryAmount give) {

    }

    public void withdrawal(MonetaryAmount amount) {

    }

    public void withdrawalSmallChange(MonetaryAmount amount) {

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

    @Override
    public String toString() {
        return new StringJoiner(", ", Card.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("issuerId='" + issuerId + "'")
                .add("termOfValidity=" + termOfValidity)
                .add("appearance=" + appearance)
                .add("cardFaceNumber='" + cardFaceNumber + "'")
                .add("wallet=" + wallet)
                .add("changeWallet=" + changeWallet)
                .toString();
    }
}
