package me.songsd.common.util;

import javafx.util.Pair;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.*;

/**
 * Created by SongSD on 2017/8/24.
 */
public class JSONUtil {

    public static Pair<JSONObject, JSONObject> getJSONDiff(String json1, String json2) {
        JSONObject obj1 = JSONObject.fromObject(json1);
        JSONObject obj2 = JSONObject.fromObject(json2);
        return getJSONObjDiff(obj1, obj2);
    }

    public static Pair<JSONObject, JSONObject> getJSONObjDiff(JSONObject obj1, JSONObject obj2) {
        if (obj1 == null || obj2 == null) {
            return new Pair<>(obj1, obj2);
        }

        JSONObject diffObj1 = new JSONObject();
        JSONObject diffObj2 = new JSONObject();

        Set fullKeySet = new HashSet();
        fullKeySet.addAll(obj1.keySet());
        fullKeySet.addAll(obj2.keySet());

        for (Object key : fullKeySet) {
            Object value1 = obj1.get(key);
            Object value2 = obj2.get(key);

            if (value1 != null && value2 != null && value1.equals(value2)) {
                continue;
            }

            if (value1 instanceof JSONObject && value2 instanceof JSONObject) {
                Pair<JSONObject, JSONObject> subDiffPair = getJSONObjDiff(
                        (JSONObject) value1, (JSONObject) value2);
                diffObj1.put(key, subDiffPair.getKey());
                diffObj2.put(key, subDiffPair.getValue());
            } else if (value1 instanceof JSONArray && value2 instanceof JSONArray) {
                Pair<JSONArray, JSONArray> subDiffPair = getJSONArrayDiff(
                        (JSONArray) value1, (JSONArray) value2, "id");
                diffObj1.put(key, subDiffPair.getKey());
                diffObj2.put(key, subDiffPair.getValue());
            } else {
                diffObj1.put(key, value1);
                diffObj2.put(key, value2);
            }
        }

        return new Pair<>(diffObj1, diffObj2);
    }

    /**
     * 用于分析两个list的diff
     * @param idKey 通过idKey的值将两个list中的对象对应起来
     */
    public static Pair<JSONArray, JSONArray> getJSONArrayDiff(String json1, String json2, String idKey) {
        return getJSONArrayDiff(JSONArray.fromObject(json1), JSONArray.fromObject(json2), idKey);
    }

    public static Pair<JSONArray, JSONArray> getJSONArrayDiff(JSONArray jsonArray1, JSONArray jsonArray2, String idKey) {
        JSONArray diffArray1 = new JSONArray();
        JSONArray diffArray2 = new JSONArray();

        if (jsonArray1.isEmpty() || jsonArray2.isEmpty() ||
                !(jsonArray1.get(0) instanceof JSONObject) || !(jsonArray2.get(0) instanceof JSONObject)) {
            return jsonArray1.equals(jsonArray2) ?
                    new Pair<>(diffArray1, diffArray2) : new Pair<>(jsonArray1, jsonArray2);
        }

        Map<Object, JSONObject> objMap1 = new LinkedHashMap<>();
        Map<Object, JSONObject> objMap2 = new LinkedHashMap<>();

        for (JSONObject object : (List<JSONObject>) jsonArray1) {
            Object keyObj = object.get(idKey);
            objMap1.put(keyObj, object);
        }
        for (JSONObject object : (List<JSONObject>) jsonArray2) {
            Object keyObj = object.get(idKey);
            objMap2.put(keyObj, object);
        }

        Set fullKeySet = new HashSet(objMap1.keySet());
        fullKeySet.addAll(objMap2.keySet());

        for (Object keyObj : fullKeySet) {
            Pair<JSONObject, JSONObject> diffPair = getJSONObjDiff(objMap1.get(keyObj), objMap2.get(keyObj));

            if (diffPair.getKey() == null || diffPair.getValue() == null) {
                diffArray1.add(diffPair.getKey());
                diffArray2.add(diffPair.getValue());
                continue;
            }

            JSONObject diffObj1 = new JSONObject().element(idKey, keyObj);
            JSONObject diffObj2 = new JSONObject().element(idKey, keyObj);

            diffObj1.putAll(diffPair.getKey());
            diffObj2.putAll(diffPair.getValue());

            diffArray1.add(diffObj1);
            diffArray2.add(diffObj2);
        }

        return new Pair<>(diffArray1, diffArray2);
    }
}
