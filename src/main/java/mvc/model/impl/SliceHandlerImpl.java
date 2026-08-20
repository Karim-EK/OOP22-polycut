package mvc.model.impl;

import mvc.model.api.SliceHandler;
import mvc.model.api.SliceableModel;
import mvc.model.api.XY;

/**
 * Implementation of SliceHandler api.
 */
public class SliceHandlerImpl implements SliceHandler {

    private static final double TOLERANCE = 0.8;

    /**
     * {@inheritdoc}.
     */
    @Override
    public boolean isSliceLegit(final SliceableModel sliceable, final XY<Double> startingPoint, final XY<Double> endingPoint) {
        
        final double ax = startingPoint.x();
        final double ay = startingPoint.y();
        final double bx = endingPoint.x();
        final double by = endingPoint.y();
        final double cx = sliceable.getPosition().x();
        final double cy = sliceable.getPosition().y();

        final double abx = bx - ax;
        final double aby = by - ay;

        final double acx = cx - ax;
        final double acy = cy - ay;

        final double abLengthSquared = abx * abx + aby * aby;

        if (abLengthSquared == 0) {
            final double distanceToA = Math.sqrt(acx * acx + acy * acy);
            return distanceToA <= sliceable.getApothem() * TOLERANCE;
        }

        double t = (acx * abx + acy * aby) / abLengthSquared;

        t = Math.max(0, Math.min(1, t));

        final double closestPointX = ax + t * abx;
        final double closestPointY = ay + t * aby;

        final double distanceX = cx - closestPointX;
        final double distanceY = cy - closestPointY;
        final double distanceToSegment = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

        return distanceToSegment <= sliceable.getApothem() * TOLERANCE;
    }
}