// separating the brush math to find a circle so it can be reused in multiple files

package editor;

import sim.AntSim;
import util.Point;

import java.util.ArrayList;
import java.util.List;

public final class BrushMath {

    private BrushMath() {}

    public static List<Point> pointsInCircle(AntSim sim, Point center, int radius) {
        if (sim == null) { throw new IllegalArgumentException("sim is null"); }
        if (center == null) { throw new IllegalArgumentException("center is null"); }
        if (radius < 0) { throw new IllegalArgumentException("radius must be >= 0"); }

        int width = sim.getWorld().getWidth();
        int height = sim.getWorld().getHeight();

        List<Point> points = new ArrayList<>();
        int radiusSquared = radius*radius; // r^2 = x^2 + y^2

        for (int dy=-radius; dy<=radius; ++dy) {
            for (int dx=-radius; dx<=radius; ++dx) {
                if ((dx*dx)+(dy*dy) > radiusSquared) { continue; } // outside radius

                // adjust relative to mouse pos
                int x = center.x + dx;
                int y = center.y + dy;
                // Only add inbounds points
                if (x>=0 && x<width && y>=0 && y<height) {
                    points.add(new Point(x, y));
                }
            }
        }
        return points;
    }
}