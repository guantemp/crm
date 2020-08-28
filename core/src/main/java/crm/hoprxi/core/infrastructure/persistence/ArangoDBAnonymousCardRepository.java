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
import com.arangodb.entity.DocumentEntity;
import com.arangodb.entity.DocumentField;
import com.arangodb.entity.VertexEntity;
import com.arangodb.entity.VertexUpdateEntity;
import com.arangodb.model.VertexUpdateOptions;
import com.arangodb.util.MapBuilder;
import com.arangodb.velocypack.VPackSlice;
import crm.hoprxi.core.domain.model.balance.Balance;
import crm.hoprxi.core.domain.model.balance.SmallChangDenominationEnum;
import crm.hoprxi.core.domain.model.balance.SmallChange;
import crm.hoprxi.core.domain.model.bonus.Bonus;
import crm.hoprxi.core.domain.model.card.AnonymousCard;
import crm.hoprxi.core.domain.model.card.AnonymousCardRepository;
import crm.hoprxi.core.domain.model.card.ValidityPeriod;
import crm.hoprxi.core.domain.model.card.appearance.Appearance;
import crm.hoprxi.core.domain.model.collaborator.Issuer;
import org.javamoney.moneta.FastMoney;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2019-12-05
 */
public class ArangoDBAnonymousCardRepository implements AnonymousCardRepository {
    private static final VertexUpdateOptions UPDATE_OPTIONS = new VertexUpdateOptions().keepNull(false);
    private static final Logger LOGGER = LoggerFactory.getLogger(ArangoDBAnonymousCardRepository.class);

    private final ArangoDatabase crm;

    public ArangoDBAnonymousCardRepository(String databaseName) {
        crm = ArangoDBUtil.getResource().db(databaseName);
    }

    @Override
    public void save(AnonymousCard anonymousCard) {
        boolean exists = crm.collection("anonymous_card").documentExists(anonymousCard.id());
        ArangoGraph graph = crm.graph("core");
        if (exists) {
            VertexUpdateEntity vertex = graph.vertexCollection("anonymous_card").updateVertex(anonymousCard.id(), anonymousCard, UPDATE_OPTIONS);
            insertHasEdgeOfAppearance(graph, vertex, anonymousCard.appearance());
        } else {
            VertexEntity vertex = graph.vertexCollection("anonymous_card").insertVertex(anonymousCard);
            insertHasEdgeOfAppearance(graph, vertex, anonymousCard.appearance());
        }
    }

    private void insertHasEdgeOfAppearance(ArangoGraph graph, DocumentEntity cardVertex, Appearance appearance) {
        if (appearance == null)
            return;
        final String query = "WITH appearance\n" +
                "FOR a IN appearance FILTER a.issuer.issuerId == @issuerId and a.name == @name RETURN a";
        final Map<String, Object> bindVars = new MapBuilder().put("issuerId", appearance.issuer().id()).put("name", appearance.name()).get();
        ArangoCursor<VertexEntity> slices = crm.query(query, bindVars, null, VertexEntity.class);
        if (slices.hasNext()) {
            VertexUpdateEntity appear = graph.vertexCollection("appearance").updateVertex(slices.next().getId(), appearance);
            graph.edgeCollection("has").insertEdge(new HasEdge(cardVertex.getId(), appear.getId()));
        } else {
            VertexEntity appear = graph.vertexCollection("appearance").insertVertex(appearance);
            graph.edgeCollection("has").insertEdge(new HasEdge(cardVertex.getId(), appear.getId()));
        }
    }

    @Override
    public int size() {
        final String query = " RETURN LENGTH(anonymous_card)";
        final ArangoCursor<VPackSlice> cursor = crm.query(query, null, null, VPackSlice.class);
        for (; cursor.hasNext(); ) {
            return cursor.next().getAsInt();
        }
        return 0;
    }

