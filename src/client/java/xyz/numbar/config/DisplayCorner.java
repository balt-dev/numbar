package xyz.numbar.config;

import org.joml.Vector2i;

public enum DisplayCorner {
    CENTER,
    TOP,
    LEFT,
    RIGHT,
    BOTTOM,
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
    TOP_LEFT,
    TOP_RIGHT;

    public Vector2i getPosition(int x, int y, int w, int h) {
        return new Vector2i(getX(x, w), getY(y, h));
    }

    int getX(int x, int w) {
        switch (this) {
            case CENTER, TOP, BOTTOM -> {
                return x + w / 2;
            }
            case LEFT, TOP_LEFT, BOTTOM_LEFT -> {
                return x;
            }
            case RIGHT, TOP_RIGHT, BOTTOM_RIGHT -> {
                return x + w;
            }
        }
        throw new IllegalArgumentException(this.name());
    }

    int getY(int y, int h) {
        switch (this) {
            case CENTER, LEFT, RIGHT -> {
                return y + h / 2;
            }
            case TOP, TOP_LEFT, TOP_RIGHT -> {
                return y;
            }
            case BOTTOM, BOTTOM_LEFT, BOTTOM_RIGHT -> {
                return y + h;
            }
        }
        throw new IllegalArgumentException(this.name());
    }

    public DisplayCorner invertX() {
        return switch (this) {
            case CENTER, TOP, BOTTOM -> this;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
            case TOP_LEFT -> TOP_RIGHT;
            case TOP_RIGHT -> TOP_LEFT;
            case BOTTOM_LEFT -> BOTTOM_RIGHT;
            case BOTTOM_RIGHT -> BOTTOM_LEFT;
        };
    }
}
