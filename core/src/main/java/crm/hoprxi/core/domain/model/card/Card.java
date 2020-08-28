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
package crm.hoprxi.core.domain.model.card;


import com.arangodb.entity.DocumentField;
import crm.hoprxi.core.domain.model.DomainRegistry;
import crm.hoprxi.core.domain.model.balance.Balance;
import crm.hoprxi.core.domain.model.balance.InsufficientBalanceException;
import crm.hoprxi.core.domain.model.balance.SmallChangDenominationEnum;
import crm.hoprxi.core.domain.model.balance.SmallChange;
import crm.hoprxi.core.domain.model.card.appearance.Appearance;
import crm.hoprxi.core.domain.model.card.appearance.AppearanceFactory;
import crm.hoprxi.core.domain.model.collaborator.Issuer;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-04-12
 */
public abstract class Card {
    private static final int ID_MAX_LENGTH = 64;
    @DocumentField(DocumentField.Type.KEY)
    protected String id;
    protected Issuer issuer;
    protected Appearance appearance;
    protected String cardFaceNumber;
    protected Balance balance;
    protected SmallChange smallChange;

    /**
     * @param id
     * @param issuer
     * @param cardFaceNumber
     * @param balance
     * @param smallChange
     * @param appearance
     * @throws IllegalArgumentException if id is null or empty
     * @throws IllegalArgumentException if issuer does not exist
     */
    public Card(Issuer issuer, String id, String cardFaceNumber, Balance balance, SmallChange smallChange, Appearance appearance) {
        setIssuer(issuer);
        setId(id);
        setCardFaceNumber(cardFaceNumber);
        setBalance(balance);
        setSmallChange(smallChange);
        setAppearance(appearance);
    }

    public Card(Issuer issuer, String id) {
        this(issuer, id, id, Balance.zero(Locale.getDefault()), SmallChange.zero(Locale.getDefault()), AppearanceFactory.getDefault());
    }

    private void setId(String id) {
        id = Objects.requireNonNull(id, "id required").trim();
        if (id.isEmpty() || id.length() > ID_MAX_LENGTH)
            throw new IllegalArgumentException("The id length rang is [1-." + ID_MAX_LENGTH + "]");
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
        this.cardFaceNumber = cardFaceNumber.trim();
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

    public void changeCardFaceNumber(String newCardFaceNumber) {
        newCardFaceNumber = Objects.requireNonNull(newCardFaceNumber, "newCardFaceNumber required").trim();
        if (!cardFaceNumber.equals(newCardFaceNumber)) {
            this.cardFaceNumber = newCardFaceNumber;
            DomainRegistry.domainEventPublisher().publish(new CardFaceNumberChanged(id, newCardFaceNumber));
        }
    }

    public Issuer issuer() {
        return issuer;
    }

    public String id() {
        return id;
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

    public SmallChange smallChange() {
        return smallChange;
    }

    public void credit(MonetaryAmount amount) {
        balance = balance.deposit(amount);
        DomainRegistry.domainEventPublisher().publish(new CardCredited(id, amount));
    }

    public void awardRedEnvelope(MonetaryAmount redEnvelope) {
        balance = balance.awardRedEnvelope(redEnvelope);
        DomainRegistry.domainEventPublisher().publish(new CardRedEnvelopeAwarded(id, redEnvelope));
    }

    /**
     * Fallback for red packet operation error
     *
     * @param redEnvelope
     */
    public void revokeRedEnvelope(MonetaryAmount redEnvelope) {
        balance = balance.revokeRedEnvelope(redEnvelope);
        DomainRegistry.domainEventPublisher().publish(new CardRedEnvelopeRevoked(id, redEnvelope));
    }

    /**
     * @param amount
     */
    public abstract void debit(MonetaryAmount amount);

    public void changeSmallChangDenominationEnum(SmallChangDenominationEnum newSmallChangDenominationEnum) {
        SmallChange newSmallChange = smallChange.changeSmallChangDenominationEnum(newSmallChangDenominationEnum);
        if (smallChange != newSmallChange) {
            this.smallChange = newSmallChange;
            DomainRegistry.domainEventPublisher().publish(new CardSmallChangDenominationEnumChanged(id, newSmallChangDenominationEnum));
        }
    }

    public void withdrawalCash(MonetaryAmount amount) {
        if (balance.valuable().add(smallChange.amount()).isLessThan(amount))
            throw new InsufficientBalanceException("Insufficient balance");
        if (balance.valuable().isGreaterThanOrEqualTo(amount)) {
            balance = balance.withdrawalCash(amount);
        } else {
            MonetaryAmount temp = amount.subtract(balance.valuable());
            balance = new Balance(Money.zero(balance.valuable().getCurrency()), balance.redEnvelope());
            smallChange = smallChange.pay(temp);
        }
        DomainRegistry.domainEventPublisher().publish(new CardWithdrewCash(id, amount));
    }

    public void withdrawalAllCash() {
        CurrencyUnit unit = balance.valuable().getCurrency();
        if (balance.valuable().add(smallChange.amount()).isLessThan(Money.zero(unit)))
            throw new InsufficientBalanceException("Insufficient balance");
        Balance snapshot = balance;
        balance = new Balance(Money.zero(unit), snapshot.redEnvelope());
        smallChange = SmallChange.zero(unit);
        DomainRegistry.domainEventPublisher().publish(new CardWithdrewCash(id, snapshot.valuable()));
    }

    public void cancel() {

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
                .add("appearance=" + appearance)
                .add("cardFaceNumber='" + cardFaceNumber + "'")
                .add("balance=" + balance)
                .add("smallChange=" + smallChange)
                .toString();
    }
}
