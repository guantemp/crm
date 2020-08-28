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

import crm.hoprxi.core.domain.model.card.appearance.Appearance;
import crm.hoprxi.core.domain.model.collaborator.Issuer;
import event.hoprxi.domain.model.DomainEvent;

import java.time.LocalDateTime;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2020-08-26
 */
public class AnonymousCardIssued implements DomainEvent {
    private LocalDateTime occurredOn;
    private int version;
    private String id;
    private String cardFaceNumber;
    private Issuer issuer;
    private ValidityPeriod validityPeriod;
    private Appearance appearance;

    public AnonymousCardIssued(String id, String cardFaceNumber, Issuer issuer, ValidityPeriod validityPeriod, Appearance appearance) {
        this.id = id;
        this.cardFaceNumber = cardFaceNumber;
        this.issuer = issuer;
        this.validityPeriod = validityPeriod;
        this.appearance = appearance;
        occurredOn = LocalDateTime.now();
        version = 1;
    }

    public String id() {
        return id;
    }

    public String cardFaceNumber() {
        return cardFaceNumber;
    }

    public Issuer issuer() {
        return issuer;
    }

    public ValidityPeriod termOfValidity() {
        return validityPeriod;
    }

    public Appearance appearance() {
        return appearance;
    }

    @Override
    public LocalDateTime occurredOn() {
        return occurredOn;
    }

    @Override
    public int version() {
        return version;
    }
}
