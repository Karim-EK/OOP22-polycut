package mvc.view.impl;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import mvc.controller.api.GameWorldController;
import mvc.view.api.GameAreaPanel;
import mvc.view.api.GameFrame;

public class GameFrameImpl extends JFrame implements GameFrame {
    
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    private final int SCREEN_W = screen.width;
    private final int SCREEN_H = screen.height;

    int width = 1280;
    int height = 720;

    GameAreaPanelImpl gamePanel; 

    /**
     * Prepare the Frame to be set visible.
     * TODO: salvare dimensione schermo preferita
     */
    public GameFrameImpl(GameWorldController controller) {
        gamePanel = new GameAreaPanelImpl(controller);
        this.setTitle("Poly-Cut");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(gamePanel);
        this.setSize(width, height);
        this.setLocationRelativeTo(null);
    }

    @Override
    public int getScreenWidth() {
        return SCREEN_W;
    }

    @Override
    public int getScreenHeight() {
        return SCREEN_H;
    }

    public void update() {
        this.repaint();
    }

    @Override
    public GameAreaPanel getGameArea() {
        return this.gamePanel;
    }
}
