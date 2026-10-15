package mvc.view.api;

/**
 * The main window of the game: it shows one screen at a time.
 * The methods can be called from any thread.
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

    /**
     * Shows the main menu.
     */
    void showMenu();

    /**
     * Shows the game area.
     */
    void showGame();

    /**
     * Shows the game over screen.
     *
     * @param score the final score of the game
     * @param record the best score of the level just played, this game included
     * @param newRecord true if this game set the record
     */
    void showGameOver(int score, int record, boolean newRecord);

    /**
     * Closes the window and the application.
     */
    void close();

    /**
     * Changes the size of the window; it has effect when the game is not full screen.
     *
     * @param width in pixels
     * @param height in pixels
     */
    void setWindowSize(int width, int height);

    /**
     * @param fullScreen true to cover the whole screen, false to go back to a window
     */
    void setFullScreen(boolean fullScreen);

    /**
     * @return true if the game covers the whole screen
     */
    boolean isFullScreen();
}
