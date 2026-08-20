package mvc.view.api;

import mvc.view.impl.GameAreaPanelImpl;

public interface GameFrame {
    /**
     * @return user's screen's width.
     */
    int getScreenWidth();

    
    /**
     * @return user's screen's height.
     */
    int getScreenHeight();

    /**
     * @return the game area
     */
    GameAreaPanelImpl getGameArea();
}
