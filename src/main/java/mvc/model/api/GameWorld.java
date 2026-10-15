package mvc.model.api;

import java.util.Optional;
import java.util.Set;

/**
 * Represent the main orchestrator of the Model, it's responsable to update the game state and notify the controller.
 */
public interface GameWorld {

    /**
     * Width of the game world in logical units, independent from the window size.
     */
    double WIDTH = 160.0;

    /**
     * Height of the game world in logical units, independent from the window size.
     */
    double HEIGHT = 90.0;

    /**
     * update the state of the game.
     *
     * @param deltaTime refresh rate
     */
    void update(double deltaTime);

    /**
     * @return All the active sliceable.
     */
    Set<SliceableModel> getActiveSliceables();

    /**
     * To prevent more than 1 powerUp to Spawn.
     *
     * @return true if active or on screen
     */
    boolean isPowerUpOnScreenOrActive();

    /**
     * adds listener for GameWord's events.
     *
     * @param listener the observer to notify
     */
    void addListener(GameWorldListener listener);

    /**
     * Cuts every sliceable crossed by the mouse movement.
     *
     * @param initialXy where the movement starts, in logical coordinates
     * @param finalXy where the movement ends, in logical coordinates
     */
    void processSwipe(XY<Double> initialXy, XY<Double> finalXy);

    /**
     * @return true if the player has no lives left
     */
    boolean isGameOver();

    /**
     * @return the lives of the player
     */
    int getLives();

    /**
     * @return the current score
     */
    int getScore();

    /**
     * @return the power-up with a duration that is active now, if any
     */
    Optional<PowerUpStatus> getActivePowerUp();

    /**
     * Clears the world and starts a new game.
     *
     * @param difficulty the level chosen in the menu
     */
    void newGame(DifficultyEnum difficulty);
}
