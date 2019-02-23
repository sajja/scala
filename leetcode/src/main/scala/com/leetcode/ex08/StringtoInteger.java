package com.leetcode.ex08;

public class StringtoInteger {
    public static int power(int i) {
        int result = 1;
        for (int j = 0; j < i; j++) {
            result = result * 10;
        }
        return result;
    }

    public static int atoi(String s) {
        int p = s.length() - 1;
        int total = 0;
        for (int i = 0; i < s.length(); i++) {
            int digit = s.charAt(i) - '0';
            System.out.println("Char " + digit + " Decimal " + i + "Power " + p);
            total += (digit*power(p));
            p--;
        }

        return total;
    }

    public static void main(String[] args) {
        System.out.println(StringtoInteger.atoi("241"));
    }
}
