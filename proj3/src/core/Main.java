package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class Main {

    private static long SEED = 0;
    private static final int WIDTH = 80;
    private static final int HEIGHT = 50;
    private static final int HUD_Height = 2;
    private static boolean displayPath = false;
    private static int chase = 0;
    private static int speed = 0;

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
        StdDraw.text(WIDTH / 2, 6 * HEIGHT / 10, "(N): New Game");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "(L): Load Game");
        StdDraw.text(WIDTH / 2, 4 * HEIGHT / 10, "(Q): Quit Game");
        StdDraw.show();

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c == 'n' || c == 'N') {
                    receiveSeed();
                    receiveSpeed();
                    World map = new World(WIDTH, HEIGHT, SEED);
                    startGame(map);
                } else if (c == 'l' || c == 'L') {
                    loadGame();
                } else if (c == 'q' || c == 'Q') {
                    System.exit(0);
                }
            }
            StdDraw.pause(10);
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
                    if (isParsable(seed.substring(0, seed.length() - 1))) {
                        SEED = Long.parseLong(seed.substring(0, seed.length() - 1));
                    } else {
                        Random rand = new Random();
                        SEED = rand.nextLong();
                    }
                    break;
                }
            }
            StdDraw.pause(10);
        }
    }

    public static void receiveSpeed() {

        StdDraw.clear(Color.BLACK);
        StdDraw.text(WIDTH / 2, 5 * HEIGHT / 6, "~ Escape the Island ~");
        StdDraw.text(WIDTH / 2, 4 * HEIGHT / 6, "Choose the difficulty");
        Font font = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.textLeft(WIDTH / 2 - 4, (double) (8 * HEIGHT) / 15, "(E): Easy");
        StdDraw.textLeft(WIDTH / 2 - 4, (double) (7 * HEIGHT) / 15, "(M): Medium");
        StdDraw.textLeft(WIDTH / 2 - 4, (double) (6 * HEIGHT) / 15, "(H): Hard");
        StdDraw.textLeft(WIDTH / 2 - 4, (double) (5 * HEIGHT) / 15, "(X): Extreme");
        StdDraw.textLeft(WIDTH / 2 - 4, (double) (4 * HEIGHT) / 15, "(S): Master");
        StdDraw.show();

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c == 'e' || c == 'E') {
                    speed = 20;
                    break;
                }
                if (c == 'm' || c == 'M') {
                    speed = 12;
                    break;
                }
                if (c == 'h' || c == 'H') {
                    speed = 8;
                    break;
                }
                if (c == 'x' || c == 'X') {
                    speed = 6;
                    break;
                }
                if (c == 's' || c == 'S') {
                    speed = 5;
                    break;
                }
            }
            StdDraw.pause(10);
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
        ter.renderFrame(map.world());
        HUD hud = new HUD();
        Point mouse = hud.displayHUD(map);
        displayPath = false;
        List<Point> previousPath = null;
        flashAvatar(ter, map, hud);

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c == ':') {
                    while (!StdDraw.hasNextKeyTyped()) {
                        StdDraw.pause(10);
                    }
                    char next = StdDraw.nextKeyTyped();
                    if (next == 'q' || next == 'Q') {
                        SavedWorld.save(SEED, map.avatar(), map.chaser(), map.coins(),speed);
                        System.exit(0);
                    }
                }

                if (c == 'w' || c == 'W' || c == 'a' || c == 'A' ||
                        c == 's' || c == 'S' || c == 'd' || c == 'D') {

                    map.moveAvatar(c);
                    ter.renderFrame(map.world());
                    hud.displayHUD(map);

                    if (map.coins().isEmpty()) {
                        StdDraw.pause(500);
                        endGame(true);
                    }
                }

                if (c == 'p' || c == 'P') {
                    displayPath = !displayPath;
                }
            }

            StdDraw.pause(5);

            int x = (int) StdDraw.mouseX();
            int y = (int) StdDraw.mouseY();

            if (x != mouse.x || y != mouse.y) {
                mouse = hud.displayHUD(map);
            }

            StdDraw.pause(5);

            if (chase == speed) {

                if (displayPath) {
                    map.clearPath(previousPath);
                    previousPath = map.displayPath();
                } else {
                    map.clearPath(previousPath);
                    previousPath = null;
                }

                map.moveChaser();

                ter.renderFrame(map.world());
                hud.displayHUD(map);

                chase = 0;

                if (map.chaser().equals(map.avatar())) {
                    StdDraw.pause(500);
                    endGame(false);
                }
            }

            chase++;
            StdDraw.pause(5);
        }
    }

    public static void flashAvatar(TERenderer ter, World map, HUD hud) {
        for (int i = 0; i < 4; i++) {
            StdDraw.pause(200);
            map.world()[map.avatar().x][map.avatar().y] = Tileset.floor;
            ter.renderFrame(map.world());
            hud.displayHUD(map);
            StdDraw.pause(100);
            map.world()[map.avatar().x][map.avatar().y] = Tileset.avatar;
            ter.renderFrame(map.world());
            hud.displayHUD(map);
        }
    }

    public static void endGame(boolean win) {
        StdDraw.setCanvasSize(WIDTH * 16, (HEIGHT + HUD_Height) * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT + HUD_Height);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.enableDoubleBuffering();

        StdDraw.clear(Color.BLACK);
        if (win) {
            StdDraw.text(WIDTH / 2, HEIGHT / 2, "You Escaped!");
        } else {
            StdDraw.text(WIDTH / 2, HEIGHT / 2, "You Were Caught!");
        }
        StdDraw.show();
        StdDraw.pause(5000);

        displayMenu();
    }

    public static void loadGame() {
        SavedWorld saved = SavedWorld.load();
        if (saved == null) {
            displayMenu();
            return;
        }
        SEED = saved.seed;
        speed = saved.speed;
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT + HUD_Height);

        World map = new World(WIDTH, HEIGHT, SEED);
        map.setAvatar(saved.avatar);
        map.setChaser(saved.chaser);
        map.setCoins(saved.coins);
        startGame(map);

    }
}
