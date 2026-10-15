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
     * @param score the final score of the game
     */
    void showGameOver(int score);
}
