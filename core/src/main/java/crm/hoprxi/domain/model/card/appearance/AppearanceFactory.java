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
package crm.hoprxi.domain.model.card.appearance;

import java.util.Hashtable;
import java.util.Map;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2019-07-20
 */
public class AppearanceFactory {
    private final static Map<String, Appearance> cache = new Hashtable<String, Appearance>();

    /**
     * @param point
     * @return
     */
    public static Appearance get(String point) {
        Appearance appearance = cache.get(point);
        return appearance;
    }

    /**
     * @param point      this is appearance.issuer.id + appearance.name
     * @param appearance
     */
    public static void put(String point, Appearance appearance) {
        cache.put(appearance.issuer().identity() + appearance.name(), appearance);
    }

    public static Appearance getDefault() {
        return null;
    }
}
