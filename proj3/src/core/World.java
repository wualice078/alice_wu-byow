package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
import java.util.*;
import java.util.List;

public class World {

    private final TETile[][] world;
    private static Random random;
    private final int width;
    private final int height;
    private Point avatar;
    private Point chaser;
    private boolean coin = false;
    private PathFinder pathFinder;
    private Set<Point> coins = new HashSet<>();


    public World(int w, int h, long seed) {

        world = new TETile[w][h];
        random = new Random(seed);
        this.width = w;
        this.height = h;

        for (int x = 0; x < w; x += 1) {
            for (int y = 0; y < h; y += 1) {
                world[x][y] = Tileset.nothing;
            }
        }

        generateWorld();
        pathFinder = new PathFinder(chaser, world);
    }

    public int height() {
        return height;
    }

    public int width() {
        return width;
    }

    public TETile[][] world() {
        return world;
    }

    public Point avatar() {
        return avatar;
    }

    public Set<Point> coins() {
        return coins;
    }

    public PathFinder getPathFinder() {
        return pathFinder;
    }

    public Point chaser(){
        return chaser;
    }

    public void setAvatar(Point avatar) {
        world[this.avatar.x][this.avatar.y] = Tileset.floor;
        this.avatar = avatar;
        world[avatar.x][avatar.y] = Tileset.avatar;
    }

    public void setChaser(Point chaser) {
        world[this.chaser.x][this.chaser.y] = Tileset.floor;
        this.chaser = chaser;
        world[chaser.x][chaser.y] = Tileset.chaser;
    }

    public void setCoins(Set<Point> coins) {
        for (Point coin : this.coins) {
            world[coin.x][coin.y] = Tileset.floor;
        }
        this.coins = coins;
        for (Point coin : coins) {
            world[coin.x][coin.y] = Tileset.coin;
        }
    }

    private void generateWorld() {
        Set<Room> rooms = generateRooms();
        generateHallways(rooms);
        generateAvatar();
        generateChaser();
        generateCoins();
    }

    private void generateChaser() {
        int x = 0;
        int y = 0;
        while(world[x][y] != Tileset.floor) {
            x = random.nextInt(3,width - 14);
            y = random.nextInt(3, height - 14);
        }
        world[x][y] = Tileset.chaser;
        this.chaser = new Point(x, y);
    }

    public void moveChaser() {
        List<Point> path = pathFinder.findPath(chaser, avatar);
        if (path.size() > 1) {
            Point next = path.get(1);
            if (coin) {
                world[chaser.x][chaser.y] = Tileset.coin;
            } else {
                world[chaser.x][chaser.y] = Tileset.floor;
            }
            chaser = next;
            coin = world[chaser.x][chaser.y] == Tileset.coin;
            world[chaser.x][chaser.y] = Tileset.chaser;
        }
    }

    public List<Point> displayPath() {
        List<Point> path = pathFinder.findPath(chaser, avatar);
        if (path != null && path.size() > 1) {
            for (Point p : path) {
                if (p.equals(chaser) || p.equals(avatar) || coins.contains(p)) {
                    continue;
                }
                world[p.x][p.y] = Tileset.path;
            }
            return path;
        }
        return null;
    }

    public void clearPath(List<Point> previousPath) {
        if (previousPath != null) {
            for (Point p : previousPath) {
                if (world()[p.x][p.y] == Tileset.path) {
                    world()[p.x][p.y] = Tileset.floor;
                }
            }
        }
    }

    private void generateAvatar() {
        int x = 0;
        int y = 0;
        while(world[x][y] != Tileset.floor) {
            x = random.nextInt(3,width - 14);
            y = random.nextInt(3, height - 14);
        }
        world[x][y] = Tileset.avatar;
        this.avatar = new Point(x, y);
    }

