"""
给定一个整数数组nums和一个整数目标值target，请你在该数组中找出和为目标值target的那两个整数，并返回它们的数组下标。
你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。
你可以按任意顺序返回答案。

示例1
输入: nums = [2,7,11,15], target = 9
输出: [0,1]
解释: 因为 nums[0] + nums[1] == 9，返回 [0, 1]。

示例2
输入: nums = [3,2,4], target = 6
输出: [1,2]
"""


def two_sum(nums, target):
    map = dict()
    for index, num in enumerate(nums):
        if target - num in map:
            return [map[target - num], index]
        map[nums[index]] = index
    return []


list = two_sum([3, 2, 4], 6)
print(list)
