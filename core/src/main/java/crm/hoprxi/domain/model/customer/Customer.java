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
package crm.hoprxi.domain.model.customer;

import com.arangodb.entity.DocumentField;
import crm.hoprxi.domain.model.DomainRegistry;
import crm.hoprxi.domain.model.collaborator.Issuer;
import crm.hoprxi.domain.model.rmf.Credit;
import mi.hoprxi.crypto.EncryptionService;
import mi.hoprxi.crypto.SM3Encryption;

import java.net.URI;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.2 builder 2019-08-26
 */
public abstract class Customer {
    public static final Customer ANONYMOUS = new Customer("anonymous", "anonymous") {
        @Override
        public void rename(String newName) {
        }
    };
    private static final int MAX_LENGTH = 255;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^\\d{6,6}$");
    @DocumentField(DocumentField.Type.KEY)
    private String id;
    private String name;
    private URI headPortrait;
    private Credit credit;
    private String transactionPassword;
    private Issuer issuer;


    public Customer(String id, String name) {
        this(id, name, Credit.NO_CREDIT, null);
    }

    public Customer(String id, String name, Credit credit, URI headPortrait) {
        setId(id);
        setName(name);
        setCredit(credit);
        this.headPortrait = headPortrait;
    }


    protected void setId(String id) {
        this.id = Objects.requireNonNull(id, "id required");
    }

    private void setPassword(String transactionPassword) {
        Objects.requireNonNull(transactionPassword, "password is required");
        if (!transactionPassword.isEmpty()) {
            Matcher matcher = PASSWORD_PATTERN.matcher(transactionPassword);
            if (!matcher.matches())
                throw new IllegalArgumentException("password must 6 digit number");
        }
        EncryptionService encryption = new SM3Encryption();
        this.transactionPassword = encryption.encrypt(transactionPassword);
    }

    public void rename(String newName) {
        newName = Objects.requireNonNull(newName, "newName required").trim();
        if (newName.isEmpty() || newName.length() > MAX_LENGTH)
            throw new IllegalArgumentException("newName length range is 1-" + MAX_LENGTH);
        if (!name.equals(newName)) {
            name = newName;
            DomainRegistry.domainEventPublisher().publish(new CustomerRenamed(id, newName));
        }
    }

    public String id() {
        return id;
    }

    public URI headPortrait() {
        return headPortrait;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        return id != null ? id.equals(customer.id) : customer.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    /*
     public FrozenCustomer frozen() {
         return new FrozenCustomer(id, name, credit, smallChange, headPortrait, certificates, birthday, postalAddressBook);
     }

     public CustomerSnapshot toCustomerSnapshot() {
         return new CustomerSnapshot(id, name, postalAddressBook.acquiescencePostalAddress());
     }
 */
    public Credit credit() {
        return credit;
    }

    private void setCredit(Credit credit) {
        this.credit = Objects.requireNonNull(credit, "credit required");
    }

    public String name() {
        return name;
    }

    private void setName(String name) {
        name = Objects.requireNonNull(name, "name required").trim();
        if (name.isEmpty() || name.length() > MAX_LENGTH)
            throw new IllegalArgumentException("name length range is [1-128]");
        this.name = name;
    }
}
