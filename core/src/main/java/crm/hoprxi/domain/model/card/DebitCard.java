/*
 * Copyright (c) 2019. www.hoprxi.com rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package crm.hoprxi.domain.model.card;

import crm.hoprxi.domain.model.card.appearance.Appearance;

import javax.money.MonetaryAmount;
import java.util.Objects;
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.2 builder 2019-08-06
 */
public class DebitCard extends Card {
    private MonetaryAmount principal;
    private MonetaryAmount give;
    private MonetaryAmount freeze;

    /**
     * @param id
     * @param issuerId
     * @param customerId
     * @param password
     * @param principal
     * @param give
     * @param freeze
     * @throws IllegalArgumentException if principal or give less zero
     */
    public DebitCard(String id, String issuerId, String customerId, String password, MonetaryAmount principal, MonetaryAmount give, MonetaryAmount freeze) {
        this(id, issuerId, customerId, password, TermOfValidity.PERMANENCE, null, principal, give, freeze);
    }

    public DebitCard(String id, String issuerId, String customerId, String password, TermOfValidity termOfValidity, Appearance appearance, MonetaryAmount principal, MonetaryAmount give, MonetaryAmount freeze) {
        super(id, issuerId, customerId, password, termOfValidity, appearance);
        setPrincipal(principal);
        setGive(give);
        setFreeze(freeze);
    }

    private void setPrincipal(MonetaryAmount principal) {
        if (principal == null)
            principal = MONETARY_ZERO;
        if (principal.isNegative())
            throw new IllegalArgumentException("principal must large zero");
        this.principal = principal;
    }

    private void setGive(MonetaryAmount give) {
        if (give == null)
            give = MONETARY_ZERO;
        if (give.isNegative())
            throw new IllegalArgumentException("give must large zero");
        this.give = give;
    }

    public MonetaryAmount principal() {
        return principal;
    }

    public MonetaryAmount totalBalance() {
        return principal.add(give);
    }

    public MonetaryAmount availableBalance() {
        MonetaryAmount temp = principal.add(give).subtract(freeze);
        if (temp.isNegative())
            return MONETARY_ZERO;
        return temp;
    }

    public MonetaryAmount give() {
        return give;
    }

    public MonetaryAmount freeze() {
        return freeze;
    }

    /**
     * @param principalAmount
     * @param giveAmount
     * @throws IllegalArgumentException if principalAmount is null
     * @throws IllegalArgumentException if  principalAmount  or giveAmount is negative
     */
    public void prepay(MonetaryAmount principalAmount, MonetaryAmount giveAmount) {
        if (!termOfValidity().isNowValid())
            throw new IncorrectExpirationDateException("Not in validity");
        Objects.requireNonNull(principalAmount, "principalAmount required");
        if (giveAmount == null)
            giveAmount = MONETARY_ZERO;
        if (principalAmount.isNegative() || giveAmount.isNegative())
            throw new IllegalArgumentException("principalAmount and giveAmount must large zero");
        if (principalAmount.isZero() && giveAmount.isZero())
            return;
        principal = principal.add(principalAmount);
        give = give.add(giveAmount);
        //domain event
    }

    /**
     * @param amount
     * @throws IllegalArgumentException if amount is null or is negative
     */
    public void prepay(MonetaryAmount amount) {
        this.prepay(amount, MONETARY_ZERO);
    }

    /**
     * The donation amount is not withdrawable
     *
     * @param amount
     */
    public void withdrawal(MonetaryAmount amount) {
        if (!termOfValidity().isNowValid())
            throw new IncorrectExpirationDateException("Not in validity");
        Objects.requireNonNull(amount, "amount is required");
        if (amount.isNegativeOrZero())
            return;
        MonetaryAmount temp = principal.subtract(freeze);
        if (amount.isGreaterThan(temp))
            throw new InsufficientBalanceException("Insufficient Balance");
        principal = principal.subtract(amount);
        //domain event
    }

    /**
     * @param amount
     * @return negative if insufficient funds
     */
    public void pay(MonetaryAmount amount, Usage usage) {
        if (!termOfValidity().isNowValid())
            throw new IncorrectExpirationDateException("Not in validity");
        MonetaryAmount available = principal.add(give).subtract(freeze);
        if (available.isLessThan(amount))
            throw new InsufficientBalanceException("Insufficient Balance");
        switch (usage) {
            case PRINCIPAL_FIRST:
                principal = principal.subtract(amount);
                if (principal.isNegative()) {
                    principal = MONETARY_ZERO;
                    give = give.add(principal);
                }
                break;
            case GIVE_FIRST:
                MonetaryAmount g = give.subtract(amount);
                if (g.isNegative()) {
                    give = MONETARY_ZERO;
                    principal = principal.add(g);
                } else {
                    give = g;
                }
                break;
            case RATIO:
                double d1 = principal.getNumber().doubleValue();
                double d2 = give.getNumber().doubleValue();
                double d3 = d1 / (d1 + d2);
                MonetaryAmount temp = amount.multiply(d3);
                principal = principal.subtract(temp);
                give = give.subtract(amount.subtract(temp));
                break;
        }
    }

    public void pay(MonetaryAmount amount) {
        pay(amount, Usage.RATIO);
    }

    private void setFreeze(MonetaryAmount freeze) {
        if (freeze == null || freeze.isNegative())
            freeze = MONETARY_ZERO;
        this.freeze = freeze;
    }

    /**
     * @param amount do nothing if amount less or equal zero
     */
    public void frozen(MonetaryAmount amount) {
        if (!termOfValidity().isNowValid())
            throw new IncorrectExpirationDateException("Not in validity");
        if (amount.isNegativeOrZero())
            return;
        freeze = freeze.add(amount);
        //domain enevt
    }

    /**
     * @param amount do nothing if amount less or equal zero
     */
    public void thaw(MonetaryAmount amount) {
        if (!termOfValidity().isNowValid())
            throw new IncorrectExpirationDateException("Not in validity");
        if (amount.isNegativeOrZero())
            return;
        if (amount.isGreaterThan(freeze))
            freeze = MONETARY_ZERO;
        else
            freeze = freeze.subtract(amount);
        //domain enevt
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DebitCard debitCard = (DebitCard) o;

        if (principal != null ? !principal.equals(debitCard.principal) : debitCard.principal != null) return false;
        if (give != null ? !give.equals(debitCard.give) : debitCard.give != null) return false;
        return freeze != null ? freeze.equals(debitCard.freeze) : debitCard.freeze == null;
    }

    @Override
    public int hashCode() {
        int result = principal != null ? principal.hashCode() : 0;
        result = 31 * result + (give != null ? give.hashCode() : 0);
        result = 31 * result + (freeze != null ? freeze.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DebitCard.class.getSimpleName() + "[", "]")
                .add("id='" + id() + "'")
                .add("issuerId='" + issuerId() + "'")
                .add("password='" + password() + "'")
                .add("termOfValidity=" + termOfValidity())
                .add("appearance=" + appearance())
                .add("customerId='" + customerId() + "'")
                .add("principal=" + principal)
                .add("give=" + give)
                .add("freeze=" + freeze)
                .toString();
    }

    public enum Usage {
        PRINCIPAL_FIRST, GIVE_FIRST, RATIO
    }
}
