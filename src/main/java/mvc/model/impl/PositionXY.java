package mvc.model.impl;

/**
 * Simple position record to avoid using Point2D and doubles.
 */
public record PositionXY(int x, int y) {}
