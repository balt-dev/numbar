package xyz.numbar.config;

import java.security.InvalidParameterException;

public enum TextMode {
    NORMAL,
    SMALL;

    public String format(String value) {
        switch (this) {
            case NORMAL -> { return value; }
            case SMALL -> {
                return value
                        .replace("0", "⁰")
                        .replace("1", "¹")
                        .replace("2", "²")
                        .replace("3", "³")
                        .replace("4", "⁴")
                        .replace("5", "⁵")
                        .replace("6", "⁶")
                        .replace("7", "⁷")
                        .replace("8", "⁸")
                        .replace("9", "⁹")
                        .replace("+", "⁺")
                        .replace("-", "⁻")
                        .replace("(", "⁽")
                        .replace(")", "⁾")
                        .replace(".", "·")
                        .replace("e", "ᵉ")
                        .replace("%", "");
            }
        }
        throw new InvalidParameterException(this.name());
    }

    public int yOffset() {
        switch (this) {
            case NORMAL -> { return 0; }
            case SMALL -> { return 2; }
        }
        throw new InvalidParameterException(this.name());
    }
}
