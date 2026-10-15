package mvc.model.api;

/**
 * Enum with all power-ups.
 */
public enum PowerUpEnum {
    ONE_UP(0.0),       // Istantaneo
    SAFE_ZONE(5.0),    // 5 secondi
    X2(10.0),          // 10 secondi
    FREEZE(3.0);       // 3 secondi

    private final double duration;

    PowerUpEnum(final double duaration) {
        this.duration = duaration;
    }

    /**
     * @return duration of PowerUp.
     */
    public double getDuration() {
        return this.duration;
    }
}
