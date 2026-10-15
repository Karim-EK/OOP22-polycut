package mvc.model.api;

/**
 * Immutable picture of the active power-up, safe to pass to the view.
 *
 * @param powerUp which power-up is active
 * @param timeLeft seconds before it ends
 */
public record PowerUpStatus(PowerUpEnum powerUp, double timeLeft) {

    /**
     * @return the part of the duration still left, from 1 (just activated) to 0 (ended)
     */
    public double fractionLeft() {
        final double duration = this.powerUp.getDuration();
        return duration <= 0 ? 0 : Math.max(0, Math.min(1, this.timeLeft / duration));
    }
}
