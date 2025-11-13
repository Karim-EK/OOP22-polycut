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
     * {@inheritDoc}
     */
    @Override
    public boolean isSliceLegit(final SliceableModel sliceable, final XY<Double> startingPoint, final XY<Double> endingPoint) {
        final double a = endingPoint.y() - startingPoint.y();
        final double b = startingPoint.x() - endingPoint.x();
        final double c = endingPoint.x() * startingPoint.y() - startingPoint.x() * endingPoint.y();
        final double numerator = Math.abs(a * sliceable.getPosition().x() + b * sliceable.getPosition().y() + c);
        final double denominator = Math.sqrt(a * a + b * b);
        final double distance = numerator / denominator;
        return distance <= sliceable.getApothem() * TOLERANCE;
    }
}
