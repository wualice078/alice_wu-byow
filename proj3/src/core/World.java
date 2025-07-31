package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class World {

    private TETile[][] world;
    private static Random random;
    private int width;
    private int height;


    public World(int w, int h, long seed) {

        world = new TETile[w][h];
        random = new Random(seed);
        this.width = w;
        this.height = h;

        for (int x = 0; x < w; x += 1) {
            for (int y = 0; y < h; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        System.out.println("generating...");
        generateWorld();

    }

    private void generateWorld() {
        Set<Room> rooms = generateRooms();
        //generateHallways(rooms);

    }

    private void generateHallways(Set<Room> rooms) {

    }

    private Set<Room> generateRooms() {
        Set<Room> rooms = new HashSet<>();
        int numRooms = random.nextInt(5);
        numRooms += 10;

        System.out.println("numRooms: " + numRooms);

        while (rooms.size() < numRooms) {
            Room room = generateRoom();
            System.out.println("room: " + room.x1() + " " + room.y1() + " " + room.x2() + " " + room.y2());
            if(!overLap(room, rooms)) {
                rooms.add(room);
                drawRoom(room);
                System.out.println("~added " + rooms.size() + " rooms~");
            }
        }

        return rooms;
    }

    private void drawRoom(Room r) {
        System.out.println("drawing...");
        for (int x = r.x1(); x < r.x2(); x++) {
            for (int y = r.y1(); y < r.y2(); y++) {
                if(x == r.x1() || x == r.x2() - 1|| y == r.y1() || y == r.y2() - 1) {
                    this.world[x][y] = Tileset.wall;
                } else {
                    this.world[x][y] = Tileset.floor;
                }
            }
        }
    }

    private boolean overLap(Room room, Set<Room> rooms) {
        System.out.print("check: ");
        for(Room r : rooms) {
            if(overLap(room, r)) {
                System.out.println("overlap");
                return true;
            }
        }
        System.out.println("no overlap");
        return false;
    }

    private boolean overLap(Room r1, Room r2) {
        if(r1.x2() < r2.x1()) {
            return false;
        }
        if(r1.x1() > r2.x2()) {
            return false;
        }
        if(r1.y2() < r2.y1()) {
            return false;
        }
        if(r1.y1() > r2.y2()) {
            return false;
        }
        return true;
    }

    private Room generateRoom() {
        int x1 = random.nextInt(width-10);
        int y1 = random.nextInt(height-10);
        Point p1 = new Point(x1, y1);
        int x2 = x1 + random.nextInt(5) + 5;
        int y2 = y1 + random.nextInt(5) + 5;
        Point p2 = new Point(x2, y2);
        Room room = new Room(p1, p2);

        return room;
    }

    public TETile[][] getWorld() {
        return this.world;
    }

}
