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
package crm.hoprxi.core.domain.model.customer;

import com.arangodb.entity.DocumentField;
import crm.hoprxi.core.domain.model.DomainRegistry;
import crm.hoprxi.core.domain.model.spss.Spss;

import java.net.URI;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.2 builder 2020-05-26
 */
public abstract class Customer {
    private static final String RESERVED_WORD = "anonymous";
    private static final int NAME_MAX_LENGTH = 255;
    private static final int ID_MAX_LENGTH = 64;
    private static final Pattern CHINA_MOBILE_PHONE_PATTERN = Pattern.compile("^[1](([3][0-9])|([4][5,7,9])|([5][^4,6,9])|([6][6])|([7][3,5,6,7,8])|([8][0-9])|([9][8,9]))[0-9]{8}$");
    private static final Pattern CHINA_TELEPHONE_PATTERN = Pattern.compile("^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\\\.)+[a-z]{2,}$");
    @DocumentField(DocumentField.Type.KEY)
    private String id;
    private String name;
    private URI headPortrait;
    private Spss spss;
    public static final Customer ANONYMOUS = new Customer(RESERVED_WORD, RESERVED_WORD, false, Spss.EMPTY_SPSS, null) {
        @Override
        public void rename(String newName) {
            //do nothing
        }
    };
    private boolean freeze;

    public Customer(String id, String name, boolean freeze, Spss spss, URI headPortrait) {
        setId(id);
        setName(name);
        this.freeze = freeze;
        setSpss(spss);
        this.headPortrait = headPortrait;
    }

    private void setId(String id) {
        id = Objects.requireNonNull(id, "id required").trim();
        //Exclude reserved words:anonymous
        if (!id.equals(RESERVED_WORD))
            //Verify phone or email
            if (id.isEmpty() || id.length() > ID_MAX_LENGTH || !isCompliesIdRule(id))
                throw new IllegalArgumentException("Not a valid cell phone number!");
        this.id = id;
    }

    private boolean isCompliesIdRule(String id) {
        Matcher mobile = CHINA_MOBILE_PHONE_PATTERN.matcher(id);
        Matcher tel = CHINA_TELEPHONE_PATTERN.matcher(id);
        Matcher email = EMAIL_PATTERN.matcher(id);
        if (mobile.matches() || tel.matches() || email.matches())
            return true;
        return false;
    }

    private void setName(String name) {
        name = Objects.requireNonNull(name, "name required").trim();
        if (name.isEmpty() || name.length() > NAME_MAX_LENGTH)
            throw new IllegalArgumentException("name length range is 1-" + NAME_MAX_LENGTH);
        this.name = name;
    }

    private void setSpss(Spss spss) {
        this.spss = Objects.requireNonNull(spss, "data required");
    }

    public void rename(String newName) {
        newName = Objects.requireNonNull(newName, "newName required").trim();
        if (newName.isEmpty() || newName.length() > NAME_MAX_LENGTH)
            throw new IllegalArgumentException("newName length range is 1-" + NAME_MAX_LENGTH);
        if (!name.equals(newName)) {
            name = newName;
            DomainRegistry.domainEventPublisher().publish(new CustomerRenamed(id, newName));
        }
    }

    public String id() {
        return id;
    }

    public URI headPortrait() {
        return headPortrait;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        return id != null ? id.equals(customer.id) : customer.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public Spss data() {
        return spss;
    }

    public String name() {
        return name;
    }

    public boolean isFreeze() {
        return freeze;
    }

    public void freeze() {
        freeze = true;
    }

    public void unfreeze() {
        freeze = false;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Customer.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("name='" + name + "'")
                .add("headPortrait=" + headPortrait)
                .add("spss=" + spss)
                .add("freeze=" + freeze)
                .toString();
    }
}
