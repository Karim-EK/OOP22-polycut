package mvc.model.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import mvc.model.api.GameState;
import mvc.model.api.GameStateEnum;
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

    private static final long SAFE_ZONE_TIMER = 200;
    private static final long X2_TIMER = 200;
    private static final long FREEZE_TIMER = 200;

    private Set<SliceableModel> activeSliceables;
    private Physics physics;
    private Spawner spawner;
    private SliceHandler sliceHandler;
    private GameState gameState;
    private SliceableModelFactory factory;
    private Timer powerUpTimer;
    private Optional<PowerUpEnum> activePowerUp;

    /**
     * creates all the needed objects.
     */
    public GameWorldImpl() {
        this.physics = new PhysicsImpl();
        this.activeSliceables = new HashSet<>();
        this.spawner = new SpawnerImpl();
        this.sliceHandler = new SliceHandlerImpl();
        this.gameState = new GameStateImpl();
        this.powerUpTimer = new Timer();
        this.factory = new SliceableModelFactoryImpl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final Physics physics, final Spawner spawner, final SliceHandler sliceHandler) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'initialize'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final double deltaTime) {
        this.physics.updatePhysics(activeSliceables, deltaTime);
    }

    /**
     * TODO.
     */
    public void spawn() {
        activeSliceables.add(spawner.spawn(gameState.getScore(), getWorldWidth(), getWorldHeight(), null).get());
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
    public void registerCut(final SliceableModel cuttedSliceable, final XY<Double> initialXy, final XY<Double> finalXy) {
        if (sliceHandler.isSliceLegit(cuttedSliceable, initialXy, finalXy)) {
            if (cuttedSliceable.getType() == SliceableTypeEnum.POWERUP) {
                final var powerUp = (PowerUpModel) cuttedSliceable;
                this.activePowerUp = Optional.of(powerUp.getPowerUp());
                activatePowerUp(powerUp);
            }
            activeSliceables.remove(cuttedSliceable);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getWorldWidth() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWorldWidth'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getWorldHeight() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWorldHeight'");
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
            case PowerUpEnum.ONE_UP: 
                this.gameState.addLife();
            case PowerUpEnum.SAFE_ZONE: 
                this.gameState.setInvulnerability(true);
                this.powerUpTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        activePowerUp = Optional.empty();
                    }
                }, SAFE_ZONE_TIMER);
            case PowerUpEnum.X2:
                this.gameState.setMultiplier(2);
                this.powerUpTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        gameState.setMultiplier(1);
                        activePowerUp = Optional.empty();
                    }
                }, X2_TIMER);
            case FREEZE:
                this.gameState.setGameState(GameStateEnum.FREEZED);
                this.powerUpTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        gameState.setGameState(GameStateEnum.RUNNING);
                        activePowerUp = Optional.empty();
                    }
                }, FREEZE_TIMER);
            default:
            throw new IllegalStateException("powerup not excepted");
        }
    }
}
