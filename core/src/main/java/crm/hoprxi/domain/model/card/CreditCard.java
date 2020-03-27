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

package crm.hoprxi.domain.model.card;


import com.arangodb.velocypack.annotations.Expose;
import crm.hoprxi.domain.model.DomainRegistry;
import crm.hoprxi.domain.model.balance.Balance;
import crm.hoprxi.domain.model.balance.SmallChange;
import crm.hoprxi.domain.model.card.appearance.Appearance;
import crm.hoprxi.domain.model.collaborator.Issuer;

import javax.money.MonetaryAmount;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2020-03-27
 */
public class CreditCard extends Card {
    @Expose(serialize = false, deserialize = false)
    private String customerId;
    private String password;
    private boolean freeze;
    private LineOfCredit lineOfCredit;

    public CreditCard(Issuer issuer, String id, String cardFaceNumber, TermOfValidity termOfValidity, Balance balance, SmallChange smallChange, Appearance appearance, String customerId, LineOfCredit lineOfCredit) {
        super(issuer, id, cardFaceNumber, termOfValidity, balance, smallChange, appearance);
        setCustomerId(customerId);
        setLineOfCredit(lineOfCredit);
    }

    private void setLineOfCredit(LineOfCredit lineOfCredit) {
        this.lineOfCredit = lineOfCredit;
    }

    private void setCustomerId(String customerId) {
        if (!DomainRegistry.validCustomerId(customerId))
            throw new IllegalArgumentException("customerId isn't valid");
        this.customerId = customerId;
    }

    @Override
    public void debit(MonetaryAmount amount) {

    }


    public void overdraw(MonetaryAmount amount) {
        if (balance().valuable().isPositive()) {
            double d1 = balance().valuable().getNumber().doubleValue();
            double d2 = balance().give().getNumber().doubleValue();
            double d3 = d1 / (d1 + d2);
            MonetaryAmount temp = amount.multiply(d3);
        }
    }
}
