package me.songsd.common.util;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by SongSD on 2017/8/4.
 */
public class ListUtil {

    public static final int SORT_DESC = 0;
    public static final int SORT_ASC = 1;

    /**
     * 对<T>泛型的Object列表进行排序
     * @param list  待排序的List
     * @param sortField 指定的排序字段，使用getter()获取该列的值
     * @param sortMode desc或者asc，表示降序或者顺序
     * @param <T> 类型
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> void sort(List<T> list, String sortField, final int sortMode) {
        if (StringUtils.isEmpty(sortField) || (sortMode != SORT_DESC && sortMode != SORT_ASC)) {
            return;
        }

        list.sort((obj1, obj2) -> {
            int retVal = 0;
            try {
                String methodStr = ListUtil.getMethodStr(sortField);
                Method method = obj1.getClass().getMethod(methodStr);

                Object cmpObj1 = method.invoke(obj1);
                Object cmpObj2 = method.invoke(obj2);

                if (cmpObj1 instanceof Integer) {
                    int cmpVal1 = Integer.parseInt(cmpObj1.toString());
                    int cmpVal2 = Integer.parseInt(cmpObj2.toString());
                    retVal = Integer.compare(cmpVal1, cmpVal2);
                } else if (cmpObj1 instanceof Long) {
                    long cmpVal1 = Long.parseLong(cmpObj1.toString());
                    long cmpVal2 = Long.parseLong(cmpObj2.toString());
                    retVal = Long.compare(cmpVal1, cmpVal2);
                } else if (cmpObj1 instanceof Double) {
                    double cmpVal1 = Double.parseDouble(cmpObj1.toString());
                    double cmpVal2 = Double.parseDouble(cmpObj2.toString());
                    retVal = Double.compare(cmpVal1, cmpVal2);
                }

                if (sortMode == SORT_DESC) {
                    retVal = -retVal;
                }
            } catch (Exception ignored) {
            }
            return retVal;
        });
    }

    public static <F, T> List<F> refactorField(List<T> collection, FieldRefactor<F, T> fieldRefactor) {
        if(collection != null && !collection.isEmpty() && fieldRefactor != null) {
            List<F> fieldList = new ArrayList<>();
            for (T t : collection) {
                try {
                    fieldList.add(fieldRefactor.getField(t));
                } catch (Exception ignored) {
                }
            }
            return fieldList;
        } else {
            return Collections.emptyList();
        }
    }

    public interface FieldRefactor<F, T> {
        F getField(T var1);
    }

    public static <T> List<T> subListByPage(List<T> list, int page, int pageSize) {
        return subListByOffset(list, (page - 1) * pageSize, pageSize);
    }

    public static <T> List<T> subListByOffset(List<T> list, int offset, int limit) {
        if (list == null || offset > list.size()) {
            return new ArrayList<>();
        }

        int endIndex = Math.min(offset + limit, list.size());
        return list.subList(offset, endIndex);
    }

    public static <T> Map<Object, List<T>> groupBy(List<T> list, String field) {
        if (list == null) {
            return new HashMap<>();
        }

        Map<Object, List<T>> resultMap = new HashMap<>();

        for (T tObject : list) {
            try {
                String methodStr = ListUtil.getMethodStr(field);
                Method method = tObject.getClass().getMethod(methodStr);

                Object keyObj = method.invoke(tObject);

                if (!resultMap.containsKey(keyObj)) {
                    resultMap.put(keyObj, new ArrayList<T>());
                }
                resultMap.get(keyObj).add(tObject);
            } catch (Exception ignored) {
            }
        }

        return resultMap;
    }

    private static String getMethodStr(String field) {
        String newStr = field.substring(0, 1).toUpperCase() + field.replaceFirst("\\w","");
        return "get" + newStr;
    }

}
