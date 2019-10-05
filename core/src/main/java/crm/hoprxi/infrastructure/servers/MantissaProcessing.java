/*
 * @(#}MantissaProcessing.java
 *
 * Copyright 2015 www.hoprxi.com rights Reserved.
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
package crm.hoprxi.infrastructure.servers;


import crm.hoprxi.infrastructure.resource.Label;

import java.math.BigDecimal;

/***
 * @author <a href="mailto:myis1000@gmail.com">guan xiangHuan</a>
 * @since JDK7.0
 * @version 0.0.1 2015年12月9日
 */
public enum MantissaProcessing {
    CEILING {
        @Override
        public String toString() {
            return Label.MantissaProcessing_CEILING;
        }

        @Override
        public BigDecimal process(BigDecimal source) {
            return source.setScale(BigDecimal.ROUND_CEILING);
        }
    },
    FLOOR {
        @Override
        public String toString() {
            return Label.MantissaProcessing_FLOOR;
        }

        @Override
        public BigDecimal process(BigDecimal source) {
            return source.setScale(BigDecimal.ROUND_FLOOR);
        }
    },
    RETAIN {
        @Override
        public String toString() {
            return Label.MantissaProcessing_ROUNDING;
        }

        @Override
        public BigDecimal process(BigDecimal source) {
            return source.setScale(0, BigDecimal.ROUND_UNNECESSARY);
        }
    },
    DOWN {
        @Override
        public BigDecimal process(BigDecimal source) {
            return super.process(source);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    },
    ROUNDING {
        @Override
        public String toString() {
            return Label.MantissaProcessing_ROUNDING;
        }

        @Override
        public BigDecimal process(BigDecimal source) {
            return source.setScale(0, BigDecimal.ROUND_HALF_UP);
        }
    };

    /**
     * @param source
     * @return
     */
    public BigDecimal process(BigDecimal source) {
        return source;
    }
}
