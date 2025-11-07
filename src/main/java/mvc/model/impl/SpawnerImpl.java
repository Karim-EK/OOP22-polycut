package mvc.model.impl;

import java.util.Optional;
import java.util.Random;

import mvc.model.api.SliceableModel;
import mvc.model.api.SliceableModelFactory;
import mvc.model.api.SliceableTypeEnum;
import mvc.model.api.Spawner;
import mvc.model.api.XY;

/**
 * This class have sole task of generating properly the sliceable
 */
public class SpawnerImpl implements Spawner  {
    /**
     * {@inheritdoc}
     */
    @Override
    public Optional<SliceableModel> spawn(int score, double screenWidth, double screenHeight, SliceableModelFactory factory) {
        double startingPosition = chooseRandomX(screenWidth);
        double shootingAngle = chooseRandomAngle(isOnLeftSide(startingPosition, screenWidth));
        XY<Double> startingVelocity = chooseVelocity(shootingAngle, startingPosition, screenWidth, screenHeight);
        SliceableTypeEnum type = SliceableTypeEnum.SQUARE;
        return Optional.of(factory.createSliceable(type, new XY<>(startingPosition, 0.0), startingVelocity));
    }
    
    private double chooseRandomX(double screenWidth){
        double startingPositionX;
        do {
            startingPositionX = new Random().nextDouble(screenWidth);
        } while (startingPositionX != screenWidth / 2);

        return startingPositionX;
    }

    private boolean isOnLeftSide(double position, double screenWidth) {
        return position < screenWidth / 2;
    }

    private double chooseRandomAngle(boolean left) {
        double angle = new Random().nextDouble(15, 75);
        return left ? Math.toRadians(angle) : Math.toRadians(angle + 90);
    }

    private XY<Double> chooseVelocity(double angle, double startingX, double screenWidth, double screenHeight) {
        double maxX = (screenWidth - startingX) / Math.cos(angle);
        double maxY = Math.sqrt(2 * 9.81 * screenHeight) / Math.sin(angle);
        double maxVelocity = Math.min(maxX, maxY);
        double minVelocity = Math.sqrt(9.81 * screenHeight) / (Math.sqrt(2) * Math.sin(angle));
        double finalVelocity = new Random().nextDouble(minVelocity, maxVelocity);
        return new XY<>(Math.cos(angle) * finalVelocity, Math.sin(angle) * finalVelocity);
    }
}
