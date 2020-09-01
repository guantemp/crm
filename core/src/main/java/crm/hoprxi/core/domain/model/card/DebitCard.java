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

import com.arangodb.velocypack.annotations.Expose;
import crm.hoprxi.core.domain.model.DomainRegistry;
import crm.hoprxi.core.domain.model.balance.Balance;
import crm.hoprxi.core.domain.model.balance.InsufficientBalanceException;
import crm.hoprxi.core.domain.model.balance.SmallChange;
import crm.hoprxi.core.domain.model.card.appearance.Appearance;
import crm.hoprxi.core.domain.model.collaborator.Issuer;
import mi.hoprxi.crypto.HashService;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.2 builder 2020-05-09
 */
public class DebitCard extends Card {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^\\d{6,6}$");
    @Expose(serialize = false, deserialize = false)
    private String customerId;
    private String password;
    private boolean available;

    public DebitCard(Issuer issuer, String customerId, String id, String password, String cardFaceNumber, boolean available,
                     Balance balance, SmallChange smallChange, Appearance appearance) {
        super(issuer, id, cardFaceNumber, balance, smallChange, appearance);
        setCustomerId(customerId);
        setPassword(password);
        this.available = available;
    }

    public DebitCard(Issuer issuer, String customerId, String id, String cardFaceNumber) {
        this(issuer, customerId, id, "", cardFaceNumber, false, Balance.zero(Locale.getDefault()),
                SmallChange.zero(Locale.getDefault()), null);
    }

    /**
     * for rebuild
     */
    private DebitCard(Issuer issuer, String customerId, String id, String cardFaceNumber, boolean available, Balance balance,
                      SmallChange smallChange, Appearance appearance) {
        super(issuer, id, cardFaceNumber, balance, smallChange, appearance);
        setCustomerId(customerId);
        this.available = available;
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

    public void changePassword(String currentPassword, String newPassword) {
        currentPassword = Objects.requireNonNull(currentPassword, "currentPassword is required").trim();
        newPassword = Objects.requireNonNull(newPassword, "newPassword is required").trim();
        if (!currentPassword.isEmpty()) {
            Matcher matcher = PASSWORD_PATTERN.matcher(currentPassword);
            if (!matcher.matches())
                throw new IllegalArgumentException("currentPassword is 6 digit number");
        }
        if (!newPassword.isEmpty()) {
            Matcher matcher = PASSWORD_PATTERN.matcher(newPassword);
            if (!matcher.matches())
                throw new IllegalArgumentException("newPassword is 6 digit number");
        }
        if (!currentPassword.equals(newPassword)) {
            HashService hashService = DomainRegistry.getHashService();
            if (hashService.check(currentPassword, password)) {
                this.password = hashService.hash(newPassword);
                DomainRegistry.domainEventPublisher().publish(new DebitCardPasswordChanged(id()));
            }
        }
    }

    public boolean authenticatePassword(String password) {
        if (password == null)
            return false;
        if (!password.isEmpty()) {
            Matcher matcher = PASSWORD_PATTERN.matcher(password);
            if (!matcher.matches())
                return false;
        }
        HashService hash = DomainRegistry.getHashService();
        return hash.check(password, this.password);
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable() {
        if (available == false) {
            this.available = true;
            DomainRegistry.domainEventPublisher().publish(new DebitCardSetAvailable(id()));
        }
    }

    public void setUnavailable() {
        if (available == true) {
            this.available = true;
            DomainRegistry.domainEventPublisher().publish(new DebitCardSetUnavailable(id()));
        }
    }

    public String customerId() {
        return customerId;
    }

    @Override
    public void debit(MonetaryAmount amount) {
        Objects.requireNonNull(amount, "amount required");
        CurrencyUnit currencyUnit = balance.currencyUnit();
        if (!currencyUnit.equals(amount.getCurrency()))
            throw new IllegalArgumentException("The amount currency required equal balance currency:" + currencyUnit);
        if (amount.isNegative())
            throw new IllegalArgumentException("The amount must is positive");
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
        DomainRegistry.domainEventPublisher().publish(new CardDebited(id, amount));
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DebitCard.class.getSimpleName() + "[", "]")
                .add("customerId='" + customerId + "'")
                .add("available=" + available)
                .add("id='" + id + "'")
                .add("issuer=" + issuer)
                .add("appearance=" + appearance)
                .add("cardFaceNumber='" + cardFaceNumber + "'")
                .add("balance=" + balance)
                .add("smallChange=" + smallChange)
                .toString();
    }

    public DebitCardSnapshot toSnapshot() {
        return new DebitCardSnapshot(issuer, customerId, id, cardFaceNumber, available, balance, smallChange, appearance);
    }
}
