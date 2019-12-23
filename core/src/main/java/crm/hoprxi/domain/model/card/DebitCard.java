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

import crm.hoprxi.domain.model.DomainRegistry;
import crm.hoprxi.domain.model.balance.Balance;
import crm.hoprxi.domain.model.balance.Rounded;
import crm.hoprxi.domain.model.balance.SmallChangDenominationEnum;
import crm.hoprxi.domain.model.balance.SmallChange;
import crm.hoprxi.domain.model.card.appearance.Appearance;
import crm.hoprxi.domain.model.collaborator.Issuer;
import mi.hoprxi.crypto.HashService;

import javax.money.MonetaryAmount;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.2 builder 2019-11-06
 */
public class DebitCard extends Card {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^\\d{6,6}$");
    private String customerId;
    private String password;

    public DebitCard(Issuer issuer, String customerId, String id, String password, String cardFaceNumber, TermOfValidity termOfValidity, Balance balance, SmallChange smallChange, Appearance appearance) {
        super(issuer, id, cardFaceNumber, termOfValidity, balance, smallChange, appearance);
        setCustomerId(customerId);
        setPassword(password);
    }

    private void setPassword(String password) {
        password = Objects.requireNonNull(password, "password is required").trim();
        if (!password.isEmpty()) {
            Matcher matcher = PASSWORD_PATTERN.matcher(password);
            if (!matcher.matches())
                throw new IllegalArgumentException("password is 6 digit number");
        }
        HashService hashService = DomainRegistry.getHashService();
        this.password = hashService.hash(password);
    }

    private void setCustomerId(String customerId) {
        if (!DomainRegistry.validCustomerId(customerId))
            throw new IllegalArgumentException("customerId isn't valid");
        this.customerId = customerId;
    }

    public void changeCardFaceNumber(String newCardFaceNumber) {
        newCardFaceNumber = Objects.requireNonNull(newCardFaceNumber, "newCardFaceNumber required").trim();
        if (!cardFaceNumber.equals(newCardFaceNumber)) {
            this.cardFaceNumber = newCardFaceNumber;
            DomainRegistry.domainEventPublisher().publish(new CardFaceNumberChanged(super.id(), newCardFaceNumber));
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
        return new StringJoiner(", ", DebitCard.class.getSimpleName() + "[", "]")
                .add("super=" + super.toString())
                .add("customerId='" + customerId + "'")
                .toString();
    }
}
