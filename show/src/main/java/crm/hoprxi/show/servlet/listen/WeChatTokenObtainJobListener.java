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

package crm.hoprxi.show.servlet.listen;

import okhttp3.OkHttpClient;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2020-01-16
 */
@WebFilter(initParams = {
        @WebInitParam(name = "appID", value = "wxefffc0d452239fda"),
        @WebInitParam(name = "appsecret", value = "608d30fd84ddc2db715630fca90a2590")})
public class WeChatTokenObtainJobListener implements ServletContextListener {
    private static final String url = "https://api.weixin.qq.com/cgi-bin/token?";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String appID = servletContextEvent.getServletContext().getInitParameter("appID");
        String appsecret = servletContextEvent.getServletContext().getInitParameter("appsecret");
        OkHttpClient okHttpClient = new OkHttpClient();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
