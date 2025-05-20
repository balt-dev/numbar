package xyz.numbar.config;

import java.security.InvalidParameterException;

public enum TimerMode {
    TICKS,
    SECONDS;

    public String formatLeft(float ticksLeft) {
        switch (this) {
            case TICKS -> {
                return "%d".formatted((int) Math.ceil(ticksLeft));
            }
            case SECONDS -> {
                if (ticksLeft > 60 * 20) {
                    int seconds = (int) Math.ceil(ticksLeft / 20f);
                    return "%d:%02d".formatted(
                            seconds / 60,
                            seconds % 60
                    );
                }

                int tenthSeconds = (int) Math.ceil(ticksLeft / 2f);
                return "%d.%d".formatted(
                        tenthSeconds / 10,
                        tenthSeconds % 10
                );
            }
        }
        throw new InvalidParameterException(this.name());
    }
}
