package mvc.controller.api;

import java.awt.geom.Point2D;
import java.util.Map;

import mvc.model.api.SliceableModel;
import mvc.view.api.SliceableView;

/**
 * Main controller of the game. Manages all the model and view of the game.
 * @param <X>
 * @param <Y>
 */
public interface GameWorldController{

    record Pair<X, Y> (X x, Y y) {}

    /**
     * @return a map of both Sliceable representations.
     */
    Map<SliceableModel, SliceableView> getSliceableMap();

    /**
     * Update the position of a sliceable on screen
     * @param sliceableMap
     * @param newPosition
     */
    void updateSliceablePosition(Map<SliceableModel, SliceableView> sliceableMap, Point2D newPosition);

    /**
     * Create both a SliceableModel and SliceableView objects and add it to the Map.
     * @return
     */
    void createSliceable();

    /**
     * Initialize the game screen and starts the game loop.
     */
    void startLoop();

    /**
     * Delete a sliceable both logically and graphically.
     * @param sliceable to be deleted.
     */
    void deleteSliceable(Pair<SliceableModel, SliceableView> sliceable);
    
}