package mvc.model.api;

@FunctionalInterface
public interface SliceableModelFactory {

    /**
     * Create a random Sliceable including PowerUps.
     * @return new Sliceable.
     */
    public SliceableModel createSliceable();
}