package mvc.model.api;

/**
 * Difficulty levels: they change how often the sliceables are spawned and when the waves begin.
 * The interval between two spawns goes from the initial value to the minimum one
 * as the score grows: interval = min + (initial - min) * e^(-decay * (score + startingScore)).
 * The starting score is a virtual head start: a level with startingScore 35 begins
 * as if the player already had 35 points, but the real score still starts from 0.
 */
public enum DifficultyEnum {
    /**
     * Slow start and slow growth.
     */
    EASY(3.5, 0.9, 0.010, 10),
    /**
     * The original curve of the game, starting as if the player had 35 points.
     */
    NORMAL(3.0, 0.5, 0.015, 35),
    /**
     * Fast start and fast growth.
     */
    HARD(2.2, 0.4, 0.022, 25);

    private final double initialInterval;
    private final double minInterval;
    private final double decay;
    private final int startingScore;

    DifficultyEnum(final double initialInterval, final double minInterval, final double decay,
            final int startingScore) {
        this.initialInterval = initialInterval;
        this.minInterval = minInterval;
        this.decay = decay;
        this.startingScore = startingScore;
    }

    /**
     * @return seconds between two spawns at virtual score 0
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

    /**
     * @return virtual points added to the real score when computing the difficulty
     */
    public int getStartingScore() {
        return this.startingScore;
    }
}
