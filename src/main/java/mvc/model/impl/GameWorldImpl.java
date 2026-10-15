package mvc.model.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import mvc.model.api.DifficultyEnum;
import mvc.model.api.GameState;
import mvc.model.api.GameStateEnum;
import mvc.model.api.GameWorldListener;
import mvc.model.api.GameWorld;
import mvc.model.api.Physics;
import mvc.model.api.PowerUpEnum;
import mvc.model.api.PowerUpModel;
import mvc.model.api.PowerUpStatus;
import mvc.model.api.SliceHandler;
import mvc.model.api.SliceableModel;
import mvc.model.api.SliceableModelFactory;
import mvc.model.api.SliceableTypeEnum;
import mvc.model.api.Spawner;
import mvc.model.api.XY;

/**
 * Implementation of GameWorld api.
 */
public final class GameWorldImpl implements GameWorld {
    private double spawnTimer;
    private final Set<SliceableModel> activeSliceables;
    private final Physics physics;
    private final Spawner spawner;
    private final SliceHandler sliceHandler;
    private final GameState gameState;
    private final SliceableModelFactory factory;
    private Optional<PowerUpModel> activePowerUp;
    private final Set<GameWorldListener> listeners = new HashSet<>();
    private boolean gameOverNotified;

    /**
     * creates all the needed objects.
     *
     * @param physics moves the sliceables
     * @param spawner decides what to launch and when
     * @param sliceHandler decides if a mouse movement cuts a sliceable
     * @param factory creates the sliceables
     * @param gameState lives, score and power-up effects
     */
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP2",
        justification = "The components are injected by App and belong to this model only"
    )
    public GameWorldImpl(
        final Physics physics, final Spawner spawner,
        final SliceHandler sliceHandler, final SliceableModelFactory factory,
        final GameState gameState
    ) {
        this.activeSliceables = new HashSet<>();

        this.physics = physics;
        this.spawner = spawner;
        this.sliceHandler = sliceHandler;
        this.gameState = gameState;
        this.factory = factory;
        this.activePowerUp = Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final double deltaTime) {
        if (gameState.getGameState() == GameStateEnum.GAMEOVER) {
            return;
        }
        if (gameState.getGameState() != GameStateEnum.FREEZED) {
            this.physics.updatePhysics(activeSliceables, deltaTime);
        }
        if (this.activePowerUp.isPresent()) {
            final PowerUpModel active = this.activePowerUp.get();
            active.decreaseDuration(deltaTime);
            if (active.isEnded()) {
                deactivateCurrentPowerUp();
            }
        }
        // Durante il Freeze non si generano oggetti: resterebbero fermi e invisibili sotto il bordo
        // e alla fine del gelo partirebbero tutti insieme
        if (gameState.getGameState() != GameStateEnum.FREEZED) {
            this.spawnTimer += deltaTime;
        }
        final double currentSpawnInterval = spawner.calculateNextSpawnInterval(gameState.getScore(), gameState.getLives());
        if (this.spawnTimer >= currentSpawnInterval) {
            spawn();
            this.spawnTimer = 0.0;
        }
        final Iterator<SliceableModel> iterator = activeSliceables.iterator();
        while (iterator.hasNext()) {
            final SliceableModel s = iterator.next();

            if (s.hasBeenCut()) {
                iterator.remove();
            } else if (s.isOutOfBound(HEIGHT)) {
                // Con la Safe Zone attiva anche i poligoni che cadono non tolgono vite
                if (s.getType() != SliceableTypeEnum.BOMB && s.getType() != SliceableTypeEnum.POWERUP
                    && !gameState.isInvulnerable()) {
                    gameState.decreseLife();
                }
                iterator.remove();
            }
        }
        checkGameOver();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processSwipe(final XY<Double> initialXy, final XY<Double> finalXy) {
        for (final SliceableModel sliceable : activeSliceables) {

            if (!sliceable.hasBeenCut() && sliceHandler.isSliceLegit(sliceable, initialXy, finalXy)) {
                sliceable.setAsCutted(true);
                if (sliceable.getType() == SliceableTypeEnum.POWERUP) {
                    activatePowerUp((PowerUpModel) sliceable);
                } else if (sliceable.getType() == SliceableTypeEnum.BOMB) {
                    // Con la Safe Zone attiva la bomba non toglie vite, ma non dà nemmeno punti
                    if (!gameState.isInvulnerable()) {
                        gameState.decreseLife();
                    }
                } else {
                    gameState.gainScore(1);
                }
                // OSSERVATORE: notifica view e audio del taglio
                notifyListeners(listener -> listener.onSliceableCut(sliceable));
            }
        }
        checkGameOver();
    }

    // Il game over si notifica una sola volta, quando le vite arrivano a 0
    private void checkGameOver() {
        if (gameState.hasPlayerLost() && !this.gameOverNotified) {
            this.gameOverNotified = true;
            final int finalScore = gameState.getScore();
            notifyListeners(listener -> listener.onGameOver(finalScore));
        }
    }

    private void notifyListeners(final Consumer<GameWorldListener> event) {
        for (final GameWorldListener listener : this.listeners) {
            event.accept(listener);
        }
    }

    // Un lancio può contenere più oggetti (ondata)
    private void spawn() {
        activeSliceables.addAll(spawner.spawn(gameState.getScore(), gameState.getLives(), WIDTH, HEIGHT, factory,
            isPowerUpOnScreenOrActive()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPowerUpOnScreenOrActive() {
        return this.activePowerUp.isPresent() || getActiveSliceables().stream().anyMatch(s -> s instanceof PowerUpModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<SliceableModel> getActiveSliceables() {
        // Vista in sola lettura: chi la riceve non può aggiungere o togliere oggetti dal mondo
        return Collections.unmodifiableSet(this.activeSliceables);
    }

    private void activatePowerUp(final PowerUpModel powerup) {
        switch (powerup.getPowerUp()) {
            case ONE_UP -> this.gameState.addLife();
            case SAFE_ZONE -> {
                this.gameState.setInvulnerability(true);
                this.activePowerUp = Optional.of(powerup);
            }
            case X2 -> {
                this.gameState.setMultiplier(2);
                this.activePowerUp = Optional.of(powerup);
            }
            case FREEZE -> {
                this.gameState.setGameState(GameStateEnum.FREEZED);
                this.activePowerUp = Optional.of(powerup);
            }
        }
        notifyListeners(listener -> listener.onPowerUpActivate(powerup.getPowerUp()));
    }

    private void deactivateCurrentPowerUp() {
        final PowerUpEnum ended = activePowerUp.get().getPowerUp();
        switch (ended) {
            case SAFE_ZONE:
                this.gameState.setInvulnerability(false);
                break;
            case X2:
                this.gameState.setMultiplier(1);
                break;
            case FREEZE:
                this.gameState.setGameState(GameStateEnum.RUNNING);
                break;
            default:
                break;
        }
        this.activePowerUp = Optional.empty();
        notifyListeners(listener -> listener.onPowerUpDeactivate(ended));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener(final GameWorldListener listener) {
        this.listeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGameOver() {
        return this.gameState.getGameState() == GameStateEnum.GAMEOVER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLives() {
        return this.gameState.getLives();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getScore() {
        return this.gameState.getScore();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<PowerUpStatus> getActivePowerUp() {
        return this.activePowerUp.map(p -> new PowerUpStatus(p.getPowerUp(), p.getTimeReameaning().orElse(0.0)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void newGame(final DifficultyEnum difficulty) {
        this.activeSliceables.clear();
        this.gameState.reset();
        this.activePowerUp = Optional.empty();
        this.spawnTimer = 0.0;
        this.gameOverNotified = false;
        this.spawner.setDifficulty(difficulty);
    }
}
