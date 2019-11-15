/*
 * Copyright (c) 2019 www.hoprxi.com rights Reserved.
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
 */
package crm.hoprxi.domain.model.card;


import com.arangodb.entity.DocumentField;
import crm.hoprxi.domain.model.card.appearance.Appearance;
import mi.hoprxi.crypto.EncryptionService;
import mi.hoprxi.crypto.SM3Encryption;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-08-12
 */
public abstract class Card {
    protected static final MonetaryAmount MONETARY_ZERO = Money.of(0, Monetary.getCurrency(Locale.getDefault()));
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^\\d{6,6}$");
    private static final Logger LOGGER = LoggerFactory.getLogger(Card.class);
    @DocumentField(DocumentField.Type.KEY)
    private String id;
    private String issuerId;
    private String password;
    private TermOfValidity termOfValidity;
    private Appearance appearance;

    public Card(String id, String issuerId, String password) {
        this(id, issuerId, "", TermOfValidity.PERMANENCE, null);
    }

    /**
     * @param id
     * @param issuerId
     * @param password
     * @param termOfValidity
     * @param appearance
     * @throws IllegalArgumentException if id or issuerId or customerId is null or empty
     * @throws IllegalArgumentException if  password isn't six digit number
     */
    public Card(String id, String issuerId, String password, TermOfValidity termOfValidity, Appearance appearance) {
        setId(id);
        setIssuerId(issuerId);
        setPassword(password);
        setTermOfValidity(termOfValidity);
        setAppearance(appearance);
    }

    private void setAppearance(Appearance appearance) {
        this.appearance = appearance;
    }

    public String issuerId() {
        return issuerId;
    }

    public String id() {
        return id;
    }


    public String password() {
        return password;
    }

    public TermOfValidity termOfValidity() {
        return termOfValidity;
    }

    private void setIssuerId(String issuerId) {
        issuerId = Objects.requireNonNull(issuerId, "issuerId is required").trim();
        if (issuerId.isEmpty())
            throw new IllegalArgumentException("Must provide a issuer id.");
        this.issuerId = issuerId;
    }

    private void setTermOfValidity(TermOfValidity termOfValidity) {
        if (termOfValidity == null)
            termOfValidity = TermOfValidity.PERMANENCE;
        this.termOfValidity = termOfValidity;
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

    private void setId(String id) {
        id = Objects.requireNonNull(id, "id required").trim();
        if (id.isEmpty())
            throw new IllegalArgumentException("Must provide a card id.");
        this.id = id;
    }

    public Appearance appearance() {
        return appearance;
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
}
