package mvc.model.api;

import java.util.List;

/**
 * Interface responsable of deciding where and when to spawn Sliceables.
 */
public interface Spawner {
    /**
     * Creates the sliceables of the next launch: a single one, or a wave of 2-3 when the score is high enough.
     * @param score current score, makes waves more frequent
     * @param lives current lives, the 1-Up power-up is spawned only without full lives
     * @param screenWidth width of the game world
     * @param screenHeight height of the game world
     * @param factory for generating the sliceables
     * @param powerUpActiveOrOnScreen if true no power-up is spawned
     * @return the sliceables to launch, never empty
     */
    List<SliceableModel> spawn(int score, int lives, double screenWidth,
        double screenHeight, SliceableModelFactory factory, boolean powerUpActiveOrOnScreen);

    /**
     * @param score current score
     * @param lives current lives
     * @return seconds to wait before the next launch
     */
    double calculateNextSpawnInterval(int score, int lives);

    /**
     * @param difficulty the level chosen in the menu
     */
    void setDifficulty(DifficultyEnum difficulty);
}
