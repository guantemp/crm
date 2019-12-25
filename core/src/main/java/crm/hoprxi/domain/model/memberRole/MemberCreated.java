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

package crm.hoprxi.domain.model.memberRole;


import crm.hoprxi.domain.model.power.PowerBook;
import crm.hoprxi.domain.model.spss.Spss;
import event.hoprxi.domain.model.DomainEvent;

import javax.swing.*;
import java.time.LocalDateTime;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-10-20
 */
public final class MemberCreated implements DomainEvent {
    private String identity;
    private String name;
    private Spss standard;
    private PowerBook powerBook;
    private Icon icon;
    private int version;
    private LocalDateTime occurredOn;

    public MemberCreated(String identity, String name, Spss standard, PowerBook powerBook, Icon icon) {
        this.identity = identity;
        this.name = name;
        this.standard = standard;
        this.powerBook = powerBook;
        this.icon = icon;
        this.version = 1;
        this.occurredOn = LocalDateTime.now();
    }

    @Override
    public LocalDateTime occurredOn() {
        return occurredOn;
    }

    @Override
    public int version() {
        return version;
    }
}
