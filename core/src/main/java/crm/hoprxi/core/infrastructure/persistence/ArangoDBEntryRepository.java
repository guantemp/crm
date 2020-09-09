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

import com.arangodb.ArangoDatabase;
import com.arangodb.ArangoGraph;
import com.arangodb.model.VertexUpdateOptions;
import crm.hoprxi.core.domain.model.bonus.consumption.Entry1;
import crm.hoprxi.core.domain.model.bonus.consumption.EntryTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-06-11
 */
public class ArangoDBEntryRepository {
    private static final VertexUpdateOptions UPDATE_OPTIONS = new VertexUpdateOptions().keepNull(false);
    private static final Logger LOGGER = LoggerFactory.getLogger(ArangoDBEntryRepository.class);
    private final ArangoDatabase crm;

    public ArangoDBEntryRepository(String databaseName) {
        crm = ArangoDBUtil.getResource().db(databaseName);
    }


    public void save(EntryTemplate template) {
        boolean exists = crm.collection("bonus_entry").documentExists(template.name());
        ArangoGraph graph = crm.graph("core");
        if (exists) {
            graph.vertexCollection("bonus_entry").updateVertex(template.name(), template, UPDATE_OPTIONS);
        } else {
            graph.vertexCollection("bonus_entry").insertVertex(template);
        }
    }


    public Entry1 find(String id) {
        return null;
    }
}
