package com.leetcode.ex02;

class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
    }
}

public class AddTwoNumbers {


    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        boolean loop = true;

        ListNode head, curr = new ListNode(0);
        head = curr;
        int carry = 0;

        while (loop) {
            int iL1 = 0;
            int iL2 = 0;

            if (l1 != null) {
                iL1 = l1.val;
                l1 = l1.next;
            }

            if (l2 != null) {
                iL2 = l2.val;
                l2 = l2.next;
            }

            int mod = (iL1 + iL2 + carry) % 10;

            curr.next = new ListNode(mod);
            curr = curr.next;

            carry = (iL1 + iL2 + carry) / 10;

            loop = (l1 != null || l2 != null) || carry > 0;

        }

        return head.next;
    }

    public static void printNode(ListNode l) {
        if (l != null) {
            System.out.print(l.val);
            printNode(l.next);
        }
    }

    public static void main(String[] args) {
        ListNode l1 = new ListNode(1);
//        ListNode l11 = new ListNode(4);
//        ListNode l111 = new ListNode(3);
//
//        l1.next = l11;
//        l11.next = l111;

        ListNode l2 = new ListNode(9);
        ListNode l22 = new ListNode(9);
//        ListNode l222 = new ListNode(4);
        l2.next = l22;
//        l22.next = l222;


        printNode(addTwoNumbers(l1, l2));

    }
}
