package com.liuyun.github.utils;

public class StringUtils {

    /**
     * 验证是否是正则表达式
     *
     * @param regex
     * @return
     */
    public static boolean validRegEx(String regex) {
        if (regex.isEmpty()) {
            return false;
        }
        for (char c : regex.toCharArray()) {
            if (!validRegExChar(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 校验输入字符是否合法
     *
     * @param c
     * @return
     */
    public static boolean validRegExChar(char c) {
        return alphabet(c) || regexOperator(c);
    }

    /**
     * 判断是否是正则运算符
     *
     * @param c
     * @return
     */
    public static boolean regexOperator(char c) {
        return c == '(' || c == ')' || c == '*' || c == '|';
    }

    /**
     * 判断输入字符是否是字母或空串
     *
     * @param c
     * @return
     */
    public static boolean alphabet(char c) {
        return alpha(c) || c == Constants.EPSILON;
    }


    /**
     * 判断输入字符是否是字母
     *
     * @param c
     * @return
     */
    public static boolean alpha(char c) {
        return c >= 'a' && c <= 'z';
    }

}
