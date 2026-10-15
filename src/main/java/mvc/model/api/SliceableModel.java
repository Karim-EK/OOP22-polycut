package mvc.model.api;

/**
 * Represents every type of Sliceable: element that can be "cutted" by the player.
 */
public interface SliceableModel {

    /**
     * Position getter.
     *
     * @return position
     */
    XY<Double> getPosition();

    /**
     * Position setter.
     *
     * @param position updated by the physics class.
     */
    void setPosition(XY<Double> position);

    /**
     * Velocity getter.
     *
     * @return velocity
     */
    XY<Double> getVelocity();

    /**
     * Velocity setter.
     *
     * @param velocity updated by the physics class.
     */
    void setVelocity(XY<Double> velocity);

    /**
     * @return type of Sliceable
     */
    SliceableTypeEnum getType();

    /**
     * @param worldHeight height of the game world in logical units
     * @return true if the sliceable is out of the game area lower bound, false otherwise.
     */
    boolean isOutOfBound(double worldHeight);

    /**
     * @return true if the Sliceable have been cut.
     */
    boolean hasBeenCut();

    /**
     * @param cutted true when actually cutted by player.
     */
    void setAsCutted(boolean cutted);

    /**
     * @return apothem from logical shape
     */
    double getApothem();
}
