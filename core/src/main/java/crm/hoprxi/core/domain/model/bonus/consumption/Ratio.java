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
package crm.hoprxi.core.domain.model.bonus.consumption;

import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.3 builder 2020-09-07
 */
public class Ratio {
    public static final Ratio ONE_TO_ONE = new Ratio(1, 1, false);
    public static final Ratio ZERO = new Ratio(0, 0, true);
    private int costAmount = 1;
    private int increase = 1;
    private boolean fixed = false;

    /**
     * @param costAmount
     * @param increase
     * @param fixed      if true, no matter how much it costs amount, just return increase
     */
    public Ratio(int costAmount, int increase, boolean fixed) {
        setCostAmount(costAmount);
        setIncrease(increase);
        this.fixed = fixed;
    }

    public Ratio(int costAmount, int increase) {
        this(costAmount, increase, false);
    }


    public static Ratio of(int costAmount, int increase) {
        if (increase == 0 || costAmount == 0)
            return ZERO;
        if (costAmount == 1 && increase == 1)
            return ONE_TO_ONE;
        return new Ratio(costAmount, increase);
    }

    private void setIncrease(int increase) {
        if (increase < 0)
            throw new IllegalArgumentException("increase cannot be negative");
        this.increase = increase;
    }

    private void setCostAmount(int costAmount) {
        if (costAmount < 0)
            throw new IllegalArgumentException("costAmount cannot be negative");
        this.costAmount = costAmount;
    }

    /**
     * @param consumptionAmount
     * @return increase if fixed is true<br/>
     * zero if consumptionAmount less than or equal zero<br/>
     * bonus value after calculation of consumption amount proportion
     */
    public Number calculation(Number consumptionAmount) {
        if (fixed)
            return increase;
        if (consumptionAmount.doubleValue() <= 0.0)
            return 0;
        return consumptionAmount.doubleValue() * increase / costAmount;
    }

    public boolean isFixed() {
        return fixed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ratio)) return false;

        Ratio ratio = (Ratio) o;

        if (costAmount != ratio.costAmount) return false;
        if (increase != ratio.increase) return false;
        return fixed == ratio.fixed;
    }

    @Override
    public int hashCode() {
        int result = costAmount;
        result = 31 * result + increase;
        result = 31 * result + (fixed ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Ratio.class.getSimpleName() + "[", "]")
                .add("costAmount=" + costAmount)
                .add("increase=" + increase)
                .add("fixed=" + fixed)
                .toString();
    }
}
