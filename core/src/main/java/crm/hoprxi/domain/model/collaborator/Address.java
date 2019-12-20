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
package crm.hoprxi.domain.model.collaborator;

import java.util.Locale;
import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2019-07-19
 */
public final class Address {
    // such as Chinese
    private Locale locale;
    //such as luzhou
    private String city;
    //such as sichuan
    private String province;
    //such as 614000
    private String zipCode;
    //such as lonmatan,luxiang
    private String county;
    //such as xiaoshi street
    private String street;
    private String details;

    public Address(Locale locale, String province, String city, String county, String street, String details, String zipCode) {
        setLocale(locale);
        setProvince(province);
        setCity(city);
        setCounty(county);
        setStreet(street);
        setDetails(details);
        setZipCode(zipCode);
    }

    public Address(String province, String city, String county, String street, String details, String zipCode) {
        this(Locale.CHINA, province, city, county, street, details, zipCode);
    }

    public static Address chinaAddress(String province, String city, String county, String street, String details, String zipCode) {
        return new Address(Locale.CHINA, province, city, county, street, details, zipCode);
    }

    private void setLocale(Locale locale) {
        if (locale == null)
            locale = Locale.CHINA;
        this.locale = locale;
    }

    private void setProvince(String province) {
        Objects.requireNonNull(province, "province required");
        this.province = province;
    }

    private void setCounty(String county) {
        if (county == null)
            county = "";
        this.county = county;
    }

    private void setStreet(String street) {
        Objects.requireNonNull(street, "street required");
        this.street = street;
    }

    private void setDetails(String details) {
        Objects.requireNonNull(details, "details required");
        this.details = details;
    }

    private void setCity(String city) {
        Objects.requireNonNull(city, "city required");
        this.city = city;
    }

    private void setZipCode(String zipCode) {
        Objects.requireNonNull(zipCode, "zipCode required");
        this.zipCode = zipCode;
    }

    public String city() {
        return city;
    }

    public String zipCode() {
        return zipCode;
    }

    public Locale locale() {
        return locale;
    }

    public String province() {
        return province;
    }

    public String county() {
        return county;
    }

    public String street() {
        return street;
    }

    public String details() {
        return details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (locale != null ? !locale.equals(address.locale) : address.locale != null) return false;
        if (city != null ? !city.equals(address.city) : address.city != null) return false;
        if (province != null ? !province.equals(address.province) : address.province != null) return false;
        if (zipCode != null ? !zipCode.equals(address.zipCode) : address.zipCode != null) return false;
        if (county != null ? !county.equals(address.county) : address.county != null) return false;
        if (street != null ? !street.equals(address.street) : address.street != null) return false;
        return details != null ? details.equals(address.details) : address.details == null;
    }

    @Override
    public int hashCode() {
        int result = locale != null ? locale.hashCode() : 0;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (province != null ? province.hashCode() : 0);
        result = 31 * result + (zipCode != null ? zipCode.hashCode() : 0);
        result = 31 * result + (county != null ? county.hashCode() : 0);
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (details != null ? details.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Address{" +
                "locale=" + locale +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", county='" + county + '\'' +
                ", street='" + street + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
