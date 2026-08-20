package mvc.model.impl;

import java.util.Optional;
import java.util.Random;

import mvc.model.api.SliceableModel;
import mvc.model.api.SliceableModelFactory;
import mvc.model.api.SliceableTypeEnum;
import mvc.model.api.Spawner;
import mvc.model.api.XY;

/**
 * This class have sole task of properly generating the sliceables.
 */
public class SpawnerImpl implements Spawner  {
    private static final int SHAPE_CHANCHE = 85;
    private static final int BOMB_CHANCHE = 95;
    private static final int CENTRAL_OFFSET = 300;
    private static final double MARGIN = 50.0;
    private final Random random = new Random();
    
    private static final double T_MAX = 3.0;
    private static final double T_MIN = 0.5;
    private static final double LAMBDA = 0.015;

    public Optional<SliceableModel> spawn(int score, int lives, double screenWidth, double screenHeight,
            SliceableModelFactory factory, boolean powerUpActiveOrOnScreen) {
        final double startingPosition = chooseRandomX(screenWidth);
        final boolean isLeft = isOnLeftSide(startingPosition, screenWidth);
        final double shootingAngle = chooseRandomAngle(isLeft);
        final XY<Double> startingVelocity = chooseVelocity(shootingAngle, startingPosition, screenWidth, screenHeight);

        final SliceableTypeEnum type = chooseWeightedType(powerUpActiveOrOnScreen);

        // 2. (Opzionale) Scelta ponderata o casuale della Forma se separata dal Tipo
        // Nel tuo Enum attuale, Forma e Tipo sembrano fusi o gestiti in modo particolare.
        // Assumiamo che la factory crei la geometria in base al type.

        return Optional.of(factory.createSliceable(type, new XY<>(startingPosition, screenHeight), startingVelocity));
    }

    public double calculateNextSpawnInterval(int score, int lives) {
        double time = T_MIN + (T_MAX - T_MIN) * Math.exp(-LAMBDA * score);

        if (lives == 1) {
            time *= 1.2;
        }
        return time;
    }

    private SliceableTypeEnum chooseWeightedType(boolean powerUpActiveOrOnScreen) {
        int chance = random.nextInt(100);

        if (chance < SHAPE_CHANCHE) {
            return getType();
        } else if (chance < BOMB_CHANCHE) {
            return SliceableTypeEnum.BOMB;
        } else {
            if (powerUpActiveOrOnScreen) {
                return getType();
            }
            return SliceableTypeEnum.POWERUP;
        }
    }

    private SliceableTypeEnum getType() {
        var types = SliceableTypeEnum.values();
        SliceableTypeEnum type = types[random.nextInt(types.length)];
        while (type == SliceableTypeEnum.BOMB || type == SliceableTypeEnum.POWERUP) {
            type = types[random.nextInt(types.length)];
        }
        return type;
    }

    private double chooseRandomX(final double screenWidth) {
        final boolean spawnLeft = random.nextBoolean();
        if (spawnLeft) {
            return random.nextDouble(MARGIN, (screenWidth / 2) - CENTRAL_OFFSET);
        } else {
            return random.nextDouble((screenWidth / 2) + CENTRAL_OFFSET, screenWidth - MARGIN);
        }
    }

    private boolean isOnLeftSide(final double position, final double screenWidth) {
        return position < screenWidth / 2;
    }

    private double chooseRandomAngle(final boolean left) {
        final double angle = random.nextDouble(15, 75);
        return left ? Math.toRadians(angle) : Math.toRadians(180 - angle);
    }

    private XY<Double> chooseVelocity(
        final double angle, final double startingX, final double screenWidth, final double screenHeight
    ) {
        final double distanceToEdge = isOnLeftSide(startingX, screenWidth)
            ? (screenWidth - startingX)
            : startingX;

        final double maxXVelocity = distanceToEdge / Math.abs(Math.cos(angle));

        final double maxYVelocity = Math.sqrt(2 * 9.81 * (screenHeight * 0.8)) / Math.sin(angle);

        final double maxVelocity = Math.min(maxXVelocity, maxYVelocity);

        double minVelocity = Math.sqrt(9.81 * (screenHeight * 0.5)) / Math.sin(angle);

        if (minVelocity >= maxVelocity) {
            minVelocity = maxVelocity * 0.8;
        }

        final double finalVelocity = random.nextDouble(minVelocity, maxVelocity);

        return new XY<>(Math.cos(angle) * finalVelocity, -Math.sin(angle) * finalVelocity);
    }
}