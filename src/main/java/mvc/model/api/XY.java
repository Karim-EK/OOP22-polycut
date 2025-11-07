package mvc.model.api;

/**
 * Simple position record to avoid using Point2D and doubles.
 */
public record XY<T>(T x, T y) {}
