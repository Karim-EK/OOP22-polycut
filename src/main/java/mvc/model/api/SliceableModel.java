package mvc.model.api;

import mvc.model.impl.PositionXY;

/**
 * Represents every type of Sliceable: element that can be "cutted" by the player.
 */
public interface SliceableModel {

    /**
     * Position getter.
     *
     * @return position
     */
    PositionXY getPosition();

    /**
     * Position setter.
     * @param position updated by the physics class.
     */
    void setPosition(PositionXY position);

    /**
     * Velocity getter.
     * @return velocity
     */
    PositionXY getVelocity();

    /**
     * Velocity setter.
     * @param velocity updated by the physics class.
     */
    void setVelocity(PositionXY velocity);

    /**
     * @return type of Sliceable
     */
    SliceableTypeEnum getType();

    /**
     * @return true if the sliceable is out of the game area lower bound, false otherwise.
     */
    boolean isOutOfBound();

    /**
     * @return true if the Sliceable have been cut.
     */
    boolean hasBeenCut();

}
