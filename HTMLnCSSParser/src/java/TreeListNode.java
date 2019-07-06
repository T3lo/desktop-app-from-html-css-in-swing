import java.util.*;
import java.io.Serializable;

/*

    Data Members:
        1. data => name of the tag
        2. innerHTML => content enclosed by the tag
        {    
            3. left => points to the left element in the tree
            4. nextNode => points to the next element like a Linked List
        } => Refer to Multi-element tree using "Linked List" or "Vectors"
        5. attributes => It is a Mapping of the attributes to their value(s).
    
    Util functions:
        1. A.printTree() => prints the tree A in level order transversal
        2. A.isChildOf(B) => return true if A is a child of B
        3. A.isSiblingOf(B) => returns true if A is a sibling of B

    [ Getters and Setters are applied using "Camel Case" ]

*/

public class TreeListNode implements Serializable {
    String data;
    String innerHTML;
    TreeListNode left;
    TreeListNode nextNode;
    // 
    HashMap<String, ArrayList<String>> attributes;

    TreeListNode() {}
    TreeListNode(String data) {
        this.data = data;
        innerHTML = "";
        left = null;
        nextNode = null;
        attributes = new HashMap<>();
    }

    // Data
    public String getData() {
        return data;
    }
    
    // Inner HTML
    public String getInnerHTML() {
        return innerHTML;
    }
    public void setInnerHTML(String innerHTML) {
        this.innerHTML = innerHTML;
    }

    // Left Pointer
    public TreeListNode getLeft() {
        return left;
    }
    public void setLeft(TreeListNode node) {
        left = node;
    }
    public boolean hasLeft() {
        return left!=null;
    }

    // Next Node Pointer
    public TreeListNode getNextNode() {
        return nextNode;
    }
    public void setNextNode(TreeListNode node) {
        nextNode = node;
    }
    public boolean hasNextNode() {
        return nextNode!=null;
    }

    // Attributes
    public HashMap<String, ArrayList<String>> getAttributes() {
        return attributes;
    }
    public void setAttributes(HashMap<String, ArrayList<String>> attributes) {
        this.attributes = attributes;
    }
    public String printAttributes() {
        if(attributes==null || attributes.size()==0) {
            return " {}";
        }
        String result = "{\n";
        for(Map.Entry<String, ArrayList<String>> attribute: attributes.entrySet()) {
            String key = attribute.getKey();
            ArrayList<String> values = attribute.getValue();
            result += "  " + key + " = (";
            for(String value: values) {
                result += value + ":";
            }
            result += ")\n";
        }
        return result + "}";
    }

    // prints in level order transversal
    public void print() {
        Stack<TreeListNode> s = new Stack<TreeListNode>();
        s.push(this);
        TreeListNode next = null;
        while(!s.empty() || next!=null) {
            if(next==null) {
                next = s.pop();
            }
            System.out.println(next.getData() + next.printAttributes() + " : [\n" + next.getInnerHTML() + "\n]\n");
            if(next.hasLeft()) {
                s.push(next.getLeft());
            }
            next = next.getNextNode();
        }
        System.out.println();
    }

    // Add node as child to another node
    public TreeListNode add(TreeListNode node) {
        if(this.hasLeft()) {
            TreeListNode temp = this.getLeft();
            this.setLeft(node);
            node.setNextNode(temp);
        }
        else {
            this.setLeft(node);
        }
        return this;
    }

    // May not be used
    public boolean isChildOf(TreeListNode node) {
        if(node==null || !node.hasLeft()) {
            return false;
        }
        if(this==node.getLeft()) {
            return true;
        }
        else if(this.isSiblingOf(node.getLeft())) {
            return true;
        }
        else {
            System.out.println("Nada");
            return false;
        }
    }
    // May not be used
    public boolean isSiblingOf(TreeListNode node) {
        TreeListNode temp = node;
        while(temp.hasNextNode()) {
            temp = temp.getNextNode();
            if(this==temp) {
                return true;
            }
        }
        return false;
    }
    public void printAllTags() {
        ArrayList<String> list_of_all_tags = new ArrayList<>();
        // traverse the tree in search of tags
        Stack<TreeListNode> s = new Stack<TreeListNode>();
        s.push(this);
        TreeListNode next = null;
        while(!s.empty() || next!=null) {
            if(next==null) {
                next = s.pop();
            }
            // System.out.println(next.getData() + next.printAttributes() + " : [\n" + next.getInnerHTML() + "\n]\n");
            if(!list_of_all_tags.contains(next.getData())) {
                list_of_all_tags.add(next.getData());
            }
            if(next.hasLeft()) {
                s.push(next.getLeft());
            }
            next = next.getNextNode();
        }
        System.out.println();
        for(String tag: list_of_all_tags) {
            System.out.print(tag + " ");
        }
        System.out.println();
    }
}