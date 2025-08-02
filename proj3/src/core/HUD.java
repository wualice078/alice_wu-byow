package core;
import edu.princeton.cs.algs4.StdDraw;
import tileengine.TETile;

import java.awt.*;

public class HUD {
    public void render(TETile[][] world, int width, int height) {
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();
        if (x > 0 && x < width && y > 0 && y < height) {
            TETile tile = world[x][y];
            StdDraw.setPenColor(Color.DARK_GRAY);
            StdDraw.textLeft(1, height - 1, "Tile : " + tile.description());
        }
    }
}
