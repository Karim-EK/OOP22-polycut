package mvc.model.api;

import java.util.Optional;

public interface GameState {
    /**
     * Score getter
     * @return current score
     */
    int getScore();

    /**
     * Score Setter
     */
    void setScore(int score);

    /**
     * lives getter
     * @return number of lives remaining
     */
    int getLives();

    /**
     * @return If lives are less than 3 will add 1 live, else return false
     */
    boolean addLife();

    /**
     * decrese by 1 number of lives
     */
    void decreseLife();

    /**
     * @return True if lives counter = 0
     */
    boolean hasPlayerLost();

    /**
     * @return True if a PowerUp is on screen
     */
    boolean isPowerUpOnScreen();

    /**
     * @return True if any PowerUp has been sliced and activated
     */
    boolean isPowerUpActive();

    /**
     * @return The type of PowerUp active, if any
     */
    Optional<PowerUpEnum> getActivePowerUp();

    /**
     * @return the state of the game
     */
    GameStateEnum getGameState();

    /**
     * set che state of the game
     */
    void setGameState(GameState gameState);
}
