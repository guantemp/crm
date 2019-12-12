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
package crm.hoprxi.infrastructure.persistence;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.Protocol;
import com.arangodb.velocypack.VPackSlice;
import com.arangodb.velocypack.module.jdk8.VPackJdk8Module;

import java.lang.reflect.Array;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 2018-07-24
 */
public class ArangoDBUtil {
    private static Pattern HUMP_SEGMENTATION_PATTERN = Pattern.compile("[A-Z]");

    private static String segmentation(String className) {
        Matcher matcher = HUMP_SEGMENTATION_PATTERN.matcher(className);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String g = matcher.group();
            matcher.appendReplacement(sb, "_" + g.toLowerCase());
        }
        matcher.appendTail(sb);
        if (sb.charAt(0) == '_') {
            sb.delete(0, 1);
        }
        return sb.toString();
    }

    public static ArangoDB getResource() {
        ArangoDB.Builder builder = new ArangoDB.Builder();
        builder.useProtocol(Protocol.VST).host("127.0.0.1", 8529);
        builder.registerModule(new VPackJdk8Module()).user("root").password("Qwe123465");
        ArangoDB arangoDB = builder.build();
        return arangoDB;
    }

    /**
     * @param arangoDatabase
     * @param t
     * @param offset
     * @param limit
     * @param <T>
     * @return
     */
    public static <T> T[] calculationCollectionSize(ArangoDatabase arangoDatabase, Class<T> t, long offset, int limit) {
        if (offset < 0l)
            offset = 0l;
        if (limit < 0)
            limit = 0;
        long count = 0;
        String collection = segmentation(t.getSimpleName());
        final String countQuery = " RETURN LENGTH(" + collection + ")";
        final ArangoCursor<VPackSlice> countCursor = arangoDatabase.query(countQuery, null, null, VPackSlice.class);
        for (; countCursor.hasNext(); ) {
            count = countCursor.next().getAsLong();
        }

        int difference = (int) (count - offset);
        if (difference <= 0)
            return (T[]) Array.newInstance(t, 0);
        int capacity = difference >= limit ? limit : difference;
        return (T[]) Array.newInstance(t, capacity);
    }

    public static void main(String[] args) {
        //Brand[] brands = ArangoDBUtil.calculationCollectionSize(null, Brand.class, 1, 512);
        //System.out.println(brands.length);
    }
}
