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
        return new Room(p1, p2);
    }

    private Hallway generateHallway() {
        Hallway newHallway;
        do {
            Hallway temp = generateRandomPoints();
            newHallway = validHallway(temp.getP1(), temp.getP2());
        } while (newHallway == null); 
        return newHallway;
    }

    private boolean reachAWall(int x, int y) {
        return world[x][y].equals(Tileset.wall);
    }

    private Hallway generateRandomPoints() {
        int x1 = 0;
        int y2 = 0;
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

        return new Hallway(p1, p2);
    }
    private Hallway validHallway(Point p1, Point p2) {
        int y = Math.max(p1.y, p2.y);
        int reachWall = 0;
        while (y > Math.min(p1.y, p2.y)) {
            if (reachAWall(p1.x, y)){
                reachWall++;
                if (reachWall == 2) {
                    p1.setLocation(p1.x, y);
                }
                if (reachWall == 3) {
                    p2.setLocation(p1.x, y);
                    return new Hallway(p1, p2);
                }
            }
            y--;
        }

        int x = Math.max(p1.x, p2.x);
        while (x > Math.min(p1.x, p2.x)) {
            if (reachAWall(x, p2.y)){
                reachWall++;
                if (reachWall == 2) {
                    p1.setLocation(x, p2.y);
                }
                if (reachWall == 3) {
                    p2.setLocation(x, p2.y);
                    return new Hallway(p1, p2);
                }
            }
            x--;
        }
        return null;
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
