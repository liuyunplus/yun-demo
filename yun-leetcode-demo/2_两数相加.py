"""
给你两个非空的链表，表示两个非负的整数。它们每位数字都是按照逆序的方式存储的，并且每个节点只能存储一位数字。
请你将两个数相加，并以相同形式返回一个表示和的链表。
你可以假设除了数字0之外，这两个数都不会以0开头。

示例1
输入：l1 = [2,4,3], l2 = [5,6,4]
输出：[7,0,8]
解释：342 + 465 = 807.

示例 2：
输入：l1 = [0], l2 = [0]
输出：[0]

示例 3：
输入：l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9]
输出：[8,9,9,9,0,0,0,1]
TODO 还没做完
"""


class ListNode:

    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next


def add_two_numbers(l1, l2):
    # 创建一个链表（最后是NoneNone）
    dummy = p = ListNode(None)
    sum = 0
    while l1 or l2 or sum != 0:
        # 更新sum
        sum += (l1.val if l1 else 0) + (l2.val if l2 else 0)
        # 给链表p.next赋值，值为sum%10
        p.next = ListNode(sum % 10)
        # 移动p的位置，是指针移动到下一个位置
        p = p.next
        # 如果l1非空，移动到下一个位置
        if l1: l1 = l1.next
        if l2: l2 = l2.next
        # Sum更新，取整
        sum = sum // 10
    return dummy.next
