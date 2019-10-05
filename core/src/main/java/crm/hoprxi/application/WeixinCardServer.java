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

package crm.hoprxi.application;


public class WeixinCardServer {
/*
    public void token(String appid, String secret) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weixin.qq.com/cgi-bin/")
                .build();
        WeixinCard weixinCard = retrofit.create(WeixinCard.class);
        Call<Token> call = weixinCard.token("client_credential", appid, secret);
        try {
            Response<Token> response = call.execute();
            System.out.println(response.body());
        } catch (IOException e) {
            e.printStackTrace();
        }
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                Token token = response.body();
                System.out.println(token);
            }

            @Override
            public void onFailure(Call<Token> call, Throwable throwable) {
                System.out.println(throwable.getMessage());
            }
        });
    }
    */
}
