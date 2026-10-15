package mvc.controller.api;

import java.util.Optional;
import java.util.Set;

import mvc.model.api.DifficultyEnum;
import mvc.model.api.PowerUpStatus;
import mvc.model.api.SliceableModel;
import mvc.model.api.XY;

/**
 * Controller of the game world: it drives the model and gives the view what to draw.
 */
public interface GameWorldController {

    /**
     * Chiamato dal GameLoop ad ogni frame.
     *
     * @param deltaTime seconds passed from the last frame
     */
    void update(double deltaTime);

    /**
     * Chiamato dal GameAreaPanel (thread di Swing) quando l'utente trascina il mouse:
     * lo swipe viene messo in coda e applicato al model al prossimo update.
     *
     * @param initialXy where the movement starts, in logical coordinates
     * @param finalXy where the movement ends, in logical coordinates
     */
    void processSwipe(XY<Double> initialXy, XY<Double> finalXy);

    /**
     * Chiamato dalla View per ottenere i dati da disegnare: copia immutabile dell'ultimo frame.
     *
     * @return the sliceables on screen
     */
    Set<SliceableModel> getVisibleSliceables();

    /**
     * @return true if the player has no lives left
     */
    boolean isGameOver();

    /**
     * @return the lives of the player
     */
    int getLives();

    /**
     * @return the current score
     */
    int getScore();

    /**
     * Power-up a tempo attivo nell'ultimo frame, per il timer in alto al centro.
     *
     * @return the active power-up, if any
     */
    Optional<PowerUpStatus> getActivePowerUp();

    /**
     * Chiamato prima di avviare il game loop di una nuova partita.
     *
     * @param difficulty the level chosen in the menu
     */
    void newGame(DifficultyEnum difficulty);
}
