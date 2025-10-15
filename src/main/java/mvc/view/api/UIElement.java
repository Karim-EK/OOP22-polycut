package mvc.view.api;

import java.awt.Image;
import java.io.IOException;

/**
 * The graphical UI elements on screen.
 */
public interface UIElement {

    /**
     * @return the Image of the element.
     */
    Image getImage() throws IOException;

    public interface HealthBar extends UIElement {
        /**
         * gain 1 life;
         */
        void gainLife();

        /**
         * lose 1 
         */
        void loseLife();
    }
    public interface Score extends UIElement {
        /**
         * update the current score.
         */
        void updateScore();
    }
}
