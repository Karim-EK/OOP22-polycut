package mvc.view.impl;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import mvc.controller.api.AppController;
import mvc.controller.api.GameWorldController;
import mvc.view.api.GameAreaPanel;
import mvc.view.api.GameFrame;

/**
 * The main window. A CardLayout shows one screen at a time: menu, game or game over.
 */
public final class GameFrameImpl extends JFrame implements GameFrame {

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
    private final MenuPanel menuPanel;
    private String currentScreen = MENU;
    // Dimensione scelta per la finestra: si ripristina quando si esce dallo schermo intero
    private Dimension windowSize = new Dimension(WIDTH, HEIGHT);
    private boolean fullScreen;

    /**
     * Prepare the Frame to be set visible.
     *
     * @param controller the controller of the game world, used by the game area
     * @param app the controller of the application flow, used by the buttons
     */
    public GameFrameImpl(final GameWorldController controller, final AppController app) {
        super("Poly-Cut");
        this.gamePanel = new GameAreaPanelImpl(controller);
        this.gameOverPanel = new GameOverPanel(app);
        this.menuPanel = new MenuPanel(app, this);
        this.screens.add(this.menuPanel, MENU);
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
        this.setSize(this.windowSize);
        this.setLocationRelativeTo(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getScreenWidth() {
        return this.screen.width;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getScreenHeight() {
        return this.screen.height;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "The game loop needs the real game area to redraw it at every frame"
    )
    public GameAreaPanel getGameArea() {
        return this.gamePanel;
    }

    /**
     * Il menu si aggiorna ogni volta che si apre: il record può essere cambiato con l'ultima partita.
     */
    @Override
    public void showMenu() {
        SwingUtilities.invokeLater(() -> {
            this.menuPanel.refresh();
            showScreen(MENU);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showGame() {
        SwingUtilities.invokeLater(() -> showScreen(GAME));
    }

    /**
     * Arriva dal thread del game loop: la schermata cambia sul thread di Swing, dopo una breve pausa,
     * e solo se nel frattempo il giocatore non è già tornato al menu.
     */
    @Override
    public void showGameOver(final int score, final int record, final boolean newRecord) {
        SwingUtilities.invokeLater(() -> {
            final Timer timer = new Timer(GAME_OVER_DELAY_MS, e -> {
                if (GAME.equals(this.currentScreen)) {
                    this.gameOverPanel.showScore(score, record, newRecord);
                    showScreen(GAME_OVER);
                }
            });
            timer.setRepeats(false);
            timer.start();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        SwingUtilities.invokeLater(() -> this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setWindowSize(final int width, final int height) {
        SwingUtilities.invokeLater(() -> {
            this.windowSize = new Dimension(width, height);
            if (!this.fullScreen) {
                this.setSize(this.windowSize);
                this.setLocationRelativeTo(null);
            }
        });
    }

    /**
     * Schermo intero "senza bordi": la finestra perde la cornice e copre tutto lo schermo.
     * Non usa la modalità esclusiva, così le finestre di dialogo restano visibili sopra il gioco.
     * Per togliere o rimettere la cornice la finestra va prima chiusa (dispose) e poi riaperta.
     */
    @Override
    public void setFullScreen(final boolean enabled) {
        SwingUtilities.invokeLater(() -> {
            if (enabled == this.fullScreen) {
                return;
            }
            this.fullScreen = enabled;
            this.dispose();
            this.setUndecorated(enabled);
            if (enabled) {
                this.setBounds(this.getGraphicsConfiguration().getBounds());
            } else {
                this.setSize(this.windowSize);
                this.setLocationRelativeTo(null);
            }
            this.setVisible(true);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFullScreen() {
        return this.fullScreen;
    }

    private void showScreen(final String name) {
        this.currentScreen = name;
        this.cards.show(this.screens, name);
    }
}
