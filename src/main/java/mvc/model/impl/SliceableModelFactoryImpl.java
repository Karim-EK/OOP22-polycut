package mvc.model.impl;

import mvc.model.api.PowerUpEnum;
import mvc.model.api.PowerUpModel;
import mvc.model.api.SliceableModel;
import mvc.model.api.SliceableModelFactory;
import mvc.model.api.SliceableTypeEnum;
import mvc.model.api.XY;

/**
 * Implementation of SliceableModelFactory.
 * The choice of which power-up to create belongs to the Spawner, that knows the game state.
 */
public class SliceableModelFactoryImpl implements SliceableModelFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public SliceableModel createSliceable(
        final SliceableTypeEnum type,
        final XY<Double> positionXY,
        final XY<Double> velocityXY
        ) {
        if (type == SliceableTypeEnum.POWERUP) {
            throw new IllegalArgumentException("power-ups are created with createPowerUp");
        }
        return new SliceableModelImpl(type, positionXY, velocityXY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PowerUpModel createPowerUp(
        final PowerUpEnum powerUp,
        final XY<Double> positionXY,
        final XY<Double> velocityXY
        ) {
        return new PowerUpModelImpl(SliceableTypeEnum.POWERUP, positionXY, velocityXY, powerUp);
    }
}
