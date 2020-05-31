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
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-11-14
 */
public class AnonymousCard extends Card {
    private Bonus bonus;

    public AnonymousCard(Issuer issuer, String id, String cardFaceNumber, TermOfValidity termOfValidity, Balance balance, SmallChange smallChange, Bonus bonus, Appearance appearance) {
        super(issuer, id, cardFaceNumber, termOfValidity, balance, smallChange, appearance);
        this.bonus = bonus;
    }

    public Bonus bonus() {
        return bonus;
    }

    public void addBonus(Bonus bonus) {
        Bonus temp = this.bonus.add(bonus);
        if (temp != this.bonus) {
            this.bonus = temp;
            DomainRegistry.domainEventPublisher().publish(new AnonymousCardBonusAdded(id(), bonus.value()));
        }
    }

    public void subtractBonus(Bonus bonus) {
        Bonus temp = this.bonus.subtract(bonus);
        if (temp != this.bonus) {
            this.bonus = temp;
            DomainRegistry.domainEventPublisher().publish(new AnonymousCardBonusSubtracted(id(), bonus.value()));
        }
    }

    @Override
    public void debit(MonetaryAmount amount) {
        if (!termOfValidity.isValidityPeriod())
            throw new BeOverdueException("Card be overdue");
        if (amount.isNegative())
            throw new IllegalArgumentException("Amount must is positive");
        CurrencyUnit currencyUnit = balance.currencyUnit();
        if (!currencyUnit.equals(amount.getCurrency()))
            throw new IllegalArgumentException("Amount currency required equal balance currency");
        if (amount.isZero())
            return;
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
        /*
        Rounded rounded = smallChange.round(amount);
        if (rounded.isOverflow()) {
            smallChange = smallChange.deposit(rounded.remainder());
        } else {
            smallChange = smallChange.pay(rounded.remainder().negate());
        }
*/
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AnonymousCard.class.getSimpleName() + "[", "]")
                .add("bonus=" + bonus)
                .add("termOfValidity=" + termOfValidity)
                .add("cardFaceNumber='" + cardFaceNumber + "'")
                .add("balance=" + balance)
                .add("smallChange=" + smallChange)
                .toString();
    }
}