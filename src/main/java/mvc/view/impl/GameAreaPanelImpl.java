package mvc.view.impl;

import static mvc.view.impl.GameAreaHelper.createPolygon;
import static mvc.view.impl.GameAreaHelper.drawCenteredOval;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import mvc.controller.api.GameWorldController;
import mvc.model.api.GameWorld;
import mvc.model.api.PowerUpEnum;
import mvc.model.api.SliceableModel;
import mvc.model.api.SliceableTypeEnum;
import mvc.model.api.XY;
import mvc.view.api.GameAreaPanel;


public class GameAreaPanelImpl extends JPanel implements GameAreaPanel {

    private Optional<Image> background = Optional.empty();
    
    private Image image1L;
    private Image image2L;
    private Image image3L;
    
    private final URL urlBg = getClass().getResource("/GraphicElements/background.jpg");
    private final URL url1L = getClass().getResource("/GraphicElements/heart.png");
    private final URL url2L = getClass().getResource("/GraphicElements/2hearts.png");
    private final URL url3L = getClass().getResource("/GraphicElements/3hearts.png");
    
    private int livesCounter;
    private GameWorldController controller;
    private final Predicate<SliceableModel> circlePredicate = s -> 
        s.getType() == SliceableTypeEnum.POWERUP || s.getType() == SliceableTypeEnum.BOMB;
    private Optional<XY<Double>> previousMousePosition = Optional.empty();

    public GameAreaPanelImpl(GameWorldController controller) {
        this.controller = controller;
        this.livesCounter = controller.getLives();

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

        //TODO: SWICH COLORI
        g.setColor(Color.RED);
        
        for (final SliceableModel s : visibleSliceables) {
            if (circlePredicate.test(s)) {
                System.out.println("Frame disegnato. Elementi su schermo: " + visibleSliceables.size());
                drawCenteredOval(g, s.getPosition(), s.getApothem(), scaleX, scaleY);
            } else {
                final Polygon p = createPolygon(s.getType(), s.getPosition(), s.getApothem(), scaleX, scaleY);
                System.out.println("Poligono: " + s.getType() + 
                           " | Pos Logica: " + s.getPosition() + 
                           " | Coordinate Pixel: x=" + p.getBounds().x + ", y=" + p.getBounds().y + 
                           " | Dimensione Pixel: " + p.getBounds().width + "x" + p.getBounds().height);
                g.drawPolygon(p);
                g.fillPolygon(p);
            }
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