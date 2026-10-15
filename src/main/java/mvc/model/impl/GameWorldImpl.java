package mvc.model.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import mvc.model.api.DifficultyEnum;
import mvc.model.api.GameState;
import mvc.model.api.GameStateEnum;
import mvc.model.api.GameWorldListener;
import mvc.model.api.GameWorld;
import mvc.model.api.Physics;
import mvc.model.api.PowerUpEnum;
import mvc.model.api.PowerUpModel;
import mvc.model.api.SliceHandler;
import mvc.model.api.SliceableModel;
import mvc.model.api.SliceableModelFactory;
import mvc.model.api.SliceableTypeEnum;
import mvc.model.api.Spawner;
import mvc.model.api.XY;

/**
 * Implementation of GameWorld api.
 */
public class GameWorldImpl implements GameWorld {    
    private double spawnTimer = 0.0;
    private Set<SliceableModel> activeSliceables;
    private Physics physics;
    private Spawner spawner;
    private SliceHandler sliceHandler;
    private GameState gameState;
    private SliceableModelFactory factory;
    private Optional<PowerUpModel> activePowerUp;
    private Set<GameWorldListener> listeners = new HashSet<>();
    private boolean gameOverNotified;

    /**
     * creates all the needed objects.
     */
    public GameWorldImpl(
        final Physics physics,final Spawner spawner,
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
            PowerUpModel active = this.activePowerUp.get();
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
        double currentSpawnInterval = spawner.calculateNextSpawnInterval(gameState.getScore(), gameState.getLives());
        if (this.spawnTimer >= currentSpawnInterval) {
            spawn();
            this.spawnTimer = 0.0;
        }
        Iterator<SliceableModel> iterator = activeSliceables.iterator();
        while (iterator.hasNext()) {
            SliceableModel s = iterator.next();
            
            if (s.hasBeenCut()) {
                iterator.remove();
            } else if (s.isOutOfBound(HEIGHT)) {
                if (s.getType() != SliceableTypeEnum.BOMB && s.getType() != SliceableTypeEnum.POWERUP) {
                    gameState.decreseLife();
                }
                iterator.remove();
            }
        }
        checkGameOver();
    }

    public void processSwipe(XY<Double> initialXy, XY<Double> finalXy) {
        for (SliceableModel sliceable : activeSliceables) {
            
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
        return this.activeSliceables;
    }

    private void activatePowerUp(final PowerUpModel powerup) {
        switch (powerup.getPowerUp()) {
            case ONE_UP: 
                this.gameState.addLife();
                break;
            case SAFE_ZONE: 
                this.gameState.setInvulnerability(true);
                this.activePowerUp = Optional.of(powerup);
                break;
            case X2:
                this.gameState.setMultiplier(2);
                this.activePowerUp = Optional.of(powerup);
                break;
            case FREEZE:
                this.gameState.setGameState(GameStateEnum.FREEZED);
                this.activePowerUp = Optional.of(powerup);
                break;
            default:
                throw new IllegalStateException("powerup not excepted");
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

    @Override
    public void addListener(GameWorldListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public boolean isGameover() {
        return this.gameState.getGameState().equals(GameStateEnum.GAMEOVER);
    }

    @Override
    public int getLives() {
        return this.gameState.getLives();
    }

    @Override
    public int getScore() {
        return this.gameState.getScore();
    }

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
