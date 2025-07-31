package core;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Hallways {
    private Point start;
    private Point end;
    private Map<Point, Integer> turningPoints;

    public Hallways(Point start, Point end) {
        this.start = start;
        this.end = end;
        turningPoints = new HashMap<Point, Integer>();
    }

    public Hallways(Point start, Point end, Map<Point, Integer> turning) {
        this.start = start;
        this.end = end;
        this.turningPoints = turning;
    }

}
