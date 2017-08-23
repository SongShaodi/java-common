package me.songsd.common.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by SongSD on 2017/7/26.
 */
public class Lists {

    public static <T> List<T> newArrayList(T... elements) {
        List<T> list = new ArrayList<T>();
        Collections.addAll(list, elements);
        return list;
    }

    public static <T> List<T> newArrayList(Collection<T> elements) {
        List<T> list = new ArrayList<T>();
        list.addAll(elements);
        return list;
    }

}
