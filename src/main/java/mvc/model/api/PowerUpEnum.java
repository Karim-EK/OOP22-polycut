package mvc.model.api;

/**
 * Enum with all power-ups.
 */
public enum PowerUpEnum {
    /**
     * Grants a x2 moltiplier for points.
     */
    X2(7, 0.5),
    /**
     * Block elements on the screen for some time.
     */
    FREEZE(5, 0.2),
    /**
     * Stop the spawn of new Bombs.
     */
    SAFE_ZONE(8, 0.2),
    /**
     * Grants an additional live to the player.
     */
    ONE_UP(0, 0.1);

    private double duration;
    private double probability;
    private PowerUpEnum(double duaration, double probability) {
        this.duration = duaration;
        this.probability = probability;
    }
    /**
     * @return duration of PowerUp.
     */
    public double getDuration() {
        return this.duration;
    }
    /**
     * @return the chance of spawning the specific PowerUp.
     */
    public double getProbability() {
        return this.probability;
    }
}