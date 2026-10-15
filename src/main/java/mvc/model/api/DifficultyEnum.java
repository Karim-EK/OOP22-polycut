package mvc.model.api;

/**
 * Difficulty levels: they change how often the sliceables are spawned.
 * The interval between two spawns goes from the initial value to the minimum one
 * as the score grows: interval = min + (initial - min) * e^(-decay * score).
 */
public enum DifficultyEnum {
    /**
     * Slow start and slow growth.
     */
    EASY(3.5, 0.9, 0.010),
    /**
     * The original balance of the game.
     */
    NORMAL(3.0, 0.5, 0.015),
    /**
     * Fast start and fast growth.
     */
    HARD(2.2, 0.4, 0.022);

    private final double initialInterval;
    private final double minInterval;
    private final double decay;

    DifficultyEnum(final double initialInterval, final double minInterval, final double decay) {
        this.initialInterval = initialInterval;
        this.minInterval = minInterval;
        this.decay = decay;
    }

    /**
     * @return seconds between two spawns at score 0
     */
    public double getInitialInterval() {
        return this.initialInterval;
    }

    /**
     * @return the shortest interval between two spawns, in seconds
     */
    public double getMinInterval() {
        return this.minInterval;
    }

    /**
     * @return how fast the interval shrinks as the score grows
     */
    public double getDecay() {
        return this.decay;
    }
}
