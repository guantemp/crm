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
package crm.hoprxi.domain.model.card;

import crm.hoprxi.domain.model.DomainRegistry;
import crm.hoprxi.domain.model.balance.Balance;
import crm.hoprxi.domain.model.balance.Rounded;
import crm.hoprxi.domain.model.balance.SmallChangDenominationEnum;
import crm.hoprxi.domain.model.balance.SmallChange;
import crm.hoprxi.domain.model.bonus.Bonus;
import crm.hoprxi.domain.model.card.appearance.Appearance;
import crm.hoprxi.domain.model.collaborator.Issuer;

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
        if (smallChange.smallChangDenominationEnum() == SmallChangDenominationEnum.ZERO) {
            balance = balance.pay(amount);
            return;
        }
        Rounded rounded = smallChange.round(amount);
        if (rounded.isOverflow()) {
            smallChange = smallChange.deposit(rounded.remainder());
        } else {
            smallChange = smallChange.pay(rounded.remainder().negate());
        }
        balance = balance.pay(rounded.integer());
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
