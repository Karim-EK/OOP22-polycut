package mvc.controller.api;

import mvc.model.api.GameWorldListener;

/**
 * Plays the background music and the sound effects.
 * It observes the model, so every game event has its own sound.
 */
public interface AudioManager extends GameWorldListener {

    /**
     * Starts the background music in loop, if the music is enabled and not already playing.
     */
    void startMusic();

    /**
     * Stops the background music.
     */
    void stopMusic();

    /**
     * Plays the sound of a menu button.
     */
    void playClick();

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
}
