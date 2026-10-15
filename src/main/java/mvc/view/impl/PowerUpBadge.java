package mvc.view.impl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

import mvc.model.api.PowerUpStatus;

/**
 * Timer of the active power-up, drawn at the top center of the game area
 * as a frosted glass badge: blurred background, icon, name, seconds left and a progress bar.
 */
public class PowerUpBadge {

    private static final int WIDTH = 280;
    private static final int HEIGHT = 56;
    private static final int TOP = 4;
    private static final int BLUR_SIZE = 21;
    private static final int ICON_PADDING = 8;
    private static final int BAR_HEIGHT = 5;
    private static final int BAR_BOTTOM = 10;
    private static final float TEXT_SIZE = 20f;
    private static final Color GLASS = new Color(255, 255, 255, 110);
    private static final Color GLASS_BORDER = new Color(255, 255, 255, 190);
    private static final Color BAR_TRACK = new Color(44, 62, 80, 60);

    private final Optional<Image> background;
    private Optional<BufferedImage> blurred = Optional.empty();
    private Rectangle blurredBounds = new Rectangle();

    /**
     * @param background the image behind the badge, blurred to get the glass effect
     */
    public PowerUpBadge(final Optional<Image> background) {
        this.background = background;
    }

    /**
     * Draws the badge centered at the top of the panel.
     * @param g graphics of the game area
     * @param status the active power-up
     * @param panelWidth width of the game area in pixels
     */
    public void draw(final Graphics2D g, final PowerUpStatus status, final int panelWidth) {
        final Rectangle bounds = new Rectangle((panelWidth - WIDTH) / 2, TOP, WIDTH, HEIGHT);
        final Shape pill = new RoundRectangle2D.Double(bounds.x, bounds.y, bounds.width, bounds.height,
            bounds.height, bounds.height);

        // Vetro smerigliato: sfondo sfocato ritagliato a forma di pillola, poi un velo bianco trasparente
        final Shape oldClip = g.getClip();
        g.clip(pill);
        blurredBackground(bounds).ifPresent(img -> g.drawImage(img, bounds.x, bounds.y, null));
        g.setColor(GLASS);
        g.fill(pill);
        g.setClip(oldClip);
        g.setColor(GLASS_BORDER);
        g.setStroke(new BasicStroke(2f));
        g.draw(pill);

        final int iconRadius = bounds.height / 2 - ICON_PADDING;
        GameAreaHelper.drawPowerUp(g, status.powerUp(), bounds.x + bounds.height / 2,
            bounds.y + bounds.height / 2, iconRadius);

        final int textLeft = bounds.x + bounds.height + ICON_PADDING / 2;
        final int textRight = bounds.x + bounds.width - bounds.height / 2;
        final String name = status.powerUp().name().replace('_', ' ');
        final String seconds = String.format(Locale.ROOT, "%.1f", status.timeLeft());
        g.setFont(UiStyle.font(TEXT_SIZE));
        final FontMetrics metrics = g.getFontMetrics();
        final int baseline = bounds.y + bounds.height / 2 + metrics.getAscent() / 2 - BAR_HEIGHT;
        g.setColor(UiStyle.DARK);
        g.drawString(name, textLeft, baseline);
        g.drawString(seconds, textRight - metrics.stringWidth(seconds), baseline);

        // Barra del tempo rimasto: si accorcia fino a sparire quando il power-up finisce
        final int barY = bounds.y + bounds.height - BAR_BOTTOM;
        final int barWidth = textRight - textLeft;
        g.setColor(BAR_TRACK);
        g.fillRoundRect(textLeft, barY, barWidth, BAR_HEIGHT, BAR_HEIGHT, BAR_HEIGHT);
        g.setColor(UiStyle.DARK);
        g.fillRoundRect(textLeft, barY, (int) Math.round(barWidth * status.fractionLeft()),
            BAR_HEIGHT, BAR_HEIGHT, BAR_HEIGHT);
    }

    // Lo sfondo è fisso: la parte sfocata si calcola una volta e si ricalcola solo se la finestra cambia larghezza
    private Optional<BufferedImage> blurredBackground(final Rectangle bounds) {
        if (this.blurred.isEmpty() || !bounds.equals(this.blurredBounds)) {
            this.blurredBounds = new Rectangle(bounds);
            this.blurred = this.background.map(bg -> blur(bg, bounds));
        }
        return this.blurred;
    }

    // Sfocatura a scatola in due passate (orizzontale e verticale), con un margine
    // intorno alla zona per non avere bordi scuri
    private static BufferedImage blur(final Image bg, final Rectangle bounds) {
        final int pad = BLUR_SIZE;
        final int srcX = Math.max(0, bounds.x - pad);
        final int srcY = Math.max(0, bounds.y - pad);
        final int width = bounds.width + 2 * pad;
        final int height = bounds.height + 2 * pad;
        final BufferedImage source = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = source.createGraphics();
        g.drawImage(bg, -srcX, -srcY, null);
        g.dispose();

        final float[] weights = new float[BLUR_SIZE];
        Arrays.fill(weights, 1f / BLUR_SIZE);
        final BufferedImage horizontal = new ConvolveOp(new Kernel(BLUR_SIZE, 1, weights),
            ConvolveOp.EDGE_NO_OP, null).filter(source, null);
        final BufferedImage both = new ConvolveOp(new Kernel(1, BLUR_SIZE, weights),
            ConvolveOp.EDGE_NO_OP, null).filter(horizontal, null);
        return both.getSubimage(bounds.x - srcX, bounds.y - srcY, bounds.width, bounds.height);
    }
}
