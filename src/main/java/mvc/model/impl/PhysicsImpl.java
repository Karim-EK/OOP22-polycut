package mvc.model.impl;

import java.util.Set;

import mvc.model.api.Physics;
import mvc.model.api.SliceableModel;
import mvc.model.api.XY;

/**
 * Implementation of the Physics api.
 */
public class PhysicsImpl implements Physics {

    /*
        pixels/s**2
    */
    private static final double GRAVITY = 1200.0;

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePhysics(final Set<SliceableModel> sliceableSet, final double deltaTime) {
        for (final SliceableModel sliceable : sliceableSet) {

            final XY<Double> velocity = sliceable.getVelocity();
            final XY<Double> position = sliceable.getPosition();

            final double newVy = velocity.y() + GRAVITY * deltaTime;
            final double newVx = velocity.x();

            final double newX = position.x() + newVx * deltaTime;
            final double newY = position.y() + newVy * deltaTime;

            sliceable.setVelocity(new XY<>(newVx, newVy));
            sliceable.setPosition(new XY<>(newX, newY));
        }
    }
}
