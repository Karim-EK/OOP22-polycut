package mvc.model.impl;

import java.util.Random;

import mvc.model.api.PowerUpEnum;
import mvc.model.api.SliceableModelFactory;
import mvc.model.api.SliceableModel;
import mvc.model.api.SliceableTypeEnum;

/**
 * {@inheritDoc}.
 */
public class SliceableModelFactoryImpl implements SliceableModelFactory {
    private static final Random RANDOM = new Random();
    private static final int SPAWN_X = 400;
    private static final int SPAWN_Y = 750;
    private static final int VEL_X_ORIGIN = 35;
    private static final int VEL_X_BOUND = 40;
    private static final int VEL_Y_ORIGIN = 85;
    private static final int VEL_Y_BOUND = 115;
    private static PositionXY startPositionNext;
    private static PositionXY startVelocityNext;

    private SliceableModelFactoryImpl(){
    }

    private static void randomizeVelocityAndPosition() {
        final int randomX = VEL_X_ORIGIN + (VEL_X_BOUND - VEL_X_ORIGIN) * RANDOM.nextInt();
        final int randomY = VEL_Y_ORIGIN + (VEL_Y_BOUND - VEL_Y_ORIGIN) * RANDOM.nextInt();

        startVelocityNext = new PositionXY(randomX, randomY);
        startPositionNext = new PositionXY(RANDOM.nextInt(SPAWN_X), SPAWN_Y);
    }

    @Override
    public SliceableModel createSliceable() {
        randomizeVelocityAndPosition();
        var sliceableType = chooseSliceable();
        if (sliceableType.equals(SliceableTypeEnum.POWERUP)) {
            return createPowerUp();
        } else {
            return new SliceableModelImpl(sliceableType, startPositionNext, startVelocityNext);
        }
    }

    private PowerUpImpl createPowerUp() {
        return new PowerUpImpl(SliceableTypeEnum.POWERUP, startPositionNext, startVelocityNext, choosePowerUp());
    }

    private static PowerUpEnum choosePowerUp() {
        var random = RANDOM.nextDouble();
        var cumulativeProbability = 0.0;
        for (PowerUpEnum powerUp : PowerUpEnum.values()) {
            cumulativeProbability += powerUp.getProbability();
            if (random <= cumulativeProbability) {
                return powerUp;
            }
        }
        return PowerUpEnum.X2;
    }

    private static SliceableTypeEnum chooseSliceable() {
        var random = RANDOM.nextDouble();
        var cumulativeProbability = 0.0;
        for (SliceableTypeEnum powerUp : SliceableTypeEnum.values()) {
            cumulativeProbability += powerUp.getProbability();
            if (random <= cumulativeProbability) {
                return powerUp;
            }
        }
        return SliceableTypeEnum.TRIANGLE;
    }
}
