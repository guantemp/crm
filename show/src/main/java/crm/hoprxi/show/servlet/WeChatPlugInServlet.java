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

package crm.hoprxi.show.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2020-01-10
 */
public class WeChatPlugInServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contex = req.getQueryString();
        String[] arrays = contex.split("&");
        String signature = arrays[0].split("=")[1];
        String echostr = arrays[1].split("=")[1];
        String timestamp = arrays[2].split("=")[1];
        String nonce = arrays[3].split("=")[1];
        String token = "guantemp";

        String[] dictionary = new String[]{token, nonce, timestamp};
        Arrays.sort(dictionary, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        List<String> check = new ArrayList<>();
        check.add(token);
        check.add(nonce);
        check.add(timestamp);
        Collections.sort(check);

        try {
            String checkSignature = getSha1((check.get(0) + check.get(1) + check.get(2)).getBytes(StandardCharsets.UTF_8));
            System.out.println(signature);
            System.out.println(checkSignature);
            if (signature.equals(checkSignature)) {
                OutputStream os = resp.getOutputStream();
                os.write(echostr.getBytes(StandardCharsets.UTF_8));
                os.flush();
                os.close();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    private String getSha1(byte[] input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
