package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;

import java.util.Random;

public class Main {

    private static final long SEED = 2306;
    private static final int WIDTH = 80;
    private static final int HEIGHT = 50;

    public static void main(String[] args) {

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        World map = new World(WIDTH, HEIGHT, SEED);
        HUD hud = new HUD();
        TETile[][] world = map.getWorldArray();

        while(true) {
            ter.renderFrame(map.getWorld());
            hud.render(world, WIDTH, HEIGHT);
            StdDraw.show();
            StdDraw.pause(1000);
        }

    }
}
