package mvc.model.api;

import java.util.Optional;

/**
 * Enum with all power-ups.
 */
public enum PowerUpEnum {
    /**
     * Grants a x2 moltiplier for points.
     */
    X2(Optional.of(7.0), 0.5),
    /**
     * Block elements on the screen for some time.
     */
    FREEZE(Optional.of(5.0), 0.2),
    /**
     * Stop the spawn of new Bombs.
     */
    SAFE_ZONE(Optional.of(8.0), 0.2),
    /**
     * Grants an additional live to the player.
     */
    ONE_UP(Optional.empty(), 0.1);

    private final Optional<Double> duration;
    private final double probability;
    PowerUpEnum(final Optional<Double> duaration, final double probability) {
        this.duration = duaration;
        this.probability = probability;
    }
    /**
     * @return duration of PowerUp.
     */
    public Optional<Double> getDuration() {
        return this.duration;
    }
    /**
     * @return the chance of spawning the specific PowerUp.
     */
    public double getProbability() {
        return this.probability;
    }
}
