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
import com.arangodb.ArangoDatabase;
import com.arangodb.ArangoGraph;
import com.arangodb.entity.DocumentEntity;
import com.arangodb.entity.DocumentField;
import com.arangodb.entity.VertexEntity;
import com.arangodb.entity.VertexUpdateEntity;
import com.arangodb.model.VertexUpdateOptions;
import com.arangodb.util.MapBuilder;
import com.arangodb.velocypack.VPackSlice;
import crm.hoprxi.domain.model.balance.Balance;
import crm.hoprxi.domain.model.balance.SmallChangDenominationEnum;
import crm.hoprxi.domain.model.balance.SmallChange;
import crm.hoprxi.domain.model.card.DebitCard;
import crm.hoprxi.domain.model.card.DebitCardRepository;
import crm.hoprxi.domain.model.card.TermOfValidity;
import crm.hoprxi.domain.model.card.appearance.Appearance;
import crm.hoprxi.domain.model.collaborator.Issuer;
import org.javamoney.moneta.FastMoney;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.money.MonetaryAmount;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
    private static Constructor<DebitCard> debitCardConstructor;

    static {
        try {
            debitCardConstructor = DebitCard.class.getDeclaredConstructor(Issuer.class, String.class, String.class, String.class, boolean.class, TermOfValidity.class, Balance.class, SmallChange.class, Appearance.class);
            debitCardConstructor.setAccessible(true);
            passwordField = DebitCard.class.getDeclaredField("password");
            passwordField.setAccessible(true);
        } catch (NoSuchFieldException | NoSuchMethodException e) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("The debitCard class cannot find such a field or constructor", e);
        }
    }

    private final ArangoDatabase crm;

    public ArangoDBDebitCardRepository(String databaseName) {
        databaseName = Objects.requireNonNull(databaseName, "databaseName required").trim();
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
    public void remove(String id) {
        boolean exists = crm.collection("debit_card").documentExists(id);
        if (exists) {
            ArangoGraph graph = crm.graph("core");
            graph.vertexCollection("debit_card").deleteVertex(id);
        }
    }

    @Override
    public DebitCard find(String id) {
        final String query = "WITH person,debit_card,appearance\n" +
                "FOR c IN debit_card FILTER c._key == @key\n" +
                "FOR p in 1..1 INBOUND c._id has\n" +
                //"FOR appearance IN 1..1 OUTBOUND a._id has\n" +
                "RETURN {'issuer':c.issuer,'customerId':p._key,'id':c._key,'password':c.password,'cardFaceNumber':c.cardFaceNumber,'freeze':c.freeze,'termOfValidity':c.termOfValidity,'balance':c.balance,'smallChange':c.smallChange}";
        final Map<String, Object> bindVars = new MapBuilder().put("key", id).get();
        ArangoCursor<VPackSlice> slices = crm.query(query, bindVars, null, VPackSlice.class);
        try {
            if (slices.hasNext())
                return rebuild(slices.next());
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Can't rebuild DebitCard", e);
            }
        }
        return null;
    }

    @Override
    public DebitCard[] findAll(int offset, int limit) {
        DebitCard[] debitCards = ArangoDBUtil.calculationCollectionSize(crm, DebitCard.class, offset, limit);
        final String query = "WITH person,debit_card,appearance\n" +
                "FOR c IN debit_card LIMIT @offset,@limit\n" +
                "FOR p in 1..1 INBOUND c._id has\n" +
                //"FOR appearance IN 1..1 OUTBOUND a._id has\n" +
                "RETURN {'issuer':c.issuer,'customerId':p._key,'id':c._key,'password':c.password,'cardFaceNumber':c.cardFaceNumber,'freeze':c.freeze,'termOfValidity':c.termOfValidity,'balance':c.balance,'smallChange':c.smallChange}";
        final Map<String, Object> bindVars = new MapBuilder().put("offset", offset).put("limit", limit).get();
        ArangoCursor<VPackSlice> slices = crm.query(query, bindVars, null, VPackSlice.class);
        try {
            for (int i = 0; slices.hasNext(); i++)
                debitCards[i] = rebuild(slices.next());
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Can't rebuild person", e);
        }
        return debitCards;
    }

    @Override
    public DebitCard[] findByCustomer(String customerId) {
        List<DebitCard> debitCardList = new ArrayList<>();
        final String query = "WITH person,debit_card,appearance\n" +
                "FOR p IN person FILTER p._key == @key\n" +
                "FOR c IN 1..1 OUTBOUND p._id has\n" +
                //"FOR appearance IN 1..1 OUTBOUND a._id has\n" +
                "RETURN {'issuer':c.issuer,'customerId':p._key,'id':c._key,'password':c.password,'cardFaceNumber':c.cardFaceNumber,'freeze':c.freeze,'termOfValidity':c.termOfValidity,'balance':c.balance,'smallChange':c.smallChange}";
        final Map<String, Object> bindVars = new MapBuilder().put("key", customerId).get();
        ArangoCursor<VPackSlice> slices = crm.query(query, bindVars, null, VPackSlice.class);
        try {
            while (slices.hasNext())
                debitCardList.add(rebuild(slices.next()));
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Can't rebuild DebitCard", e);
        }
        return debitCardList.toArray(new DebitCard[debitCardList.size()]);
    }

    @Override
    public DebitCard findByCardFaceNumber(String cardFaceNumber) {
        final String query = "WITH person,debit_card,appearance\n" +
                "FOR c IN debit_card FILTER c.cardFaceNumber == @cardFaceNumber\n" +
                "FOR p in 1..1 INBOUND c._id has\n" +
                //"FOR appearance IN 1..1 OUTBOUND a._id has\n" +
                "RETURN {'issuer':c.issuer,'customerId':p._key,'id':c._key,'password':c.password,'cardFaceNumber':c.cardFaceNumber,'freeze':c.freeze,'termOfValidity':c.termOfValidity,'balance':c.balance,'smallChange':c.smallChange}";
        final Map<String, Object> bindVars = new MapBuilder().put("cardFaceNumber", cardFaceNumber).get();
        ArangoCursor<VPackSlice> slices = crm.query(query, bindVars, null, VPackSlice.class);
        try {
            if (slices.hasNext())
                return rebuild(slices.next());
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Can't rebuild DebitCard", e);
            }
        }
        return null;
    }

    @Override
    public int size() {
        final String query = " RETURN LENGTH(debit_card)";
        final ArangoCursor<VPackSlice> cursor = crm.query(query, null, null, VPackSlice.class);
        for (; cursor.hasNext(); ) {
            return cursor.next().getAsInt();
        }
        return 0;
    }

    private DebitCard rebuild(VPackSlice slice) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (slice == null)
            return null;
        VPackSlice issuerSlice = slice.get("issuer");
        Issuer issuer = new Issuer(issuerSlice.get("id").getAsString(), issuerSlice.get("name").getAsString());
        String customerId = slice.get("customerId").getAsString();
        String id = slice.get("id").getAsString();
        String password = slice.get("password").getAsString();
        String cardFaceNumber = slice.get("cardFaceNumber").getAsString();
        boolean freeze = slice.get("freeze").getAsBoolean();
        //termOfValidity
        VPackSlice termOfValiditySlice = slice.get("termOfValidity");
        LocalDate startDate = LocalDate.parse(termOfValiditySlice.get("startDate").getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate expiryDate = LocalDate.parse(termOfValiditySlice.get("expiryDate").getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
        TermOfValidity termOfValidity = TermOfValidity.getInstance(startDate, expiryDate);
        //balance
        VPackSlice balanceSlice = slice.get("balance");
        VPackSlice valuableSlice = balanceSlice.get("valuable");
        MonetaryAmount valuable = Money.of(valuableSlice.get("number").getAsBigDecimal(), valuableSlice.get("currency").get("baseCurrency").get("currencyCode").getAsString());
        VPackSlice giveSlice = balanceSlice.get("redPackets");
        MonetaryAmount redPackets = Money.of(giveSlice.get("number").getAsBigDecimal(), giveSlice.get("currency").get("baseCurrency").get("currencyCode").getAsString());
        Balance balance = new Balance(valuable, redPackets);
        //smallChange
        VPackSlice smallChangeSlice = slice.get("smallChange");
        SmallChangDenominationEnum smallChangDenominationEnum = SmallChangDenominationEnum.valueOf(smallChangeSlice.get("smallChangDenominationEnum").getAsString());
        VPackSlice smallChangeAmountSlice = smallChangeSlice.get("amount");
        MonetaryAmount amount = FastMoney.of(smallChangeAmountSlice.get("number").getAsNumber(), smallChangeAmountSlice.get("currency").get("baseCurrency").get("currencyCode").getAsString());
        SmallChange smallChange = new SmallChange(amount, smallChangDenominationEnum);

        DebitCard debitCard = debitCardConstructor.newInstance(issuer, customerId, id, cardFaceNumber, freeze, termOfValidity, balance, smallChange, null);
        passwordField.set(debitCard, password);
        return debitCard;
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
