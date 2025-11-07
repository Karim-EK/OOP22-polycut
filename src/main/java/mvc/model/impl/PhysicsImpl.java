package mvc.model.impl;

import java.util.Set;

import mvc.model.api.Physics;
import mvc.model.api.SliceableModel;
import mvc.model.api.XY;

public class PhysicsImpl implements Physics {

    /*
        pixels/s**2 
    */
    final static double GRAVITY = 1200.0;

    /**
     * {@inheritdoc}
     */
    @Override
    public void updatePhysics(Set<SliceableModel> SliceableSet, double deltaTime) {
        for (SliceableModel sliceable : SliceableSet) {

            XY<Double> velocity = sliceable.getVelocity();
            XY<Double> position = sliceable.getPosition();

            double newVy = velocity.y() + GRAVITY * deltaTime;
            double newVx = velocity.x();

            double newX = position.x() + newVx * deltaTime;
            double newY = position.y() + newVy * deltaTime;

            sliceable.setVelocity(new XY<>(newVx, newVy));
            sliceable.setPosition(new XY<>(newX, newY));
        }
    }
}
