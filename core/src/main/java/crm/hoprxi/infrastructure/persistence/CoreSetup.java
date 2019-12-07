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

import com.arangodb.ArangoDB;
import com.arangodb.entity.CollectionType;
import com.arangodb.entity.EdgeDefinition;
import com.arangodb.entity.KeyType;
import com.arangodb.model.CollectionCreateOptions;
import com.arangodb.model.HashIndexOptions;
import com.arangodb.model.SkiplistIndexOptions;
import com.arangodb.model.UserCreateOptions;

import java.util.ArrayList;
import java.util.Collection;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 2019-11-25
 */

public class CoreSetup {

    public static void setup(String databaseName) {
        ArangoDB arangoDB = ArangoDBUtil.getResource();
        if (arangoDB.db(databaseName).exists()) {
            System.out.println(databaseName + " has exists,will be drop");
            arangoDB.db(databaseName).drop();
        }
        arangoDB.createDatabase(databaseName);
        //vertex
        for (String s : new String[]{"customer", "frozen_customer", "debit_card", "anonymous_card", "appearance", "memberRole", "integral_history", "balance_history", "change_history"}) {
            CollectionCreateOptions options = new CollectionCreateOptions();
            options.keyOptions(true, KeyType.traditional, 1, 1);
            arangoDB.db(databaseName).createCollection(s, options);
        }
        //index
        Collection<String> index = new ArrayList<>();
        //customer.nickName
        index.add("nickName");
        SkiplistIndexOptions skiplistIndexOptions = new SkiplistIndexOptions().unique(false).sparse(true);
        arangoDB.db(databaseName).collection("customer").ensureSkiplistIndex(index, skiplistIndexOptions);
        arangoDB.db(databaseName).collection("frozen_customer").ensureSkiplistIndex(index, skiplistIndexOptions);
        //name.mnemonic
        index.clear();
        index.add("cardFaceNumber");
        HashIndexOptions hashIndexOptions = new HashIndexOptions().unique(true).sparse(true);
        arangoDB.db(databaseName).collection("debit_card").ensureHashIndex(index, hashIndexOptions);
        arangoDB.db(databaseName).collection("anonymous_card").ensureHashIndex(index, hashIndexOptions);

        //edge
        for (String s : new String[]{"belong", "next", "has"}) {
            CollectionCreateOptions options = new CollectionCreateOptions().type(CollectionType.EDGES);
            arangoDB.db(databaseName).createCollection(s, options);
        }
        //graph
        Collection<EdgeDefinition> edgeList = new ArrayList<>();
        edgeList.add(new EdgeDefinition().collection("belong").from("debit_card").to("customer", "frozen_customer"));
        edgeList.add(new EdgeDefinition().collection("has").from("debit_card", "anonymous_card").to("appearance"));
        arangoDB.db(databaseName).createGraph("core", edgeList);
        arangoDB.shutdown();
        System.out.println(databaseName + " create success");
        arangoDB = null;
    }

    public static void builderUserAndGrant(String username, String password) {
        ArangoDB arangoDB = ArangoDBUtil.getResource();
        UserCreateOptions userCreateOptions = new UserCreateOptions();
        arangoDB.createUser(username, username);
    }
}
