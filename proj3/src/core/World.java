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

    private Hallway gennerateHallway() {
        int x1 = -1;
        int y2 = -1;
        int x2 = width;
        int y1 = height;
        while (x1 == 0 || x1 == width ) {
            x1 = random.nextInt(2 * width);
        }
        while (y2 == 0 || y2 == height) {
            y2 = random.nextInt( 2 * height);
        }
        if (x1 > width) {
            x1 = 2 * width - x1;
            y1 = 0;
        }
        if (y2 > height) {
            y2 = 2 * height - y2;
            x2 = 0;
        }


        Point p1 = new Point(x1, y1);
        Point p2 = new Point(x2, y2);
        Point turn = new Point(x1, y2);
        boolean buildAHallway = false;

        int y = Math.max(y1, y2);
        while (y > Math.min(y1, y2)) {
            int reachWall = 0;
            if (reachAWall(x1, y)){
                reachWall++;
                if (reachWall == 2) {
                    p1.setLocation(x1, y);
                }
                if (reachWall == 3) {
                    p2.setLocation(x1, y);
                    Hallway hallway = new Hallway(p1, p2);
                    buildAHallway = true;
                    return hallway;
                }
            }
            y--;
        }

        int x = Math.max(x1, x2);
        while (x > Math.min(x1, x2)) {
            int reachWall = 0;
            if (reachAWall(x, y2)){
                reachWall++;
                if (reachWall == 2) {
                    p1.setLocation(x, y2);
                }
                if (reachWall == 3) {
                    p2.setLocation(x, y2);
                    Hallway hallway = new Hallway(p1, p2);
                    buildAHallway = true;
                    return hallway;
                }
            }
            x--;
        }

        return null;
    }

    private boolean reachAWall(int x, int y) {
        TETile wall = new TETile('#', new Color(216, 128, 128), Color.darkGray,
                "wall", 1);
        return world[x][y].equals(wall);
    }

    private boolean  overlap(Hallway h1, Hallway h2) {
        int x11 = h1.getP1().x;
        int y11 = h1.getP1().y;
        int x12 = h1.getP2().x;
        int y12 = h1.getP2().y;
        int x21 = h2.getP1().x;
        int y21 = h2.getP1().y;
        int x22 = h2.getP2().x;
        int y22 = h2.getP2().y;
        if (Math.abs(x11 - x21) < 2 || Math.abs(x11 - x22) < 2 || Math.abs(x12 - x21) < 2 || Math.abs(x12 - x22) < 2) {
            return true;
        } else if (Math.abs(y11 - y21) < 2 || Math.abs(y11 - y22) < 2 || Math.abs(y12 - y21) < 2 || Math.abs(y12 - y22) < 2 ) {
            return true;
        } else {
            return false;
        }
    }

    private boolean overlap(Hallway h1, Set<Hallway> hallways) {
        System.out.print("check: ");
        for(Hallway h : hallways) {
            if(overlap(h1, h)) {
                System.out.println("overlap");
                return true;
            }
        }
        System.out.println("no overlap");
        return false;
    }

    public TETile[][] getWorld() {
        return this.world;
    }

}
