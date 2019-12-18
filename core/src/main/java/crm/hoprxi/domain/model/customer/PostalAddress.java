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


import crm.hoprxi.domain.model.collaborator.Address;
import crm.hoprxi.domain.model.collaborator.Contact;

import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2019-07-19
 */
public class PostalAddress {
    private Address address;
    private Contact contact;

    public PostalAddress(Address address, Contact contact) {
        setAddress(address);
        setContact(contact);
    }

    private void setContact(Contact contact) {
        this.contact = Objects.requireNonNull(contact, "contact required");
    }

    private void setAddress(Address address) {
        this.address = Objects.requireNonNull(address, "address required");
    }

    public Address address() {
        return address;
    }

    public Contact contact() {
        return contact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PostalAddress that = (PostalAddress) o;

        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        return contact != null ? contact.equals(that.contact) : that.contact == null;
    }

    @Override
    public int hashCode() {
        int result = address != null ? address.hashCode() : 0;
        result = 31 * result + (contact != null ? contact.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PostalAddress{" +
                "address=" + address +
                ", contact=" + contact +
                '}';
    }
}
