package mvc.view.api;

/**
 * Interface that represents the Player's score.
 */
public interface ScoreView {

    /**
     * Gets the shown player's score.
     * @return The score of the Player.
     */
    int getScore();

    /**
     * Draw the current score.
     */
    void drawScore();

    /**
     * Increease the player's score.
     */
    void increaseScore();
}
