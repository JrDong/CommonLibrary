package com.djr.commonlibrary.algorithm.leetcode;

/**
 * Created by DongJr on 2017/2/28.
 * <p>
 * Given two binary trees, write a function to check if they are equal or not.
 * Two binary trees are considered equal if they are structurally identical
 * and the nodes have the same value.
 */

public class SameTree {

    public boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && q == null) {
            return true;
        } else if (p == null || q == null) {
            return false;
        }

        boolean isLeftSame = isSameTree(p.left, q.left);
        boolean isRightSame = isSameTree(p.right, q.right);

        return p.val == q.val && isLeftSame && isRightSame;
    }

}
