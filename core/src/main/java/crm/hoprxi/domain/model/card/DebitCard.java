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

import crm.hoprxi.domain.model.card.appearance.Appearance;
import crm.hoprxi.domain.model.card.wallet.CoinWallet;
import crm.hoprxi.domain.model.card.wallet.Wallet;
import crm.hoprxi.domain.model.integral.Integral;
import mi.hoprxi.crypto.EncryptionService;
import mi.hoprxi.crypto.SM3Encryption;

import javax.money.MonetaryAmount;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.2 builder 2019-08-06
 */
public class DebitCard extends Card {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^\\d{6,6}$");
    private String password;
    private Wallet wallet;
    private CoinWallet coinWallet;
    private Integral integral;
    private String customerId;

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
        super(id, issuerId, password, termOfValidity, appearance);
    }

    private void setPassword(String password) {
        Objects.requireNonNull(password, "password is required");
        if (!password.isEmpty()) {
            Matcher matcher = PASSWORD_PATTERN.matcher(password);
            if (!matcher.matches())
                throw new IllegalArgumentException("password must 6 digit number");
        }
        EncryptionService encryption = new SM3Encryption();
        this.password = encryption.encrypt(password);
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

    }

    /**
     * @param amount
     * @throws IllegalArgumentException if amount is null or is negative
     */
    public void prepay(MonetaryAmount amount) {
        this.prepay(amount, null);
    }

    /**
     * The donation amount is not withdrawable
     *
     * @param amount
     */
    public void withdrawal(MonetaryAmount amount) {
        if (!termOfValidity().isNowValid())
            throw new IncorrectExpirationDateException("Not in validity");

        //domain event
    }

    /**
     * @param amount
     * @return negative if insufficient funds
     */
    public void pay(MonetaryAmount amount, PaymentStrategy strategy) {
        if (!termOfValidity().isNowValid())
            throw new IncorrectExpirationDateException("Not in validity");

    }

    public void pay(MonetaryAmount amount) {
        pay(amount, PaymentStrategy.BALANCE_FIRST);
    }
}
