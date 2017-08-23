package me.songsd.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SongSD on 2017/7/17.
 */
public class ChineseCharUtil {

    private static String[] units = { "", "十", "百", "千"};
    private static String[] numArray = { "零", "一", "二", "三",
            "四", "五", "六", "七", "八", "九"};

    public static String readNumber(long inputNum) {
        List<String> stack = new ArrayList<>();

        int digit = 0;
        boolean zeroFlag = false;
        long toProcessNum = inputNum;

        while (true) {
            int thisDigit = (int) (toProcessNum % 10);
            if (thisDigit == 0) {
                if (!zeroFlag) {
                    stack.add("零");
                }
                zeroFlag = true;
            } else {
                zeroFlag = false;
                stack.add(numArray[thisDigit] + units[digit % 4]);
            }

            toProcessNum /= 10;
            if (toProcessNum == 0) {
                break;
            }

            if (digit % 4 == 3) {
                stack.add(getStrMarkByDigit(digit));
            }
            digit ++;
        }

        StringBuilder result = new StringBuilder();
        for (int i = stack.size() - 1; i >= 0; i--) {
            result.append(stack.get(i));
        }
        return result.toString();
    }

    private static String getStrMarkByDigit(int digit) {
        int wanMark = digit / 4 + 1;
        StringBuilder result = new StringBuilder();
        if (wanMark % 2 == 1) {
            result.append("万");
        }
        for (int i = 0; i < wanMark / 2; i++) {
            result.append("亿");
        }
        return result.toString();
    }

}