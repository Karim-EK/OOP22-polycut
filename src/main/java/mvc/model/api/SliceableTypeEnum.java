package mvc.model.api;

/**
 * Enum For the Sliceable's Type.
 */
public enum SliceableTypeEnum {
    /**
     * 3 sides.
     */
    TRIANGLE(3),
    /**
     * 4 sides.
     */
    SQUARE(4),
    /**
     * 5 sides.
     */
    PENTAGON(5),
    /**
     * 6 sides.
     */
    HEXAGON(6),
    /**
     * Bomb.
     */
    BOMB(0),
    /**
     * PowerUps.
     */
    POWERUP(0);

    private final int sides;

    SliceableTypeEnum(final int sides) {
        this.sides = sides;
    }

    /**
     * @return number of sides of the polygon, 0 for the round sliceables (bombs and power-ups)
     */
    public int getSides() {
        return this.sides;
    }
}
