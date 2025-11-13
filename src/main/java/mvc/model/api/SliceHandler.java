package mvc.model.api;

/**
 * Functional Interface for evalueting the movement of the user's input.
 */
@FunctionalInterface
public interface SliceHandler {
    /**
     * Gets called by controller and calculate if the slice is valid.
     * A slice is considered valid if the lenght of the cut is long about 30% of the maximum lenght of the Sliceable, at least
     * @param sliceable to check if has been cut properly
     * @param startingPoint
     * @param endingPoint
     * @return True if the cut is legit
     */
    boolean isSliceLegit(SliceableModel sliceable, XY<Double> startingPoint, XY<Double> endingPoint);
}
