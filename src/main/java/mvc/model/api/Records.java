package mvc.model.api;

/**
 * Best score of every difficulty level, kept between one execution of the game and the next.
 */
public interface Records {

    /**
     * @param difficulty the level
     * @return the best score of that level, 0 if it was never played
     */
    int getRecord(DifficultyEnum difficulty);

    /**
     * Saves the score if it beats the record of its level.
     *
     * @param difficulty the level of the game just finished
     * @param score the final score
     * @return true if the score is a new record
     */
    boolean submit(DifficultyEnum difficulty, int score);
}
