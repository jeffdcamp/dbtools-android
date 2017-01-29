package org.dbtools.android.domain.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class EnumUtil {
    /**
     * Safely get an enum for the given ordinal
     * @return the Enum value for the given ordinal otherwise the defaultValue
     */
    public static <E extends Enum<E>> E ordinalToEnum(Class<E> enumClass, int ordinal, E defaultValue) {
        E[] enumConstants = enumClass.getEnumConstants();
        if (ordinal >=0 && ordinal < enumConstants.length) {
            return enumConstants[ordinal];
        } else {
            return defaultValue;
        }
    }

    /**
     * Safely get an enum for the given enumText
     * @return the Enum value for the given enumText otherwise the defaultValue
     */
    public static <E extends Enum<E>> E stringToEnum(@NonNull Class<E> enumClass, @Nullable String enumText, @NonNull E defaultValue) {
        E[] enumConstants = enumClass.getEnumConstants();
        if (enumText != null && !enumText.isEmpty() && enumConstants != null && enumConstants.length > 0) {
            for (E enumConstant : enumConstants) {
                if (enumConstant.name().equals(enumText)) {
                    return enumConstant;
                }
            }
        }

        return defaultValue;
    }
}
