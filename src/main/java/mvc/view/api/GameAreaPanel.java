package mvc.view.api;

import mvc.model.api.GameWorldListener;

/**
 * The area where the game is drawn. It observes the model's events
 * and is asked to redraw itself by the game loop.
 */
public interface GameAreaPanel extends GameWorldListener {

    /**
     * Requests a new frame to be drawn with the latest state of the game.
     */
    void render();
}