    @Override
    public AnonymousCard[] findAll(int offset, int limit) {
        AnonymousCard[] anonymousCards = ArangoDBUtil.calculationCollectionSize(crm, AnonymousCard.class, offset, limit);
        final String query = "WITH anonymous_card,appearance\n" +
                "FOR c IN anonymous_card LIMIT @offset,@limit \n" +
                //"FOR appearance IN 1..1 OUTBOUND a._id has\n" +
                "RETURN {'id':c._key,'issuer':c.issuer,'cardFaceNumber':c.cardFaceNumber,'termOfValidity':c.termOfValidity,'balance':c.balance,'smallChange':c.smallChange,'bonus':c.bonus}";
        final Map<String, Object> bindVars = new MapBuilder().put("offset", offset).put("limit", limit).get();
        ArangoCursor<VPackSlice> slices = crm.query(query, bindVars, null, VPackSlice.class);
        for (int i = 0; slices.hasNext(); i++)
            anonymousCards[i] = rebuild(slices.next());
        return anonymousCards;
    }

    @Override
    public AnonymousCard[] findByCardFaceNumber(String cardFaceNumber) {
        List<AnonymousCard> anonymousCardList = new ArrayList<>();
        final String query = "WITH anonymous_card,appearance\n" +
                "FOR c IN anonymous_card FILTER c.cardFaceNumber == @cardFaceNumber \n" +
                //"FOR appearance IN 1..1 OUTBOUND a._id has\n" +
                "RETURN {'id':c._key,'issuer':c.issuer,'cardFaceNumber':c.cardFaceNumber,'termOfValidity':c.termOfValidity,'balance':c.balance,'smallChange':c.smallChange,'bonus':c.bonus}";
        final Map<String, Object> bindVars = new MapBuilder().put("cardFaceNumber", cardFaceNumber).get();
        ArangoCursor<VPackSlice> slices = crm.query(query, bindVars, null, VPackSlice.class);
        while (slices.hasNext())
            anonymousCardList.add(rebuild(slices.next()));
        return anonymousCardList.toArray(new AnonymousCard[anonymousCardList.size()]);
    }

    @Override
    public AnonymousCard find(String id) {
        final String query = "WITH anonymous_card,appearance\n" +
                "FOR c IN anonymous_card FILTER c._key == @key \n" +
                //"FOR appearance IN 1..1 OUTBOUND a._id has\n" +
                "RETURN {'id':c._key,'issuer':c.issuer,'cardFaceNumber':c.cardFaceNumber,'termOfValidity':c.termOfValidity,'balance':c.balance,'smallChange':c.smallChange,'bonus':c.bonus}";
        final Map<String, Object> bindVars = new MapBuilder().put("key", id).get();
        ArangoCursor<VPackSlice> slices = crm.query(query, bindVars, null, VPackSlice.class);
        if (slices.hasNext())
            return rebuild(slices.next());
        return null;
    }

    private AnonymousCard rebuild(VPackSlice slice) {
        if (slice == null)
            return null;
        VPackSlice issuerSlice = slice.get("issuer");
        Issuer issuer = new Issuer(issuerSlice.get("id").getAsString(), issuerSlice.get("name").getAsString());
        String id = slice.get("id").getAsString();
        String cardFaceNumber = slice.get("cardFaceNumber").getAsString();
        //termOfValidity
        VPackSlice termOfValiditySlice = slice.get("termOfValidity");
        LocalDate startDate = LocalDate.parse(termOfValiditySlice.get("startDate").getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate expiryDate = LocalDate.parse(termOfValiditySlice.get("expiryDate").getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
        ValidityPeriod validityPeriod = ValidityPeriod.getInstance(startDate, expiryDate);
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
        //bonus
        Bonus bonus = new Bonus(slice.get("bonus").get("value").getAsLong());

        return new AnonymousCard(issuer, id, cardFaceNumber, validityPeriod, balance, smallChange, bonus, null);
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
    public void remove(String id) {
        boolean exists = crm.collection("anonymous_card").documentExists(id);
        if (exists) {
            crm.graph("core").vertexCollection("anonymous_card").deleteVertex(id);
        }
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
