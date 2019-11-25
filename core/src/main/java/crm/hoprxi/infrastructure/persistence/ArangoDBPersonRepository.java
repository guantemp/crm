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
import crm.hoprxi.domain.model.customer.person.PersonRepository;
import crm.hoprxi.domain.model.customer.person.PostalAddressBook;
import crm.hoprxi.domain.model.customer.person.certificates.IdentityCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.*;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-07-25
 */
public class ArangoDBPersonRepository implements PersonRepository {
    private static final VertexUpdateOptions UPDATE_OPTIONS = new VertexUpdateOptions().keepNull(false);
    private static final Logger LOGGER = LoggerFactory.getLogger(ArangoDBPersonRepository.class);
    private final ArangoDatabase database;

    public ArangoDBPersonRepository(String databaseName) {
        database = ArangoDBUtil.getResource().db(databaseName);
    }

    @Override
    public void save(Customer customer) {
        boolean exists = database.collection("customer").documentExists(customer.id());
        ArangoGraph graph = database.graph("core");
        if (exists) {
            graph.vertexCollection("customer").updateVertex(customer.id(), customer, UPDATE_OPTIONS);
        } else {
            graph.vertexCollection("customer").insertVertex(customer);
        }
    }

    @Override
    public Customer find(String id) {
        ArangoGraph graph = database.graph("core");
        VPackSlice slice = graph.vertexCollection("customer").getVertex(id, VPackSlice.class);
        return rebuild(slice);
    }

    private Customer rebuild(VPackSlice slice) {
        if (slice == null)
            return null;
        String id = slice.get(DocumentField.Type.KEY.getSerializeName()).getAsString();
        String name = slice.get("name").getAsString();
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
        if (!slice.get("idCard").isNull()) {

        }
        MonthDay birthday = null;
        if (!slice.get("birthday").isNull())
            birthday = MonthDay.from(LocalDate.parse(slice.get("birthday").getAsString(), DateTimeFormatter.ISO_DATE_TIME));
        return null;//new Person(id, name, Credit.NO_CREDIT, headPortrait, birthday, SmallChange.ZERO, book, identityCard);
    }

    @Override
    public Customer[] findAll(long offset, int limit) {
        Customer[] customers = ArangoDBUtil.calculationCollectionSize(database, Customer.class, offset, limit);
        if (customers.length == 0)
            return customers;
        final String query = "FOR c IN customer LIMIT @offset,@limit RETURN c";
        final Map<String, Object> bindVars = new MapBuilder().put("offset", offset).put("limit", limit).get();
        final ArangoCursor<VPackSlice> slices = database.query(query, bindVars, null, VPackSlice.class);
        for (int i = 0; slices.hasNext(); i++)
            customers[i] = rebuild(slices.next());
        return customers;
    }

    @Override
    public void remove(String identity) {
        final String remove = "FOR c IN customer FILTER c._id == @identity REMOVE c IN customer";
        final Map<String, Object> bindVars = new MapBuilder().put("identity", "customer/" + identity).get();
        database.query(remove, bindVars, null, VPackSlice.class);
    }
}
