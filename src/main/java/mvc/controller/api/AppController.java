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
     * @param difficulty the level
     * @return the best score ever made at that level
     */
    int getRecord(DifficultyEnum difficulty);

    /**
     * @param volume of the background music, from 0 (muted) to AudioManager.MAX_VOLUME
     */
    void setMusicVolume(int volume);

    /**
     * @return the volume of the background music
     */
    int getMusicVolume();

    /**
     * @param volume of the sound effects, from 0 (muted) to AudioManager.MAX_VOLUME
     */
    void setEffectsVolume(int volume);

    /**
     * @return the volume of the sound effects
     */
    int getEffectsVolume();

    /**
     * Plays the sound of a menu button.
     */
    void playClick();
}
