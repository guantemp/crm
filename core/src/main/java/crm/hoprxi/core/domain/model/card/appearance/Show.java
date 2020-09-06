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
    //微信会员卡自定义背景设计规范 ,像素大小控制在 1000像素*600像素以下
    //支付宝 background 图片规范：2M 以内，格式：bmp，png，jpeg，jpg，gif；尺寸不小于 1020px* 643px 的等边矩形；图片不得有圆角，不得拉伸变形；
    //取最大值，即支付宝会员卡背景图片规范
    private static final int WIDTH = 1020;
    private static final int HEIGHT = 643;
    private URL background;
    private Style style;

    /**
     * @param background
     * @throws IllegalArgumentException if background is null
     */
    public Show(URL background) {
        Objects.requireNonNull(background, "background is required");
        if (!checkBackgroundSpecification())
            throw new IllegalArgumentException("");
        this.background = background;
    }

    public URL background() {
        return background;
    }

    /**
     * @return
     * @throws BackgroundImageNotFoundException
     */
    private boolean checkBackgroundSpecification() {
        try {
            BufferedImage image = ImageIO.read(background);

            if (image.getWidth() < WIDTH || image.getHeight() < HEIGHT)
                return false;
        } catch (IOException e) {
            throw new BackgroundImageNotFoundException("Not found background image" + background.toString());
        }
        return true;
    }

    public BufferedImage paint(int x, int y, String content) {
        return style.paint(content);
    }
}
