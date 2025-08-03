package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Random;

public class Main {

    private static long SEED = 2306;
    private static final int WIDTH = 80;
    private static final int HEIGHT = 50;
    private static final int HUD_Height = 1;

    public static void main(String[] args) {
        displayMenu();
    }

    public static void displayMenu() {
        StdDraw.setCanvasSize(WIDTH * 16, (HEIGHT + HUD_Height) * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT + HUD_Height);
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
                    World map = new World(WIDTH, HEIGHT, SEED);
                    startGame(map);
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

    public static void startGame(World map) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT + HUD_Height);
        HUD hud = new HUD();
        ter.renderFrame(map.world());

        int prevMouseX = -1;
        int prevMouseY = -1;

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c == ':') {
                    while (!StdDraw.hasNextKeyTyped()) {
                        StdDraw.pause(10);
                    }
                    char next = StdDraw.nextKeyTyped();
                    if (next == 'q' || next == 'Q') {
                        SaveLoad.save(SEED, map.avatarX(), map.avatarY());
                        System.exit(0);
                    }
                }
                if (c == 'w' || c == 'W' || c == 'a' || c == 'A' ||
                        c == 's' || c == 'S' || c == 'd' || c == 'D') {
                    map.moveAvatar(c);
                    renderGame(map, hud, ter);
                }
            }

            int x = (int) StdDraw.mouseX();
            int y = (int) StdDraw.mouseY();

            if (x != prevMouseX || y != prevMouseY) {
                prevMouseX = x;
                prevMouseY = y;
                hud.displayHUD(map);
                StdDraw.show();
            }
            StdDraw.pause(20);
        }
    }

    public static void renderGame(World map, HUD hud, TERenderer ter) {
        ter.renderFrame(map.world());
        hud.displayHUD(map);
        StdDraw.show();
    }

    public static void loadGame() {
        SaveLoad saved = SaveLoad.load();
        if (saved == null) {
            displayMenu();
            return;
        }
        SEED = saved.seed;
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT + HUD_Height);

        World map = new World(WIDTH, HEIGHT, SEED);
        map.setAvatar(saved.avatarX, saved.avatarY);
        startGame(map);

    }
}
