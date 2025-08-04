package core;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Hallway {
    private Point p1;
    private Point p2;
    private Point turnPoint;
    private int direction;

    public Hallway(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
        turnPoint = new Point(p1.x, p2.y);

        if(p1.x < p2.x && p1.y > p2.y) {
            this.direction = 1;
        } else if(p1.x > p2.x && p1.y > p2.y) {
            this.direction = 2;
        } else if(p1.x > p2.x && p1.y < p2.y) {
            this.direction = 3;
        } else if(p1.x < p2.x && p1.y < p2.y) {
            this.direction = 4;
        }
    }


    public int getDirection() {
        return this.direction;
    }

    public Point getTurnPoint() {
        return this.turnPoint;
    }

    public Point getP1() {
        return this.p1;
    }

    public Point getP2() {
        return this.p2;
    }

    public void setTurnPoint(int x, int y) {
        turnPoint.x = x;
        turnPoint.y = y;
    }
}
