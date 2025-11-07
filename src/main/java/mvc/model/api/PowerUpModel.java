package mvc.model.api;

import java.util.Optional;

/**
 * Represent power-ups: beneficial bonuses for the player to obtain, they change the game state significantly.
 */
public interface PowerUpModel extends SliceableModel {
    /**
     * @return the time that remains until the PowerUp ends in millisecons.
     */
    Optional<Double> getTimeReameaning();

    /**
     * @return wich PowerUp.
     */
    PowerUpEnum getPowerUp();

    /**
     * @return true if it's not changhing the game state.
     */
    boolean isEnded();
}
