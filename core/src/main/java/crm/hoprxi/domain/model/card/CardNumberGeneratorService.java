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

package crm.hoprxi.domain.model.card;

import mi.hoprxi.to.StringToNumber;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-05-29
 */
public class CardNumberGeneratorService {
    private int[] filter;
    private Format format;
    private boolean global = false;
    private String initialValue;
    private String prefix;
    private int stepping;
    private String suffix;

    /**
     * @param builder
     */
    private CardNumberGeneratorService(Builder builder) {
        global = builder.global;
        filter = builder.filter;
        format = builder.format;
        initialValue = builder.initialValue;
        prefix = builder.prefix;
        stepping = builder.stepping;
        suffix = builder.suffix;
    }

    /**
     * @param amount
     * @return amount of formatted barcode
     */
    public String[] generator(int amount) {
        if (format.isCorrectInitialValue(initialValue))
            return format.generator(initialValue, prefix, suffix, stepping,
                    amount, filter, global);
        return new String[0];
    }

    public enum Format {
        EAN_13 {
            private final Pattern PATTERN = Pattern.compile("\\d{12}");

            @Override
            public String[] generator(String initiaValue, String prefix,
                                      String suffix, int stepping, int amount, int[] filter,
                                      boolean global) {
                List<String> result = new ArrayList<String>();
                long j = StringToNumber.longOf(initiaValue);
                while (result.size() < amount) {
                    StringBuilder sb = new StringBuilder().append(j);
                    int code = computeCheckSum(sb);
                    sb.append(code);
                    // Meet the filter criteria
                    if (filter(sb, filter, global)) {
                        StringBuilder tmp = new StringBuilder(prefix);
                        tmp.append(sb);
                        tmp.append(suffix);
                        result.add(tmp.toString().trim());
                    }
                    j += stepping;
                }
                return result.toArray(new String[0]);
            }

            @Override
            public boolean isCorrectInitialValue(CharSequence s) {
                Matcher matcher = PATTERN.matcher(s);
                return matcher.matches();
            }
        },
        EAN_8 {
            private final Pattern PATTERN = Pattern.compile("2\\d{6}");

            @Override
            public String[] generator(String initiaValue, String prefix,
                                      String suffix, int stepping, int amount, int[] filter,
                                      boolean global) {
                List<String> result = new ArrayList<String>();
                long j = StringToNumber.longOf(initiaValue);
                while (result.size() < amount) {
                    StringBuilder sb = new StringBuilder("00000").append(j);
                    int code = computeCheckSum(sb);
                    sb.delete(0, 5).append(code);
                    // Meet the filter criteria
                    if (filter(sb, filter, global)) {
                        StringBuilder tmp = new StringBuilder(prefix);
                        tmp.append(sb);
                        tmp.append(suffix);
                        result.add(tmp.toString().trim());
                    }
                    j += stepping;
                }
                return result.toArray(new String[0]);
            }

            @Override
            public boolean isCorrectInitialValue(CharSequence s) {
                Matcher matcher = PATTERN.matcher(s);
                return matcher.matches();
            }
        };

        /**
         * @param barcode
         * @return
         */
        private static int computeCheckSum(CharSequence barcode) {
            int sum = 0;
            for (int i = barcode.length() - 1; i >= 0; i--) {
                if (i % 2 == 0)
                    sum += (barcode.charAt(i) - '0');// i=0,2,4,6....
                else
                    sum += 3 * (barcode.charAt(i) - '0');// i=1,3,5,7...
            }
            return (10 - sum % 10) % 10;
        }

        /**
         * @param c
         * @param i
         * @return
         */
        private static boolean check(char c, int i) {
            if (i == 0) {
                return c - '0' != 0;
            } else if (i == 1) {// filter 1
                return c - '0' != 1;
            } else {// filter other such as:2,3,4,5,6,7,8,9
                int divisor = c - '0';
                return divisor == 0 || divisor % i != 0;
            }
        }

        /**
         * @param s
         * @return
         */
        public static boolean filter(CharSequence s, int[] filter,
                                     boolean global) {
            if (global) {// Filtering char each character
                for (int i : filter) {
                    for (int j = s.length() - 1; j >= 0; j--) {
                        if (!check(s.charAt(j), i))
                            return false;
                    }
                }
            } else {// Filter char in mantissa
                for (int i : filter) {
                    if (!check(s.charAt(s.length() - 1), i))
                        return false;
                }
            }
            return true;
        }

        /**
         * @param initiaValue
         * @param prefix
         * @param suffix
         * @param stepping
         * @param amount
         * @param filter
         * @param global
         * @return
         */
        public abstract String[] generator(String initiaValue, String prefix,
                                           String suffix, int stepping, int amount, int[] filter,
                                           boolean global);

        public abstract boolean isCorrectInitialValue(CharSequence s);
    }

    /**
     * Constructor pattern
     */
    public static class Builder implements
            mi.hoprxi.util.Builder<CardNumberGeneratorService> {
        private int[] filter = new int[0];
        private Format format = Format.EAN_13;
        private boolean global = false;
        private String initialValue;
        private String prefix = "";
        private int stepping = 1;
        private String suffix = "";

        public Builder(String initialValue, Format format) {
            this.initialValue = initialValue;
            this.format = format;
        }

        public CardNumberGeneratorService build() {
            return new CardNumberGeneratorService(this);
        }

        /**
         * The end number does not contain
         *
         * @param filter
         * @return
         */
        public Builder filter(int[] filter) {
            return filter(filter, false);
        }

        /**
         * @param filter
         * @param global if true not contain any number in the filter,if false only the end number does not contain
         * @return
         */
        public Builder filter(int[] filter, boolean global) {
            this.filter = filter;
            this.global = global;
            return this;
        }

        public Builder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder stepping(int stepping) {
            this.stepping = stepping;
            return this;
        }

        public Builder suffix(String suffix) {
            this.suffix = suffix;
            return this;
        }
    }
}

