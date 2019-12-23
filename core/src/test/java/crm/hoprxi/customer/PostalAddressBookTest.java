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

package crm.hoprxi.customer;

import crm.hoprxi.domain.model.collaborator.Address;
import crm.hoprxi.domain.model.collaborator.Contact;
import crm.hoprxi.domain.model.customer.PostalAddress;
import crm.hoprxi.domain.model.customer.person.PostalAddressBook;
import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-07-15
 */
public class PostalAddressBookTest {
    private PostalAddress one = new PostalAddress(new Address(Locale.getDefault(), "四川", "泸州", "龙马潭区", "小时接到", "中大街38号", "614000"),
            new Contact("挎包里", "18982466056", null));
    private PostalAddress two = new PostalAddress(new Address(Locale.getDefault(), "四川", "泸州", "龙马潭区", "喝咖啡", "愚公街", "614000"),
            new Contact("格式化", "18982466062", null));
    private PostalAddress three = new PostalAddress(new Address(Locale.getDefault(), "四川", "泸州", "龙马潭区", "度佳节", "惯性源", "614000"),
            new Contact("规格", null, "0830-2518210"));
    private PostalAddress four = new PostalAddress(new Address(Locale.getDefault(), "四川", "泸州", "龙马潭区", "鱼塘街道", "沙湖路22", "614000"),
            new Contact("库电话", "13679692401", null));

    @Test
    public void addAndSetAcquiescence() {
        PostalAddressBook book = new PostalAddressBook();
        book = book.addAndSetAcquiescence(one);
        Assert.assertEquals(book.acquiescencePostalAddress(), one);
        Assert.assertEquals(book.postalAddresses().length, 1);
        book = book.addAndSetAcquiescence(four);
        Assert.assertEquals(book.postalAddresses().length, 2);
        Assert.assertEquals(book.acquiescencePostalAddress(), four);
        book = book.addAndSetAcquiescence(four);
        Assert.assertEquals(book.postalAddresses().length, 2);
        Assert.assertEquals(book.acquiescencePostalAddress(), four);
        book = book.addAndSetAcquiescence(two);
        Assert.assertEquals(book.postalAddresses().length, 3);
        Assert.assertEquals(book.acquiescencePostalAddress(), two);
    }

    @Test
    public void add() {
        PostalAddressBook book = new PostalAddressBook();
        book = book.add(one);
        Assert.assertEquals(book.acquiescencePostalAddress(), one);
        Assert.assertEquals(book.postalAddresses().length, 1);
        book = book.add(two);
        Assert.assertEquals(book.acquiescencePostalAddress(), one);
        Assert.assertEquals(book.postalAddresses().length, 2);
        book = book.add(two);
        Assert.assertEquals(book.acquiescencePostalAddress(), one);
        Assert.assertEquals(book.postalAddresses().length, 2);
        book = book.add(three);
        Assert.assertEquals(book.acquiescencePostalAddress(), one);
        Assert.assertEquals(book.postalAddresses().length, 3);
    }

    @Test
    public void setAcquiescencePostalAddress() {
        PostalAddressBook book = new PostalAddressBook();
        book = book.add(two).add(one).add(three);
        Assert.assertEquals(book.acquiescencePostalAddress(), two);
        book = book.changeAcquiescencePostalAddress(one);
        Assert.assertEquals(book.acquiescencePostalAddress(), one);
        book = book.add(four).changeAcquiescencePostalAddress(four);
        Assert.assertEquals(book.acquiescencePostalAddress(), four);
    }

    @Test
    public void remove() {
        PostalAddressBook book = new PostalAddressBook();
        book = book.remove(four).add(two).add(one).remove(two);
        Assert.assertEquals(book.acquiescencePostalAddress(), one);
        Assert.assertEquals(book.postalAddresses().length, 1);
        book = book.add(two).addAndSetAcquiescence(four);
        Assert.assertEquals(book.acquiescencePostalAddress(), four);
        Assert.assertEquals(book.postalAddresses().length, 3);
        book = book.remove(two);
        Assert.assertEquals(book.acquiescencePostalAddress(), four);
        Assert.assertEquals(book.postalAddresses().length, 2);
        book = book.addAndSetAcquiescence(three).add(two).remove(four);
        Assert.assertEquals(book.acquiescencePostalAddress(), three);
        Assert.assertEquals(book.postalAddresses().length, 3);
    }
}