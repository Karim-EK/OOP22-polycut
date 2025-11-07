package mvc.model.impl;

import mvc.model.api.XY;
import mvc.model.api.SliceableModel;
import mvc.model.api.SliceableTypeEnum;

/**
 * {@inheritDoc}
 */
public class SliceableModelImpl implements SliceableModel {

    protected final SliceableTypeEnum type;
    protected XY<Double> positionXY;
    protected XY<Double> velocityXY;
    private final double apothem;

    /**
     * @param type of sliceable
     * @param positionXY XY coordinates on the map
     * @param velocityXY velocity for aeach axes
     */
    public SliceableModelImpl(final SliceableTypeEnum type, final XY<Double> positionXY, final XY<Double> velocityXY, double lenght) {
        this.type = type;
        this.positionXY = positionXY;
        this.velocityXY = velocityXY;
        this.apothem = new ShapeImpl(type, lenght).getApothem();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public XY<Double> getPosition() {
        return positionXY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPosition(final XY<Double> position) {
        this.positionXY = position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public XY<Double> getVelocity() {
        return this.velocityXY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setVelocity(final XY<Double> velocity) {
        this.velocityXY = velocity;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public SliceableTypeEnum getType() {
        return this.type;
    }

    @Override
    public boolean isOutOfBound() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isOutOfBound'");
    }

    @Override
    public boolean hasBeenCut() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasBeenCut'");
    }

    @Override
    public double getApothem() {
        return this.apothem;
    }
}
