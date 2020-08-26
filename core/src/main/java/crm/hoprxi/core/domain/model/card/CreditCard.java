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
import crm.hoprxi.core.domain.model.balance.SmallChange;
import crm.hoprxi.core.domain.model.card.appearance.Appearance;
import crm.hoprxi.core.domain.model.collaborator.Issuer;
import mi.hoprxi.crypto.HashService;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2020-03-27
 */
public class CreditCard extends Card {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^\\d{6,6}$");
    @Expose(serialize = false, deserialize = false)
    private String customerId;
    private String password;
    private boolean freeze;
    private LineOfCredit lineOfCredit;

    public CreditCard(Issuer issuer, String customerId, String id, String password, String cardFaceNumber, boolean freeze, TermOfValidity termOfValidity,
                      LineOfCredit lineOfCredit, Balance balance, SmallChange smallChange, Appearance appearance) {
        super(issuer, id, cardFaceNumber, termOfValidity, balance, smallChange, appearance);
        setCustomerId(customerId);
        setPassword(password);
        setLineOfCredit(lineOfCredit);
        this.freeze = freeze;
    }

    private void setLineOfCredit(LineOfCredit lineOfCredit) {
        Objects.requireNonNull(lineOfCredit, "lineOfCredit required.");
        this.lineOfCredit = lineOfCredit;
    }

    private void setCustomerId(String customerId) {
        if (!DomainRegistry.validCustomerId(customerId))
            throw new IllegalArgumentException("customerId isn't valid.");
        this.customerId = customerId;
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
            }
        }
    }

    public boolean isFreeze() {
        return freeze;
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
        Balance temp = balance.overdraw(amount);
        if (temp.redEnvelope().isZero() && temp.valuable().isNegative())
            temp = new Balance(temp.valuable().add(smallChange.amount()), temp.redEnvelope());
        if (temp.valuable().abs().isGreaterThan(lineOfCredit.quota()))
            throw new ExceedQuotaException("The credit card limit exceeded");
        balance = temp;
    }

    public void freeze() {
        freeze = true;
    }

    public void unfreeze() {
        freeze = false;
    }

    public String customerId() {
        return customerId;
    }

    public LineOfCredit lineOfCredit() {
        return lineOfCredit;
    }

    /*
            public void overdraw(MonetaryAmount amount) {
                if (balance().valuable().isPositive()) {
                    double d1 = balance().valuable().getNumber().doubleValue();
                    double d2 = balance().give().getNumber().doubleValue();
                    double d3 = d1 / (d1 + d2);
                    MonetaryAmount temp = amount.multiply(d3);
                }
            }
        */

    @Override
    public String toString() {
        return new StringJoiner(", ", CreditCard.class.getSimpleName() + "[", "]")
                .add("customerId='" + customerId + "'")
                .add("password='" + password + "'")
                .add("freeze=" + freeze)
                .add("lineOfCredit=" + lineOfCredit)
                .add("termOfValidity=" + termOfValidity)
                .add("cardFaceNumber='" + cardFaceNumber + "'")
                .add("balance=" + balance)
                .add("smallChange=" + smallChange)
                .toString();
    }
}
