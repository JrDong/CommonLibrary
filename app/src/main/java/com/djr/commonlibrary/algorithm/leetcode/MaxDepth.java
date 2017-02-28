package com.djr.commonlibrary.algorithm.leetcode;

/**
 * Created by DongJr on 2017/2/28.
 * <p>
 * Given a binary tree, find its maximum depth.
 * The maximum depth is the number of nodes along the longest path from the root node down to the farthest leaf node.
 */

public class MaxDepth {


    private int solution(TreeNode root) {

        if (root == null) {
            return 0;
        }

        int leftDepth = solution(root.left);
        int rightDepth = solution(root.right);

        return Math.max(leftDepth, rightDepth) + 1;
    }

}
