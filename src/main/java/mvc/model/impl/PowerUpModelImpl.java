package mvc.model.impl;

import java.util.Optional;

import mvc.model.api.PowerUpEnum;
import mvc.model.api.PowerUpModel;
import mvc.model.api.SliceableTypeEnum;
import mvc.model.api.XY;

/**
 * {@inheritDoc}
 */
public class PowerUpModelImpl extends SliceableModelImpl implements PowerUpModel {

    final PowerUpEnum powerUpType;
    final double duration;
    
    /**
     * 
     * {@inheritDoc}
     * @param powerUpType the type of PowerUp
     * @param duration of the PowerUp
     */
    public PowerUpModelImpl(final SliceableTypeEnum type, final XY<Double> positionXY, final XY<Double> velocityXY, final PowerUpEnum powerUpType, final double duration, double lenght) {
        super(type, positionXY, velocityXY, lenght);
        this.powerUpType = powerUpType;
        this.duration = duration;
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
    public boolean isEnded() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isEnded'");
    }
}
