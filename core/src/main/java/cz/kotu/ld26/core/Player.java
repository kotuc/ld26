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
import playn.core.PlayN;
import playn.core.Sound;

public class Player extends DynamicPhysicsEntity implements PhysicsEntity.HasContactListener {

    public static final float JUMP_SPEED = -20f;
    public static final float MOVE_SPEED = 10f;
    public static final float MOVE_ACC = 5f;

    public static final Sound SOUND_OUCH = PlayN.assets().getSound("sounds/ouch");
    public static final Sound SOUND_JUMP = PlayN.assets().getSound("sounds/jump");

    public static final float RADIUS = 0.49f;
    public static String TYPE = "Player";

    boolean dead = false;

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
    public void update(float delta) {
        super.update(delta);
        if (getBody().getPosition().y>16) {
            PlayN.log().debug("Fallen!");
            SOUND_OUCH.play();
            dead = true;
        }
    }

    @Override
    public void contact(PhysicsEntity other) {
        if (other instanceof Spike) {
            PlayN.log().debug("Ouch!");
            SOUND_OUCH.play();
            dead = true;
        }
    }

    void playerControl(LudumDare26Game.ControlsState controlsState) {
        boolean doStop = !(controlsState.leftPressed || controlsState.rightPressed);

        final Body body = getBody();
        float linearDamping = 0;
        if (doStop) {
            body.setAngularVelocity(0);
//                linearDamping = 10f;
            Vec2 linearVelocity = body.getLinearVelocity();
            linearVelocity.x *= 0.9;
//                body.applyForce(new Vec2(v, 0), body.getPosition());
            body.setLinearVelocity(linearVelocity);
        } else {
            Vec2 linearVelocity = body.getLinearVelocity();
            final int dir = (controlsState.leftPressed ? -1 : 0) + (controlsState.rightPressed ? 1 : 0);
            float v = linearVelocity.x;
            v += dir * MOVE_ACC;
            v = Math.max(-MOVE_SPEED, Math.min(v, MOVE_SPEED));
            linearVelocity.x = v;
//                body.applyForce(new Vec2(v, 0), body.getPosition());
            body.setLinearVelocity(linearVelocity);
//                linearDamping = 1f;
        }
        body.setLinearDamping(linearDamping);
    }


    public void jump() {
        Vec2 linearVelocity = getBody().getLinearVelocity();
        linearVelocity.y = JUMP_SPEED;
        getBody().setLinearVelocity(linearVelocity);

        SOUND_JUMP.play();

    }



    @Override
    public Image getImage() {
        return image;
        // return chrome;
    }

    private static Image image = loadImage("player.png");


}
