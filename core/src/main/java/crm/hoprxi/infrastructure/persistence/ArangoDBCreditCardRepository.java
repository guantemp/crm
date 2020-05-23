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

package crm.hoprxi.infrastructure.persistence;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDatabase;
import com.arangodb.ArangoGraph;
import com.arangodb.entity.DocumentField;
import com.arangodb.entity.VertexEntity;
import com.arangodb.entity.VertexUpdateEntity;
import com.arangodb.model.VertexUpdateOptions;
import com.arangodb.util.MapBuilder;
import com.arangodb.velocypack.VPackSlice;
import crm.hoprxi.domain.model.balance.Balance;
import crm.hoprxi.domain.model.balance.SmallChangDenominationEnum;
import crm.hoprxi.domain.model.balance.SmallChange;
import crm.hoprxi.domain.model.card.CreditCard;
import crm.hoprxi.domain.model.card.CreditCardRepository;
import crm.hoprxi.domain.model.card.DebitCard;
import crm.hoprxi.domain.model.card.TermOfValidity;
import crm.hoprxi.domain.model.collaborator.Issuer;
import org.javamoney.moneta.FastMoney;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.money.MonetaryAmount;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2020-05-23
 */
public class ArangoDBCreditCardRepository implements CreditCardRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArangoDBCreditCardRepository.class);
    private static final VertexUpdateOptions UPDATE_OPTIONS = new VertexUpdateOptions().keepNull(false);
    private static Field passwordField;

    static {
        try {
            //debitCardConstructor = DebitCard.class.getDeclaredConstructor(Issuer.class, String.class, String.class, String.class, boolean.class, TermOfValidity.class, Balance.class, SmallChange.class, Appearance.class);
            //debitCardConstructor.setAccessible(true);
            passwordField = DebitCard.class.getDeclaredField("password");
            passwordField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("The creditCard class cannot find such a field.", e);
        }
    }

    private final ArangoDatabase crm;

    public ArangoDBCreditCardRepository(String databaseName) {
        databaseName = Objects.requireNonNull(databaseName, "databaseName required").trim();
        if (!ArangoDBUtil.getResource().db(databaseName).exists())
            LOGGER.error("{} not exists", databaseName);
        crm = ArangoDBUtil.getResource().db(databaseName);
    }


    @Override
    public void save(CreditCard creditCard) {
        boolean exists = crm.collection("credit_card").documentExists(creditCard.id());
        ArangoGraph graph = crm.graph("core");
        if (exists) {
            VertexUpdateEntity vertex = graph.vertexCollection("credit_card").updateVertex(creditCard.id(), creditCard, UPDATE_OPTIONS);
        } else {
            VertexEntity cardVertex = graph.vertexCollection("credit_card").insertVertex(creditCard);
            insertHasEdgeToPerson(graph, cardVertex, creditCard.customerId());
        }
    }

    private void insertHasEdgeToPerson(ArangoGraph graph, VertexEntity cardVertex, String customerId) {
        VertexEntity personVertex = graph.vertexCollection("person").getVertex(customerId, VertexEntity.class);
        graph.edgeCollection("has").insertEdge(new HasEdge(personVertex.getId(), cardVertex.getId()));
    }

    @Override
    public void remove(String id) {
        boolean exists = crm.collection("credit_card").documentExists(id);
        if (exists) {
            ArangoGraph graph = crm.graph("core");
            graph.vertexCollection("credit_card").deleteVertex(id);
        }
    }

    @Override
    public CreditCard find(String id) {
        final String query = "WITH person,credit_card,appearance\n" +
                "FOR c IN credit_card FILTER c._key == @key\n" +
                "FOR p in 1..1 INBOUND c._id has\n" +
                //"FOR appearance IN 1..1 OUTBOUND a._id has\n" +
                "RETURN {'issuer':c.issuer,'customerId':p._key,'id':c._key,'password':c.password,'cardFaceNumber':c.cardFaceNumber,'freeze':c.freeze,'termOfValidity':c.termOfValidity,'balance':c.balance,'smallChange':c.smallChange}";
        final Map<String, Object> bindVars = new MapBuilder().put("key", id).get();
        ArangoCursor<VPackSlice> slices = crm.query(query, bindVars, null, VPackSlice.class);
        try {
            if (slices.hasNext())
                return rebuild(slices.next());
        } catch (IllegalAccessException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Can't rebuild CreditCard", e);
            }
        }
        return null;
    }

    private CreditCard rebuild(VPackSlice next) throws IllegalAccessException {
        if (next == null)
            return null;
        VPackSlice issuerSlice = next.get("issuer");
        Issuer issuer = new Issuer(issuerSlice.get("id").getAsString(), issuerSlice.get("name").getAsString());
        String customerId = next.get("customerId").getAsString();
        String id = next.get("id").getAsString();
        String password = next.get("password").getAsString();
        String cardFaceNumber = next.get("cardFaceNumber").getAsString();
        boolean freeze = next.get("freeze").getAsBoolean();
        //termOfValidity
        VPackSlice termOfValiditySlice = next.get("termOfValidity");
        LocalDate startDate = LocalDate.parse(termOfValiditySlice.get("startDate").getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate expiryDate = LocalDate.parse(termOfValiditySlice.get("expiryDate").getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
        TermOfValidity termOfValidity = TermOfValidity.getInstance(startDate, expiryDate);
        //balance
        VPackSlice balanceSlice = next.get("balance");
        VPackSlice valuableSlice = balanceSlice.get("valuable");
        MonetaryAmount valuable = Money.of(valuableSlice.get("number").getAsBigDecimal(), valuableSlice.get("currency").get("baseCurrency").get("currencyCode").getAsString());
        VPackSlice giveSlice = balanceSlice.get("redPackets");
        MonetaryAmount redPackets = Money.of(giveSlice.get("number").getAsBigDecimal(), giveSlice.get("currency").get("baseCurrency").get("currencyCode").getAsString());
        Balance balance = new Balance(valuable, redPackets);
        //smallChange
        VPackSlice smallChangeSlice = next.get("smallChange");
        SmallChangDenominationEnum smallChangDenominationEnum = SmallChangDenominationEnum.valueOf(smallChangeSlice.get("smallChangDenominationEnum").getAsString());
        VPackSlice smallChangeAmountSlice = smallChangeSlice.get("amount");
        MonetaryAmount amount = FastMoney.of(smallChangeAmountSlice.get("number").getAsNumber(), smallChangeAmountSlice.get("currency").get("baseCurrency").get("currencyCode").getAsString());
        SmallChange smallChange = new SmallChange(amount, smallChangDenominationEnum);

        CreditCard creditCard = new CreditCard(issuer, customerId, id, password, cardFaceNumber, freeze, termOfValidity, null, balance, smallChange, null);
        passwordField.set(creditCard, password);
        return creditCard;
    }

    @Override
    public CreditCard[] findByCardFaceNumber(String cardFaceNumber) {
        return new CreditCard[0];
    }

    @Override
    public CreditCard[] findByCustomer(String customerId) {
        return new CreditCard[0];
    }

    @Override
    public CreditCard[] findAll(int offset, int limit) {
        return new CreditCard[0];
    }

    @Override
    public int size() {
        return 0;
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
