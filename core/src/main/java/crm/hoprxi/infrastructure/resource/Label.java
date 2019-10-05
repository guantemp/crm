/*
 * Copyright (c) 2018 www.hoprxi.com rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package crm.hoprxi.infrastructure.resource;


import mi.foxtail.util.NLS;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-07-25
 */
public class Label extends NLS {
    private static final String BUNDLE_NAME = "cc.foxtail.crm.core.infrastructure.resource.label"; //$NON-NLS-1$

    public static String MantissaProcessing_ROUNDING;
    public static String MantissaProcessing_FLOOR;
    public static String MantissaProcessing_CEILING;
    public static String MantissaProcessing_RETAIN;

    public static String Payment_WeChatPay;
    public static String Payment_Alipay;
    public static String Payment_BestPay;
    public static String Payment_CmPay;
    public static String Payment_PayPal;
    public static String Payment_Lakala;

    static {
        NLS.initializeMessages(BUNDLE_NAME, Label.class);
    }
}
