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

import crm.hoprxi.core.domain.model.balance.Rounded;

import javax.money.MonetaryAmount;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-08-26
 */
public class CardServices {
    private AnonymousCardRepository anonymousCardRepository;
    private DebitCardRepository debitCardRepository;

    public Rounded smallChangCalculation(MonetaryAmount receivables, Card card) {
        return null;
    }

    public void anonymousCardUpgradeToDebitCard(String cardId, String customerId, String password) {
        AnonymousCard anonymousCard = anonymousCardRepository.find(cardId);
        if (anonymousCard == null)
            return;
        DebitCard debitCard = new DebitCard(anonymousCard.issuer(), customerId, anonymousCard.id(), password, anonymousCard.cardFaceNumber(), false,
                anonymousCard.balance(), anonymousCard.smallChange(), anonymousCard.appearance());
        debitCardRepository.save(debitCard);
        anonymousCardRepository.remove(cardId);
    }
}
