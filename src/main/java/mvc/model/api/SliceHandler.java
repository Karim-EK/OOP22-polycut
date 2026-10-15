package mvc.model.api;

/**
 * Functional Interface for evalueting the movement of the user's input.
 */
@FunctionalInterface
public interface SliceHandler {
    /**
     * Gets called by controller and calculate if the slice is valid.
     * A slice is considered valid if the segment drawn by the mouse passes close enough to the center
     * of the Sliceable: its distance from the center must be at most the apothem times a tolerance.
     *
     * @param sliceable to check if has been cut properly
     * @param startingPoint where the mouse movement starts, in logical coordinates
     * @param endingPoint where the mouse movement ends, in logical coordinates
     * @return True if the cut is legit
     */
    boolean isSliceLegit(SliceableModel sliceable, XY<Double> startingPoint, XY<Double> endingPoint);
}
