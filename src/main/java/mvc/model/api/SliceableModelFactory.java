package mvc.model.api;

/**
 * responsible of creating SliceableModel objects.
 */
public interface SliceableModelFactory {

    /**
     * Create a random Sliceable excluding PowerUps.
     * @param type of sliceable to create
     * @param positionXy the coordinates to spawn the Sliceable
     * @param velocityXy the initial velocity for the Sliceable
     * @return new Sliceable
     */
    SliceableModel createSliceable(SliceableTypeEnum type, XY<Double> positionXy, XY<Double> velocityXy);
}
