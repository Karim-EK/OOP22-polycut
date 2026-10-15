package mvc.controller.api;

import java.util.Set;

import mvc.model.api.DifficultyEnum;
import mvc.model.api.SliceableModel;
import mvc.model.api.XY;

public interface GameWorldController {
    
    // Chiamato dal GameLoop ad ogni frame
    void update(double deltaTime);
    
    // Chiamato dal GameAreaPanel (thread di Swing) quando l'utente trascina il mouse:
    // lo swipe viene messo in coda e applicato al model al prossimo update
    void processSwipe(XY<Double> initialXy, XY<Double> finalXy);

    // Chiamato dalla View per ottenere i dati da disegnare: copia immutabile dell'ultimo frame
    Set<SliceableModel> getVisibleSliceables();

    boolean isGameOver();

    int getLives();

    int getScore();

    // Chiamato prima di avviare il game loop di una nuova partita
    void newGame(DifficultyEnum difficulty);
}
