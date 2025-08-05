package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TETile;

import java.awt.*;

public class HUD {

    public Point displayHUD(World map) {
        int x = (int)StdDraw.mouseX();
        int y = (int)StdDraw.mouseY();
        TETile[][] world = map.world();

        StdDraw.setPenColor(new Color(201, 201, 201));
        StdDraw.filledRectangle(map.width() / 2.0, map.height() + 1, map.width() / 2.0, 1);
        StdDraw.setPenColor(Color.DARK_GRAY);
        StdDraw.textLeft(20, map.height() + 1, "press p/P to show the path, press again to hide the path");

        if (x > 0 && x < map.width() && y > 0 && y < map.height()) {
            TETile tile = world[x][y];
            StdDraw.setPenColor(Color.DARK_GRAY);
            StdDraw.textLeft(1, map.height() + 1, "Tile: " + tile.description());
        }

        StdDraw.show();
        return new Point(x, y);
    }
}
