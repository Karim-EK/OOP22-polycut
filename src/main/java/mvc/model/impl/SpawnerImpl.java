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
 * Every sliceable is launched from below the bottom edge and its parabola
 * always falls back on the bottom edge, inside the screen.
 */
public class SpawnerImpl implements Spawner  {
    private static final int SHAPE_CHANCHE = 85;
    private static final int BOMB_CHANCHE = 95;
    private static final double MARGIN = 10.0;
    // Altezza del culmine (centro dell'oggetto), in frazione dell'altezza dello schermo misurata dall'alto
    private static final double MIN_PEAK = 0.15;
    private static final double MAX_PEAK = 0.5;
    // Spostamento orizzontale massimo tra lancio e atterraggio, in frazione della larghezza
    private static final double MAX_DRIFT = 0.4;
    private final Random random = new Random();

    private static final double T_MAX = 3.0;
    private static final double T_MIN = 0.5;
    private static final double LAMBDA = 0.015;

    @Override
    public Optional<SliceableModel> spawn(final int score, final int lives, final double screenWidth,
            final double screenHeight, final SliceableModelFactory factory, final boolean powerUpActiveOrOnScreen) {
        final double radius = SliceableModelImpl.RADIUS;
        final double minX = radius + MARGIN;
        final double maxX = screenWidth - radius - MARGIN;

        // Partenza appena sotto il bordo inferiore, quindi ancora invisibile
        final double startX = random.nextDouble(minX, maxX);
        final double startY = screenHeight + radius;

        // Velocità verticale scelta in base al culmine: h = vy^2 / (2g)  =>  vy = sqrt(2gh)
        final double peakY = random.nextDouble(screenHeight * MIN_PEAK, screenHeight * MAX_PEAK);
        final double verticalSpeed = Math.sqrt(2 * PhysicsImpl.GRAVITY * (startY - peakY));

        // Il moto parabolico torna alla quota di partenza dopo T = 2 * vy / g
        final double flightTime = 2 * verticalSpeed / PhysicsImpl.GRAVITY;

        // Punto di atterraggio dentro lo schermo: il moto orizzontale è uniforme, quindi tutta la traiettoria resta dentro
        final double maxDrift = screenWidth * MAX_DRIFT;
        final double endX = random.nextDouble(Math.max(minX, startX - maxDrift), Math.min(maxX, startX + maxDrift));
        final double horizontalSpeed = (endX - startX) / flightTime;

        final SliceableTypeEnum type = chooseWeightedType(powerUpActiveOrOnScreen);
        // L'asse y punta verso il basso: per salire la velocità verticale è negativa
        final XY<Double> startingVelocity = new XY<>(horizontalSpeed, -verticalSpeed);

        return Optional.of(factory.createSliceable(type, new XY<>(startX, startY), startingVelocity));
    }

    @Override
    public double calculateNextSpawnInterval(final int score, final int lives) {
        double time = T_MIN + (T_MAX - T_MIN) * Math.exp(-LAMBDA * score);

        if (lives == 1) {
            time *= 1.2;
        }
        return time;
    }

    private SliceableTypeEnum chooseWeightedType(final boolean powerUpActiveOrOnScreen) {
        final int chance = random.nextInt(100);

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
        final var types = SliceableTypeEnum.values();
        SliceableTypeEnum type = types[random.nextInt(types.length)];
        while (type == SliceableTypeEnum.BOMB || type == SliceableTypeEnum.POWERUP) {
            type = types[random.nextInt(types.length)];
        }
        return type;
    }
}
