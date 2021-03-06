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

package crm.hoprxi.core.domain.model.collaborator;


import crm.hoprxi.core.infrastructure.resource.Label;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-08-07
 */
public enum Payment {
    WECHAT_PAY {
        @Override
        public String toString() {
            return Label.Payment_WeChat_Pay;
        }
    }, ALIPAY {
        @Override
        public String toString() {
            return Label.Payment_Alipay;
        }
    }, BESTPAY {
        @Override
        public String toString() {
            return Label.Payment_BestPay;
        }
    }, CMPAY {
        @Override
        public String toString() {
            return Label.Payment_CmPay;
        }
    },
    PAYPAL {
        @Override
        public String toString() {
            return Label.Payment_PayPal;
        }
    }, APPLE_PAY {
        @Override
        public String toString() {
            return Label.Payment_Apple_Pay;
        }
    }, JDPAY {
        @Override
        public String toString() {
            return Label.Payment_Apple_Pay;
        }
    }
}
