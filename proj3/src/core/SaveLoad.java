package core;

import edu.princeton.cs.algs4.In;
import tileengine.TETile;

import java.io.*;
import java.util.Scanner;

public class SaveLoad {
    private static final String SAVE = "save.txt";
    long seed;
    int avatarX;
    int avatarY;

    public SaveLoad(long seed, int avatarX, int avatarY) {
        this.seed = seed;
        this.avatarX = avatarX;
        this.avatarY = avatarY;
    }

    public static void save(long seed, int avatarX, int avatarY){
        try (PrintWriter out = new PrintWriter(new FileOutputStream(SAVE))) {
            out.println(seed + "," + avatarX + "," + avatarY);
        } catch (IOException e) {
            System.err.println("Failed to save game: " + e.getMessage());
        }
    }

    public static SaveLoad load() {
        try(Scanner in = new Scanner(new File(SAVE))){
            String line = in.nextLine();
            String[] parts = line.split(",");
            long seed = Long.parseLong(parts[0]);
            int avatarX = Integer.parseInt(parts[1]);
            int avatarY = Integer.parseInt(parts[2]);
            return new SaveLoad(seed, avatarX, avatarY);
        } catch(Exception e) {
            System.err.println("Failed to load game: " + e.getMessage());
            return null;
        }
    }
}
