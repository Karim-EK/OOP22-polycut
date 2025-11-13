package mvc.model.impl;

import mvc.model.api.XY;
import mvc.model.api.SliceableModel;
import mvc.model.api.SliceableTypeEnum;

/**
 * Implementation of SliceableModel api.
 */
public class SliceableModelImpl implements SliceableModel {

    private final SliceableTypeEnum type;
    private XY<Double> positionXy;
    private XY<Double> velocityXy;
    private final double apothem;

    /**
     * @param type of sliceable
     * @param positionXy XY coordinates on the map
     * @param velocityXy velocity for aeach axes
     * @param lenght of side of the sliceable
     */
    public SliceableModelImpl(
        final SliceableTypeEnum type, 
        final XY<Double> positionXy, 
        final XY<Double> velocityXy, 
        final double lenght
        ) {
            this.type = type;
            this.positionXy = positionXy;
            this.velocityXy = velocityXy;
            this.apothem = new ShapeImpl(type, lenght).getApothem();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public XY<Double> getPosition() {
        return this.positionXy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPosition(final XY<Double> position) {
        this.positionXy = position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public XY<Double> getVelocity() {
        return this.velocityXy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setVelocity(final XY<Double> velocity) {
        this.velocityXy = velocity;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public SliceableTypeEnum getType() {
        return this.type;
    }

    /**
     * TODO.
     */
    @Override
    public boolean isOutOfBound() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isOutOfBound'");
    }

    /**
     * TODO.
     */
    @Override
    public boolean hasBeenCut() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasBeenCut'");
    }

    /**
     * TODO.
     */
    @Override
    public double getApothem() {
        return this.apothem;
    }
}
