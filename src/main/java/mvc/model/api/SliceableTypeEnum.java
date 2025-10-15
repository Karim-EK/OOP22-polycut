package mvc.model.api;

/**
 * Enum For the Sliceable's Type.
 */
public enum SliceableTypeEnum {
    /**
     * 3 sides.
     */
    TRIANGLE(0.1625),
    /**
     * 4 sides.
     */
    SQUARE(0.1625),
    /**
     * 5 sides.
     */
    PENTAGON(0.1625),
    /**
     * 6 sides.
     */
    HEXAGON(0.1625),
    /**
     * Bomb.
     */
    BOMB(0.25),
    /**
     * PowerUps
     */
    POWERUP(0.1),;
    
    private final double probability;
    SliceableTypeEnum(double probability) {
        this.probability = probability;
    }

    public double getProbability() {
        return probability;
    }
}
