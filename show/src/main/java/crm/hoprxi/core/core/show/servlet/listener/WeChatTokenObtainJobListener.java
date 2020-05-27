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

package crm.hoprxi.core.core.show.servlet.listener;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2020-01-16
 */
@WebListener
public class WeChatTokenObtainJobListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String url = servletContextEvent.getServletContext().getInitParameter("url");
        String appid = "&appid=" + servletContextEvent.getServletContext().getInitParameter("appid");
        String secret = "&secret=" + servletContextEvent.getServletContext().getInitParameter("secret");
        url = url + appid + secret;
        tokenSchedule(servletContextEvent, url);
    }

    private void tokenSchedule(ServletContextEvent servletContextEvent, String url) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                JsonFactory jasonFactory = new JsonFactory();
                JsonParser parser = jasonFactory.createParser(response.body().byteStream());
                while (parser.nextToken() != JsonToken.END_OBJECT) {
                    String fieldname = parser.getCurrentName();
                    if ("access_token".equals(fieldname)) {
                        parser.nextToken();
                        servletContextEvent.getServletContext().setAttribute("token", parser.getValueAsString());
                        System.out.println(servletContextEvent.getServletContext().getAttribute("token"));
                    }
                    if ("expires_in".equals(fieldname)) {
                        parser.nextToken();
                        servletContextEvent.getServletContext().setAttribute("expires_in", parser.getValueAsString());
                    }
                    if ("errcode".equals(fieldname)) {
                        parser.nextToken();
                        servletContextEvent.getServletContext().setAttribute("errcode", parser.getValueAsString());
                    }
                    if ("ererrmsg".equals(fieldname)) {
                        parser.nextToken();
                        servletContextEvent.getServletContext().setAttribute("errmsg", parser.getValueAsString());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 3, 3600, TimeUnit.SECONDS);
    }


    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        servletContextEvent.getServletContext().removeAttribute("token");
        servletContextEvent.getServletContext().removeAttribute("expires_in");
    }
}
