package mvc.view.api;

/**
 * The main window of the game.
 */
public interface GameFrame {
    /**
     * @return user's screen's width.
     */
    int getScreenWidth();

    /**
     * @return user's screen's height.
     */
    int getScreenHeight();

    /**
     * @return the game area
     */
    GameAreaPanel getGameArea();
}
