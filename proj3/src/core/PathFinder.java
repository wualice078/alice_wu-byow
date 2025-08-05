package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
import java.util.*;
import java.util.List;

public class PathFinder {
    private Point position;
    private TETile[][] world;

    public PathFinder(Point position, TETile[][] world) {
        this.position = position;
        this.world = world;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    private ArrayList<Point> getNeighbors(Point p) {
        ArrayList<Point> neighbors = new ArrayList<>();
        int x = p.x;
        int y = p.y;
        int[] dx = {0, 0, 1, -1};
        int[] dy = {1, -1, 0, 0};
        for (int i = 0; i < 4; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];
            if (world[newX][newY] != Tileset.wall) {
                neighbors.add(new Point(newX, newY));
            }
        }
        return neighbors;
    }

    //Manhattan distance
    private double h(Point start, Point end) {
        return Math.abs(start.x - end.x) + Math.abs(start.y - end.y);
    }

    //A*
    public List<Point> findPath(Point start, Point end) {
        PriorityQueue<Node> fringe = new PriorityQueue<>(Comparator.comparing(node -> node.f));
        Map<Point, Node> all = new HashMap<>();

        Node startN = new Node(start, null, 0, h(start, end));
        fringe.add(startN);
        all.put(start, startN);

        while(!fringe.isEmpty()) {
            Node curr = fringe.poll();
            if (curr.point.equals(end)) {
                return path(curr);
            }
            for (Point neighbor : getNeighbors(curr.point)) {
                double newG = curr.g + 1;
                Node neighborN = all.getOrDefault(neighbor, new Node(neighbor));
                if (newG < neighborN.g) {
                    neighborN.g = newG;
                    neighborN.parent = curr;
                    neighborN.f = newG + h(neighbor, end);
                    if (!fringe.contains(neighborN)) {
                        fringe.add(neighborN);
                        all.put(neighbor, neighborN);
                    }
                }
            }
        }
        return Collections.emptyList();
    }

    //construct path
    public List<Point> path(Node endN) {
        List<Point> path = new ArrayList<>();
        Node curr = endN;
        while(curr != null) {
            path.add(0, curr.point);
            curr = curr.parent;
        }
        return path;
    }

    private static class Node {
        Point point;
        Node parent;
        double g; //distance from start
        double f; //g + h

        public Node(Point point) {
            this.point = point;
            g = Double.POSITIVE_INFINITY;
            f = Double.POSITIVE_INFINITY;
        }

        public Node(Point point, Node parent, double g, double f) {
            this.point = point;
            this.parent = parent;
            this.g = g;
            this.f = f;
        }
    }
}
