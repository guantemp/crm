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

import crm.hoprxi.core.domain.model.DomainRegistry;
import crm.hoprxi.core.domain.model.balance.Balance;
import crm.hoprxi.core.domain.model.balance.InsufficientBalanceException;
import crm.hoprxi.core.domain.model.balance.SmallChange;
import crm.hoprxi.core.domain.model.bonus.Bonus;
import crm.hoprxi.core.domain.model.card.appearance.Appearance;
import crm.hoprxi.core.domain.model.collaborator.Issuer;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.util.Objects;
import java.util.StringJoiner;

/***
 * <p>
 *     This card is anonymous card. For example, the company does not provide employee information<br/>
 *     pre issue does not know the information of the buyer
 * </p>
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-11-14
 */
public class AnonymousCard extends Card {
    private ValidityPeriod validityPeriod;
    private Bonus bonus;

    public AnonymousCard(Issuer issuer, String id, String cardFaceNumber, ValidityPeriod validityPeriod, Balance balance, SmallChange smallChange, Bonus bonus, Appearance appearance) {
        super(issuer, id, cardFaceNumber, balance, smallChange, appearance);
        setValidityPeriod(validityPeriod);
        this.bonus = bonus;
    }

    private void setValidityPeriod(ValidityPeriod validityPeriod) {
        if (validityPeriod == null)
            validityPeriod = ValidityPeriod.PERMANENCE;
        if ((validityPeriod != ValidityPeriod.PERMANENCE) && (validityPeriod.expiryDate().getYear() - validityPeriod.startDate().getYear() < 3))
            throw new IllegalArgumentException("Valid for at least three years ");
        this.validityPeriod = validityPeriod;
    }

    public Bonus bonus() {
        return bonus;
    }

    /**
     * get anonymous card validity period
     *
     * @return AnonymousCard ValidityPeriod
     */
    public ValidityPeriod validityPeriod() {
        return validityPeriod;
    }

    @Override
    public void credit(MonetaryAmount amount) {
        if (!validityPeriod.isValidityPeriod())
            throw new BeOverdueException("Card be overdue");
        super.credit(amount);
    }

    @Override
    public void awardRedEnvelope(MonetaryAmount redEnvelope) {
        if (!validityPeriod.isValidityPeriod())
            throw new BeOverdueException("Card be overdue");
        super.awardRedEnvelope(redEnvelope);
    }

    @Override
    public void revokeRedEnvelope(MonetaryAmount redEnvelope) {
        if (!validityPeriod.isValidityPeriod())
            throw new BeOverdueException("Card be overdue");
        super.revokeRedEnvelope(redEnvelope);
    }

    @Override
    public void withdrawalCash(MonetaryAmount amount) {
        if (!validityPeriod.isValidityPeriod())
            throw new BeOverdueException("Card be overdue");
        super.withdrawalCash(amount);
    }

    @Override
    public void withdrawalAllCash() {
        if (!validityPeriod.isValidityPeriod())
            throw new BeOverdueException("Card be overdue");
        super.withdrawalAllCash();
    }

    public void addBonus(Bonus bonus) {
        Bonus temp = this.bonus.add(bonus);
        if (temp != this.bonus) {
            this.bonus = temp;
            DomainRegistry.domainEventPublisher().publish(new AnonymousCardBonusAdded(id(), bonus.toNumber()));
        }
    }

    public void subtractBonus(Bonus bonus) {
        Bonus temp = this.bonus.subtract(bonus);
        if (temp != this.bonus) {
            this.bonus = temp;
            DomainRegistry.domainEventPublisher().publish(new AnonymousCardBonusSubtracted(id(), bonus.toNumber()));
        }
    }

    @Override
    public void debit(MonetaryAmount amount) {
        Objects.requireNonNull(amount, "The amount required");
        CurrencyUnit currencyUnit = balance.currencyUnit();
        if (!currencyUnit.equals(amount.getCurrency()))
            throw new IllegalArgumentException("The amount currency required equal balance currency");
        if (amount.isNegative())
            throw new IllegalArgumentException("The amount must is positive");
        if (amount.isZero())
            return;
        if (!validityPeriod.isValidityPeriod())
            throw new BeOverdueException("Card be overdue");
        MonetaryAmount difference = balance.total().subtract(amount);
        if (difference.isPositiveOrZero()) {
            balance = balance.pay(amount);
        } else {
            if (smallChange.amount().subtract(difference).isNegative()) {
                throw new InsufficientBalanceException("The insufficient balance");
            } else {
                balance = Balance.zero(currencyUnit);
                smallChange = smallChange.pay(difference.negate());
            }
        }
        DomainRegistry.domainEventPublisher().publish(new CardDebited(id, amount));
        /*
        Rounded rounded = smallChange.round(amount);
        if (rounded.isOverflow()) {
            smallChange = smallChange.deposit(rounded.remainder());
        } else {
            smallChange = smallChange.pay(rounded.remainder().negate());
        }
*/
    }

    public AnonymousCardSnapshot toSnapshot() {
        return new AnonymousCardSnapshot(issuer, id, cardFaceNumber, validityPeriod, balance, smallChange, bonus, appearance);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AnonymousCard.class.getSimpleName() + "[", "]")
                .add("validityPeriod=" + validityPeriod)
                .add("bonus=" + bonus)
                .add("id='" + id + "'")
                .add("issuer=" + issuer)
                .add("appearance=" + appearance)
                .add("cardFaceNumber='" + cardFaceNumber + "'")
                .add("balance=" + balance)
                .add("smallChange=" + smallChange)
                .toString();
    }
}
