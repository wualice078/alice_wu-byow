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
        StdDraw.text(map.width() / 2, map.height() + 1, "(:Q) to save and exit");
        StdDraw.textLeft(68, map.height() + 1, "Shells remaining: " + map.coins().size() + "/20");

        if (x > 0 && x < map.width() && y > 0 && y < map.height()) {
            TETile tile = world[x][y];
            StdDraw.setPenColor(Color.DARK_GRAY);
            StdDraw.textLeft(1, map.height() + 1, "Tile: " + tile.description());
        }

        StdDraw.show();
        return new Point(x, y);
    }
}
