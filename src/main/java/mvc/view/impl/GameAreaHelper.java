package mvc.view.impl;

import mvc.model.api.PowerUpEnum;
import mvc.model.api.SliceableTypeEnum;
import mvc.model.api.XY;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Path2D;

/**
 * Drawing utilities of the game area: polygons, their colors and the power-up icons.
 * All the icon proportions are fractions of the power-up radius, so they scale with the window.
 */
public final class GameAreaHelper {

    private static final Color GREEN = new Color(46, 204, 113);
    private static final Color PURPLE = new Color(142, 68, 173);
    private static final Color ORANGE = new Color(230, 126, 34);
    private static final Color BLUE = new Color(52, 152, 219);
    private static final Color ICE = new Color(41, 128, 185);
    private static final Color GOLD = new Color(241, 196, 15);
    private static final Color DARK = new Color(44, 62, 80);
    private static final Color LIGHT = new Color(236, 240, 241);
    private static final Color RED = new Color(231, 76, 60);

    // Spessore del bordo dei cerchi e dei tratti, in frazione del raggio
    private static final float BORDER = 0.08f;
    private static final float SNOWFLAKE_STROKE = 0.1f;

    // Fiocco di neve: lunghezza dei bracci, dei rametti e punto in cui partono i rametti
    private static final int SNOWFLAKE_ARMS = 6;
    private static final double ARM_LENGTH = 0.65;
    private static final double BRANCH_LENGTH = 0.22;
    private static final double BRANCH_POSITION = 0.6;

    // Cuore: punti di controllo delle due curve di Bézier, in frazione della sua metà larghezza
    private static final double HEART_SIZE = 0.5;
    private static final double HEART_TIP = 1.1;
    private static final double HEART_TOP = 0.5;
    private static final double[] HEART_CONTROL_1 = {1.8, -0.1};
    private static final double[] HEART_CONTROL_2 = {0.8, -1.4};

    // Scudo: metà larghezza, punta in basso e curvatura dei fianchi
    private static final double SHIELD_SIZE = 0.55;
    private static final double SHIELD_TIP = 1.2;
    private static final double SHIELD_CURVE = 0.8;

    private GameAreaHelper() {
    }

    /**
     * Builds the polygon of a sliceable in pixel coordinates.
     *
     * @param type of the polygon, its sides come from the enum
     * @param logicalCenter center in logical coordinates
     * @param logicalApothem apothem in logical units
     * @param scaleX pixels per logical unit, horizontally
     * @param scaleY pixels per logical unit, vertically
     * @return the polygon to fill
     */
    public static Polygon createPolygon(final SliceableTypeEnum type, final XY<Double> logicalCenter,
                                        final double logicalApothem, final double scaleX, final double scaleY) {
        final int nPoints = type.getSides();
        if (nPoints == 0) {
            throw new IllegalArgumentException("Type not supported: " + type);
        }

        final double circumradius = logicalApothem / Math.cos(Math.PI / nPoints);

        final int[] xPoints = new int[nPoints];
        final int[] yPoints = new int[nPoints];

        final double angleOffset = -Math.PI / 2;

        for (int i = 0; i < nPoints; i++) {
            final double angle = 2 * Math.PI / nPoints * i + angleOffset;

            final double logicalX = logicalCenter.x() + circumradius * Math.cos(angle);
            final double logicalY = logicalCenter.y() + circumradius * Math.sin(angle);

            xPoints[i] = (int) Math.round(logicalX * scaleX);
            yPoints[i] = (int) Math.round(logicalY * scaleY);
        }

        return new Polygon(xPoints, yPoints, nPoints);
    }

    /**
     * Every polygon has its own color.
     *
     * @param type of the polygon
     * @return the color to fill it with
     */
    public static Color colorOf(final SliceableTypeEnum type) {
        return switch (type) {
            case TRIANGLE -> GREEN;
            case SQUARE -> PURPLE;
            case PENTAGON -> ORANGE;
            case HEXAGON -> BLUE;
            case BOMB, POWERUP -> throw new IllegalArgumentException("Type not supported: " + type);
        };
    }

