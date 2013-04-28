/**
 * Copyright 2011 The PlayN Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cz.kotu.ld26.core;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.PlayN;
import playn.core.util.Callback;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

public abstract class Entity {
    final ImageLayer layer;
    float x;
    float y;
    private final float width;
    private final float height;
    float angle;

    public Entity(final PeaWorld peaWorld, float px, float py, float pangle) {
           this(peaWorld, px, py, 1, 1, pangle);
    }

    public Entity(final PeaWorld peaWorld, float px, float py, final float width, final float height, float pangle) {
        this.x = px;
        this.y = py;
        this.width = width;
        this.height = height;
        this.angle = pangle;
        layer = graphics().createImageLayer(getImage());
        initPreLoad(peaWorld);
        getImage().addCallback(new Callback<Image>() {
            @Override
            public void onSuccess(Image image) {
                // since the image is loaded, we can use its width and height
                layer.setOrigin(image.width() / 2f, image.height() / 2f);
                layer.setScale(width / image.width(), height / image.height());
                layer.setTranslation(x, y);
                layer.setRotation(angle);
                initPostLoad(peaWorld);
            }

            @Override
            public void onFailure(Throwable cause) {
                PlayN.log().error("Error loading image: " + cause.getMessage());
            }
        });
    }

    /**
     * Perform pre-image load initialization (e.g., attaching to PeaWorld layers).
     *
     * @param peaWorld
     */
    public abstract void initPreLoad(final PeaWorld peaWorld);

    /**
     * Perform post-image load initialization (e.g., attaching to PeaWorld layers).
     *
     * @param peaWorld
     */
    public abstract void initPostLoad(final PeaWorld peaWorld);

    public void paint(float alpha) {
    }

    public void update(float delta) {
    }

    public void setPos(float x, float y) {
        layer.setTranslation(x, y);
    }

    public void setAngle(float a) {
        layer.setRotation(a);
    }

    final float getWidth() {
        return width;
    }

    final float getHeight() {
        return height;
    }

    public abstract Image getImage();

    protected static Image loadImage(String name) {
        return assets().getImage("images/" + name);
    }
}
