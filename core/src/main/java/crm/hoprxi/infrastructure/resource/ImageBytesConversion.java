/*
 * ImageByteSConversion.java
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
package crm.hoprxi.infrastructure.resource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/***
 * @author <a href="mailto:myis1000@gmail.com">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 2016年12月5日
 */
public final class ImageBytesConversion {
    private static final String IMAGE_FORMAT = "png";

    public static BufferedImage bytesToImage(byte[] bytes) {
        if (null == bytes)
            return null;
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            return ImageIO.read(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] imageToByte(BufferedImage image) {
        if (null == image)
            return new byte[0];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, IMAGE_FORMAT, out);
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
