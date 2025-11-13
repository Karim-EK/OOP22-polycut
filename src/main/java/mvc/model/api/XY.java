package mvc.model.api;

/**
 * Simple position record to avoid using Point2D and doubles.
 * @param x first element
 * @param y second element
 * @param <T> type for Pair
 */
public record XY<T>(T x, T y) { }
