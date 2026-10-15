package mvc.model.api;

/**
 * Observer of the game world: it is notified of every relevant event of the model.
 */
public interface GameWorldListener {
    /**
     * notifies when a Sliceable is cut.
     *
     * @param sliceable the sliceable just cut
     */
    void onSliceableCut(SliceableModel sliceable);

    /**
     * notifies when a powerUp is activated.
     *
     * @param powerUp the power-up just activated
     */
    void onPowerUpActivate(PowerUpEnum powerUp);

    /**
     * notifies when a powerUp is deactivated.
     *
     * @param powerUp the power-up that just ended
     */
    void onPowerUpDeactivate(PowerUpEnum powerUp);

    /**
     * notifies when the player has no lives left, only once per game.
     *
     * @param score the final score of the game
     */
    void onGameOver(int score);
}
