/*
 * Copyright (c) 2018. www.hoprxi.com rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package crm.hoprxi.infrastructure.persistence;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDatabase;
import com.arangodb.ArangoGraph;
import com.arangodb.entity.DocumentEntity;
import com.arangodb.entity.DocumentField;
import com.arangodb.entity.VertexEntity;
import com.arangodb.model.VertexUpdateOptions;
import com.arangodb.util.MapBuilder;
import com.arangodb.velocypack.VPackSlice;
import crm.hoprxi.domain.model.card.Card;
import crm.hoprxi.domain.model.card.CardRepository;
import crm.hoprxi.domain.model.card.TermOfValidity;
import crm.hoprxi.domain.model.card.appearance.Appearance;
import crm.hoprxi.domain.model.collaborator.Contact;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.money.MonetaryAmount;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-10-27
 */
public class ArangoDBCardRepository implements CardRepository {
    private static final VertexUpdateOptions UPDATE_OPTIONS = new VertexUpdateOptions().keepNull(false);
    private static final Logger LOGGER = LoggerFactory.getLogger(ArangoDBCardRepository.class);
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

    private final ArangoDatabase crm;

    public ArangoDBCardRepository(String databaseName) {
        crm = ArangoDBUtil.getResource().db(databaseName);
    }

    @Override
    public void save(Card card) {
        boolean exists = crm.collection("card").documentExists(card.id());
        ArangoGraph graph = crm.graph("core");
        if (exists) {
            graph.vertexCollection("customer").updateVertex(card.id(), card, UPDATE_OPTIONS);
        } else {
            VertexEntity cardVertex = graph.vertexCollection("card").insertVertex(card);
            insertBelongEdgeOfCustomer(graph, cardVertex, card.customerId());
        }
    }

    private void insertBelongEdgeOfCustomer(ArangoGraph graph, DocumentEntity cardVertex, String customerId) {
        VertexEntity customerVertex = graph.vertexCollection("customer").getVertex(customerId, VertexEntity.class);
        graph.edgeCollection("belong").insertEdge(new CommonEdge(customerVertex.getId(), cardVertex.getId()));
    }

    private void insertHasEdgeOfAppearance(ArangoGraph graph, DocumentEntity cardVertex, Appearance appearance) {
        if (appearance == null)
            return;
        final String query = "FOR a IN appearance FILTER a.issuer.issuerId == @issuerId and a.name == @name  RETURN a";
        final Map<String, Object> bindVars = new MapBuilder().put("issuerId", appearance.issuer().identity()).put("name", appearance.name()).get();
        ArangoCursor<VertexEntity> slices = crm.query(query, bindVars, null, VertexEntity.class);
        if (slices.hasNext()) {
            graph.edgeCollection("has").insertEdge(new CommonEdge(cardVertex.getId(), slices.next().getId()));
        } else {
            VertexEntity appear = graph.vertexCollection("appearance").insertVertex(appearance);
            graph.edgeCollection("has").insertEdge(new CommonEdge(cardVertex.getId(), appear.getId()));
        }
    }

    private boolean isCustomerChange() {
        final String query = "WITH customer,card,belong\n"
                + "FOR c IN card FILTER c.id==@identity";
        return true;
    }

    @Override
    public Card[] findAll(long offset, int limit) {
        return new Card[0];
    }

    @Override
    public Card find(String identity) {
        final String query = "WITH sku,has,barcode,attribute\n" +
                "FOR s IN sku FILTER s._id == @identity \n" +
                "LET attributes = (FOR v,e IN 1..1 OUTBOUND s._id has FILTER v._id =~ '^attribute' RETURN v)\n" +
                "LET barcodes = (FOR v,e IN 1..1 OUTBOUND s._id has FILTER v._id =~ '^barcode' RETURN v)\n" +
                "RETURN {'sku':s,barcodes,attributes}";
        final Map<String, Object> bindVars = new MapBuilder().put("identity", "sku/" + identity).get();
        ArangoCursor<VPackSlice> slices = crm.query(query, bindVars, null, VPackSlice.class);
        if (slices.hasNext())
            return rebuild(slices.next());
        /*
        while (slices.hasNext()) {
            try {
                return rebuild(slices.next());
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Can't rebuild card", e);
            }
        }*/
        return null;
    }

    private Card rebuild(VPackSlice slice) {
        String identity = slice.get(DocumentField.Type.KEY.getSerializeName()).getAsString();
        String issuerId = slice.get("issuerId").getAsString();
        String number = slice.get("number").getAsString();
        String password = slice.get("password").getAsString();
        //contact
        VPackSlice contactSlice = slice.get("contact");
        String mobilePhone = null;
        String telephone = null;
        if (!contactSlice.get("mobilePhone").isNone() && !contactSlice.get("mobilePhone").isNull())
            mobilePhone = contactSlice.get("mobilePhone").getAsString();
        if (!contactSlice.get("telephone").isNone() && !contactSlice.get("telephone").isNull())
            telephone = contactSlice.get("telephone").getAsString();
        Contact contact = new Contact(contactSlice.get("fullName").getAsString(), mobilePhone, telephone);
        //TermOfValidity
        VPackSlice termOfValiditySlice = slice.get("termOfValidity");
        LocalDate startDate = LocalDate.parse(termOfValiditySlice.get("startDate").getAsString(), DateTimeFormatter.ISO_DATE);
        LocalDate expiryDate = LocalDate.parse(termOfValiditySlice.get("expiryDate").getAsString(), DateTimeFormatter.ISO_DATE);
        TermOfValidity termOfValidity = null;
        if (startDate.equals(expiryDate) && startDate.equals(TermOfValidity.DAY_OF_INFAMY) && expiryDate.equals(TermOfValidity.DAY_OF_INFAMY))
            termOfValidity = TermOfValidity.PERMANENCE;
        else
            termOfValidity = new TermOfValidity(startDate, expiryDate);
        //Balance
        VPackSlice balanceSlice = slice.get("balance");
        MonetaryAmount recharge = Money.parse("");
        MonetaryAmount give = Money.parse("");
        Balance balance = new Balance(recharge, give);
        // Change
        MonetaryAmount amount = null;
        Change change = new Change(amount);
        //Integral
        Integral integral = new Integral(slice.get("integral").getAsBigDecimal());
        Card card = new Card(identity, issuerId, number, password, contact, termOfValidity, null,
                balance, change, integral, "");
        //passwordField.set(card,password);
        return card;
    }


    @Override
    public Card findByTelephone(String telephone) {
        return null;
    }

    @Override
    public Card findByNumber(String number) {
        return null;
    }

    @Override
    public String nextIdentity() {
        return null;
    }

    @Override
    public Card authenticCredentials(String number, String password) {
        return null;
    }

    @Override
    public void remove(String identity) {

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
