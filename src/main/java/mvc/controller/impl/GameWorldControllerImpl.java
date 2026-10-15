package mvc.controller.impl;

import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import mvc.controller.api.GameWorldController;
import mvc.model.api.DifficultyEnum;
import mvc.model.api.GameWorld;
import mvc.model.api.PowerUpStatus;
import mvc.model.api.SliceableModel;
import mvc.model.api.XY;

/**
 * Implementation of GameWorldController.
 * The model is only touched by the game loop thread: swipes coming from the Swing thread
 * are queued and applied at the next update, while the view reads an immutable snapshot.
 */
public class GameWorldControllerImpl implements GameWorldController {

    private final GameWorld model;
    private final Queue<Swipe> pendingSwipes = new ConcurrentLinkedQueue<>();

    private volatile Set<SliceableModel> visibleSliceables;
    private volatile int lives;
    private volatile int score;
    private volatile boolean gameOver;
    private volatile Optional<PowerUpStatus> activePowerUp = Optional.empty();

    /**
     * @param model the game world to drive
     */
    public GameWorldControllerImpl(final GameWorld model) {
        this.model = model;
        publishSnapshot();
    }

    /**
     * Called by the game loop thread: applies the queued swipes, updates the model
     * and publishes a new snapshot for the view.
     */
    @Override
    public void update(final double deltaTime) {
        Swipe swipe = this.pendingSwipes.poll();
        while (swipe != null) {
            this.model.processSwipe(swipe.from(), swipe.to());
            swipe = this.pendingSwipes.poll();
        }
        this.model.update(deltaTime);
        publishSnapshot();
    }

    /**
     * Called by the Swing thread: the swipe is only queued, the model is not touched here.
     */
    @Override
    public void processSwipe(final XY<Double> initialXy, final XY<Double> finalXy) {
        this.pendingSwipes.offer(new Swipe(initialXy, finalXy));
    }

    @Override
    public Set<SliceableModel> getVisibleSliceables() {
        return this.visibleSliceables;
    }

    @Override
    public boolean isGameOver() {
        return this.gameOver;
    }

    @Override
    public int getLives() {
        return this.lives;
    }

    @Override
    public int getScore() {
        return this.score;
    }

    @Override
    public Optional<PowerUpStatus> getActivePowerUp() {
        return this.activePowerUp;
    }

    /**
     * Called while no game loop is running: resets the model and publishes the empty world.
     */
    @Override
    public void newGame(final DifficultyEnum difficulty) {
        this.pendingSwipes.clear();
        this.model.newGame(difficulty);
        publishSnapshot();
    }

    private void publishSnapshot() {
        this.visibleSliceables = Set.copyOf(this.model.getActiveSliceables());
        this.lives = this.model.getLives();
        this.score = this.model.getScore();
        this.gameOver = this.model.isGameover();
        this.activePowerUp = this.model.getActivePowerUp();
    }

    /**
     * A mouse movement in logical coordinates, waiting to be applied to the model.
     */
    private record Swipe(XY<Double> from, XY<Double> to) { }
}
