package mvc.model.api;

/**
 * Public Enum to define the game state
 */
public enum GameStateEnum {
    /**
     * User has lost all lives
     */
    GAMEOVER,

    /**
     * User has paused the game
     */
    PAUSED,

    /**
     * User is playing normally
     */
    RUNNING
}
