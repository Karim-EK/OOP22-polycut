package mvc.model.api;

/**
 * Functional interface responsible of creating PowerUp objects.
 */
@FunctionalInterface
public interface PowerUpFactory {
    /**
     * Create a random PowerUp.
     * @return new PowerUp
     */
    PowerUpModel createPowerUp();
}
