package com.leetcode.ex03;

import java.util.HashMap;

public class LongestSubstringWithoutRepeatingCharacters {
    public static int testMe(String str) {

        HashMap<Character, Character> subStr = new HashMap<>();
        int longest = 0;

        for (int i = 0; i < str.length(); i++) {
            char currChar = str.charAt(i);

            if (subStr.containsKey(currChar)) {
                if (longest < subStr.size()) {
                    longest = subStr.size();
                }
            } else {
                subStr.put(currChar,currChar);
            }
        }

        return subStr.size() > longest? subStr.size() : longest;
    }


    public static void main(String[] args) {
        System.out.println(LongestSubstringWithoutRepeatingCharacters.testMe("pwwkew"));
    }
}
