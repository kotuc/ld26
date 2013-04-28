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

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.Image;

public class Player extends DynamicPhysicsEntity {

    public static final float RADIUS = 0.5f;
    public static String TYPE = "Player";

    public Player(PeaWorld peaWorld, World world, float x, float y, float angle) {
        super(peaWorld, world, x, y, 2 * RADIUS, 2 * RADIUS, angle);
    }

    @Override
    Body initPhysicsBody(World world, float x, float y, float width, float height, float angle) {
        FixtureDef fixtureDef = new FixtureDef();
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = new Vec2(0, 0);
        Body body = world.createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.m_radius = RADIUS;
        fixtureDef.shape = circleShape;
        fixtureDef.density = 0.4f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0.0f;
        circleShape.m_p.set(0, 0);
        body.createFixture(fixtureDef);
//        body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x, y), angle);
        return body;
    }

    @Override
    public Image getImage() {
        return image;
        // return chrome;
    }

    private static Image image = loadImage("player.png");
    // private static Image chrome = loadImage("chrome.png");
}
