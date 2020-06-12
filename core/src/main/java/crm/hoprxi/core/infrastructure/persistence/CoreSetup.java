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
package crm.hoprxi.core.infrastructure.persistence;

import com.arangodb.ArangoDB;
import com.arangodb.entity.CollectionType;
import com.arangodb.entity.EdgeDefinition;
import com.arangodb.entity.KeyType;
import com.arangodb.model.CollectionCreateOptions;
import com.arangodb.model.HashIndexOptions;
import com.arangodb.model.SkiplistIndexOptions;
import com.arangodb.model.UserCreateOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 2019-11-25
 */

public class CoreSetup {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoreSetup.class);

    public static void setup(String databaseName) {
        ArangoDB arangoDB = ArangoDBUtil.getResource();
        if (arangoDB.db(databaseName).exists()) {
            LOGGER.info("{} has exists,will be drop.", databaseName);
            arangoDB.db(databaseName).drop();
        }
        arangoDB.createDatabase(databaseName);
        //vertex
        for (String s : new String[]{"person", "enterprise", "debit_card", "anonymous_card", "credit_card", "appearance", "memberRole", "bonus_history", "balance_history",
                "change_history", "bonus_entry"}) {
            CollectionCreateOptions options = new CollectionCreateOptions();
            options.keyOptions(true, KeyType.traditional, 1, 1);
            arangoDB.db(databaseName).createCollection(s, options);
        }
        //index
        Collection<String> index = new ArrayList<>();
        //name
        index.add("name");
        SkiplistIndexOptions skiplistIndexOptions = new SkiplistIndexOptions().unique(false).sparse(true);
        arangoDB.db(databaseName).collection("person").ensureSkiplistIndex(index, skiplistIndexOptions);
        arangoDB.db(databaseName).collection("enterprise").ensureSkiplistIndex(index, skiplistIndexOptions);
        //name.mnemonic
        index.clear();
        index.add("cardFaceNumber");
        HashIndexOptions hashIndexOptions = new HashIndexOptions().unique(true).sparse(true);
        arangoDB.db(databaseName).collection("debit_card").ensureHashIndex(index, hashIndexOptions);
        arangoDB.db(databaseName).collection("anonymous_card").ensureHashIndex(index, hashIndexOptions);
        arangoDB.db(databaseName).collection("credit_card").ensureHashIndex(index, hashIndexOptions);

        //edge
        for (String s : new String[]{"has"}) {
            CollectionCreateOptions options = new CollectionCreateOptions().type(CollectionType.EDGES);
            arangoDB.db(databaseName).createCollection(s, options);
        }
        //graph
        Collection<EdgeDefinition> edgeList = new ArrayList<>();
        //edgeList.add(new EdgeDefinition().collection("belong").from("debit_card").to("person", "frozen_person"));
        edgeList.add(new EdgeDefinition().collection("has").from("debit_card", "anonymous_card", "credit_card", "person", "enterprise").to("debit_card", "credit_card", "appearance"));
        arangoDB.db(databaseName).createGraph("core", edgeList);
        arangoDB.shutdown();
        LOGGER.info("{} create success.", databaseName);
        arangoDB = null;
    }

    public static void builderUserAndGrant(String username, String password) {
        ArangoDB arangoDB = ArangoDBUtil.getResource();
        UserCreateOptions userCreateOptions = new UserCreateOptions();
        arangoDB.createUser(username, username);
    }
}
