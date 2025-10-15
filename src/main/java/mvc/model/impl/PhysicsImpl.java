package mvc.model.impl;

import java.awt.geom.Point2D;
import java.util.Set;

import mvc.model.api.Physics;
import mvc.model.api.SliceableModel;

/**
 * Physics Implementation.
 */
public class PhysicsImpl implements Physics {
    static final Point2D GRAVITY = new Point2D.Double(0, 9.81);
    private final double dt;

    /**
     * Constructor.
     * @param dt delta time
     */
    public PhysicsImpl(final double dt) {
        this.dt = dt;
    }

    /**
     * {@inheritDoc}.
     * @param sliceableSet
     */
    @Override
    public void doMaths(final Set<SliceableModel> sliceableSet) {
        for (final SliceableModel poly : sliceableSet) {
            final var oldPos = poly.getPosition();
            final var oldVel = poly.getVelocity();

            final double velX = oldVel.x() + dt * GRAVITY.getX();
            final double velY = oldVel.y() - dt * GRAVITY.getY();
            final var newVel = new PositionXY((int) velX, (int) velY);

            final double posX = oldPos.x() + dt * oldVel.x();
            final double posY = oldPos.y() - dt * oldVel.y();
            final var newPos = new PositionXY((int) posX, (int) posY);

            poly.setPosition(newPos);
            poly.setVelocity(newVel);
        }
    }
}
