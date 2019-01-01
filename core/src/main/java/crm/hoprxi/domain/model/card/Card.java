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
import crm.hoprxi.domain.model.balance.Balance;
import crm.hoprxi.domain.model.balance.InsufficientBalanceException;
import crm.hoprxi.domain.model.balance.SmallChangDenominationEnum;
import crm.hoprxi.domain.model.balance.SmallChange;
import crm.hoprxi.domain.model.card.appearance.Appearance;
import crm.hoprxi.domain.model.card.appearance.AppearanceFactory;
import crm.hoprxi.domain.model.collaborator.Issuer;

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
    protected String id;
    private Issuer issuer;
    protected TermOfValidity termOfValidity;
    private Appearance appearance;
    protected String cardFaceNumber;
    protected Balance balance;
    protected SmallChange smallChange;

    /**
     * @param id
     * @param issuer
     * @param cardFaceNumber
     * @param termOfValidity
     * @param balance
     * @param smallChange
     * @param appearance
     * @throws IllegalArgumentException if id is null or empty
     * @throws IllegalArgumentException if issuer does not exist
     * @throws IllegalArgumentException if issuer does not exist
     */
    public Card(Issuer issuer, String id, String cardFaceNumber, TermOfValidity termOfValidity, Balance balance, SmallChange smallChange, Appearance appearance) {
        setIssuer(issuer);
        setId(id);
        setCardFaceNumber(cardFaceNumber);
        setTermOfValidity(termOfValidity);
        setBalance(balance);
        setSmallChange(smallChange);
        setAppearance(appearance);
    }

    public Card(String id, Issuer issuer) {
        this(issuer, id, id, TermOfValidity.PERMANENCE, Balance.zero(Locale.getDefault()), SmallChange.zero(Locale.getDefault()), AppearanceFactory.getDefault());
    }

    private void setId(String id) {
        id = Objects.requireNonNull(id, "id required").trim();
        if (id.isEmpty())
            throw new IllegalArgumentException("Must provide a card id.");
        this.id = id;
    }

    private void setIssuer(Issuer issuer) {
        Objects.requireNonNull(issuer, "issuer required");
        //if (!DomainRegistry.validIssuerId(issuer))
        //throw new IllegalArgumentException("issuerId not valid");
        this.issuer = issuer;
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

    private void setSmallChange(SmallChange smallChange) {
        if (smallChange == null)
            smallChange = SmallChange.zero(Locale.getDefault());
        this.smallChange = smallChange;
    }

    private void setBalance(Balance balance) {
        if (balance == null)
            balance = Balance.zero(Locale.getDefault());
        this.balance = balance;
    }

    private void setAppearance(Appearance appearance) {
        this.appearance = appearance;
    }

    public Issuer issuer() {
        return issuer;
    }

    public String id() {
        return id;
    }

    public TermOfValidity termOfValidity() {
        return termOfValidity;
    }

    public Appearance appearance() {
        return appearance;
    }

    public String cardFaceNumber() {
        return cardFaceNumber;
    }

    public Balance balance() {
        return balance;
    }

    public SmallChange smallChangeBalance() {
        return smallChange;
    }

    public void credit(MonetaryAmount amount) {
        if (!termOfValidity.isValidityPeriod())
            throw new BeOverdueException("Card be overdue");
        balance = balance.deposit(amount);
    }

    /**
     * @param amount
     * @param give
     */
    public void credit(MonetaryAmount amount, MonetaryAmount give) {
        if (!termOfValidity.isValidityPeriod())
            throw new BeOverdueException("Card be overdue");
        balance = balance.deposit(amount, give);
    }

    /**
     * @param amount
     */
    public abstract void debit(MonetaryAmount amount);

    public void changeSmallChangDenominationEnum(SmallChangDenominationEnum newSmallChangDenominationEnum) {
        SmallChange newSmallChange = smallChange.changeSmallChangDenominationEnum(newSmallChangDenominationEnum);
        if (smallChange != newSmallChange)
            this.smallChange = newSmallChange;
    }

    /**
     * @param amount
     */
    public void withdraw(MonetaryAmount amount) {
        if (!termOfValidity.isValidityPeriod())
            throw new BeOverdueException("Card failed");
        if (balance.valuable().add(smallChange.amount()).isLessThan(amount))
            throw new InsufficientBalanceException("insufficient balance");
        if (balance.valuable().isGreaterThanOrEqualTo(amount)) {
            balance = balance.withdrawal(amount);
            return;
        }
        MonetaryAmount temp = amount.subtract(balance.valuable());
        balance = Balance.zero(balance.valuable().getCurrency());
        smallChange = smallChange.pay(temp);
    }

    public void withdrawAll() {

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
                .add("issuer=" + issuer)
                .add("termOfValidity=" + termOfValidity)
                .add("appearance=" + appearance)
                .add("cardFaceNumber='" + cardFaceNumber + "'")
                .add("balance=" + balance)
                .add("smallChange=" + smallChange)
                .toString();
    }
}
