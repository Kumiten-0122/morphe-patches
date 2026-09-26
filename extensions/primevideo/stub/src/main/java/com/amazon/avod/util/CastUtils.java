package com.amazon.avod.util;

public final class CastUtils {
    public static <T> T castTo(Object obj, Class<T> cls) {
        if (cls.isInstance(obj)) {
            return cls.cast(obj);
        }
        return null;

    }
}
