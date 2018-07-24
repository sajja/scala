package com.leetcode.ex01;

import java.util.HashMap;
import java.util.Map;

public class TwoSum {
    public int[] attempt1(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            int n1 = nums[i];
            for (int j = i + 1; j < nums.length; j++) {
                int n2 = nums[j];
                if (n1 + n2 == target) {
                    return new int[]{n1, n2};
                }
            }
        }
        throw new IllegalArgumentException("No two sum solution");
    }

    public int[] attempt2(int[] nums, int target) {
        HashMap<Integer, Integer> lookup = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            lookup.put(nums[i], i);
        }

        for (int i = 0; i < nums.length; i++) {
            int rest = target - nums[i];
            if (lookup.containsKey(rest) && lookup.get(rest) != i) {
                return new int[]{i, lookup.get(rest)};
            }
        }
        throw new IllegalArgumentException("No two sum solution");

    }

    public int[] attempt3(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[] { map.get(complement), i };
            }
            map.put(nums[i], i);
        }
        throw new IllegalArgumentException("No two sum solution");

    }

    public static void main(String[] args) {
        TwoSum ts = new TwoSum();
//        ts.attempt1(new int[]{2,1,9}, 10);
        int[] vals = ts.attempt3(new int[]{3, 2, 4}, 6);
        System.out.println("");
    }
}
