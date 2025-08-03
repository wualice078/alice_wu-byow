package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;

import java.awt.*;
import java.util.Random;

public class Main {

    private static long SEED = 2306;
    private static final int WIDTH = 80;
    private static final int HEIGHT = 50;

    public static void main(String[] args) {
        displayMenu();
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
        StdDraw.text(WIDTH / 2, 5 * HEIGHT / 6, "~ Escape the Island ~");
        StdDraw.text(WIDTH / 2, 6 * HEIGHT / 10, "(N):  New Game");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "(L): Load Game");
        StdDraw.text(WIDTH / 2, 4 * HEIGHT / 10, "(Q): Quit Game");
        StdDraw.show();

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c == 'n' || c == 'N') {
                    receiveSeed();
                    startGame();
                } else if (c == 'l' || c == 'L') {
                    loadGame();
                } else if (c == 'q' || c == 'Q') {
                    System.exit(0);
                }
            }
        }
    }

    public static void receiveSeed() {
        String seed = "";

        StdDraw.clear(Color.BLACK);
        StdDraw.text(WIDTH / 2, 5 * HEIGHT / 6, "~ Escape the Island ~");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Enter a seed followed by (S)");
        StdDraw.text(WIDTH / 2, 4 * HEIGHT / 10, seed);
        StdDraw.show();

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                seed += c;
                if (c >= '0' && c <= '9') {
                    StdDraw.clear(Color.BLACK);
                    StdDraw.text(WIDTH / 2, 5 * HEIGHT / 6, "~ Escape the Island ~");
                    StdDraw.text(WIDTH / 2, HEIGHT / 2, "Enter a seed followed by (S)");
                    StdDraw.text(WIDTH / 2, 4 * HEIGHT / 10, seed);
                    StdDraw.show();
                }
                if (c == 's' || c == 'S' || !isParsable(seed)) {
                    SEED = Long.parseLong(seed.substring(0, seed.length() - 1));
                    break;
                }
            }
        }
    }

    public static boolean isParsable(String s) {
        try {
            Long.parseLong(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
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
                if (c == ':') {
                    while (!StdDraw.hasNextKeyTyped()) {
                        StdDraw.pause(10);
                    }
                    c = StdDraw.nextKeyTyped();
                    if (c == 'q' || c == 'Q') {
                        saveGame();
                        System.exit(0);
                    }
                }
                if (c == 'w' || c == 'W' || c == 'a' || c == 'A' ||
                        c == 's' || c == 'S' || c == 'd' || c == 'D') {
                    map.moveAvatar(c);
                    ter.renderFrame(map.getWorld());
                    map.displayHUD(x, y);
                }
            }

            x = (int) StdDraw.mouseX();
            y = (int) StdDraw.mouseY();

            if (x != map.mouse.x || y != map.mouse.y) {
                ter.renderFrame(map.getWorld());
                map.displayHUD(x, y);
            }
            
        }
    }

    public static void saveGame() {

    }
    public static void loadGame() {

    }
}
