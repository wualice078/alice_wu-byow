package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TETile;

import java.awt.*;

public class HUD {

    public void displayHUD(World map) {
        int x = (int)StdDraw.mouseX();
        int y = (int)StdDraw.mouseY();
        TETile[][] world = map.world();

        StdDraw.setPenColor(StdDraw.CYAN);
        StdDraw.filledRectangle(map.width() / 2.0, map.height() + 0.5, map.width() / 2.0, 0.5);

        if (x > 0 && x < map.width() && y > 0 && y < map.height()) {
            TETile tile = world[x][y];
            StdDraw.setPenColor(Color.DARK_GRAY);
            StdDraw.textLeft(1, map.height() + 0.5, "Tile: " + tile.description());
        }
    }
}
