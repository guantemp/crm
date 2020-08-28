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

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDatabase;
import com.arangodb.ArangoGraph;
import com.arangodb.entity.DocumentField;
import com.arangodb.entity.VertexEntity;
import com.arangodb.entity.VertexUpdateEntity;
import com.arangodb.model.VertexUpdateOptions;
import com.arangodb.util.MapBuilder;
import com.arangodb.velocypack.VPackSlice;
import crm.hoprxi.core.domain.model.balance.Balance;
import crm.hoprxi.core.domain.model.balance.SmallChangDenominationEnum;
import crm.hoprxi.core.domain.model.balance.SmallChange;
import crm.hoprxi.core.domain.model.card.CreditCard;
import crm.hoprxi.core.domain.model.card.CreditCardRepository;
import crm.hoprxi.core.domain.model.card.LineOfCredit;
import crm.hoprxi.core.domain.model.collaborator.Issuer;
import org.javamoney.moneta.FastMoney;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
            passwordField = CreditCard.class.getDeclaredField("password");
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
                //"FOR appearance IN 1..1 OUTBOUND c._id has\n" +
                "RETURN {'issuer':c.issuer,'customerId':p._key,'id':c._key,'password':c.password,'cardFaceNumber':c.cardFaceNumber,'freeze':c.freeze,'termOfValidity':c.termOfValidity," +
                "'lineOfCredit':c.lineOfCredit,'balance':c.balance,'smallChange':c.smallChange}";
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

    private CreditCard rebuild(VPackSlice slice) throws IllegalAccessException {
        if (slice == null)
            return null;
        VPackSlice issuerSlice = slice.get("issuer");
        Issuer issuer = new Issuer(issuerSlice.get("id").getAsString(), issuerSlice.get("name").getAsString());
        String customerId = slice.get("customerId").getAsString();
        String id = slice.get("id").getAsString();
        String password = slice.get("password").getAsString();
        String cardFaceNumber = slice.get("cardFaceNumber").getAsString();
        boolean freeze = slice.get("freeze").getAsBoolean();
        //lineOfCredit
        VPackSlice lineOfCreditSlice = slice.get("lineOfCredit");
        VPackSlice quotaSlice = lineOfCreditSlice.get("quota");
        MonetaryAmount quota = this.toMonetaryAmount(quotaSlice);
        int billDays = lineOfCreditSlice.get("billDays").getAsInt();
        LocalDate repaymentDate = LocalDate.parse(lineOfCreditSlice.get("repaymentDate").getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
        LineOfCredit lineOfCredit = new LineOfCredit(quota, billDays, repaymentDate);
        //balance
        VPackSlice balanceSlice = slice.get("balance");
        VPackSlice valuableSlice = balanceSlice.get("valuable");
        MonetaryAmount valuable = toMonetaryAmount(valuableSlice);
        VPackSlice redPacketsSlice = balanceSlice.get("redPackets");
        MonetaryAmount redPackets = this.toMonetaryAmount(redPacketsSlice);
        Balance balance = new Balance(valuable, redPackets);
        //smallChange
        VPackSlice smallChangeSlice = slice.get("smallChange");
        SmallChangDenominationEnum smallChangDenominationEnum = SmallChangDenominationEnum.valueOf(smallChangeSlice.get("smallChangDenominationEnum").getAsString());
        VPackSlice smallChangeAmountSlice = smallChangeSlice.get("amount");
        MonetaryAmount amount = this.toMonetaryAmount(smallChangeAmountSlice);
        SmallChange smallChange = new SmallChange(amount, smallChangDenominationEnum);

        CreditCard creditCard = new CreditCard(issuer, customerId, id, "", cardFaceNumber, freeze, lineOfCredit, balance, smallChange, null);
        passwordField.set(creditCard, password);
        return creditCard;
    }

    private MonetaryAmount toMonetaryAmount(VPackSlice slice) {
        String currencyCode = slice.get("currency").get("baseCurrency").get("currencyCode").getAsString();
        MonetaryAmount amount = FastMoney.zero(Monetary.getCurrency(currencyCode));
        String className = slice.get("_class").getAsString();
        if (FastMoney.class.getName().equals(className)) {
            amount = FastMoney.of(slice.get("number").getAsNumber().doubleValue() / 100000, currencyCode);
        } else if (Money.class.getName().equals(className)) {
            amount = Money.of(slice.get("number").getAsBigDecimal(), currencyCode);
        }
        return amount;
    }

    @Override
    public CreditCard[] findByCardFaceNumber(String cardFaceNumber) {
        final String query = "WITH person,credit_card,appearance\n" +
                "FOR c IN credit_card FILTER c.cardFaceNumber =~ @cardFaceNumber\n" +
                "FOR p in 1..1 INBOUND c._id has\n" +
                //"FOR appearance IN 1..1 OUTBOUND c._id has\n" +
                "RETURN {'issuer':c.issuer,'customerId':p._key,'id':c._key,'password':c.password,'cardFaceNumber':c.cardFaceNumber,'freeze':c.freeze,'termOfValidity':c.termOfValidity," +
                "'lineOfCredit':c.lineOfCredit,'balance':c.balance,'smallChange':c.smallChange}";
        final Map<String, Object> bindVars = new MapBuilder().put("cardFaceNumber", cardFaceNumber).get();
        ArangoCursor<VPackSlice> slices = crm.query(query, bindVars, null, VPackSlice.class);
        return transform(slices);
    }

    @Override
    public CreditCard[] findByCustomer(String customerId) {
        List<CreditCard> creditCardList = new ArrayList<>();
        final String query = "WITH person,credit_card,appearance\n" +
                "FOR p IN person FILTER p._key == @key\n" +
                "FOR c IN 1..1 OUTBOUND p._id has\n" +
                //"FOR appearance IN 1..1 OUTBOUND a._id has\n" +
                "RETURN {'issuer':c.issuer,'customerId':p._key,'id':c._key,'password':c.password,'cardFaceNumber':c.cardFaceNumber,'freeze':c.freeze,'termOfValidity':c.termOfValidity," +
                "'lineOfCredit':c.lineOfCredit,'balance':c.balance,'smallChange':c.smallChange}";
        final Map<String, Object> bindVars = new MapBuilder().put("key", customerId).get();
        ArangoCursor<VPackSlice> slices = crm.query(query, bindVars, null, VPackSlice.class);
        return transform(slices);
    }

    @Override
    public CreditCard[] findAll(int offset, int limit) {
        CreditCard[] creditCards = ArangoDBUtil.calculationCollectionSize(crm, CreditCard.class, offset, limit);
        final String query = "WITH person,credit_card,appearance\n" +
                "FOR c IN credit_card LIMIT @offset,@limit\n" +
                "FOR p in 1..1 INBOUND c._id has\n" +
                //"FOR appearance IN 1..1 OUTBOUND a._id has\n" +
                "RETURN {'issuer':c.issuer,'customerId':p._key,'id':c._key,'password':c.password,'cardFaceNumber':c.cardFaceNumber,'freeze':c.freeze,'termOfValidity':c.termOfValidity," +
                "'lineOfCredit':c.lineOfCredit,'balance':c.balance,'smallChange':c.smallChange}";
        final Map<String, Object> bindVars = new MapBuilder().put("offset", offset).put("limit", limit).get();
        ArangoCursor<VPackSlice> slices = crm.query(query, bindVars, null, VPackSlice.class);
        try {
            for (int i = 0; slices.hasNext(); i++)
                creditCards[i] = rebuild(slices.next());
        } catch (IllegalAccessException e) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Can't rebuild credit card", e);
        }
        return creditCards;
    }

    private CreditCard[] transform(ArangoCursor<VPackSlice> slices) {
        List<CreditCard> creditCardList = new ArrayList<>();
        while (slices.hasNext()) {
            try {
                creditCardList.add(rebuild(slices.next()));
            } catch (IllegalAccessException e) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Can't rebuild credit card.", e);
            }
        }
        return creditCardList.toArray(new CreditCard[creditCardList.size()]);
    }

    @Override
    public int size() {
        final String query = " RETURN LENGTH(credit_card)";
        final ArangoCursor<VPackSlice> cursor = crm.query(query, null, null, VPackSlice.class);
        for (; cursor.hasNext(); ) {
            return cursor.next().getAsInt();
        }
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
