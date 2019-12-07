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

import crm.hoprxi.domain.model.card.appearance.Appearance;
import crm.hoprxi.domain.model.card.balance.Balance;
import crm.hoprxi.domain.model.card.balance.SmallChange;
import crm.hoprxi.domain.model.collaborator.Issuer;
import crm.hoprxi.domain.model.integral.Integral;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-11-14
 */
public class AnonymousCard extends Card {
    private static final Logger LOGGER = LoggerFactory.getLogger(Card.class);
    private Integral integral;

    public AnonymousCard(String id, Issuer issuerId, String cardFaceNumber, TermOfValidity termOfValidity, Balance balance, SmallChange smallChange, Integral integral, Appearance appearance) {
        super(id, issuerId, cardFaceNumber, termOfValidity, balance, smallChange, appearance);
        this.integral = integral;
    }

    public Integral integral() {
        return integral;
    }

    @Override
    protected boolean isCardFaceNumberSpec() {
        return true;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Card.class.getSimpleName() + "[", "]")
                .add("super=" + super.toString())
                .add("integral=" + integral)
                .toString();
    }
}
