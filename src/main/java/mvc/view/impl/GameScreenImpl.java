package mvc.view.impl;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import mvc.controller.api.GameWorldController;
import mvc.view.api.GameScreen;
import mvc.view.api.SliceableView;

/**
 * Rapresents the main GUI that pops out after start.
 */
public class GameScreenImpl implements GameScreen {
    private static final String BACKGROUND = "background.jpg";
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    private Set<SliceableView> sliceableSet;
    private final JFrame frame;
    private MainPanel mainPanel;
    private final GameWorldController controller;

    public GameScreenImpl(GameWorldController controller) throws IOException {
        this.controller = controller;
        this.sliceableSet = new HashSet<>();

        frame = new JFrame("Polygon Cutter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {

            }

            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    frame.dispose();
                    Runtime.getRuntime().exit(0);
                }
            }

            @Override
            public void keyReleased(final KeyEvent e) {

            }
        });

        final Optional<URL> url = Optional.of(GameScreenImpl.class.getClassLoader().getResource(BACKGROUND));
        if (url.isPresent()) {
            this.mainPanel = new MainPanel(ImageIO.read(url.get()));
            frame.setContentPane(mainPanel);
        } else {
            throw new IOException();
        }
        SCREEN_WIDTH = this.mainPanel.getWidth();
        SCREEN_HEIGHT = this.mainPanel.getHeight();
    }

    @Override
    public void createAndShowGui() {
        this.frame.setVisible(true);
    }

    @Override
    public void addNewSliceable(SliceableView newSliceable) {
        this.sliceableSet.add(newSliceable);
    }

    @Override
    public void updateSliceablePosition(SliceableView sliceable, Point2D newPosition) {
        Optional<SliceableView> targetSliceable = 
        this.sliceableSet.stream()
        .filter(x -> x.equals(sliceable))
        .findAny();

        if (targetSliceable.isPresent()) {
            targetSliceable.get().setPosition(newPosition);
        } else {
            throw new IllegalStateException();
        }
        this.frame.repaint();
    }

    @Override
    public void loseLife() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loseLife'");
        this.frame.repaint();
    }

    @Override
    public void gainLife() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'gainLife'");
        this.frame.repaint();
    }

    private class MainPanel extends JPanel {
        private final Image bg;
        
        public MainPanel(Image image) {
            this.bg = image;
        }

        @Override   
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            var g2d = (Graphics2D) g;
            var scale = Math.min(this.getWidth(), this.getHeight());

            g2d.scale(scale, scale);
            g2d.drawImage(bg, 0, 0, getWidth(), getHeight(), this);

            GameScreenImpl.this.sliceableSet.stream()
            .peek(x -> g2d.drawImage(x.getImage(), (int)x.getPosition().getX(), (int)x.getPosition().getY(), this));
        }
    }
}
