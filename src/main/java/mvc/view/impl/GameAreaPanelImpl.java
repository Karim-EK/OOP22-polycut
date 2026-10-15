package mvc.view.impl;

import static mvc.view.impl.GameAreaHelper.colorOf;
import static mvc.view.impl.GameAreaHelper.createPolygon;
import static mvc.view.impl.GameAreaHelper.drawPowerUp;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Optional;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import mvc.controller.api.GameWorldController;
import mvc.model.api.GameWorld;
import mvc.model.api.PowerUpEnum;
import mvc.model.api.PowerUpModel;
import mvc.model.api.SliceableModel;
import mvc.model.api.SliceableTypeEnum;
import mvc.model.api.XY;
import mvc.view.api.GameAreaPanel;

/**
 * The game area: background, lives, score, sliceables and the timer of the active power-up.
 * It reads the immutable snapshot published by the controller and sends it the mouse movements.
 */
public final class GameAreaPanelImpl extends JPanel implements GameAreaPanel {

    private static final long serialVersionUID = 1L;

    // I cuori sono un'immagine fissa alta 60 pixel: il punteggio si centra sulla stessa riga
    private static final int HEARTS_HEIGHT = 60;
    private static final float SCORE_SIZE = 40f;
    private static final int SCORE_MARGIN = 20;

    // Le immagini non sono serializzabili: transient, come richiede Swing per i campi dei componenti
    private final transient Optional<Image> bombImage = loadImage("/GraphicElements/bomb.png");
    private final transient Optional<Image> image1L = loadImage("/GraphicElements/heart.png");
    private final transient Optional<Image> image2L = loadImage("/GraphicElements/2hearts.png");
    private final transient Optional<Image> image3L = loadImage("/GraphicElements/3hearts.png");

    private final transient PowerUpBadge powerUpBadge = new PowerUpBadge(UiStyle.background());

    private final transient GameWorldController controller;
    private transient Optional<XY<Double>> previousMousePosition = Optional.empty();

    /**
     * @param controller the controller of the game world: it gives what to draw and receives the swipes
     */
    public GameAreaPanelImpl(final GameWorldController controller) {
        this.controller = controller;

        final MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                previousMousePosition = Optional.of(new XY<>((double) e.getX(), (double) e.getY()));
            }

            @Override
            public void mouseDragged(final MouseEvent e) {
                if (previousMousePosition.isPresent()) {
                    final double pixelX = e.getX();
                    final double pixelY = e.getY();

                    final double scaleX = getWidth() / GameWorld.WIDTH;
                    final double scaleY = getHeight() / GameWorld.HEIGHT;

                    final XY<Double> logicalPrev = new XY<>(
                        previousMousePosition.get().x() / scaleX,
                        previousMousePosition.get().y() / scaleY
                    );
                    final XY<Double> logicalCurrent = new XY<>(
                        pixelX / scaleX,
                        pixelY / scaleY
                    );

                    controller.processSwipe(logicalPrev, logicalCurrent);

                    previousMousePosition = Optional.of(new XY<>(pixelX, pixelY));
                }
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                previousMousePosition = Optional.empty();
            }
        };

        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
    }

    @Override
    protected void paintComponent(final Graphics g) {
        final int livesCounter = controller.getLives();
        final Set<SliceableModel> visibleSliceables = this.controller.getVisibleSliceables();
        final double scaleX = this.getWidth() / GameWorld.WIDTH;
        final double scaleY = this.getHeight() / GameWorld.HEIGHT;
        super.paintComponent(g);
        UiStyle.background().ifPresent(bg -> g.drawImage(bg, 0, 0, this));

        // Con 0 vite non si disegnano cuori: c'è la schermata di game over
        final Optional<Image> livesImage = switch (livesCounter) {
            case 1 -> image1L;
            case 2 -> image2L;
            case 3 -> image3L;
            default -> Optional.empty();
        };

        livesImage.ifPresent(img -> g.drawImage(img, 0, 0, this));

        final Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (final SliceableModel s : visibleSliceables) {
            if (s.getType() == SliceableTypeEnum.BOMB || s.getType() == SliceableTypeEnum.POWERUP) {
                // Bombe e power-up sono cerchi: si disegnano in pixel con un raggio unico
                final int cx = (int) Math.round(s.getPosition().x() * scaleX);
                final int cy = (int) Math.round(s.getPosition().y() * scaleY);
                final int r = (int) Math.round(s.getApothem() * Math.min(scaleX, scaleY));
                if (s instanceof PowerUpModel powerUp) {
                    drawPowerUp(g2, powerUp.getPowerUp(), cx, cy, r);
                } else {
                    drawBomb(g2, cx, cy, r);
                }
            } else {
                final Polygon p = createPolygon(s.getType(), s.getPosition(), s.getApothem(), scaleX, scaleY);
                g2.setColor(colorOf(s.getType()));
                g2.fillPolygon(p);
            }
        }

        drawScore(g2);
        this.controller.getActivePowerUp().ifPresent(status -> this.powerUpBadge.draw(g2, status, this.getWidth()));
    }

    // Punteggio in alto a destra, alla stessa altezza dei cuori
    private void drawScore(final Graphics2D g) {
        final String text = String.valueOf(this.controller.getScore());
        g.setFont(UiStyle.font(SCORE_SIZE));
        final FontMetrics metrics = g.getFontMetrics();
        // Linea di base scelta in modo che il centro delle cifre cada a metà dei cuori
        final int baseline = HEARTS_HEIGHT / 2 + (metrics.getAscent() - metrics.getDescent()) / 2;
        g.setColor(UiStyle.DARK);
        g.drawString(text, this.getWidth() - SCORE_MARGIN - metrics.stringWidth(text), baseline);
    }

    private void drawBomb(final Graphics2D g, final int cx, final int cy, final int r) {
        if (this.bombImage.isPresent()) {
            g.drawImage(this.bombImage.get(), cx - r, cy - r, 2 * r, 2 * r, this);
        } else {
            g.setColor(Color.BLACK);
            g.fillOval(cx - r, cy - r, 2 * r, 2 * r);
        }
    }

    // Un'immagine mancante non blocca il gioco: si disegna senza
    private static Optional<Image> loadImage(final String path) {
        final URL url = GameAreaPanelImpl.class.getResource(path);
        return url == null ? Optional.empty() : Optional.of(new ImageIcon(url).getImage());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void render() {
        this.repaint();
    }

    /**
     * Nessun effetto grafico per ora: i suoni li gestisce l'AudioManager.
     */
    @Override
    public void onSliceableCut(final SliceableModel sliceable) {
        // Nessuna azione
    }

    /**
     * Il timer del power-up si legge dallo snapshot del controller a ogni frame.
     */
    @Override
    public void onPowerUpActivate(final PowerUpEnum powerUp) {
        // Nessuna azione
    }

    /**
     * Il timer sparisce da solo quando lo snapshot non ha più un power-up attivo.
     */
    @Override
    public void onPowerUpDeactivate(final PowerUpEnum powerUp) {
        // Nessuna azione
    }

    /**
     * La schermata di game over la mostra la finestra, su richiesta dell'AppController.
     */
    @Override
    public void onGameOver(final int score) {
        // Nessuna azione
    }
}
