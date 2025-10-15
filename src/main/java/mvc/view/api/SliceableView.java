package mvc.view.api;

import java.awt.Image;
import java.awt.geom.Point2D;

import mvc.model.api.SliceableTypeEnum;

/**
 * The graphic rappresentation of sliceable objects.
 */
public interface SliceableView {
    /**
     * @return the position on the screen.
     */
    Point2D getPosition();

    void setPosition(Point2D newPosition);

    /**
     * @return true if cutted.
     */
    boolean hasBeenCutted();

    /**
     * @return the Icon to be draw.
     */
    Image getImage();

    /**
     * @return the type of Sliceable.
     */
    SliceableTypeEnum getType();

    /**
     * @return true if inside screen boards.
     */
    boolean isOnScreen();
}
