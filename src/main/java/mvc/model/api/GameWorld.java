package mvc.model.api;

import java.util.Set;

/**
 * Represent the main orchestrator of the Model, it's responsable to update the game state and notify the controller.
 */
public interface GameWorld {

    /**
     * update the state of the game.
     * @param deltaTime refresh rate
     */
    void update(double deltaTime);

    /**
     * @return All the active sliceable.
     */
    Set<SliceableModel> getActiveSliceables();

    /**
     * To prevent more than 1 powerUp to Spawn.
     * @return true if active or on screen
     */
    boolean isPowerUpOnScreenOrActive();

    /**
     * adds listener for GameWord's events
     * @param listener
     */
    void addListener(GameWorldListener listener);

    void processSwipe(XY<Double> initialXy, XY<Double> finalXy);
}
