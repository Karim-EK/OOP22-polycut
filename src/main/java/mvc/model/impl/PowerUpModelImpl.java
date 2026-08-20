package mvc.model.impl;

import java.util.Optional;

import mvc.model.api.PowerUpEnum;
import mvc.model.api.PowerUpModel;
import mvc.model.api.SliceableTypeEnum;
import mvc.model.api.XY;

/**
 * Implementation of SliceableModel api.
 */
public class PowerUpModelImpl extends SliceableModelImpl implements PowerUpModel {

    private final PowerUpEnum powerUpType;
    private double duration;
    /**
     * {@inheritDoc}.
     * @param powerUpType the type of PowerUp
     * @param duration of the PowerUp
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
    
    @Override
    public void decreaseDuration(double dt) {
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
