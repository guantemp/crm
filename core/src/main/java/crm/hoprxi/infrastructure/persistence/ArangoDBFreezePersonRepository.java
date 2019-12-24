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
import com.arangodb.entity.DocumentField;
import com.arangodb.model.VertexUpdateOptions;
import com.arangodb.util.MapBuilder;
import com.arangodb.velocypack.VPackSlice;
import crm.hoprxi.domain.model.collaborator.Address;
import crm.hoprxi.domain.model.collaborator.Contact;
import crm.hoprxi.domain.model.customer.Customer;
import crm.hoprxi.domain.model.customer.PostalAddress;
import crm.hoprxi.domain.model.customer.person.FreezePerson;
import crm.hoprxi.domain.model.customer.person.FreezePersonRepository;
import crm.hoprxi.domain.model.customer.person.PostalAddressBook;
import crm.hoprxi.domain.model.customer.person.certificates.IdentityCard;
import crm.hoprxi.domain.model.spss.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.time.MonthDay;
import java.util.*;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2019-12-17
 */
public class ArangoDBFreezePersonRepository implements FreezePersonRepository {
    private static final VertexUpdateOptions UPDATE_OPTIONS = new VertexUpdateOptions().keepNull(false);
    private static final Logger LOGGER = LoggerFactory.getLogger(ArangoDBFreezePersonRepository.class);
    private static Field transactionPasswordField;
    private static Constructor<FreezePerson> frozenPersonConstructor;

    static {
        try {
            frozenPersonConstructor = FreezePerson.class.getDeclaredConstructor(String.class, String.class, Data.class, URI.class,
                    PostalAddressBook.class, IdentityCard.class, MonthDay.class);
            frozenPersonConstructor.setAccessible(true);
            transactionPasswordField = Customer.class.getDeclaredField("transactionPassword");
            transactionPasswordField.setAccessible(true);
        } catch (NoSuchFieldException | NoSuchMethodException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("The FrozenPerson class cannot find such a field or constructor", e);
            }
        }
    }

    private final ArangoDatabase crm;

    public ArangoDBFreezePersonRepository(String databaseName) {
        crm = ArangoDBUtil.getResource().db(databaseName);
    }

    private boolean isExists(String id) {
        return crm.collection("frozen_person").documentExists(id);
    }

    @Override
    public void save(FreezePerson freezePerson) {
        boolean exists = isExists(freezePerson.id());
        ArangoGraph graph = crm.graph("core");
        if (exists) {
            graph.vertexCollection("frozen_person").updateVertex(freezePerson.id(), freezePerson, UPDATE_OPTIONS);
        } else {
            graph.vertexCollection("frozen_person").insertVertex(freezePerson);
        }
    }

    @Override
    public FreezePerson findBy(String id) {
        ArangoGraph graph = crm.graph("core");
        VPackSlice slice = graph.vertexCollection("frozen_person").getVertex(id, VPackSlice.class);
        try {
            return rebuild(slice);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Can't rebuild FrozenPerson", e);
        }
        return null;
    }

    private FreezePerson rebuild(VPackSlice slice) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (slice == null)
            return null;
        String id = slice.get(DocumentField.Type.KEY.getSerializeName()).getAsString();
        String name = slice.get("name").getAsString();
        String transactionPassword = slice.get("transactionPassword").getAsString();
        Data data = Data.EMPTY_DATA;
        if (!slice.get("data").isNone()) {

        }
        URI headPortrait = null;
        if (!slice.get("headPortrait").isNone())
            headPortrait = URI.create(slice.get("headPortrait").get("string").getAsString());

        PostalAddressBook book = null;
        if (!slice.get("postalAddressBook").isNone()) {
            VPackSlice bookSlice = slice.get("postalAddressBook");
            int acquiescence = bookSlice.get("acquiescence").getAsInt();
            Iterator<VPackSlice> iter = bookSlice.get("postalAddresses").arrayIterator();
            List<PostalAddress> list = new ArrayList<>();
            while (iter.hasNext()) {
                VPackSlice postalAddress = iter.next();
                VPackSlice addressSlice = postalAddress.get("address");
                Address address = new Address(Locale.getDefault(), addressSlice.get("province").getAsString(), addressSlice.get("city").getAsString(), addressSlice.get("county").getAsString(),
                        addressSlice.get("street").getAsString(), addressSlice.get("details").getAsString(), addressSlice.get("zipCode").getAsString());
                VPackSlice contactSlice = postalAddress.get("contact");
                String mobilePhone = null;
                String telephone = null;
                if (!contactSlice.get("mobilePhone").isNone() && !contactSlice.get("mobilePhone").isNull())
                    mobilePhone = contactSlice.get("mobilePhone").getAsString();
                if (!contactSlice.get("telephone").isNone() && !contactSlice.get("telephone").isNull())
                    telephone = contactSlice.get("telephone").getAsString();
                Contact contact = new Contact(contactSlice.get("fullName").getAsString(), mobilePhone, telephone);
                list.add(new PostalAddress(address, contact));
            }
            book = new PostalAddressBook(list.toArray(new PostalAddress[list.size()]), acquiescence);
        }

        IdentityCard identityCard = null;
        if (!slice.get("identityCard").isNone()) {
            VPackSlice identityCardSlice = slice.get("identityCard");
            String number = identityCardSlice.get("number").getAsString();
            String identityName = identityCardSlice.get("name").getAsString();
            VPackSlice addressSlice = identityCardSlice.get("address");
            crm.hoprxi.domain.model.customer.person.certificates.Address address = new crm.hoprxi.domain.model.customer.person.certificates.Address(addressSlice.get("province").getAsString(),
                    addressSlice.get("city").getAsString(), addressSlice.get("county").getAsString(), addressSlice.get("details").getAsString());
            identityCard = new IdentityCard(number, identityName, address);
        }

        MonthDay birthday = null;
        if (!slice.get("birthday").isNull()) {
            VPackSlice birthdaySlice = slice.get("birthday");
            birthday = MonthDay.of(birthdaySlice.get("month").getAsInt(), birthdaySlice.get("day").getAsInt());
        }
        FreezePerson freezePerson = frozenPersonConstructor.newInstance(id, name, data, headPortrait, book, identityCard, birthday);
        transactionPasswordField.set(freezePerson, transactionPassword);
        return freezePerson;
    }

    @Override
    public void remove(String id) {
        boolean exists = isExists(id);
        if (exists) {
            final String remove = "FOR f IN frozen_person FILTER f._key == @identity REMOVE f IN frozen_person";
            final Map<String, Object> bindVars = new MapBuilder().put("identity", id).get();
            crm.query(remove, bindVars, null, VPackSlice.class);
        }
    }

    @Override
    public FreezePerson[] findAll(int offset, int limit) {
        FreezePerson[] frozenPeople = ArangoDBUtil.calculationCollectionSize(crm, FreezePerson.class, offset, limit);
        if (frozenPeople.length == 0)
            return frozenPeople;
        final String query = "FOR f IN frozen_person LIMIT @offset,@limit RETURN f\"";
        final Map<String, Object> bindVars = new MapBuilder().put("offset", offset).put("limit", limit).get();
        final ArangoCursor<VPackSlice> slices = crm.query(query, bindVars, null, VPackSlice.class);
        try {
            for (int i = 0; slices.hasNext(); i++)
                frozenPeople[i] = rebuild(slices.next());
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Can't rebuild FrozenPerson", e);
        }
        return frozenPeople;
    }
}
