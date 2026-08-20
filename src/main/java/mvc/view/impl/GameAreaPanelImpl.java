package mvc.view.impl;

import static mvc.model.impl.SliceableModelImpl.LENGHT;
import static mvc.view.impl.GameAreaHelper.createPolygon;
import static mvc.view.impl.GameAreaHelper.drawCenteredOval;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import mvc.controller.api.GameWorldController;
import mvc.model.api.GameWorldListener;
import mvc.model.api.PowerUpEnum;
import mvc.model.api.SliceableModel;
import mvc.model.api.SliceableTypeEnum;
import mvc.model.api.XY;
import mvc.view.api.GameAreaPanel;


public class GameAreaPanelImpl extends JPanel implements GameAreaPanel, GameWorldListener {

    private Optional<Image> background = Optional.empty();
    
    private Image image1L;
    private Image image2L;
    private Image image3L;
    
    private final URL urlBg = getClass().getResource("/GraphicElements/background.jpg");
    private final URL url1L = getClass().getResource("/GraphicElements/hearth.png");
    private final URL url2L = getClass().getResource("/GraphicElements/2hearts.png");
    private final URL url3L = getClass().getResource("/GraphicElements/3hearts.png");
    
    private final Set<SliceableModel> visibleSliceables;
    
    private static final int ONE_LIVE = 1;
    private static final int TWO_LIVES = 2;
    private static final int THREE_LIVES = 3;
    
    private int livesCounter;
    private final Predicate<SliceableModel> circlePredicate = s -> 
        s.getType() == SliceableTypeEnum.POWERUP || s.getType() == SliceableTypeEnum.BOMB;
    private Optional<XY<Double>> previousMousePosition = Optional.empty();

    public GameAreaPanelImpl(GameWorldController controller) {
        this.livesCounter = THREE_LIVES;
        
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
                if (previousMousePosition != null) {
                    XY<Double> currentMousePosition = new XY<>((double) e.getX(), (double) e.getY());
                    
                    // Per il resolution independent MVC, prima di inviare i punti al controller,
                    // dovresti invertire la scala (da pixel a coordinate logiche).
                    // double scaleX = (double) getWidth() / WORLD_LOGICAL_WIDTH;
                    // XY<Double> logicalPrev = new XY<>(previousMousePosition.x() / scaleX, ...);
                    
                    controller.processSwipe(previousMousePosition.get(), currentMousePosition);
                    previousMousePosition = Optional.of(currentMousePosition);
                }
            }

            public void mouseReleased(MouseEvent e) {
                previousMousePosition = Optional.empty();
            }
        };

        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);

        this.visibleSliceables = new HashSet<>(controller.getVisibleSliceables());
    }

    @Override
    protected void paintComponent(final Graphics g) {
        final double scaleX = (double) this.getWidth() / WIDTH;
        final double scaleY = (double) this.getHeight() / HEIGHT;
        super.paintComponent(g);
        background.ifPresent(bg -> g.drawImage(bg, 0, 0, this));

        final Image livesImage = switch (livesCounter) {
            case ONE_LIVE -> image1L;
            case TWO_LIVES -> image2L;
            case THREE_LIVES -> image3L;
            default -> throw new IllegalStateException("can't draw " + livesCounter + " lives");
        };
        
        g.drawImage(livesImage, 0, 0, this);

        //TODO: SWICH COLORI
        g.setColor(Color.RED);
        
        for (final SliceableModel s : visibleSliceables) {
            if (circlePredicate.test(s)) {
                drawCenteredOval(g, s.getPosition(), LENGHT, scaleX, scaleY);
            } else {
                final Polygon p = createPolygon(s.getType(), s.getPosition(), s.getApothem(), scaleX, scaleY);
                g.drawPolygon(p);
                g.fillPolygon(p);
            }
        }
    }

    @Override
    public void addSliceables(SliceableModel sliceable) {
        visibleSliceables.add(sliceable);
    }

    @Override
    public int getWidth() {
        return this.getWidth();
    }

    @Override
    public int getHeight() {
        return this.getHeight();
    }

    @Override
    public void onSliceableCut(SliceableModel sliceable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onSliceableCut'");
    }

    @Override
    public void onPowerUpActivate(PowerUpEnum powerUp) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onPowerUpActivate'");
    }

    @Override
    public void onPowerUpDeactivate(PowerUpEnum powerUp) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onPowerUpDeactivate'");
    }

    @Override
    public void onGameOver(int score) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onGameOver'");
    }
}