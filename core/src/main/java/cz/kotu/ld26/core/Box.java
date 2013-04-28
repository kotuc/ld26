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

public class Box extends DynamicPhysicsEntity {


    public static String TYPE = "Player";

    public Box(PeaWorld peaWorld, World world, float x, float y, float angle) {
        super(peaWorld, world, x, y, 1, 1, angle);
    }


    @Override
    Body initPhysicsBody(World world, float x, float y, float angle) {
        FixtureDef fixtureDef = new FixtureDef();
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position = new Vec2(0, 0);
        Body body = world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        Vec2[] polygon = new Vec2[3];
        polygon[0] = new Vec2(-getWidth()/2f, -getHeight()/2f);
        polygon[1] = new Vec2(getWidth()/2f, -getHeight()/2f);
        polygon[2] = new Vec2(0.1f, getHeight()/2f);
        polygonShape.set(polygon, polygon.length);
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0.f;
        body.createFixture(fixtureDef);
        body.setTransform(new Vec2(x, y), angle);
        return body;
    }


    @Override
    public Image getImage() {
        return image;
        // return chrome;
    }

    private static Image image = loadImage("spikev.png");
    // private static Image chrome = loadImage("chrome.png");
}
