package com.djr.commonlibrary.algorithm.leetcode;

/**
 * Created by DongJr on 2017/2/28.
 *
 *
 * Given a binary tree containing digits from0-9only, each root-to-leaf path could represent a number.
 * An example is the root-to-leaf path1->2->3which represents the number123.
 * Find the total sum of all root-to-leaf numbers.
 *
 * For example,
 *
 *             1
 *            / \
 *           2   3
 *
 * The root-to-leaf path1->2represents the number12.
 * The root-to-leaf path1->3represents the number13.
 *
 * Return the sum = 12 + 13 =25.
 */
public class SumNumbers {

    public int solution(){
        TreeNode treeNode = new TreeNode(1);
        TreeNode treeNode1 = new TreeNode(2);
        treeNode.left = treeNode1;
        treeNode.right = new TreeNode(3);
        treeNode1.left = new TreeNode(4);

        return dfs(treeNode,0);
    }

    private int dfs(TreeNode root,int preSum){
        if (root == null){
            return 0;
        }
        int sum = root.val + preSum *10;

        System.out.println("sum = " + sum +" preSum = " + preSum);

        if (root.left == null || root.right == null) {
            return sum;
        }

        return dfs(root.left,sum)+dfs(root.right,sum);
    }

    public static void main(String[] args){
        SumNumbers sumNumbers = new SumNumbers();
        System.out.println(sumNumbers.solution()+"");
    }

}
