package mvc.view.impl;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import mvc.controller.api.GameWorldController;
import mvc.view.api.UIElement.HealthBar;

public class HealthBarImpl implements HealthBar{

    private static final String ONE_HEARTH = "1heart.png";
    private static final String TWO_HEARTHS = "2hearts.png";
    private static final String TRHEE_HEARTHS = "3hearts.png";
    private static final int FULL_HEALTH = 3;
    private static final int HALF_HEALTH = 2;
    private static final int LOW_HEALTH = 1;

    private GameWorldController controller;
    public HealthBarImpl(GameWorldController controller) {
        this.controller = controller;
    }

    @Override
    public Image getImage() throws IOException {
        switch (controller.getLives()) {
            case FULL_HEALTH:
                return ImageIO.read(HealthBar.class.getClassLoader().getResource(TRHEE_HEARTHS));
            case HALF_HEALTH:
                return ImageIO.read(HealthBar.class.getClassLoader().getResource(TWO_HEARTHS));
            case LOW_HEALTH:
                return ImageIO.read(HealthBar.class.getClassLoader().getResource(ONE_HEARTH));
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public void gainLife() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'gainLife'");
    }

    @Override
    public void loseLife() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loseLife'");
    }
}
