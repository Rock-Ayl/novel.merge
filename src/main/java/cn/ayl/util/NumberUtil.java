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
public class NumberUtil {

    //阿拉伯组
    static int[] ArabicNumbers = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    //大写数字组
    static String[] ChinaNumbers = {"一", "二", "三", "四", "五", "六", "七", "八", "九"};
    static char[] ChinaNumbersByChar = {'一', '二', '三', '四', '五', '六', '七', '八', '九'};
    static char[] carry = {'千', '百', '十'};

    //要用的缓存组
    static Map<String, Integer> a = new TreeMap<>();
    static Map<Character, Integer> b = new TreeMap<>();

    /**
     * 通用方法-大写转阿拉伯数字
     * eg:输入一个 三十二 返回一个  32
     */
    public static int getNumberByChina(String str) {
        for (int i = 0; i < 9; i++) {
            a.put(ChinaNumbers[i], ArabicNumbers[i]);
            b.put(ChinaNumbersByChar[i], ArabicNumbers[i]);
        }
        String yw;
        String ww;
        String qw;
        if (str.indexOf("亿") != -1) {
            String[] spy = str.split("亿");
            if (spy.length == 1) {
                yw = spy[0];
                return yw(yw);
            } else {
                yw = spy[0];
                String[] spwq = spy[1].split("万");
                if (spwq.length == 1) {
                    ww = spwq[0];
                    return yw(yw) + ww(ww);
                } else {
                    yw = spy[0];
                    ww = spwq[0];
                    qw = spwq[1];
                    return yw(yw) + ww(ww) + qw(qw);
                }
            }
        } else if (str.indexOf("万") != -1) {
            String[] spwq = str.split("万");
            if (spwq.length == 1) {
                ww = spwq[0];
                return ww(ww);
            } else {
                ww = spwq[0];
                qw = spwq[1];
                return ww(ww) + qw(qw);
            }
        } else {
            return qw(str);
        }
    }

    //计算9999位
    public static int qw(String str) {
        return gj(str);
    }

    //计算9999万位
    public static int ww(String str) {
        return gj(str) * 10000;
    }

    //计算24亿(int最大是24亿多)
    public static int yw(String str) {
        return gj(str) * 10000 * 10000;
    }

    //工具方法，用于计算4个位,把亿，万，进行拆分
    public static int gj(String str) {
        int[] sum = {0, 0, 0, 0};
        int[] sum1 = {0, 0, 0, 0};
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == carry[0]) {
                sum[0] = b.get(str.charAt(i - 1));
            } else if (str.charAt(i) == carry[1]) {
                sum[1] = b.get(str.charAt(i - 1));
            } else if (str.charAt(i) == carry[2]) {
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
