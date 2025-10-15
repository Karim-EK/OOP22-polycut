package mvc.model.api;

import java.util.Set;

/**
 * Physics class of the project, it contains all the methods useful for
 * the update of the position and velocity of the polygons and bombs.
 */
public interface Physics {
    /**
     * Mathematics method of the class, it calculates the new Point2D coordinates of velocity and
     * position in a trajectory.
     * @param SliceableSet the list of all the polygons present, in this precise moment, in the game world.
     */
    void doMaths(Set<SliceableModel> SliceableSet);
}
