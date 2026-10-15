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


public class GameAreaHelper {

    private static final Color GREEN = new Color(46, 204, 113);
    private static final Color PURPLE = new Color(142, 68, 173);
    private static final Color ORANGE = new Color(230, 126, 34);
    private static final Color BLUE = new Color(52, 152, 219);
    private static final Color ICE = new Color(41, 128, 185);
    private static final Color GOLD = new Color(241, 196, 15);
    private static final Color DARK = new Color(44, 62, 80);
    private static final Color LIGHT = new Color(236, 240, 241);
    private static final Color RED = new Color(231, 76, 60);

    public static Polygon createPolygon(final SliceableTypeEnum type, final XY<Double> logicalCenter,
                                        final double logicalApothem, final double scaleX, final double scaleY) {
        final int nPoints = switch (type) {
            case TRIANGLE -> 3;
            case SQUARE -> 4;
            case PENTAGON -> 5;
            case HEXAGON -> 6;
            default -> throw new IllegalArgumentException("Type not supported: " + type);
        };

        final double circumradius = logicalApothem / Math.cos(Math.PI / nPoints);

        final int[] xPoints = new int[nPoints];
        final int[] yPoints = new int[nPoints];

        final double angleOffset = -Math.PI / 2;

        for (int i = 0; i < nPoints; i++) {
            final double angle = (2 * Math.PI / nPoints) * i + angleOffset;

            final double logicalX = logicalCenter.x() + (circumradius * Math.cos(angle));
            final double logicalY = logicalCenter.y() + (circumradius * Math.sin(angle));

            xPoints[i] = (int) Math.round(logicalX * scaleX);
            yPoints[i] = (int) Math.round(logicalY * scaleY);
        }

        return new Polygon(xPoints, yPoints, nPoints);
    }

    /**
     * Every polygon has its own color.
     * @param type of the polygon
     * @return the color to fill it with
     */
    public static Color colorOf(final SliceableTypeEnum type) {
        return switch (type) {
            case TRIANGLE -> GREEN;
            case SQUARE -> PURPLE;
            case PENTAGON -> ORANGE;
            case HEXAGON -> BLUE;
            default -> throw new IllegalArgumentException("Type not supported: " + type);
        };
    }

    /**
     * Draws a power-up as a colored circle with its own icon.
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
        g.setStroke(new BasicStroke(Math.max(1f, r * 0.08f)));
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
        final double arm = r * 0.65;
        final double branch = r * 0.22;
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(Math.max(1f, r * 0.1f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 0; i < 6; i++) {
            final double angle = Math.PI / 3 * i;
            final double ex = cx + arm * Math.cos(angle);
            final double ey = cy + arm * Math.sin(angle);
            g.drawLine(cx, cy, (int) ex, (int) ey);
            final double bx = cx + arm * 0.6 * Math.cos(angle);
            final double by = cy + arm * 0.6 * Math.sin(angle);
            for (final double side : new double[] {-Math.PI / 4, Math.PI / 4}) {
                g.drawLine((int) bx, (int) by,
                    (int) (bx + branch * Math.cos(angle + side)), (int) (by + branch * Math.sin(angle + side)));
            }
        }
    }

    // Cuore fatto con due curve di Bézier simmetriche
    private static void drawHeart(final Graphics2D g, final int cx, final int cy, final int r) {
        final double s = r * 0.5;
        final Path2D heart = new Path2D.Double();
        heart.moveTo(cx, cy + s * 1.1);
        heart.curveTo(cx - s * 1.8, cy - s * 0.1, cx - s * 0.8, cy - s * 1.4, cx, cy - s * 0.5);
        heart.curveTo(cx + s * 0.8, cy - s * 1.4, cx + s * 1.8, cy - s * 0.1, cx, cy + s * 1.1);
        heart.closePath();
        g.setColor(RED);
        g.fill(heart);
    }

    // Scudo: lato superiore dritto, fianchi che scendono e punta in basso
    private static void drawShield(final Graphics2D g, final int cx, final int cy, final int r) {
        final double s = r * 0.55;
        final Path2D shield = new Path2D.Double();
        shield.moveTo(cx - s, cy - s);
        shield.lineTo(cx + s, cy - s);
        shield.lineTo(cx + s, cy);
        shield.quadTo(cx + s, cy + s * 0.8, cx, cy + s * 1.2);
        shield.quadTo(cx - s, cy + s * 0.8, cx - s, cy);
        shield.closePath();
        g.setColor(LIGHT);
        g.fill(shield);
        g.setColor(DARK);
        g.setStroke(new BasicStroke(Math.max(1f, r * 0.08f)));
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
