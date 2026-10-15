package mvc.model.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import mvc.model.api.DifficultyEnum;
import mvc.model.api.GameState;
import mvc.model.api.PowerUpEnum;
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
    // Su 100 lanci: 82 poligoni, 10 bombe, 8 power-up
    private static final int SHAPE_CHANCHE = 82;
    private static final int BOMB_CHANCHE = 92;
    private static final double MARGIN = 10.0;
    // Altezza del culmine (centro dell'oggetto), in frazione dell'altezza dello schermo misurata dall'alto
    private static final double MIN_PEAK = 0.15;
    private static final double MAX_PEAK = 0.5;
    // Spostamento orizzontale massimo tra lancio e atterraggio, in frazione della larghezza
    private static final double MAX_DRIFT = 0.4;
    // Ondate: da 30 punti in su, ogni punto aggiunge l'1% di probabilità, fino al 50%
    private static final int WAVE_START_SCORE = 30;
    private static final double WAVE_CHANCE_PER_POINT = 0.01;
    private static final double MAX_WAVE_CHANCE = 0.5;
    // Dentro un'ondata: 35% di probabilità che sia da 3 oggetti, altrimenti da 2
    private static final double TRIPLE_WAVE_CHANCE = 0.35;
    // Con una sola vita il ritmo rallenta del 20%
    private static final double LAST_LIFE_SLOWDOWN = 1.2;
    private final Random random = new Random();

    private DifficultyEnum difficulty = DifficultyEnum.NORMAL;

    @Override
    public List<SliceableModel> spawn(final int score, final int lives, final double screenWidth,
            final double screenHeight, final SliceableModelFactory factory, final boolean powerUpActiveOrOnScreen) {
        final List<SliceableModel> launched = new ArrayList<>();
        // Al massimo un power-up per ondata, e nessuno se ce n'è già uno a schermo o attivo
        boolean powerUpAllowed = !powerUpActiveOrOnScreen;
        for (int i = 0; i < chooseWaveSize(score); i++) {
            final XY<Double> position = chooseStartingPosition(screenWidth, screenHeight);
            final XY<Double> velocity = chooseVelocity(position, screenWidth, screenHeight);
            final SliceableTypeEnum type = chooseWeightedType(powerUpAllowed);
            if (type == SliceableTypeEnum.POWERUP) {
                launched.add(factory.createPowerUp(choosePowerUp(lives), position, velocity));
                powerUpAllowed = false;
            } else {
                launched.add(factory.createSliceable(type, position, velocity));
            }
        }
        return launched;
    }

    @Override
    public double calculateNextSpawnInterval(final int score, final int lives) {
        final double min = this.difficulty.getMinInterval();
        final double initial = this.difficulty.getInitialInterval();
        double time = min + (initial - min) * Math.exp(-this.difficulty.getDecay() * score);

        if (lives == 1) {
            time *= LAST_LIFE_SLOWDOWN;
        }
        return time;
    }

    @Override
    public void setDifficulty(final DifficultyEnum difficulty) {
        this.difficulty = difficulty;
    }

    private int chooseWaveSize(final int score) {
        if (score < WAVE_START_SCORE) {
            return 1;
        }
        final double waveChance = Math.min(MAX_WAVE_CHANCE, (score - WAVE_START_SCORE) * WAVE_CHANCE_PER_POINT);
        if (random.nextDouble() >= waveChance) {
            return 1;
        }
        return random.nextDouble() < TRIPLE_WAVE_CHANCE ? 3 : 2;
    }

    // Partenza appena sotto il bordo inferiore, quindi ancora invisibile
    private XY<Double> chooseStartingPosition(final double screenWidth, final double screenHeight) {
        final double radius = SliceableModelImpl.RADIUS;
        return new XY<>(random.nextDouble(radius + MARGIN, screenWidth - radius - MARGIN), screenHeight + radius);
    }

    private XY<Double> chooseVelocity(final XY<Double> start, final double screenWidth, final double screenHeight) {
        final double radius = SliceableModelImpl.RADIUS;
        final double minX = radius + MARGIN;
        final double maxX = screenWidth - radius - MARGIN;

        // Velocità verticale scelta in base al culmine: h = vy^2 / (2g)  =>  vy = sqrt(2gh)
        final double peakY = random.nextDouble(screenHeight * MIN_PEAK, screenHeight * MAX_PEAK);
        final double verticalSpeed = Math.sqrt(2 * PhysicsImpl.GRAVITY * (start.y() - peakY));

        // Il moto parabolico torna alla quota di partenza dopo T = 2 * vy / g
        final double flightTime = 2 * verticalSpeed / PhysicsImpl.GRAVITY;

        // Punto di atterraggio dentro lo schermo: il moto orizzontale è uniforme, quindi tutta la traiettoria resta dentro
        final double maxDrift = screenWidth * MAX_DRIFT;
        final double endX = random.nextDouble(Math.max(minX, start.x() - maxDrift), Math.min(maxX, start.x() + maxDrift));
        final double horizontalSpeed = (endX - start.x()) / flightTime;

        // L'asse y punta verso il basso: per salire la velocità verticale è negativa
        return new XY<>(horizontalSpeed, -verticalSpeed);
    }

    private SliceableTypeEnum chooseWeightedType(final boolean powerUpAllowed) {
        final int chance = random.nextInt(100);

        if (chance < SHAPE_CHANCHE) {
            return getType();
        } else if (chance < BOMB_CHANCHE) {
            return SliceableTypeEnum.BOMB;
        } else {
            if (!powerUpAllowed) {
                return getType();
            }
            return SliceableTypeEnum.POWERUP;
        }
    }

    // I power-up sono equiprobabili; l'1-Up esce solo se il giocatore non ha già tutte le vite
    private PowerUpEnum choosePowerUp(final int lives) {
        final List<PowerUpEnum> candidates = Arrays.stream(PowerUpEnum.values())
            .filter(p -> p != PowerUpEnum.ONE_UP || lives < GameState.MAX_LIVES)
            .toList();
        return candidates.get(random.nextInt(candidates.size()));
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
