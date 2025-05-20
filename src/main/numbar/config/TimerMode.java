package xyz.numbar.config;

import java.security.InvalidParameterException;

public enum TimerMode {
    TICKS,
    SECONDS;

    public String formatLeft(float ticksLeft) {
        switch (this) {
            case TICKS -> {
                return "%d".formatted((int) ticksLeft);
            }
            case SECONDS -> {
                if (ticksLeft > 60 * 20) {
                    return "%d:%02d".formatted((int) (ticksLeft / (20f * 60f)), ((int) (ticksLeft / 20f)) % 60);
                }
                return "%.1g".formatted(ticksLeft / 20f);
            }
        }
        throw new InvalidParameterException(this.name());
    }
}
