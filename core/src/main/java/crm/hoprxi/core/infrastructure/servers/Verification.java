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
package crm.hoprxi.core.infrastructure.servers;

import java.time.LocalDateTime;

/***
 * @author <a href="mailto:myis1000@126.com">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.2 2017年2月8日
 */
public class Verification {
    private static final int EXPIRED_MINUTES = 2 * 24 * 60;
    // Random six digit verification appearance
    private int code;
    private LocalDateTime expired;

    /**
     * 2 days overdue
     */
    public Verification() {
        this(EXPIRED_MINUTES);
    }

    public Verification(int minutesOfExpired) {
        this.code = ((int) ((Math.random() * 9 + 1) * 100000));
        expired = LocalDateTime.now().plusMinutes(minutesOfExpired);
    }

    public static void main(String[] args) {
        Verification verification = new Verification();
        System.out.println(verification);
        System.out.println(verification.validate(verification.code()));
    }

    public int code() {
        return code;
    }

    public LocalDateTime expired() {
        return expired;
    }

    @Override
    public String toString() {
        return "Confirmation [appearance=" + code + ", expired=" + expired + "]";
    }

    public boolean validate(int code) {
        if (this.code == code && LocalDateTime.now().compareTo(expired) <= 0)
            return true;
        return false;
    }
}