    /**
     * Draws a power-up as a colored circle with its own icon.
     *
     * @param g graphics to draw on
     * @param powerUp which power-up
     * @param cx center x in pixels
     * @param cy center y in pixels
     * @param r radius in pixels
     */
    public static void drawPowerUp(final Graphics2D g, final PowerUpEnum powerUp, final int cx, final int cy, final int r) {
        final Color background = switch (powerUp) {
            case FREEZE -> ICE;
            case ONE_UP -> LIGHT;
            case SAFE_ZONE -> GOLD;
            case X2 -> DARK;
        };
        g.setColor(background);
        g.fillOval(cx - r, cy - r, 2 * r, 2 * r);
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(Math.max(1f, r * BORDER)));
        g.drawOval(cx - r, cy - r, 2 * r, 2 * r);

        switch (powerUp) {
            case FREEZE -> drawSnowflake(g, cx, cy, r);
            case ONE_UP -> drawHeart(g, cx, cy, r);
            case SAFE_ZONE -> drawShield(g, cx, cy, r);
            case X2 -> drawText(g, "x2", GOLD, cx, cy, r);
        }
    }

    // Fiocco di neve: 6 bracci a 60 gradi, ognuno con due rametti a V
    private static void drawSnowflake(final Graphics2D g, final int cx, final int cy, final int r) {
        final double arm = r * ARM_LENGTH;
        final double branch = r * BRANCH_LENGTH;
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(Math.max(1f, r * SNOWFLAKE_STROKE), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 0; i < SNOWFLAKE_ARMS; i++) {
            final double angle = 2 * Math.PI / SNOWFLAKE_ARMS * i;
            final double ex = cx + arm * Math.cos(angle);
            final double ey = cy + arm * Math.sin(angle);
            g.drawLine(cx, cy, (int) ex, (int) ey);
            final double bx = cx + arm * BRANCH_POSITION * Math.cos(angle);
            final double by = cy + arm * BRANCH_POSITION * Math.sin(angle);
            for (final double side : new double[] {-Math.PI / 4, Math.PI / 4}) {
                g.drawLine((int) bx, (int) by,
                    (int) (bx + branch * Math.cos(angle + side)), (int) (by + branch * Math.sin(angle + side)));
            }
        }
    }

    // Cuore fatto con due curve di Bézier simmetriche
    private static void drawHeart(final Graphics2D g, final int cx, final int cy, final int r) {
        final double s = r * HEART_SIZE;
        final Path2D heart = new Path2D.Double();
        heart.moveTo(cx, cy + s * HEART_TIP);
        heart.curveTo(cx - s * HEART_CONTROL_1[0], cy + s * HEART_CONTROL_1[1],
            cx - s * HEART_CONTROL_2[0], cy + s * HEART_CONTROL_2[1], cx, cy - s * HEART_TOP);
        heart.curveTo(cx + s * HEART_CONTROL_2[0], cy + s * HEART_CONTROL_2[1],
            cx + s * HEART_CONTROL_1[0], cy + s * HEART_CONTROL_1[1], cx, cy + s * HEART_TIP);
        heart.closePath();
        g.setColor(RED);
        g.fill(heart);
    }

    // Scudo: lato superiore dritto, fianchi che scendono e punta in basso
    private static void drawShield(final Graphics2D g, final int cx, final int cy, final int r) {
        final double s = r * SHIELD_SIZE;
        final Path2D shield = new Path2D.Double();
        shield.moveTo(cx - s, cy - s);
        shield.lineTo(cx + s, cy - s);
        shield.lineTo(cx + s, cy);
        shield.quadTo(cx + s, cy + s * SHIELD_CURVE, cx, cy + s * SHIELD_TIP);
        shield.quadTo(cx - s, cy + s * SHIELD_CURVE, cx - s, cy);
        shield.closePath();
        g.setColor(LIGHT);
        g.fill(shield);
        g.setColor(DARK);
        g.setStroke(new BasicStroke(Math.max(1f, r * BORDER)));
        g.draw(shield);
    }

    private static void drawText(final Graphics2D g, final String text, final Color color,
                                 final int cx, final int cy, final int r) {
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, Math.max(1, r)));
        final FontMetrics metrics = g.getFontMetrics();
        g.setColor(color);
        g.drawString(text, cx - metrics.stringWidth(text) / 2, cy + (metrics.getAscent() - metrics.getDescent()) / 2);
    }
}
