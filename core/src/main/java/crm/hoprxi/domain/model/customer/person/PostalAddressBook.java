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
package crm.hoprxi.domain.model.customer.person;

import crm.hoprxi.domain.model.customer.PostalAddress;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-07-13
 */
public class PostalAddressBook implements Serializable {
    transient private static final int MAX_SHIP_ADDRESS = 24;
    private static final long serialVersionUID = 1L;
    private int acquiescence = 0;
    private PostalAddress[] postalAddresses;

    public PostalAddressBook() {
        postalAddresses = new PostalAddress[0];
    }

    public PostalAddressBook(PostalAddress[] postalAddresses, int acquiescence) {
        setPostalAddresses(postalAddresses);
        setAcquiescence(acquiescence);
    }

    public PostalAddressBook(PostalAddress postalAddress) {
        this(new PostalAddress[]{postalAddress}, 0);
    }

    private void setPostalAddresses(PostalAddress[] postalAddresses) {
        Objects.requireNonNull(postalAddresses, "postal addresses required");
        if (postalAddresses.length > MAX_SHIP_ADDRESS)
            throw new IllegalArgumentException(MAX_SHIP_ADDRESS + " postal addresses are allowed at most");
        this.postalAddresses = postalAddresses;
    }

    private void setAcquiescence(int acquiescence) {
        if (acquiescence < 0 || acquiescence >= postalAddresses.length)
            throw new IllegalArgumentException("Invalid default location");
        this.acquiescence = acquiescence;
    }

    public PostalAddressBook addAndSetAcquiescence(PostalAddress address) {
        if (this.postalAddresses.length >= MAX_SHIP_ADDRESS)
            return this;
        for (PostalAddress pa : postalAddresses)
            if (pa.equals(address))
                return this;
        int length = postalAddresses.length;
        PostalAddress[] temp = new PostalAddress[length + 1];
        System.arraycopy(postalAddresses, 0, temp, 0, length);
        temp[length] = address;
        return new PostalAddressBook(temp, length);
    }

    public PostalAddressBook add(PostalAddress address) {
        if (this.postalAddresses.length >= MAX_SHIP_ADDRESS)
            return this;
        for (PostalAddress pa : postalAddresses)
            if (pa.equals(address))
                return this;
        int length = postalAddresses.length;
        PostalAddress[] temp = new PostalAddress[length + 1];
        System.arraycopy(postalAddresses, 0, temp, 0, length);
        temp[length] = address;
        return new PostalAddressBook(temp, acquiescence);
    }

    public PostalAddressBook setAcquiescencePostalAddress(PostalAddress address) {
        for (int i = 0; i < postalAddresses.length; i++) {
            if (postalAddresses[i].equals(address)) {
                if (i != acquiescence)
                    return new PostalAddressBook(postalAddresses, i);
            }
        }
        return this;
    }

    public PostalAddress acquiescencePostalAddress() {
        return postalAddresses[acquiescence];
    }

    public PostalAddress[] postalAddresses() {
        return postalAddresses.clone();
    }

    public PostalAddressBook remove(PostalAddress address) {
        int length = postalAddresses.length;
        if (length == 0)
            return this;
        for (int i = 0; i < length; i++) {
            if (postalAddresses[i].equals(address)) {
                PostalAddress[] temp = new PostalAddress[length - 1];
                System.arraycopy(postalAddresses, 0, temp, 0, i);
                System.arraycopy(postalAddresses, i + 1, temp, i, length - i - 1);
                if (acquiescence == i)
                    return new PostalAddressBook(temp, 0);
                else if (acquiescence > i)
                    return new PostalAddressBook(temp, acquiescence - 1);
                else
                    return new PostalAddressBook(temp, acquiescence);
            }
        }
        return this;
    }

    @Override
    public String toString() {
        return "PostalAddressBook{" +
                "acquiescence=" + acquiescence +
                ", postalAddresses=" + Arrays.toString(postalAddresses) +
                '}';
    }
}
