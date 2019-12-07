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
import crm.hoprxi.domain.model.card.AnonymousCard;
import crm.hoprxi.domain.model.card.AnonymousCardRepository;
import crm.hoprxi.domain.model.card.TermOfValidity;
import crm.hoprxi.domain.model.card.appearance.Appearance;
import crm.hoprxi.domain.model.card.balance.Balance;
import crm.hoprxi.domain.model.card.balance.SmallChangDenominationEnum;
import crm.hoprxi.domain.model.card.balance.SmallChange;
import crm.hoprxi.domain.model.collaborator.Issuer;
import crm.hoprxi.domain.model.integral.Integral;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.money.MonetaryAmount;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        final String query = "WITH appearance\n"
                + "FOR a IN appearance FILTER a.issuer.issuerId == @issuerId and a.name == @name RETURN a";
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
        return new AnonymousCard[0];
    }

    @Override
    public AnonymousCard findByCardFaceNumber(String cardFaceNumber) {
        return null;
    }

    @Override
    public AnonymousCard find(String id) {
        final String query = "WITH anonymous_card,appearance\n" +
                "FOR a IN anonymous_card FILTER a._key == @id \n" +
                "FOR appearance IN 1..1 OUTBOUND a._id has\n" +
                "RETURN {'id':c._key,'issuer':c.issuer,'cardFaceNumber':c.cardFaceNumber,'termOfValidity':c.termOfValidity,'balance':c.balance,'smallChange':c.smallChange,'integral':c.integral}";
        final Map<String, Object> bindVars = new MapBuilder().put("identity", "sku/" + id).get();
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

    private AnonymousCard rebuild(VPackSlice slice) {
        String id = slice.get("id").getAsString();
        VPackSlice issuerSlice = slice.get("issuer");
        Issuer issuer = new Issuer(issuerSlice.get("id").getAsString(), issuerSlice.get("name").getAsString());
        String cardFaceNumber = slice.get("cardFaceNumber").getAsString();
        //termOfValidity
        VPackSlice termOfValiditySlice = slice.get("termOfValidity");
        LocalDate startDate = LocalDate.parse(termOfValiditySlice.get("startDate").getAsString(), DateTimeFormatter.ISO_DATE);
        LocalDate expiryDate = LocalDate.parse(termOfValiditySlice.get("expiryDate").getAsString(), DateTimeFormatter.ISO_DATE);
        TermOfValidity termOfValidity = TermOfValidity.getInstance(startDate, expiryDate);
        //balance
        VPackSlice balanceSlice = slice.get("balance");
        VPackSlice valuableSlice = balanceSlice.get("valuable");
        MonetaryAmount valuable = Money.of(valuableSlice.get("number").getAsBigDecimal(), valuableSlice.get("currency").get("baseCurrency").get("currencyCode").getAsString());
        VPackSlice giveSlice = balanceSlice.get("give");
        MonetaryAmount give = Money.of(giveSlice.get("number").getAsBigDecimal(), giveSlice.get("currency").get("baseCurrency").get("currencyCode").getAsString());
        Balance balance = new Balance(valuable, give);
        //smallChange
        VPackSlice smallChangeSlice = slice.get("smallChange");
        SmallChangDenominationEnum smallChangDenominationEnum = SmallChangDenominationEnum.valueOf(smallChangeSlice.get("smallChangDenominationEnum").getAsString());
        VPackSlice smallChangeAmountSlice = smallChangeSlice.get("amount");
        MonetaryAmount amount = Money.of(smallChangeAmountSlice.get("number").getAsBigDecimal(), smallChangeAmountSlice.get("currency").get("baseCurrency").get("currencyCode").getAsString());
        SmallChange smallChange = new SmallChange(amount, smallChangDenominationEnum);
        //Integral
        Integral integral = new Integral(slice.get("integral").getAsBigDecimal());

        return new AnonymousCard(id, issuer, cardFaceNumber, termOfValidity, balance, smallChange, integral, null);
    }


    @Override
    public void remove(String identity) {

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
