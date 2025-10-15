package mvc.view.impl;

import java.awt.Image;

import java.awt.geom.Point2D;

import mvc.model.api.SliceableTypeEnum;
import mvc.view.api.SliceableView;

/**
 * {@inheritDoc}.
 */
public class SliceableViewImpl implements SliceableView {

    
    // Public width, same for every sliceable.
    public static final int SLICEABLE_WIDTH = 100;
    
    private static final int HEXAGON_HEIGHT = 113;
    private static final int PENTAGON_HEIGHT = 97;
    private static final int SQUARE_HEIGHT = 98;
    private static final int TRIANGLE_HEIGHT = 88;
    private static final int BOMB_HEIGHT = 98;

    private Point2D position;
    private final Image image;
    private final SliceableTypeEnum type;
    private boolean gotCutted;

    /**
     * @param point2d position on the screen.
     * @param image associeted image.
     * @param sliceableTypeEnum type of Sliceable.
     */
    public SliceableViewImpl(Point2D point2d, Image image, SliceableTypeEnum sliceableTypeEnum) {
        this.position = point2d;
        this.image = image;
        this.type = sliceableTypeEnum;
        this.gotCutted = false;
    }
    
    /**
     * {@inheritDoc}.
     */
    @Override
    public Point2D getPosition() {
        return this.position;
    }
    
    /** TODO: scope da definirsi.
     * Change the graphical position.
     * @param newPosition to be draw.
     */ 
    @Override
    public void setPosition(Point2D newPosition) {
        this.position = newPosition;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public boolean hasBeenCutted() {
        return this.gotCutted;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public Image getImage() {
        return this.image;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public SliceableTypeEnum getType() {
       return this.type;
    }

     /**
     * @param sliceableType
     * @return the height associated with the polygon
     */
    public static int getSliceableHeight(final SliceableTypeEnum sliceableType) {
        switch (sliceableType) {
            case TRIANGLE:
                return TRIANGLE_HEIGHT;
            case SQUARE:
                return SQUARE_HEIGHT;
            case PENTAGON:
                return PENTAGON_HEIGHT;
            case HEXAGON:
                return HEXAGON_HEIGHT;
            case BOMB:
                return BOMB_HEIGHT;
            default:
                return -1;
        }
    }

    @Override
    public boolean isOnScreen() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isOnScreen'");
    }
}
