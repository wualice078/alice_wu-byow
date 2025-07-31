package core;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Hallway {
    private Point p1;
    private Point p2;
    private Map<Point, Integer> turns;

    public Hallway(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
        turns = new HashMap<Point, Integer>();
    }

    public Hallway(Point p1, Point p2, Map<Point, Integer> turns) {
        this.p1 = p1;
        this.p2 = p2;
        this.turns = turns;
    }

    public void addTurn(Point p, int d) {
        this.turns.put(p, d);
    }
}
