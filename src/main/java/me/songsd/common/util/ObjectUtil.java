package me.songsd.common.util;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by SongSD on 2017/8/31.
 */
public class ObjectUtil {

    public static <T> T convertFromMap(Map<String, String> params, Class<T> tClass) {
        if (params == null) {
            return null;
        }

        for (Map.Entry<String, String> param : params.entrySet()) {
            Field field;
            try {
                field = tClass.getField(param.getKey());
            } catch (NoSuchFieldException ignored) {
                continue;
            }

            if (field.getType() == int.class) {

            } else if (field.getType() == long.class) {

            } else if (field.getType() == String.class) {

            } else if (field.getType() == double.class) {

            }
        }

        return null;
    }

}
