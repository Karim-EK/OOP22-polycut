package mvc.model.impl;

import mvc.model.api.Shape;
import mvc.model.api.SliceableTypeEnum;

public class ShapeImpl implements Shape {
    private final double length;
    private final double apothem;
    private final SliceableTypeEnum type;

    ShapeImpl(final SliceableTypeEnum type, final double length) {
        this.type = type;
        this.length = length;
        this.apothem = calculateApothem();
    }

    private double calculateApothem() {
        return switch (this.type) {
            case POWERUP, BOMB -> this.length;
            case TRIANGLE -> calculatePolygonApothem(3);
            case SQUARE -> calculatePolygonApothem(4);
            case PENTAGON -> calculatePolygonApothem(5);
            case HEXAGON -> calculatePolygonApothem(6);
            default -> throw new IllegalArgumentException("Shape non supportata");
        };
    }

    private double calculatePolygonApothem(int sides) {
        return this.length / (2 * Math.tan(Math.PI / sides));
    }

    @Override
    public double getApothem() {
        return this.apothem;
    }
}