package mvc.model.impl;

import mvc.model.api.Shape;
import mvc.model.api.SliceableTypeEnum;

/**
 * Implementation of Shape api.
 */
public class ShapeImpl implements Shape {
    private static final double HEXAGON_SIDES = 6;
    private static final double PENTAGON_SIDES = 5;
    private static final double SQUARE_SIDES = 4;
    private static final double TRIANGLE_SIDES = 3;
    private static final double PENTAGON_AREA_FACTOR = 1.72;
    private static final double HEXAGON_AREA_FACTOR = 2.598;
    private final double lenght;
    private final double perimeter;
    private final double area;
    private final double apothem;
    private final SliceableTypeEnum type;
    /**
     * Calculate and setup useful geometrical values.
     * @param type
     * @param lenght
     */
    ShapeImpl(final SliceableTypeEnum type, final double lenght) {
        this.type = type;
        this.lenght = lenght;
        this.perimeter = perimeter();
        this.area = area();
        this.apothem = apothem();
    }

    private double apothem() {
        return switch (this.type) {
            case SliceableTypeEnum.POWERUP -> this.lenght;
            default -> this.area * 2 / this.perimeter * 2;
        };
    }
    private double area() {
        return switch (this.type) {
            case SliceableTypeEnum.TRIANGLE -> Math.pow(this.lenght, 2) * Math.sqrt(3) / 4;
            case SliceableTypeEnum.SQUARE -> Math.pow(this.lenght, 2);
            case SliceableTypeEnum.PENTAGON -> Math.pow(this.lenght, 2) * PENTAGON_AREA_FACTOR;
            case SliceableTypeEnum.HEXAGON -> Math.pow(this.lenght, 2) * HEXAGON_AREA_FACTOR;
            default -> Math.PI * Math.pow(this.lenght, 2);
        };
    }
    private double perimeter() {
        return switch (this.type) {
            case SliceableTypeEnum.TRIANGLE -> this.lenght * TRIANGLE_SIDES;
            case SliceableTypeEnum.SQUARE -> this.lenght * SQUARE_SIDES;
            case SliceableTypeEnum.PENTAGON -> this.lenght * PENTAGON_SIDES;
            case SliceableTypeEnum.HEXAGON -> this.lenght * HEXAGON_SIDES;
            default -> this.lenght;
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getApothem() {
        return this.apothem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getLenght() {
        return this.lenght;
    }
}
