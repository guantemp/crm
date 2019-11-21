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

import com.arangodb.entity.DocumentField;
import crm.hoprxi.domain.model.card.appearance.Appearance;
import crm.hoprxi.domain.model.card.wallet.CoinWallet;
import crm.hoprxi.domain.model.card.wallet.Wallet;
import crm.hoprxi.domain.model.integral.Integral;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-11-14
 */
public class AnonymousCard {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnonymousCard.class);
    @DocumentField(DocumentField.Type.KEY)
    private String id;
    private String issuerId;
    private TermOfValidity termOfValidity;
    private Appearance appearance;
    private String cardFaceNumber;
    private Wallet wallet;
    private CoinWallet coinWallet;
    private Integral integral;

}
