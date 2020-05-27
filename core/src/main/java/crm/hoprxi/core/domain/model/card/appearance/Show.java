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

package crm.hoprxi.core.domain.model.card.appearance;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2019-08-07
 */
public class Show {
    //Applicable to Wechat Membership Card Size
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;
    private URL background;

    /**
     * @param background
     * @throws IllegalArgumentException if background is null
     */
    public Show(URL background) {
        this.background = Objects.requireNonNull(background, "background is required");
    }

    public URL background() {
        return background;
    }

    /**
     * @return
     * @throws BackgroundImageNotFoundException
     */
    public boolean checkBackgroundSize() {
        try {
            BufferedImage image = ImageIO.read(background);
            if (image.getWidth() > WIDTH || image.getHeight() > HEIGHT)
                return false;
        } catch (IOException e) {
            throw new BackgroundImageNotFoundException("Not found background image");
        }
        return true;
    }


    public BufferedImage paint(String content, Style style) {
        return null;
    }
}
