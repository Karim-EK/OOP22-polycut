package mvc.model.impl;

import mvc.model.api.XY;

import mvc.model.api.SliceableModel;
import mvc.model.api.SliceableTypeEnum;

/**
 * Implementation of SliceableModel api.
 */
public class SliceableModelImpl implements SliceableModel {

    /**
     * Circumradius shared by every sliceable, in logical units.
     */
    public static final double RADIUS = 5.0;
    
    private final SliceableTypeEnum type;
    private XY<Double> positionXy;
    private XY<Double> velocityXy;
    private final double apothem;
    private boolean toBeCut;


    /**
     * @param type of sliceable
     * @param positionXy XY coordinates on the map
     * @param velocityXy velocity for aeach axes
     */
    public SliceableModelImpl(
        final SliceableTypeEnum type, 
        final XY<Double> positionXy, 
        final XY<Double> velocityXy) 
        {
            this.type = type;
            this.positionXy = positionXy;
            this.velocityXy = velocityXy;
            this.apothem = new ShapeImpl(type, RADIUS).getApothem();
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
     * {@inheritDoc}
     */
    @Override
    public boolean hasBeenCut() {
        return this.toBeCut;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getApothem() {
        return this.apothem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAsCutted(boolean cutted) {
        this.toBeCut = cutted;
    }
    
    /**
     * The sliceable is out only when it is falling and completely below the bottom edge:
     * at spawn time it is below the edge too, but it is going up.
     */
    @Override
    public boolean isOutOfBound(final double worldHeight) {
        final boolean falling = this.getVelocity().y() > 0;
        return falling && (this.getPosition().y() - RADIUS) > worldHeight;
    }
}
