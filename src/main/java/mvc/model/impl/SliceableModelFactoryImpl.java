package mvc.model.impl;

import java.util.Random;

import mvc.model.api.PowerUpEnum;
import mvc.model.api.SliceableModel;
import mvc.model.api.SliceableModelFactory;
import mvc.model.api.SliceableTypeEnum;
import mvc.model.api.XY;

/**
 * Implementation of SliceableModelFactory.
 */
public class SliceableModelFactoryImpl implements SliceableModelFactory {


    private static final int X2_CHANCE = 50;
    private static final int FREEZE_CHANCE = 75;
    private static final int SAFEZONE_CHANCE = 90;

    Random random = new Random();

    /**
     * {@inheritDoc}
     */
    @Override
    public SliceableModel createSliceable(
        final SliceableTypeEnum type, 
        final XY<Double> positionXY, 
        final XY<Double> velocityXY
        ) {
        if (type.equals(SliceableTypeEnum.POWERUP)) {
            return new PowerUpModelImpl(type, positionXY, velocityXY, choosePowerUp());
        }
        return new SliceableModelImpl(type, positionXY, velocityXY);
    }

    private PowerUpEnum choosePowerUp() {
        int chance = random.nextInt(100);
        if (chance < X2_CHANCE) {
            return PowerUpEnum.X2;
        } else if (chance < FREEZE_CHANCE) {
            return PowerUpEnum.FREEZE;
        } else if (chance < SAFEZONE_CHANCE) {
            return PowerUpEnum.SAFE_ZONE;
        } else {
            return PowerUpEnum.ONE_UP;
        }
    }
}
