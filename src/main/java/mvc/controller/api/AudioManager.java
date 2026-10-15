package mvc.controller.api;

import mvc.model.api.GameWorldListener;

/**
 * Plays the background music and the sound effects.
 * It observes the model, so every game event has its own sound.
 */
public interface AudioManager extends GameWorldListener {

    /**
     * Highest volume; 0 means muted.
     */
    int MAX_VOLUME = 100;

    /**
     * Starts the background music in loop, if its volume is not 0 and it is not already playing.
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
     * @param volume of the background music, from 0 (muted) to MAX_VOLUME
     */
    void setMusicVolume(int volume);

    /**
     * @return the volume of the background music, from 0 to MAX_VOLUME
     */
    int getMusicVolume();

    /**
     * @param volume of the sound effects, from 0 (muted) to MAX_VOLUME
     */
    void setEffectsVolume(int volume);

    /**
     * @return the volume of the sound effects, from 0 to MAX_VOLUME
     */
    int getEffectsVolume();
}
