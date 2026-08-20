package mvc.controller.api;

import java.util.Set;

import mvc.model.api.SliceableModel;
import mvc.model.api.XY;

public interface GameWorldController {
    
    // Chiamato dal GameLoop ad ogni frame
    void update(double deltaTime);
    
    // Chiamato dal GameAreaPanel quando l'utente trascina il mouse
    void processSwipe(XY<Double> initialXy, XY<Double> finalXy);
    
    // Chiamato dalla View per ottenere i dati da disegnare (in Sola Lettura!)
    Set<SliceableModel> getVisibleSliceables();
}
