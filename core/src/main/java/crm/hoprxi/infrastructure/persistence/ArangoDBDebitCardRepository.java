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

import com.arangodb.ArangoGraph;
import com.arangodb.entity.DocumentEntity;
import com.arangodb.entity.DocumentField;
import com.arangodb.entity.VertexEntity;
import crm.hoprxi.domain.model.card.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-12-05
 */
public class ArangoDBDebitCardRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArangoDBDebitCardRepository.class);
    private static Field passwordField;
    private static Constructor<Card> cardConstructor;

    static {
        try {
            passwordField = Card.class.getDeclaredField("password");
            passwordField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Not find field password in class card", e);
        }
    }

    private void insertBelongEdgeOfCustomer(ArangoGraph graph, DocumentEntity cardVertex, String customerId) {
        VertexEntity customerVertex = graph.vertexCollection("customer").getVertex(customerId, VertexEntity.class);
        graph.edgeCollection("belong").insertEdge(new CommonEdge(customerVertex.getId(), cardVertex.getId()));
    }

    private static class CommonEdge {
        @DocumentField(DocumentField.Type.FROM)
        private String from;

        @DocumentField(DocumentField.Type.TO)
        private String to;

        private CommonEdge(String from, String to) {
            this.from = from;
            this.to = to;
        }
    }
}
