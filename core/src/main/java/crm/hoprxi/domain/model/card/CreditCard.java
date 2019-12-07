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


import crm.hoprxi.domain.model.DomainRegistry;
import crm.hoprxi.domain.model.LineOfCredit;
import crm.hoprxi.domain.model.card.appearance.Appearance;
import crm.hoprxi.domain.model.card.balance.Balance;
import crm.hoprxi.domain.model.card.balance.SmallChange;
import crm.hoprxi.domain.model.collaborator.Issuer;

import javax.money.MonetaryAmount;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-08-15
 */
public class CreditCard extends Card {
    private String customerId;
    private LineOfCredit lineOfCredit;

    public CreditCard(String id, Issuer issuerId, String customerId, String cardFaceNumber, TermOfValidity termOfValidity, LineOfCredit lineOfCredit, Balance balance, SmallChange smallChange, Appearance appearance) {
        super(id, issuerId, cardFaceNumber, termOfValidity, balance, smallChange, appearance);
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
    protected boolean isCardFaceNumberSpec() {
        return false;
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
