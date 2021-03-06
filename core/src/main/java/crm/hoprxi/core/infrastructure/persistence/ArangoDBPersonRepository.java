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
import com.arangodb.model.VertexUpdateOptions;
import com.arangodb.util.MapBuilder;
import com.arangodb.velocypack.VPackSlice;
import crm.hoprxi.core.domain.model.collaborator.Address;
import crm.hoprxi.core.domain.model.collaborator.Contact;
import crm.hoprxi.core.domain.model.customer.PostalAddress;
import crm.hoprxi.core.domain.model.customer.person.Person;
import crm.hoprxi.core.domain.model.customer.person.PersonRepository;
import crm.hoprxi.core.domain.model.customer.person.PostalAddressBook;
import crm.hoprxi.core.domain.model.customer.person.certificates.IdentityCard;
import crm.hoprxi.core.domain.model.spss.Spss;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.time.MonthDay;
import java.util.*;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2019-12-19
 */
public class ArangoDBPersonRepository implements PersonRepository {
    private static final VertexUpdateOptions UPDATE_OPTIONS = new VertexUpdateOptions().keepNull(false);
    private static final Logger LOGGER = LoggerFactory.getLogger(ArangoDBPersonRepository.class);
    private static Constructor<Person> personConstructor;
    /*
        static {
            try {
                personConstructor = Person.class.getDeclaredConstructor(String.class, String.class, boolean.class, Spss.class, URI.class,
                        PostalAddressBook.class, IdentityCard.class, MonthDay.class);
                personConstructor.setAccessible(true);
            } catch (NoSuchMethodException e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("The Person class cannot find such a field or constructor", e);
                }
            }
        }
    */
    private final ArangoDatabase crm;

    public ArangoDBPersonRepository(String databaseName) {
        databaseName = Objects.requireNonNull(databaseName, "databaseName required").trim();
        if (!ArangoDBUtil.getResource().db(databaseName).exists())
            LOGGER.error("{} not exists", databaseName);
        crm = ArangoDBUtil.getResource().db(databaseName);
    }

    @Override
    public void save(Person person) {
        boolean exists = crm.collection("person").documentExists(person.id());
        ArangoGraph graph = crm.graph("core");
        if (exists) {
            graph.vertexCollection("person").updateVertex(person.id(), person, UPDATE_OPTIONS);
        } else {
            graph.vertexCollection("person").insertVertex(person);
        }
    }

    @Override
    public Person find(String id) {
        VPackSlice slice = crm.collection("person").getDocument(id, VPackSlice.class);
        //ArangoGraph graph = crm.graph("core");
        //VPackSlice slice = graph.vertexCollection("person").getVertex(id, VPackSlice.class);
        return rebuild(slice);
    }

    private Person rebuild(VPackSlice slice) {
        if (slice == null)
            return null;
        String id = slice.get(DocumentField.Type.KEY.getSerializeName()).getAsString();
        String name = slice.get("name").getAsString();
        boolean freeze = slice.get("freeze").getAsBoolean();
        Spss spss = Spss.EMPTY_SPSS;
        if (!slice.get("spss").isNone()) {

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
            VPackSlice addressSlice = identityCardSlice.get("simplifyAddress");
            crm.hoprxi.core.domain.model.customer.SimplifyAddress simplifyAddress = new crm.hoprxi.core.domain.model.customer.SimplifyAddress(addressSlice.get("province").getAsString(),
                    addressSlice.get("city").getAsString(), addressSlice.get("county").getAsString(), addressSlice.get("details").getAsString());
            identityCard = new IdentityCard(number, identityName, simplifyAddress);
        }

        MonthDay birthday = null;
        if (!slice.get("birthday").isNull()) {
            VPackSlice birthdaySlice = slice.get("birthday");
            birthday = MonthDay.of(birthdaySlice.get("month").getAsInt(), birthdaySlice.get("day").getAsInt());
        }
        Person person = new Person(id, name, freeze, spss, headPortrait, book, identityCard, birthday);
        return person;
    }


    @Override
    public Person[] findAll(long offset, int limit) {
        Person[] people = ArangoDBUtil.calculationCollectionSize(crm, Person.class, offset, limit);
        if (people.length == 0)
            return people;
        final String query = "FOR p IN person LIMIT @offset,@limit RETURN p";
        final Map<String, Object> bindVars = new MapBuilder().put("offset", offset).put("limit", limit).get();
        final ArangoCursor<VPackSlice> slices = crm.query(query, bindVars, null, VPackSlice.class);
            for (int i = 0; slices.hasNext(); i++)
                people[i] = rebuild(slices.next());
        return people;
    }

    @Override
    public void remove(String id) {
        boolean exists = crm.collection("person").documentExists(id);
        if (exists) {
            final String remove = "FOR p IN person FILTER p._key == @identity REMOVE p IN person";
            final Map<String, Object> bindVars = new MapBuilder().put("identity", id).get();
            crm.query(remove, bindVars, null, VPackSlice.class);
        }
    }
}
