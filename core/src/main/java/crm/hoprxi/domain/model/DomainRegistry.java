/*
 * @(#}DomainRegistry.java
 *
 * Copyright 2016 www.hoprxi.com rights Reserved.
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
package crm.hoprxi.domain.model;


import event.hoprxi.domain.model.DomainEventPublisher;
import event.hoprxi.infrastruture.simple.SimpleDomainEventPublisher;

/***
 * @author <a href="mailto:myis1000@gmail.com">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 2016-10-24
 */
public class DomainRegistry {
    public static DomainEventPublisher domainEventPublisher() {
        return SimpleDomainEventPublisher.instance();
    }
}
