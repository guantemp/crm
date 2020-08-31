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

import crm.hoprxi.core.domain.model.balance.Balance;
import crm.hoprxi.core.domain.model.balance.SmallChange;
import crm.hoprxi.core.domain.model.card.appearance.Appearance;
import crm.hoprxi.core.domain.model.collaborator.Issuer;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-05-19
 */
public class DebitCardSnapshot {
    private String cardFaceNumber;
    private Balance balance;
    private SmallChange smallChange;
    private String id;
    private Issuer issuer;
    private Appearance appearance;
    private String customerId;
    private boolean available;

    public DebitCardSnapshot(Issuer issuer, String customerId, String id, String cardFaceNumber, boolean available, Balance balance, SmallChange smallChange, Appearance appearance) {
        this.cardFaceNumber = cardFaceNumber;
        this.balance = balance;
        this.smallChange = smallChange;
        this.id = id;
        this.issuer = issuer;
        this.appearance = appearance;
        this.customerId = customerId;
        this.available = available;
    }

    public String getCardFaceNumber() {
        return cardFaceNumber;
    }

    public Balance getBalance() {
        return balance;
    }

    public SmallChange getSmallChange() {
        return smallChange;
    }

    public String getId() {
        return id;
    }

    public Issuer getIssuer() {
        return issuer;
    }

    public Appearance getAppearance() {
        return appearance;
    }

    public String getCustomerId() {
        return customerId;
    }

    public boolean isAvailable() {
        return available;
    }
}
