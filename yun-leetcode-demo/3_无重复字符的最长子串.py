"""
给定一个字符串s，请你找出其中不含有重复字符的最长子串的长度。

示例1
输入: s = "abcabcbb"
输出: 3
解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。

示例2
输入: s = "bbbbb"
输出: 1
解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
"""


def length_of_longest_sub_string(s):
    # 哈希集合，记录每个字符是否出现过
    windows = set()
    n = len(s)
    # 右指针，初始值为-1，相当于我们在字符串的左边界的左侧，还没有开始移动
    right = -1
    # 无重复子串最长长度
    max_len = 0
    for i in range(n):
        if i != 0:
            # 左指针向右移动了一格，滑动窗口移除上一个字符
            windows.remove(s[i - 1])
        while right + 1 < n and s[right + 1] not in windows:
            # 不断地移动右指针
            windows.add(s[right + 1])
            right += 1
        # 第 i 到 right 个字符是一个极长的无重复字符子串
        max_len = max(max_len, right - i + 1)
    return max_len


length = length_of_longest_sub_string("abcabcbb")
print(length)