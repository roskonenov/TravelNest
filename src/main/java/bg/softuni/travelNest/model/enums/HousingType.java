package bg.softuni.travelNest.model.enums;

import java.util.Arrays;

public enum HousingType {
    HOUSE, APARTMENT, PENTHOUSE, ATELIER, STUDIO;

    @Override
    public String toString() {
        return Arrays.toString(Arrays.stream(name().split("_"))
                        .map(word -> word.charAt(0) + word.substring(1).toLowerCase()).toArray())
                .replaceAll("[\\[\\],]", "");

    }
}
