package mvc.model.api;

import java.util.Optional;

/**
 * Functional Interface responsable od deciding where and when to spawn Sliceables
 */
@FunctionalInterface
public interface Spawner {
    /**
     * Decide when to spawn a Sliceable considering the user's score and the screen size, calls {@link mvc.model.api.SliceableModelFactory#createSliceable()}
     * @param score to decide what to spawn
     * @param screenWidth for proper calculus
     * @param screenHeight for proper calculus
     * @param factory for generating the sliceable
     * @return the sliceable
     */
    Optional<SliceableModel> spawn(int score, double screenWidth, double screenHeight, SliceableModelFactory factory);
}
