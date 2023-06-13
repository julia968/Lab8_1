package Classes;

import gui.Launcher;

public class Location {
    private Long x;
    private Float y;
    public void setX(Long x) {
        this.x = x;
    }

    public void setY(Float y) {
        if (y == null) {
            throw new IllegalArgumentException();
        } else {
            this.y = y;
        }
    }

    public Location() {
    }

    public Long getX() {
        return x;
    }

    public Float getY() {
        return y;
    }
    public Location(long x, float y) {
        setX(x);
        setY(y);
    }

    @Override
    public String toString() {
        return ("x = " + x + " y = " + y).replace(".", Launcher.getAppLanguage().getString("separator"));
    }
}
