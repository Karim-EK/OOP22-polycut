package mvc.model.api;

public interface GameWorldListener {
    /**
     * notifies when a Sliceable is cut.
     * @param sliceable
     */
    void onSliceableCut(SliceableModel sliceable);

    /**
     * notifies when a powerUp is activated.
     * @param powerUp
     */
    void onPowerUpActivate(PowerUpEnum powerUp);

    /**
     * notifies when a powerUp is deactivated.
     * @param powerUp
     */
    void onPowerUpDeactivate(PowerUpEnum powerUp);

    void onGameOver(int score);
}
