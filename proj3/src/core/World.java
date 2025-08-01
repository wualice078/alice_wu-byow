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
        Set<Hallway> hallways = generateHallways();
    }


    private Set<Hallway> generateHallways() {
        Set<Hallway> hallways = new HashSet<>();
        int numHallways = random.nextInt(15, 21);
        System.out.println("numHallways: " + numHallways);

        while (hallways.size() < numHallways) {
            Hallway hallway = generateHallway();
            System.out.println("hallway: " + hallway.getP1().x + " " + hallway.getP1().y + " " + hallway.getP2().x + " " + hallway.getP2().y);
            drawHallway(hallway);
           /* if(!overlap(hallway, hallways)) {
                hallways.add(hallway);
                drawHallway(hallway);
                System.out.println("~added " + hallways.size() + " hallways~");
            }*/
        }

        return hallways;
    }

    private void drawHallways(Set<Hallway> hallways) {
        for (Hallway hallway : hallways) {
            drawHallway(hallway);
        }
    }

    private void drawHallway(Hallway hallway) {
        System.out.println("drawing...");
        Point p1 = hallway.getP1();
        Point p2 = hallway.getP2();
        if (p1.x == p2.x) {
            drawXEqualsHallway(hallway);
        } else if (p1.y == p2.y) {
            drawYEqualsHallway(hallway);
        }else {
            drawTuringHallway(hallway);
        }
    }

    private void drawXEqualsHallway(Hallway hallway) {
        Point p1 = hallway.getP1();
        Point p2 = hallway.getP2();
        int y = Math.max(p1.y, p2.y);
        int min = Math.min(p1.y, p2.y);
        while (y > min) {
            world[p1.x - 1][y] = Tileset.wall;
            world[p1.x][y] = Tileset.floor;
            world[p1.x + 1][y] = Tileset.floor;
            y--;
        }
    }

    private void drawYEqualsHallway(Hallway hallway) {
        Point p1 = hallway.getP1();
        Point p2 = hallway.getP2();
        int x = Math.max(p1.x, p2.x);
        int min = Math.min(p1.x, p2.x);
        while (x > min) {
            world[p1.y - 1][x] = Tileset.wall;
            world[p1.y][x] = Tileset.floor;
            world[p1.y + 1][x] = Tileset.floor;
            x--;
        }
    }

    private void drawTuringHallway(Hallway hallway) {
        Point p1 = hallway.getP1();
        Point p2 = hallway.getP2();
        Point p3 = new Point(hallway.getTurnPoint().x, hallway.getTurnPoint().y + 1);
        drawXEqualsHallway(new Hallway(p1, p3));
        Point p4 = new Point(hallway.getTurnPoint().x - 1, hallway.getTurnPoint().y);
        drawYEqualsHallway(new Hallway(p2, p4));
        world[hallway.getTurnPoint().x][hallway.getTurnPoint().y] = Tileset.floor;
        world[hallway.getTurnPoint().x - 1][hallway.getTurnPoint().y] = Tileset.wall;
        world[hallway.getTurnPoint().x][hallway.getTurnPoint().y - 1] = Tileset.wall;
        world[hallway.getTurnPoint().x - 1][hallway.getTurnPoint().y - 1] = Tileset.wall;
    }

    private Set<Room> generateRooms() {
        Set<Room> rooms = new HashSet<>();
        int numRooms = random.nextInt(10, 16);

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
        int x1 = random.nextInt(width - 3) ;
        int y2 = random.nextInt( height - 3) ;
        int x2 = random.nextInt(0, 2);
        int y1 = random.nextInt(0, 2);
        if (y1 == 1) {
            y1 = height - 1;
        }
        if (x2 == 1) {
            x2 = width - 1;
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
        if (Math.abs(x11 - x21) <= 2 || Math.abs(x11 - x22) <= 2 || Math.abs(x12 - x21) <= 2 || Math.abs(x12 - x22) <= 2) {
            return true;
        } else if (Math.abs(y11 - y21) <= 2 || Math.abs(y11 - y22) <= 2 || Math.abs(y12 - y21) <= 2 || Math.abs(y12 - y22) <= 2 ) {
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
