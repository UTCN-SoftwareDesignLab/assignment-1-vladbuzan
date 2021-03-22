package model.generators;

import java.util.UUID;

public class UniqueLongGenerator {
    public static Long generateUniqueLong() {
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;

    }
}