    public void moveAvatar(char c) {
        if (c == 'w' || c == 'W') {
            if (world[avatar.x][avatar.y + 1] != Tileset.wall) {
                avatar.y++;
                checkCoin();
                world[avatar.x][avatar.y] = Tileset.avatar;
                world[avatar.x][avatar.y - 1] = Tileset.floor;
            }
        }
        if (c == 'a' || c == 'A') {
            if (world[avatar.x - 1][avatar.y] != Tileset.wall) {
                avatar.x--;
                checkCoin();
                world[avatar.x][avatar.y] = Tileset.avatar;
                world[avatar.x + 1][avatar.y] = Tileset.floor;
            }
        }
        if (c == 's' || c == 'S') {
            if (world[avatar.x][avatar.y - 1] != Tileset.wall) {
                avatar.y--;
                checkCoin();
                if (world[avatar.x][avatar.y] == Tileset.coin) {
                    this.coins.remove(new Point(avatar.x, avatar.y));
                }
                world[avatar.x][avatar.y] = Tileset.avatar;
                world[avatar.x][avatar.y + 1] = Tileset.floor;
            }
        }
        if (c == 'd' || c == 'D') {
            if (world[avatar.x + 1][avatar.y] != Tileset.wall) {
                avatar.x++;
                checkCoin();
                world[avatar.x][avatar.y] = Tileset.avatar;
                world[avatar.x - 1][avatar.y] = Tileset.floor;
            }
        }
    }

    private void generateCoins() {
        for (int i = 0; i < 20; i++) {
            generateCoin();
        }
    }

    private void generateCoin() {
        int x = 0;
        int y = 0;
        while(world[x][y] != Tileset.floor) {
            x = random.nextInt(3,width - 14);
            y = random.nextInt(3, height - 14);
        }
        world[x][y] = Tileset.coin;
        this.coins.add(new Point(x, y));
    }

    private void checkCoin() {
        if (world[avatar.x][avatar.y] == Tileset.coin) {
            this.coins.remove(new Point(avatar.x, avatar.y));
        }
    }

    private void generateHallways(Set<Room> rooms) {
        ArrayList<Room> roomList = new ArrayList<>(rooms);
        roomList.sort(Comparator.comparingInt(r -> r.getCenter().x));
        for(int i = 0; i < roomList.size() - 1; i++) {
            Point p1 = roomList.get(i).getCenter();
            Point p2 = roomList.get(i + 1).getCenter();
            drawHallway(p1, p2);
        }
        roomList.sort(Comparator.comparingInt(r -> r.getCenter().y));
        for(int i = 0; i < roomList.size() - 1; i++) {
            Point p1 = roomList.get(i).getCenter();
            Point p2 = roomList.get(i + 1).getCenter();
            drawHallway(p1, p2);
        }
    }

    private void drawStraightHallway(Point p1, Point p2) {
        if (p1. x == p2.x) {
            int yStart = Math.max(p1.y, p2.y);
            int yEnd = Math.min(p1.y, p2.y);
            for (int y = yStart; y >= yEnd; y--) {
                setHallwayTile(p1.x, y);
            }
        } else if (p1.y == p2.y) {
            int xStart = Math.max(p1.x, p2.x);
            int xEnd = Math.min(p1.x, p2.x);
            for (int x = xStart; x >= xEnd; x--) {
                setHallwayTile(x, p1.y);
            }
        }
    }

    private void drawHallway(Point p1, Point p2) {
        Point turn;
        if (random.nextBoolean()) {
            turn = new Point(p1.x, p2.y);
        } else {
            turn = new Point(p2.x, p1.y);
        }
        drawStraightHallway(p1, turn);
        drawStraightHallway(turn, p2);
    }

    private void setHallwayTile(int x, int y) {
        world[x][y] = Tileset.floor;
        if (world[x + 1][y] == Tileset.nothing) {
            world[x + 1][y] = Tileset.wall;
        }
        if (world[x - 1][y] == Tileset.nothing) {
            world[x - 1][y] = Tileset.wall;
        }
        if (world[x][y + 1] == Tileset.nothing) {
            world[x][y + 1] = Tileset.wall;
        }
        if (world[x][y - 1] == Tileset.nothing) {
            world[x][y - 1] = Tileset.wall;
        }
    }

    private Set<Room> generateRooms() {
        Set<Room> rooms = new HashSet<>();
        int numRooms = random.nextInt(14, 18);

        int attempts = 0;
        while (rooms.size() < numRooms && attempts < 1000) {
            Room room = generateRoom();
            attempts++;
            if(!overLap(room, rooms)) {
                rooms.add(room);
                drawRoom(room);
            }
        }

        return rooms;
    }

    private void drawRoom(Room r) {
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
        for(Room r : rooms) {
            if(overLap(room, r)) {
                return true;
            }
        }
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
        int x1 = random.nextInt(2, width - 13);
        int y1 = random.nextInt(2, height - 13);
        Point p1 = new Point(x1, y1);
        int x2 = x1 + random.nextInt(7, 14);
        int y2 = y1 + random.nextInt(7, 14);
        Point p2 = new Point(x2, y2);
        return new Room(p1, p2);
    }

}