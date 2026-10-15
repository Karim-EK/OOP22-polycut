package mvc.view.impl;

import static mvc.view.impl.GameAreaHelper.colorOf;
import static mvc.view.impl.GameAreaHelper.createPolygon;
import static mvc.view.impl.GameAreaHelper.drawPowerUp;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
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


public class GameAreaPanelImpl extends JPanel implements GameAreaPanel {

    private Optional<Image> background = Optional.empty();
    private Optional<Image> bombImage = Optional.empty();

    private Image image1L;
    private Image image2L;
    private Image image3L;
    
    private final URL urlBg = getClass().getResource("/GraphicElements/background.jpg");
    private final URL url1L = getClass().getResource("/GraphicElements/heart.png");
    private final URL url2L = getClass().getResource("/GraphicElements/2hearts.png");
    private final URL url3L = getClass().getResource("/GraphicElements/3hearts.png");
    private final URL urlBomb = getClass().getResource("/GraphicElements/bomb.png");

    private static final String SCORE_FONT = "/GraphicElements/Orbitron/static/Orbitron-Bold.ttf";
    // Dimensione del punteggio e margine, in frazione dell'altezza del pannello
    private static final double SCORE_SIZE = 0.07;
    private static final double SCORE_MARGIN = 0.03;
    private static final Color SCORE_COLOR = new Color(44, 62, 80);

    private Font scoreFont = new Font(Font.SANS_SERIF, Font.BOLD, 1);
    private int livesCounter;
    private GameWorldController controller;
    private Optional<XY<Double>> previousMousePosition = Optional.empty();

    public GameAreaPanelImpl(GameWorldController controller) {
        this.controller = controller;
        this.livesCounter = controller.getLives();
        if (urlBomb != null) {
            this.bombImage = Optional.of(new ImageIcon(urlBomb).getImage());
        }
        try (InputStream fontStream = getClass().getResourceAsStream(SCORE_FONT)) {
            if (fontStream != null) {
                this.scoreFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            }
        } catch (final IOException | FontFormatException e) {
            System.err.println("impossible to load the score font, using the default one");
        }

        try {
            if (urlBg == null) throw new IllegalStateException("impossible to load background");
            background = Optional.of(new ImageIcon(urlBg).getImage());
            
            if (url1L == null || url2L == null || url3L == null) {
                throw new IllegalStateException("impossible to load hearts image");
            }
            this.image1L = new ImageIcon(url1L).getImage();
            this.image2L = new ImageIcon(url2L).getImage();
            this.image3L = new ImageIcon(url3L).getImage();
            
        } catch (final Exception e) {
            System.err.println(e.getMessage());
        }
        
        MouseAdapter mouseAdapter = new MouseAdapter() {
    public void mousePressed(MouseEvent e) {
        previousMousePosition = Optional.of(new XY<>((double) e.getX(), (double) e.getY()));
    }

    public void mouseDragged(MouseEvent e) {
        if (previousMousePosition.isPresent()) {
            double pixelX = (double) e.getX();
            double pixelY = (double) e.getY();
            
            double scaleX = (double) getWidth() / GameWorld.WIDTH;
            double scaleY = (double) getHeight() / GameWorld.HEIGHT;
            
            XY<Double> logicalPrev = new XY<>(
                previousMousePosition.get().x() / scaleX, 
                previousMousePosition.get().y() / scaleY
            );
            XY<Double> logicalCurrent = new XY<>(
                pixelX / scaleX, 
                pixelY / scaleY
            );
            
            controller.processSwipe(logicalPrev, logicalCurrent);
            
            previousMousePosition = Optional.of(new XY<>(pixelX, pixelY));
        }
    }

    public void mouseReleased(MouseEvent e) {
        previousMousePosition = Optional.empty();
    }
};

        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
    }

    @Override
    protected void paintComponent(final Graphics g) {
        this.livesCounter = controller.getLives();
        final Set<SliceableModel> visibleSliceables = this.controller.getVisibleSliceables();
        final double scaleX = (double) this.getWidth() / GameWorld.WIDTH;
        final double scaleY = (double) this.getHeight() / GameWorld.HEIGHT;
        super.paintComponent(g);
        background.ifPresent(bg -> g.drawImage(bg, 0, 0, this));

        // Con 0 vite non si disegnano cuori: la schermata di game over arriverà in seguito
        final Optional<Image> livesImage = switch (livesCounter) {
            case 1 -> Optional.of(image1L);
            case 2 -> Optional.of(image2L);
            case 3 -> Optional.of(image3L);
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
    }

    // Punteggio in alto a destra, allineato al bordo destro
    private void drawScore(final Graphics2D g) {
        final String text = String.valueOf(this.controller.getScore());
        final int margin = (int) Math.round(this.getHeight() * SCORE_MARGIN);
        g.setFont(this.scoreFont.deriveFont((float) (this.getHeight() * SCORE_SIZE)));
        final FontMetrics metrics = g.getFontMetrics();
        g.setColor(SCORE_COLOR);
        g.drawString(text, this.getWidth() - margin - metrics.stringWidth(text), margin + metrics.getAscent());
    }

    private void drawBomb(final Graphics2D g, final int cx, final int cy, final int r) {
        if (this.bombImage.isPresent()) {
            g.drawImage(this.bombImage.get(), cx - r, cy - r, 2 * r, 2 * r, this);
        } else {
            g.setColor(Color.BLACK);
            g.fillOval(cx - r, cy - r, 2 * r, 2 * r);
        }
    }

    @Override
    public void render() {
        this.repaint();
    }

    @Override
    public void onSliceableCut(SliceableModel sliceable) {
        System.out.println("Evento ricevuto: ...");
    }

    @Override
    public void onPowerUpActivate(PowerUpEnum powerUp) {
        System.out.println("Evento ricevuto: ...");
    }

    @Override
    public void onPowerUpDeactivate(PowerUpEnum powerUp) {
        System.out.println("Evento ricevuto: ...");
    }

    @Override
    public void onGameOver(int score) {
        System.out.println("Evento ricevuto: ...");
    }
}