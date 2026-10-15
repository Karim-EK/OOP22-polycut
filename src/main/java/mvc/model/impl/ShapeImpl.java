package mvc.model.impl;

import mvc.model.api.Shape;
import mvc.model.api.SliceableTypeEnum;

/**
 * Implementation of Shape. Every shape is built from the same circumradius
 * (radius of the circumscribed circle), so all shapes have the same size on screen.
 */
public final class ShapeImpl implements Shape {
    private final double circumradius;
    private final double apothem;
    private final SliceableTypeEnum type;

    /**
     * @param type of the sliceable
     * @param circumradius radius of the circle circumscribed to the shape
     */
    ShapeImpl(final SliceableTypeEnum type, final double circumradius) {
        this.type = type;
        this.circumradius = circumradius;
        this.apothem = calculateApothem();
    }

    private double calculateApothem() {
        return switch (this.type) {
            case POWERUP, BOMB -> this.circumradius;
            case TRIANGLE, SQUARE, PENTAGON, HEXAGON -> calculatePolygonApothem(this.type.getSides());
        };
    }

    // In un poligono regolare di n lati: apotema = R * cos(PI / n)
    private double calculatePolygonApothem(final int sides) {
        return this.circumradius * Math.cos(Math.PI / sides);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getApothem() {
        return this.apothem;
    }
}
