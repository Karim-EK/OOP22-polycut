package mvc.model.api;

/**
 * Functional interface responsible of creating SliceableModel objects
 */
@FunctionalInterface
public interface SliceableModelFactory {

    /**
     * Create a random Sliceable excluding PowerUps.
     * @return new Sliceable.
     */
    public SliceableModel createSliceable(final SliceableTypeEnum type, final XY<Double> positionXY, final XY<Double> velocityXY);
}