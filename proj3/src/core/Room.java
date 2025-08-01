package core;

import java.awt.*;

public class Room {
    private Point p1;
    private Point p2;

    public Room(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public int x1() {
        return p1.x;
    }

    public int x2() {
        return p2.x;
    }

    public int y1() {
        return p1.y;
    }

    public int y2() {
        return p2.y;
    }

    public Point getCenter() {
        int centerX = (x1() + x2()) / 2;
        int centerY = (y1() + y2()) / 2;
        return new Point(centerX, centerY);
    }


}
