package me.songsd.common.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by SongSD on 2017/8/24.
 */
public class ChineseCharUtilTest {

    @Test
    public void testReadNumber() {
        Assert.assertEquals(ChineseCharUtil.readNumber(1000101), "一百万零一百零一");
        Assert.assertEquals(ChineseCharUtil.readNumber(10000001), "一千万零一");
        Assert.assertEquals(ChineseCharUtil.readNumber(1010101), "一百零一万零一百零一");
        Assert.assertEquals(ChineseCharUtil.readNumber(10000101), "一千万零一百零一");
        Assert.assertEquals(ChineseCharUtil.readNumber(1000100001), "一十亿零一十万零一");
        Assert.assertEquals(ChineseCharUtil.readNumber(1231001), "一百二十三万一千零一");
        Assert.assertEquals(ChineseCharUtil.readNumber(123451234), "一亿二千三百四十五万一千二百三十四");
        Assert.assertEquals(ChineseCharUtil.readNumber(Long.MAX_VALUE),
                "九百二十二亿亿三千三百七十二万亿零三百六十八亿五千四百七十七万五千八百零七");
    }
}
