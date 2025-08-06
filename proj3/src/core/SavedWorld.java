package core;

import java.awt.*;
import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class SavedWorld {
    private static final String SAVE = "save.txt";
    long seed;
    Point avatar;
    Point chaser;
    Set<Point> coins;
    int speed;

    public SavedWorld(long seed, Point avatar, Point chaser, Set<Point> coins, int speed) {
        this.seed = seed;
        this.avatar = avatar;
        this.chaser = chaser;
        this.coins = coins;
        this.speed = speed;
    }

    public static void save(long seed, Point avatar, Point chaser, Set<Point> coins, int speed){
        try (PrintWriter out = new PrintWriter(new FileOutputStream(SAVE))) {
            out.println(seed);
            out.println(avatar.x + "," + avatar.y);
            out.println(chaser.x + "," + chaser.y);
            for (Point coin : coins) {
                out.println(coin.x + "," + coin.y);
            }
            out.println(speed);
        } catch (IOException e) {
            System.err.println("Failed to save game: " + e.getMessage());
        }
    }

    public static SavedWorld load() {
        try(Scanner in = new Scanner(new File(SAVE))){
            String s = in.nextLine();
            long seed = Long.parseLong(s);

            String a = in.nextLine();
            String[] ap = a.split(",");
            Point avatar = new Point(Integer.parseInt(ap[0]), Integer.parseInt(ap[1]));

            String e = in.nextLine();
            String[] ep = e.split(",");
            Point chaser = new Point(Integer.parseInt(ep[0]), Integer.parseInt(ep[1]));

            Set<Point> coins = new HashSet<>();
            while (in.hasNextLine()) {
                String c = in.nextLine();
                String[] cp = c.split(",");
                coins.add(new Point(Integer.parseInt(cp[0]), Integer.parseInt(cp[1])));
            }

            int speed = Integer.parseInt(in.nextLine());
            return new SavedWorld(seed, avatar, chaser, coins, speed);

        } catch(Exception e) {
            System.err.println("Failed to load game: " + e.getMessage());
            return null;
        }
    }
}
