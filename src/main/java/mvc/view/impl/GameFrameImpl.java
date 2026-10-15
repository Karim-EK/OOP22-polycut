package mvc.view.impl;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import mvc.controller.api.AppController;
import mvc.controller.api.GameWorldController;
import mvc.view.api.GameAreaPanel;
import mvc.view.api.GameFrame;

/**
 * The main window. A CardLayout shows one screen at a time: menu, game or game over.
 */
public class GameFrameImpl extends JFrame implements GameFrame {

    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    // Pausa prima della schermata di game over, per vedere com'è finita la partita
    private static final int GAME_OVER_DELAY_MS = 800;
    private static final String MENU = "menu";
    private static final String GAME = "game";
    private static final String GAME_OVER = "gameOver";

    private final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    private final CardLayout cards = new CardLayout();
    private final JPanel screens = new JPanel(this.cards);
    private final GameAreaPanelImpl gamePanel;
    private final GameOverPanel gameOverPanel;
    private String currentScreen = MENU;

    /**
     * Prepare the Frame to be set visible.
     * @param controller the controller of the game world, used by the game area
     * @param app the controller of the application flow, used by the buttons
     */
    public GameFrameImpl(final GameWorldController controller, final AppController app) {
        super("Poly-Cut");
        this.gamePanel = new GameAreaPanelImpl(controller);
        this.gameOverPanel = new GameOverPanel(app);
        this.screens.add(new MenuPanel(app), MENU);
        this.screens.add(this.gamePanel, GAME);
        this.screens.add(this.gameOverPanel, GAME_OVER);
        this.add(this.screens);

        // ESC durante la partita riporta al menu
        this.gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "backToMenu");
        this.gamePanel.getActionMap().put("backToMenu", new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                app.backToMenu();
            }
        });

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(WIDTH, HEIGHT);
        this.setLocationRelativeTo(null);
    }

    @Override
    public int getScreenWidth() {
        return this.screen.width;
    }

    @Override
    public int getScreenHeight() {
        return this.screen.height;
    }

    @Override
    public GameAreaPanel getGameArea() {
        return this.gamePanel;
    }

    @Override
    public void showMenu() {
        SwingUtilities.invokeLater(() -> show(MENU));
    }

    @Override
    public void showGame() {
        SwingUtilities.invokeLater(() -> show(GAME));
    }

    // Arriva dal thread del game loop: la schermata cambia sul thread di Swing, dopo una breve pausa,
    // e solo se nel frattempo il giocatore non è già tornato al menu
    @Override
    public void showGameOver(final int score) {
        SwingUtilities.invokeLater(() -> {
            final Timer timer = new Timer(GAME_OVER_DELAY_MS, e -> {
                if (GAME.equals(this.currentScreen)) {
                    this.gameOverPanel.showScore(score);
                    show(GAME_OVER);
                }
            });
            timer.setRepeats(false);
            timer.start();
        });
    }

    private void show(final String name) {
        this.currentScreen = name;
        this.cards.show(this.screens, name);
    }
}
