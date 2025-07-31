package core;

import java.awt.*;

public class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;  // same object
        if (!(o instanceof Coordinate)) return false;

        Coordinate other = (Coordinate) o;
        return this.x == other.x && this.y == other.y;
    }
}
