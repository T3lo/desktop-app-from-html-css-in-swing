import java.io.*;

public class RunTree {
    public static void main(String []args) {
        System.out.println("Running TreeList");
        String val = "A";
        TreeListNode root = new TreeListNode("-1");
        TreeListNode nodes[] = new TreeListNode[6];
        for(int i=0;i<6;i++) {
            nodes[i] = new TreeListNode(new Integer(i).toString());
            val = val + val;
        }
        root.setLeft(nodes[0]);
        nodes[0].setNextNode(nodes[1]);
        nodes[0].setLeft(nodes[2]);
        nodes[1].setLeft(nodes[3]);
        nodes[1].add(nodes[4]);
        root.print();
        System.out.println(nodes[0].isChildOf(root) + ", " + nodes[2].isChildOf(root) + "\n\n");
        printTree(root, "   ");
    }

    static void printTree(TreeListNode root, String space) {
        // print the root->data
        // if(it has leftNode) => printTree(root->left)
        // if(it has nextNode) => printTree(root->nextNode)
        System.out.println(space + root.getData());
        if(root.hasLeft()) {
            printTree(root.getLeft(), space+"   ");
        }
        if(root.hasNextNode()) {
            printTree(root.getNextNode(), space);
        }
    }
}