package mvc.model.api;

import java.util.Set;

/**
 * Physics class of the project, it contains all the methods useful for
 * the update of the position and velocity of Sliceables.
 */
public interface Physics {
    /**
     * It calculates the new Point2D coordinates of velocity and position in a trajectory.
     * @param SliceableSet the set of all the Sliceable present, in this precise moment, in the game world.
     * @param deltatime time passed from the last update
     */
    void updatePhysics(Set<SliceableModel> SliceableSet, double deltaTime);
}
