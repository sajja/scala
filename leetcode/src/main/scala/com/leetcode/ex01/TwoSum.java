package com.leetcode.ex01;

public class TwoSum {
    public int[] attempt1(int[] nums, int target) {
            for (int i = 0; i < nums.length; i++) {
                int n1 = nums[i];
                for (int j = i+1; j < nums.length; j++) {
                    int n2 = nums[j];
                    if (n1 + n2 == target) {
                        return new int[]{n1,n2};
                    }
                }
        }
        throw new IllegalArgumentException("No two sum solution");
    }

    public static void main(String[] args) {
        TwoSum ts = new TwoSum();
//        ts.attempt1(new int[]{2,1,9}, 10);
        int []vals = ts.attempt1(new int[]{3,2,4}, 6);
        System.out.println("");
    }
}
