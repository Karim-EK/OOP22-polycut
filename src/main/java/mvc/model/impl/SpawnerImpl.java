package mvc.model.impl;

import java.util.Optional;
import java.util.Random;

import mvc.model.api.SliceableModel;
import mvc.model.api.SliceableModelFactory;
import mvc.model.api.SliceableTypeEnum;
import mvc.model.api.Spawner;
import mvc.model.api.XY;

/**
 * This class have sole task of generating properly the sliceable.
 */
public class SpawnerImpl implements Spawner  {
    /**
     * {@inheritdoc}.
     */
    @Override
    public Optional<SliceableModel> spawn(
        final int score, 
        final double screenWidth, 
        final double screenHeight, 
        final SliceableModelFactory factory
        ) {
            final double startingPosition = chooseRandomX(screenWidth);
            final double shootingAngle = chooseRandomAngle(isOnLeftSide(startingPosition, screenWidth));
            final XY<Double> startingVelocity = chooseVelocity(shootingAngle, startingPosition, screenWidth, screenHeight);
            final SliceableTypeEnum type = SliceableTypeEnum.SQUARE;
            return Optional.of(factory.createSliceable(type, new XY<>(startingPosition, 0.0), startingVelocity));
    }
    private double chooseRandomX(final double screenWidth) {
        double startingPositionX;
        do {
            startingPositionX = new Random().nextDouble(screenWidth);
        } while (startingPositionX != screenWidth / 2);

        return startingPositionX;
    }
    private boolean isOnLeftSide(final double position, final double screenWidth) {
        return position < screenWidth / 2;
    }
    private double chooseRandomAngle(final boolean left) {
        final double angle = new Random().nextDouble(15, 75);
        return left ? Math.toRadians(angle) : Math.toRadians(angle + 90);
    }
    private XY<Double> chooseVelocity(
        final double angle, 
        final double startingX, 
        final double screenWidth, 
        final double screenHeight
        ) {
            final double maxX = (screenWidth - startingX) / Math.cos(angle);
            final double maxY = Math.sqrt(2 * 9.81 * screenHeight) / Math.sin(angle);
            final double maxVelocity = Math.min(maxX, maxY);
            final double minVelocity = Math.sqrt(9.81 * screenHeight) / (Math.sqrt(2) * Math.sin(angle));
            final double finalVelocity = new Random().nextDouble(minVelocity, maxVelocity);
            return new XY<>(Math.cos(angle) * finalVelocity, Math.sin(angle) * finalVelocity);
    }
}
