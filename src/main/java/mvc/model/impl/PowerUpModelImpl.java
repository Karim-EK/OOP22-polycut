package mvc.model.impl;

import java.util.Optional;

import mvc.model.api.PowerUpEnum;
import mvc.model.api.PowerUpModel;
import mvc.model.api.SliceableTypeEnum;
import mvc.model.api.XY;

/**
 * Implementation of SliceableModel api.
 */
public final class PowerUpModelImpl extends SliceableModelImpl implements PowerUpModel {

    private final PowerUpEnum powerUpType;
    private double duration;

    /**
     * The duration starts from the one defined in PowerUpEnum.
     *
     * @param type of sliceable, always POWERUP
     * @param positionXY XY coordinates on the map
     * @param velocityXY velocity for each axes
     * @param powerUpType the type of PowerUp
     */
    public PowerUpModelImpl(
        final SliceableTypeEnum type,
        final XY<Double> positionXY,
        final XY<Double> velocityXY,
        final PowerUpEnum powerUpType
        ) {
            super(type, positionXY, velocityXY);
            this.powerUpType = powerUpType;
            this.duration = powerUpType.getDuration();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Double> getTimeReameaning() {
        return Optional.of(this.duration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PowerUpEnum getPowerUp() {
        return this.powerUpType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decreaseDuration(final double dt) {
        this.duration -= dt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnded() {
       return this.duration <= 0;
    }
}
