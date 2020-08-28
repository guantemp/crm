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
import crm.hoprxi.core.domain.model.bonus.Bonus;
import crm.hoprxi.core.domain.model.card.appearance.Appearance;
import crm.hoprxi.core.domain.model.collaborator.Issuer;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-08-26
 */
public class AnonymousCardSnapshot {
    protected ValidityPeriod validityPeriod;
    protected String cardFaceNumber;
    protected Balance balance;
    protected SmallChange smallChange;
    private String id;
    private Issuer issuer;
    private Appearance appearance;
    private Bonus bonus;

    public AnonymousCardSnapshot(Issuer issuer, String id, String cardFaceNumber, ValidityPeriod validityPeriod, Balance balance, SmallChange smallChange, Bonus bonus, Appearance appearance) {
        this.validityPeriod = validityPeriod;
        this.cardFaceNumber = cardFaceNumber;
        this.balance = balance;
        this.smallChange = smallChange;
        this.id = id;
        this.issuer = issuer;
        this.appearance = appearance;
        this.bonus = bonus;
    }

    public ValidityPeriod termOfValidity() {
        return validityPeriod;
    }

    public String cardFaceNumber() {
        return cardFaceNumber;
    }

    public Balance balance() {
        return balance;
    }

    public SmallChange smallChange() {
        return smallChange;
    }

    public String id() {
        return id;
    }

    public Issuer issuer() {
        return issuer;
    }

    public Appearance appearance() {
        return appearance;
    }

    public Bonus bonus() {
        return bonus;
    }
}
