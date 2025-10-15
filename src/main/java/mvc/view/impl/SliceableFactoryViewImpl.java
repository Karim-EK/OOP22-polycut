package mvc.view.impl;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Optional;
import java.awt.Image;

import javax.imageio.ImageIO;

import mvc.model.api.PowerUpEnum;
import mvc.model.api.SliceableTypeEnum;
import mvc.view.api.SliceableFactoryView;
import mvc.view.api.SliceableView;

/**
 * Utility class for the correct visualization of Sliceables.
 */
public final class SliceableFactoryViewImpl implements SliceableFactoryView {
    
    private static final String TRIANGLE = "triangle.png";
    private static final String SQUARE = "square.png";
    private static final String PENTAGON = "pentagon.png";
    private static final String HEXAGON = "hexagon.png";
    private static final String BOMB = "bomb.png";
    private static final String X2 = "x2.png";
    private static final String FREEZE = "freeze.png";
    private static final String SAFE_ZONE = "safeZone.png";
    private static final String ONE_UP = "1up.png";

    private SliceableFactoryViewImpl() { }

    /**
     * {@inheritDoc}.
     */
    @Override
    public SliceableView createSliceable(Point2D point2d, SliceableTypeEnum sliceableTypeEnum) throws IOException {
        return new SliceableViewImpl(point2d, getImage(sliceableTypeEnum), sliceableTypeEnum);
    }

    // TODO: lascia caricamento immagine al controller
    private Image getImage(final SliceableTypeEnum sliceableType) throws IOException {
        Optional<String> urlString;
        Optional<PowerUpEnum> powerUpType = Optional.empty();
        if (sliceableType.name() == SliceableTypeEnum.POWERUP.name()) {
            // TODO: get the type of powerUp from the controller, maybe statically.
        }
        switch (sliceableType) {
            case TRIANGLE:
                urlString = Optional.of(TRIANGLE);
                break;
            case SQUARE:
                urlString = Optional.of(SQUARE);
                break;
            case PENTAGON:
                urlString = Optional.of(PENTAGON);
                break;
            case HEXAGON:
                urlString = Optional.of(HEXAGON);
                break;
            case BOMB:
                urlString = Optional.of(BOMB);
                break;
            default:
                urlString = Optional.empty();
                break;
        }
        if (powerUpType.isPresent()) {
            switch (powerUpType.get()) {
                case X2:
                    urlString = Optional.of(X2);
                    break;
                case FREEZE:
                    urlString = Optional.of(FREEZE);
                    break;
                case SAFE_ZONE:
                    urlString = Optional.of(SAFE_ZONE);
                    break;
                case ONE_UP:
                    urlString = Optional.of(ONE_UP);
                    break;
                default:
                    urlString = Optional.empty();
                    break;
            }
        }
        if (urlString.isEmpty()) {
            throw new IllegalStateException();
        }
        return ImageIO.read(SliceableFactoryViewImpl.class.getClassLoader().getResource(urlString.get()));
    }
}
