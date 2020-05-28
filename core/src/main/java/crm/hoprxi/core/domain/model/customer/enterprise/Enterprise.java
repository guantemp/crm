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

package crm.hoprxi.core.domain.model.customer.enterprise;

import crm.hoprxi.core.domain.model.customer.Customer;
import crm.hoprxi.core.domain.model.customer.Vip;
import crm.hoprxi.core.domain.model.customer.person.PostalAddressBook;
import crm.hoprxi.core.domain.model.spss.Spss;

import java.net.URI;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2020-05-26
 */
public class Enterprise extends Customer {
    private Register register;
    private PublicAccount publicAccount;

    public Enterprise(String id, String name, boolean freeze, Vip vip, Spss spss, URI headPortrait, PostalAddressBook postalAddressBook, Register register, PublicAccount publicAccount) {
        super(id, name, freeze, vip, spss, headPortrait, postalAddressBook);
        this.register = register;
        this.publicAccount = publicAccount;
    }

    @Override
    public String toString() {
        return "Enterprise{" +
                "register=" + register +
                ", publicAccount=" + publicAccount +
                "} " + super.toString();
    }
}
