package mvc.model.api;

/**
 * responsible of creating SliceableModel objects.
 */
public interface SliceableModelFactory {

    /**
     * Create a Sliceable excluding PowerUps.
     *
     * @param type of sliceable to create
     * @param positionXy the coordinates to spawn the Sliceable
     * @param velocityXy the initial velocity for the Sliceable
     * @return new Sliceable
     */
    SliceableModel createSliceable(SliceableTypeEnum type, XY<Double> positionXy, XY<Double> velocityXy);

    /**
     * Create a PowerUp.
     *
     * @param powerUp which power-up to create
     * @param positionXy the coordinates to spawn the PowerUp
     * @param velocityXy the initial velocity for the PowerUp
     * @return new PowerUp
     */
    PowerUpModel createPowerUp(PowerUpEnum powerUp, XY<Double> positionXy, XY<Double> velocityXy);
}
