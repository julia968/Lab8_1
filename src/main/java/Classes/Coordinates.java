package Classes;

import gui.Launcher;

public class Coordinates {
    private long x;
    private int y;

    public void setY(int y) {
        if (y == Integer.MIN_VALUE| y == Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Слишком большое значение, попробуйте снова");
        }
        this.y = y;
    }
    public void setX(Long x) {
        if (x == Long.MIN_VALUE | x == Long.MAX_VALUE) {
            throw new IllegalArgumentException("Слишком большое значение, попробуйте снова");
        } else {
            this.x = x;
        }
    }

    public long getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return ("x: " + x + " y: " + y).replace(".", Launcher.getAppLanguage().getString("separator"));
    }
    public Coordinates(long x, int y) {
        setX(x);
        setY(y);
    }
    public Coordinates() {

    }
}
