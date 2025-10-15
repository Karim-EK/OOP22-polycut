package mvc.view.api;

import java.awt.geom.Point2D;

/**
 * Interface that defines the frame where the game is displayed.
 */
public interface GameScreen {

    /**
     * Prepare the frame and main JPanel where objects will be drawn.
     */
    void createAndShowGui();

    /**
     * Add and draw a new sliceable on screen.
     * @param newSliceable
     */
    void addNewSliceable(SliceableView newSliceable);

    /**
     * draw the new life total -1.
     */
    void loseLife();

    /**
     * draw the new life total +1.
     */
    void gainLife();

    /**
     * 
     * @param sliceable
     * @param newPosition
     */
    void updateSliceablePosition(SliceableView sliceable, Point2D newPosition);

    // TODO: metti in controller
    // void setNewBestScore(int record);
    // TODO: metti in controller
    // int getCurrentBestScore();
    // void displayGameOverPanel();
}
