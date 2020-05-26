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

package crm.hoprxi.domain.model.customer.person.certificates;

import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-12-20
 */
public class SimplifyAddress {
    //such as luzhou
    private String city;
    //such as sichuan
    private String province;
    //such as lonmatan,luxiang
    private String county;
    private String details;

    public SimplifyAddress(String province, String city, String county, String details) {
        this.city = city;
        this.province = province;
        this.county = county;
        this.details = details;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SimplifyAddress.class.getSimpleName() + "[", "]")
                .add("city='" + city + "'")
                .add("province='" + province + "'")
                .add("county='" + county + "'")
                .add("details='" + details + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimplifyAddress simplifyAddress = (SimplifyAddress) o;

        if (city != null ? !city.equals(simplifyAddress.city) : simplifyAddress.city != null) return false;
        if (province != null ? !province.equals(simplifyAddress.province) : simplifyAddress.province != null)
            return false;
        if (county != null ? !county.equals(simplifyAddress.county) : simplifyAddress.county != null) return false;
        return details != null ? details.equals(simplifyAddress.details) : simplifyAddress.details == null;
    }

    @Override
    public int hashCode() {
        int result = city != null ? city.hashCode() : 0;
        result = 31 * result + (province != null ? province.hashCode() : 0);
        result = 31 * result + (county != null ? county.hashCode() : 0);
        result = 31 * result + (details != null ? details.hashCode() : 0);
        return result;
    }
}
