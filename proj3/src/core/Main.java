package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;

import java.awt.*;
import java.util.Random;

public class Main {

    private static final long SEED = 2306;
    private static final int WIDTH = 80;
    private static final int HEIGHT = 50;

    public static void main(String[] args) {
        displayMenu();
        //startGame();

    }

    public static void displayMenu() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.enableDoubleBuffering();

        StdDraw.clear(Color.BLACK);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "hi");
        StdDraw.show();
    }

    public static void startGame() {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        World map = new World(WIDTH, HEIGHT, SEED);
        ter.renderFrame(map.getWorld());
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                map.moveAvatar(c);
                ter.renderFrame(map.getWorld());
                map.displayHUD(x, y);
            }

            x = (int) StdDraw.mouseX();
            y = (int) StdDraw.mouseY();

            if (x != map.mouse.x || y != map.mouse.y) {
                ter.renderFrame(map.getWorld());
                map.displayHUD(x, y);
            }

        }
    }
}
