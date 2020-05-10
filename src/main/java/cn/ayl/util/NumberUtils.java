package cn.ayl.util;

import java.util.Map;
import java.util.TreeMap;

/**
 * create by Rock-Ayl 2019-8-14
 * 用来处理大写转阿拉伯数字的转换工具类
 * eg:
 * 十一 → 11
 * 三十二 → 32
 */
public class NumberUtils {

    //阿拉伯数字组
    private static int[] ArabicNumbers = {1, 2, 3, 4, 5, 6, 7, 8, 9};

    //大写数字组
    private static String[] ChinaNumbers = {"一", "二", "三", "四", "五", "六", "七", "八", "九"};
    private static char[] ChinaNumbersByChar = {'一', '二', '三', '四', '五', '六', '七', '八', '九'};
    //单位组
    private static char[] measuringUnit = {'千', '百', '十'};

    //要用的缓存组
    private static Map<String, Integer> a = new TreeMap<>();
    private static Map<Character, Integer> b = new TreeMap<>();

    /**
     * 通用方法-大写转阿拉伯数字
     *
     * @param upperCaseNumber 大写数字 eg: 三十二
     * @return 32
     */
    public static int toArabicNumerals(String upperCaseNumber) {
        for (int i = 0; i < 9; i++) {
            a.put(ChinaNumbers[i], ArabicNumbers[i]);
            b.put(ChinaNumbersByChar[i], ArabicNumbers[i]);
        }
        String yw;
        String ww;
        String qw;
        if (upperCaseNumber.indexOf("亿") != -1) {
            String[] hundredMillion = upperCaseNumber.split("亿");
            if (hundredMillion.length == 1) {
                yw = hundredMillion[0];
                return yw(yw);
            } else {
                yw = hundredMillion[0];
                String[] spwq = hundredMillion[1].split("万");
                if (spwq.length == 1) {
                    ww = spwq[0];
                    return yw(yw) + ww(ww);
                } else {
                    yw = hundredMillion[0];
                    ww = spwq[0];
                    qw = spwq[1];
                    return yw(yw) + ww(ww) + qw(qw);
                }
            }
        } else if (upperCaseNumber.indexOf("万") != -1) {
            String[] spwq = upperCaseNumber.split("万");
            if (spwq.length == 1) {
                ww = spwq[0];
                return ww(ww);
            } else {
                ww = spwq[0];
                qw = spwq[1];
                return ww(ww) + qw(qw);
            }
        } else {
            return qw(upperCaseNumber);
        }
    }

    /**
     * 计算9999位
     *
     * @param str
     * @return
     */
    private static int qw(String str) {
        return gj(str);
    }

    /**
     * 计算9999万位
     *
     * @param str
     * @return
     */
    private static int ww(String str) {
        return gj(str) * 10000;
    }

    /**
     * 计算24亿(int最大是24亿多)
     *
     * @param str
     * @return
     */
    private static int yw(String str) {
        return gj(str) * 10000 * 10000;
    }

    /**
     * 用于计算4个位,把亿，万，进行拆分
     *
     * @param str
     * @return
     */
    private static int gj(String str) {
        int[] sum = {0, 0, 0, 0};
        int[] sum1 = {0, 0, 0, 0};
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == measuringUnit[0]) {
                sum[0] = b.get(str.charAt(i - 1));
            } else if (str.charAt(i) == measuringUnit[1]) {
                sum[1] = b.get(str.charAt(i - 1));
            } else if (str.charAt(i) == measuringUnit[2]) {
                if (str.charAt(0) == '十' || (str.charAt(0) == '零' && b.get(str.charAt(1)) == null)
                        || str.charAt(i - 1) == '零') {
                    sum[2] = 1;
                } else {
                    sum[2] = b.get(str.charAt(i - 1));
                }
            }
        }
        Integer number = a.get(str.substring(str.length() - 1));
        if (number != null) {
            sum[3] = number;
        } else {
            sum[3] = 0;
        }
        if (sum[0] != 0) {
            sum1[0] = sum[0] * 1000;
        }
        if (sum[1] != 0) {
            sum1[1] = sum[1] * 100;
        }
        if (sum[2] != 0) {
            sum1[2] = sum[2] * 10;
        }
        if (sum[3] != 0) {
            sum1[3] = sum[3];
        }
        return sum1[0] + sum1[1] + sum1[2] + sum1[3];
    }

}
