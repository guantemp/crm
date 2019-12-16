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

package crm.hoprxi.domain.model.power;

import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-10-20
 */
public class Power {
    private String name;
    private Command command;

    public Power(String name, Command command) {
        setName(name);
        setCommand(command);
    }

    public void execute() {
        command.execute();
    }

    public String name() {
        return name;
    }

    private void setName(String name) {
        this.name = Objects.requireNonNull(name, "name required");
    }

    private void setCommand(Command command) {
        this.command = Objects.requireNonNull(command, "command require");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Power power = (Power) o;

        if (name != null ? !name.equals(power.name) : power.name != null) return false;
        return command != null ? command.equals(power.command) : power.command == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (command != null ? command.hashCode() : 0);
        return result;
    }
}
