package mvc.model.api;

/**
 * Represent power-ups: beneficial bonuses for the player to obtain, they change the game state significantly.
 */
public interface PowerUpModel extends SliceableModel {
    /**
     * @return the time that remains until the PowerUp ends in millisecons.
     */
    double getTimeReameaning();

    /**
     * @return wich PowerUp.
     */
    PowerUpEnum getPowerUp();

    /**
     * @return true if it's not changhing the game state.
     */
    boolean isEnded();
}
