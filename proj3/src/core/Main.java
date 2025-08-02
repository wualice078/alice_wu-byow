package core;

import tileengine.TERenderer;
import tileengine.TETile;

import java.util.Random;

public class Main {

    private static final long SEED = 2398;
    private static final int WIDTH = 80;
    private static final int HEIGHT = 50;

    public static void main(String[] args) {

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        World map = new World(WIDTH, HEIGHT, SEED);
        System.out.println("~finished~");

        ter.renderFrame(map.getWorld());

    }
}
