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

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import playn.core.PlayN;
import playn.core.Sound;

public abstract class DynamicPhysicsEntity extends Entity implements PhysicsEntity {
    public static final Sound SOUND_FALL = PlayN.assets().getSound("sounds/fall");
    float fallTime = Float.MAX_VALUE;
    // for calculating interpolation
    private float prevX, prevY, prevA;
    private Body body;
    private boolean collidable = true;

    public DynamicPhysicsEntity(PeaWorld peaWorld, World world, float x, float y, float width, float height, float angle) {
        super(peaWorld, x, y, width, height, angle);
        body = initPhysicsBody(world, x, y, width, height, angle);
        setPos(x, y);
        setAngle(angle);
    }

    abstract Body initPhysicsBody(World world, float x, float y, float width, float height, float angle);

    @Override
    public void paint(float alpha) {
        // interpolate based on previous state
        float x = (body.getPosition().x * alpha) + (prevX * (1f - alpha));
        float y = (body.getPosition().y * alpha) + (prevY * (1f - alpha));
        float a = (body.getAngle() * alpha) + (prevA * (1f - alpha));
        layer.setTranslation(x, y);
        layer.setRotation(a);
    }

    @Override
    public void update(float delta) {
        // store state for interpolation in paint()
        prevX = body.getPosition().x;
        prevY = body.getPosition().y;
        prevA = body.getAngle();

        body.getFixtureList().getFilterData().categoryBits = (collidable)?1:0;

        fallTime -= delta;

        if (fallTime < 0 && getBody().getType() != BodyType.DYNAMIC) {
            getBody().setType(BodyType.DYNAMIC);
            SOUND_FALL.play();
        }
    }


    @Override
    public void initPreLoad(final PeaWorld peaWorld) {
        // attach our layer to the dynamic layer
        peaWorld.dynamicLayer.add(layer);
    }

    @Override
    public void initPostLoad(final PeaWorld peaWorld) {
    }

    public void setLinearVelocity(float x, float y) {
        body.setLinearVelocity(new Vec2(x, y));
    }

    public void setAngularVelocity(float w) {
        body.setAngularVelocity(w);
    }

    @Override
    public void setPos(float x, float y) {
        super.setPos(x, y);
        getBody().setTransform(new Vec2(x, y), getBody().getAngle());
        prevX = x;
        prevY = y;
    }

    @Override
    public void setAngle(float a) {
        super.setAngle(a);
        getBody().setTransform(getBody().getPosition(), a);
        prevA = a;
    }

    @Override
    public Body getBody() {
        return body;
    }

    public void setCollidable(boolean collidable) {
        this.collidable = collidable;
        layer.setAlpha(collidable?1f:0.5f);
    }

    public boolean isCollidable() {
        return collidable;
    }

    public void setFallTime(float fallTime) {
        this.fallTime = fallTime;
    }
}
