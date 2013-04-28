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

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.Image;
import playn.core.PlayN;
import playn.core.util.Callback;

import static playn.core.PlayN.graphics;

public class Box extends DynamicPhysicsEntity {


    public static String TYPE = "Player";

    float fallTime = Float.MAX_VALUE;

    public Box(PeaWorld peaWorld, World world, float x, float y, float angle) {
        super(peaWorld, world, x, y, 1, 1, angle);
    }

    protected void initLayer(final PeaWorld peaWorld, final float width, final float height) {
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

    @Override
    Body initPhysicsBody(World world, float x, float y, float width, float height, float angle) {
        FixtureDef fixtureDef = new FixtureDef();
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position = new Vec2(0, 0);
        Body body = world.createBody(bodyDef);

        PolygonShape polygonShape = getTriangleShape(getWidth(), getHeight());
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0.f;
        body.createFixture(fixtureDef);
        body.setTransform(new Vec2(x, y), angle);
        return body;
    }

    private static PolygonShape getTriangleShape(float w, float h) {
        PolygonShape polygonShape = new PolygonShape();
        Vec2[] polygon = new Vec2[3];
        polygon[0] = new Vec2(-w/2f, -h/2f);
        polygon[1] = new Vec2(w/2f, -h/2f);
        polygon[2] = new Vec2(0.1f, h/2f);
        polygonShape.set(polygon, polygon.length);
        return polygonShape;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        fallTime -= delta;


        getBody().setType((fallTime < 0)?BodyType.DYNAMIC:BodyType.STATIC);

    }

    @Override
    public Image getImage() {
        return image;
        // return chrome;
    }

    private static Image image = loadImage("spikev.png");
    // private static Image chrome = loadImage("chrome.png");


    public void setFallTime(float fallTime) {
        this.fallTime = fallTime;
    }
}


