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

import java.awt.*;
import java.awt.image.BufferedImage;

/***
 * @author <a href="www.hoprxi.com/authors/guan xianghuang">guan xiangHuan</a>
 * @since JDK8.0
 * @version 0.0.1 builder 2018-06-11
 */
public class BarcodeStyle extends Style {
    private boolean readability = true;
    private double wideRatio;
    private Font font;

    public BarcodeStyle(int width, int height, double wideRatio, Font font, boolean readability) {
        super(width, height);
        this.readability = readability;
        this.wideRatio = wideRatio;
        this.font = font;
    }

    public boolean isReadability() {
        return readability;
    }

    @Override
    public BufferedImage paint(String content) {
        return null;
    }

    public double ratio() {
        return wideRatio;
    }

    public Font font() {
        return font;
    }
}
