package mvc.model.api;

import java.util.Set;

/**
 * Represent the main orchestrator of the Model, it's responsable to update the game state and notify the controller
 */
public interface GameWorld {

    /**
     * for purprose of Dependency Injection pattern
     * @param physics instance of Physics
     * @param spawner instance of Spawner
     * @param sliceHandler instance of SliceHandler
     */
    void initialize(Physics physics, Spawner spawner, SliceHandler sliceHandler);

    /**
     * update the state of the game
     * @param deltaTime refresh rate
     */
    void update(double deltaTime);

    /**
     * Calls the Controller to notify the cut, it's
     * @param cuttedSliceable the sliceable to be removed
     */
    void registerCut(SliceableModel cuttedSliceable);

    /**
     * @return get the Width of the playable area
     */
    double getWorldWidth();

    /**
     * @return get the Height of the playable area
     */
    double getWorldHeight();

    /**
     * @return All the active sliceable
     */
    Set<SliceableModel> getActiveSliceable();
}
