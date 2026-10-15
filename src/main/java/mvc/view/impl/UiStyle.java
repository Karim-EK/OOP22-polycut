package mvc.view.impl;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Colors, font, background and buttons shared by every screen of the game.
 */
public final class UiStyle {

    /**
     * Dark blue used for text and buttons.
     */
    public static final Color DARK = new Color(44, 62, 80);
    /**
     * Button color when the mouse is over it.
     */
    public static final Color HOVER = new Color(142, 68, 173);
    /**
     * Light color for the text on the buttons.
     */
    public static final Color LIGHT = new Color(236, 240, 241);

    private static final Logger LOGGER = LoggerFactory.getLogger(UiStyle.class);

    private static final String FONT_PATH = "/GraphicElements/Orbitron/static/Orbitron-Bold.ttf";
    private static final String BACKGROUND_PATH = "/GraphicElements/background.jpg";
    private static final float BUTTON_FONT_SIZE = 22f;
    private static final int BUTTON_PADDING_V = 12;
    private static final int BUTTON_PADDING_H = 30;

    private static final Font BASE_FONT = loadFont();
    private static final Optional<Image> BACKGROUND = loadBackground();

    private UiStyle() {
    }

    /**
     * @param size in points
     * @return the Orbitron font of the resources, or a bold sans serif if it can't be loaded
     */
    public static Font font(final float size) {
        return BASE_FONT.deriveFont(size);
    }

    /**
     * @return the background image shared by all the screens
     */
    public static Optional<Image> background() {
        return BACKGROUND;
    }

    /**
     * Creates a flat button in the game style, that changes color when the mouse is over it.
     *
     * @param text the label
     * @return the button
     */
    public static JButton button(final String text) {
        final JButton button = new JButton(text);
        button.setFont(font(BUTTON_FONT_SIZE));
        button.setForeground(LIGHT);
        button.setBackground(DARK);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(BUTTON_PADDING_V, BUTTON_PADDING_H,
            BUTTON_PADDING_V, BUTTON_PADDING_H));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent e) {
                button.setBackground(HOVER);
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                button.setBackground(DARK);
            }
        });
        return button;
    }

    private static Font loadFont() {
        try (InputStream stream = UiStyle.class.getResourceAsStream(FONT_PATH)) {
            if (stream != null) {
                return Font.createFont(Font.TRUETYPE_FONT, stream);
            }
        } catch (final IOException | FontFormatException e) {
            LOGGER.warn("impossible to load the Orbitron font, using the default one", e);
        }
        return new Font(Font.SANS_SERIF, Font.BOLD, 1);
    }

    private static Optional<Image> loadBackground() {
        final URL url = UiStyle.class.getResource(BACKGROUND_PATH);
        return url == null ? Optional.empty() : Optional.of(new ImageIcon(url).getImage());
    }
}
