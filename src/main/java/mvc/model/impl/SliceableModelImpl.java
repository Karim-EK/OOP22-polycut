package mvc.model.impl;

import mvc.model.api.SliceableModel;
import mvc.model.api.SliceableTypeEnum;

/**
 * {@inheritDoc}.
 */
public class SliceableModelImpl implements SliceableModel {
    // TODO: costante come limite dello schermo ?
    // forse gui potrebbe dire a controller di cambiare valore al model
    private static final Integer LOWER_BOUND = 1000;
    private PositionXY position;
    private final SliceableTypeEnum type;
    private PositionXY velocity;
    private boolean gotCut;

    /**
     * Constructor of a regular polygon.
     * @param type   type of Sliceable.
     * @param position Position position of the sliceable, to update every timestep.
     * @param velocity Position vector of the new velocity of the object.
     * @param sliceableId the sliceable identifier.
     */
    public SliceableModelImpl(final SliceableTypeEnum type, final PositionXY position,
                              final PositionXY velocity) {
        this.velocity = new PositionXY(velocity.x(), velocity.y());
        this.position = new PositionXY(position.x(), position.y());
        this.type = type;
        this.gotCut = false;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public PositionXY getPosition() {
        return this.position;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public void setPosition(final PositionXY position) {
        this.position = position;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public PositionXY getVelocity() {
        return this.velocity;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public void setVelocity(final PositionXY velocity) {
        this.velocity = velocity;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public SliceableTypeEnum getType() {
        return this.type;
    }


    /**
     * {@inheritDoc}.
     */
    @Override
    public boolean isOutOfBound() {
        return this.getPosition().y() > LOWER_BOUND;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public boolean hasBeenCut() {
        return this.gotCut;
    }
}
