/*

 * Copyright 2019 www.hoprxi.com rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package crm.hoprxi.domain.model.card.appearance;


import crm.hoprxi.domain.model.collaborator.Issuer;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2019-08-13
 */
public class Appearance {
    private Issuer issuer;
    private Show back;
    private String name;
    private Show positive;

    protected Appearance(Issuer issuer, String name, Show back, Show positive) {
        this.issuer = issuer;
        this.name = name;
        this.back = back;
        this.positive = positive;
    }

    public Issuer issuer() {
        return issuer;
    }

    public Show back() {
        return back;
    }

    public String name() {
        return name;
    }

    public Show positive() {
        return positive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Appearance that = (Appearance) o;

        if (issuer != null ? !issuer.equals(that.issuer) : that.issuer != null) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = issuer != null ? issuer.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
