package me.songsd.common.util;

import javafx.util.Pair;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by SongSD on 2017/8/18.
 */
public class JSONUtilTest {

    @Test
    public void testJsonDiff() {
        Pair<JSONObject, JSONObject> diff;

        diff = JSONUtil.getJSONDiff(
                "{\"k1\":\"v1\",\"k2\":[\"v1\",\"v2\"],\"k3\":123}",
                "{\"k1\":\"v1\",\"k2\":[\"v2\",\"v3\"],\"k4\":123}");
        Assert.assertEquals("{\"k2\":[\"v1\",\"v2\"],\"k3\":123}", diff.getKey().toString());
        Assert.assertEquals("{\"k2\":[\"v2\",\"v3\"],\"k4\":123}", diff.getValue().toString());

        diff = JSONUtil.getJSONDiff(
                "{\"k1\":\"v1\",\"k2\":[\"v1\",\"v2\"],\"k3\":123}",
                "{\"k1\":\"v1\",\"k2\":[\"v1\",\"v2\"],\"k3\":123}");
        Assert.assertEquals("{}", diff.getKey().toString());
        Assert.assertEquals("{}", diff.getValue().toString());

        diff = JSONUtil.getJSONDiff(
                "{\"k1\":\"v1\",\"k2\":[\"v1\",\"v2\"],\"k3\":123}",
                "{\"k1\":\"v2\",\"k2\":[\"v2\",\"v2\"],\"k3\":234}");
        Assert.assertEquals("{\"k1\":\"v1\",\"k2\":[\"v1\",\"v2\"],\"k3\":123}", diff.getKey().toString());
        Assert.assertEquals("{\"k1\":\"v2\",\"k2\":[\"v2\",\"v2\"],\"k3\":234}", diff.getValue().toString());

        diff = JSONUtil.getJSONDiff(
                "{\"k1\":{\"sub1\":\"text1\",\"sub2\":[1,2,3]}}",
                "{\"k1\":{\"sub1\":\"text2\",\"sub2\":[1,2,3]}}");
        Assert.assertEquals("{\"k1\":{\"sub1\":\"text1\"}}", diff.getKey().toString());
        Assert.assertEquals("{\"k1\":{\"sub1\":\"text2\"}}", diff.getValue().toString());

        diff = JSONUtil.getJSONDiff(
                "{\"k1\":{\"sub1\":\"text1\",\"sub2\":[1,2,3]},\"k2\":{\"sub1\":\"text1\",\"sub2\":[1,2,3]}}",
                "{\"k1\":{\"sub1\":\"text1\",\"sub2\":[1,2,3]},\"k2\":{\"sub1\":\"text1\",\"sub2\":[2,3,4]}}");
        Assert.assertEquals("{\"k2\":{\"sub2\":[1,2,3]}}", diff.getKey().toString());
        Assert.assertEquals("{\"k2\":{\"sub2\":[2,3,4]}}", diff.getValue().toString());

        diff = JSONUtil.getJSONDiff(
                "{\"k1\":[{\"id\":1,\"k1\":123,\"k2\":\"v1\"},{\"id\":2,\"k1\":123,\"k2\":\"v2\"}],\"k2\":{\"sub1\":\"text1\",\"sub2\":[1,2,3]}}",
                "{\"k1\":[{\"id\":1,\"k1\":123,\"k2\":\"v1\"},{\"id\":3,\"k1\":234,\"k2\":\"v2\"}],\"k2\":{\"sub1\":\"text1\",\"sub2\":[2,3,4]}}");
        Assert.assertEquals("{\"k1\":[{\"id\":1},{\"id\":2,\"k1\":123,\"k2\":\"v2\"},null],\"k2\":{\"sub2\":[1,2,3]}}", diff.getKey().toString());
        Assert.assertEquals("{\"k1\":[{\"id\":1},null,{\"id\":3,\"k1\":234,\"k2\":\"v2\"}],\"k2\":{\"sub2\":[2,3,4]}}", diff.getValue().toString());

        diff = JSONUtil.getJSONDiff(
                "{\"k1\":[{\"id\":1,\"k1\":123,\"k2\":\"v1\"},{\"id\":2,\"k1\":123}],\"k2\":{\"sub1\":\"text1\",\"sub2\":[1,2,3]}}",
                "{\"k1\":[{\"id\":1,\"k1\":123,\"k2\":\"v1\"},{\"id\":2,\"k1\":234}],\"k2\":{\"sub1\":\"text1\",\"sub2\":[2,3,4]}}");
        Assert.assertEquals("{\"k1\":[{\"id\":1},{\"id\":2,\"k1\":123}],\"k2\":{\"sub2\":[1,2,3]}}", diff.getKey().toString());
        Assert.assertEquals("{\"k1\":[{\"id\":1},{\"id\":2,\"k1\":234}],\"k2\":{\"sub2\":[2,3,4]}}", diff.getValue().toString());
    }

    @Test
    public void testJsonArrayDiff() {
        Pair<JSONArray, JSONArray> diff;

        diff = JSONUtil.getJSONArrayDiff(
                "[{\"id\":1,\"k1\":123,\"k2\":\"v1\"},{\"id\":2,\"k1\":123,\"k2\":\"v2\"}]",
                "[{\"id\":1,\"k1\":123,\"k2\":\"v1\"},{\"id\":3,\"k1\":234,\"k2\":\"v2\"}]", "id");
        Assert.assertEquals("[{\"id\":1},{\"id\":2,\"k1\":123,\"k2\":\"v2\"},null]", diff.getKey().toString());
        Assert.assertEquals("[{\"id\":1},null,{\"id\":3,\"k1\":234,\"k2\":\"v2\"}]", diff.getValue().toString());

        diff = JSONUtil.getJSONArrayDiff(
                "[{\"id\":1,\"k1\":123,\"k2\":\"v1\"},{\"id\":2,\"k1\":123,\"k2\":\"v2\"}]",
                "[{\"id\":1,\"k1\":123,\"k2\":\"v1\"},{\"id\":2,\"k1\":234,\"k2\":\"v2\"}]", "id");
        Assert.assertEquals("[{\"id\":1},{\"id\":2,\"k1\":123}]", diff.getKey().toString());
        Assert.assertEquals("[{\"id\":1},{\"id\":2,\"k1\":234}]", diff.getValue().toString());

    }

}
