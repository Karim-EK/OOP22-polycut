package mvc.model.api;

/**
 * interface modelling the state of the game.
 */
public interface GameState {
    /**
     * Lives at the start of the game, and the maximum the player can have.
     */
    int MAX_LIVES = 3;

    /**
     * Score getter.
     *
     * @return current score
     */
    int getScore();

    /**
     * Score Setter.
     *
     * @param points to add, multiplied by the active multiplier
     */
    void gainScore(int points);

    /**
     * lives getter.
     *
     * @return number of lives remaining
     */
    int getLives();

    /**
     * @return If lives are less than 3 will add 1 live, else return false.
     */
    boolean addLife();

    /**
     * decrese by 1 number of lives.
     */
    void decreseLife();

    /**
     * @return True if lives counter = 0
     */
    boolean hasPlayerLost();

    /**
     * @return the state of the game
     */
    GameStateEnum getGameState();

    /**
     * set che state of the game.
     *
     * @param gameState to change the state of the application
     */
    void setGameState(GameStateEnum gameState);

    /**
     * prevent player from losing lives.
     *
     * @param activate if true
     */
    void setInvulnerability(boolean activate);

    /**
     * multiply the score gained.
     *
     * @param multiplier to wich multiply the score
     */
    void setMultiplier(int multiplier);

    /**
     * @return true while the Safe Zone protects the player from losing lives
     */
    boolean isInvulnerable();

    /**
     * Brings the state back to the start of a game: full lives, score 0, no power-up effects.
     */
    void reset();
}
