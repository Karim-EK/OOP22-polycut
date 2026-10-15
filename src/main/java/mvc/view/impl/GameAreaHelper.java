package mvc.view.impl;

import mvc.model.api.SliceableTypeEnum;
import mvc.model.api.XY;
import java.awt.Polygon;
import java.awt.Graphics;


public class GameAreaHelper {

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

    public static void drawCenteredOval(final Graphics g, final XY<Double> logicalCenter, 
                                        final double logicalRadius, final double scaleX, final double scaleY) {
        
        final int pixelRadiusX = (int) Math.round(logicalRadius * scaleX);
        final int pixelRadiusY = (int) Math.round(logicalRadius * scaleY);
        
        final int pixelCenterX = (int) Math.round(logicalCenter.x() * scaleX);
        final int pixelCenterY = (int) Math.round(logicalCenter.y() * scaleY);
        
        final int topLeftX = pixelCenterX - pixelRadiusX;
        final int topLeftY = pixelCenterY - pixelRadiusY;
        
        g.drawOval(topLeftX, topLeftY, pixelRadiusX * 2, pixelRadiusY * 2);
        g.fillOval(topLeftX, topLeftY, pixelRadiusX * 2, pixelRadiusY * 2);
    }
}
