package com.djr.commonlibrary.algorithm;

import android.database.CursorIndexOutOfBoundsException;
import android.view.ViewGroup;

import java.util.LinkedList;

/**
 * Created by jerry on 2017/2/20.
 */

public class LinkedListReverse {




    public static void main(String[] args) {
        print(initNode(3));
        print(reverseByRecur(initNode(3)));
        print(reverse(initNode(5)));
    }

    public static Node reverse(Node head) {

        if (head == null){
            return head;
        }

        Node pre = head;
        Node current = head.nextNode;
        Node next = null;

        while (current!=null){
            next = current.nextNode;
            current.nextNode = pre;
            pre = current;
            current = next;
        }

        head.nextNode = null;

        return pre;
    }

    private static Node reverseByRecur(Node current) {
        if (current == null || current.nextNode == null) return current;
        Node nextNode = current.nextNode;
        current.nextNode = null;
        Node reverseRest = reverseByRecur(nextNode);
        nextNode.nextNode = current;
        return reverseRest;
    }


    private static Node initNode(int num) {
        Node node = new Node(0, null);
        Node cur = null;
        Node temp = null;
        for (int i = 1; i < num; i++) {
            temp = new Node(i, null);
            if (i == 1) {
                node.nextNode = temp;
            } else {
                cur.nextNode = temp;
            }
            cur = temp;
        }
        return node;
    }

    private static void print(Node head) {
        Node tempNode = head;
        while(tempNode != null){
            System.out.print(tempNode.value+" ");
            tempNode = tempNode.nextNode;
        }
        System.out.println();
    }

    static class Node {
        int value;
        Node nextNode;

        public Node(int value, Node nextNode) {
            this.value = value;
            this.nextNode = nextNode;
        }
    }
}

