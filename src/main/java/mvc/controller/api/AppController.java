package mvc.controller.api;

import mvc.model.api.DifficultyEnum;

/**
 * Controls the application flow: menu, game and game over screens, options and difficulty.
 * The view calls it when the user presses a button.
 */
public interface AppController {

    /**
     * Starts a new game with the chosen difficulty.
     */
    void startGame();

    /**
     * Stops the current game, if any, and shows the menu.
     */
    void backToMenu();

    /**
     * Closes the application.
     */
    void quit();

    /**
     * @return the difficulty used by the next game
     */
    DifficultyEnum getDifficulty();

    /**
     * @param difficulty the difficulty for the next game
     */
    void setDifficulty(DifficultyEnum difficulty);

    /**
     * @param enabled true to play the background music
     */
    void setMusicEnabled(boolean enabled);

    /**
     * @return true if the background music is enabled
     */
    boolean isMusicEnabled();

    /**
     * @param enabled true to play the sound effects
     */
    void setEffectsEnabled(boolean enabled);

    /**
     * @return true if the sound effects are enabled
     */
    boolean isEffectsEnabled();

    /**
     * Plays the sound of a menu button.
     */
    void playClick();
}
