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

import com.arangodb.ArangoDatabase;
import com.arangodb.ArangoGraph;
import com.arangodb.entity.DocumentEntity;
import com.arangodb.entity.DocumentField;
import com.arangodb.entity.VertexEntity;
import com.arangodb.entity.VertexUpdateEntity;
import com.arangodb.model.VertexUpdateOptions;
import com.arangodb.util.MapBuilder;
import com.arangodb.velocypack.VPackSlice;
import crm.hoprxi.domain.model.card.Card;
import crm.hoprxi.domain.model.card.DebitCard;
import crm.hoprxi.domain.model.card.DebitCardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-12-25
 */
public class ArangoDBDebitCardRepository implements DebitCardRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArangoDBDebitCardRepository.class);
    private static final VertexUpdateOptions UPDATE_OPTIONS = new VertexUpdateOptions().keepNull(false);
    private static Field passwordField;
    private static Constructor<Card> cardConstructor;

    static {
        try {
            passwordField = DebitCard.class.getDeclaredField("password");
            passwordField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Not find field password in class DebitCard", e);
        }
    }

    private final ArangoDatabase crm;

    public ArangoDBDebitCardRepository(String databaseName) {
        databaseName = Objects.requireNonNull(databaseName, "").trim();
        if (!ArangoDBUtil.getResource().db(databaseName).exists())
            LOGGER.error("{} not exists", databaseName);
        crm = ArangoDBUtil.getResource().db(databaseName);
    }

    @Override
    public void save(DebitCard debitCard) {
        boolean exists = crm.collection("debit_card").documentExists(debitCard.id());
        ArangoGraph graph = crm.graph("core");
        if (exists) {
            VertexUpdateEntity vertex = graph.vertexCollection("debit_card").updateVertex(debitCard.id(), debitCard, UPDATE_OPTIONS);
        } else {
            VertexEntity cardVertex = graph.vertexCollection("debit_card").insertVertex(debitCard);
            insertHasEdgeToPerson(graph, cardVertex, debitCard.customerId());
        }
    }

    private void insertHasEdgeToPerson(ArangoGraph graph, DocumentEntity cardVertex, String customerId) {
        VertexEntity personVertex = graph.vertexCollection("person").getVertex(customerId, VertexEntity.class);
        graph.edgeCollection("has").insertEdge(new HasEdge(personVertex.getId(), cardVertex.getId()));
    }

    @Override
    public DebitCard authenticCredentials(String id, String password) {
        return null;
    }

    @Override
    public void remove(String id) {

    }

    @Override
    public DebitCard find(String id) {
        final String query = "WITH person,debit_card,appearance\n" +
                "FOR c IN debit_card FILTER c._key == @key \n" +
                "FOR p in 1..1 INBOUND c._id has\n" +
                //"FOR appearance IN 1..1 OUTBOUND a._id has\n" +
                "RETURN {'id':c._key,'issuer':c.issuer,'cardFaceNumber':c.cardFaceNumber,'termOfValidity':c.termOfValidity,'balance':c.balance,'smallChange':c.smallChange,'bonus':c.bonus}";
        final Map<String, Object> bindVars = new MapBuilder().put("key", id).get();
        return null;
    }

    @Override
    public DebitCard[] findAll(int offset, int limit) {
        return new DebitCard[0];
    }

    @Override
    public DebitCard findByCustomer(String customerId) {
        return null;
    }

    @Override
    public DebitCard findByCardFaceNumber(String cardFaceNumber) {
        return null;
    }

    private DebitCard rebuilder(VPackSlice slice) {
        return null;
    }

    private static class HasEdge {
        @DocumentField(DocumentField.Type.FROM)
        private String from;

        @DocumentField(DocumentField.Type.TO)
        private String to;

        private HasEdge(String from, String to) {
            this.from = from;
            this.to = to;
        }
    }
}
