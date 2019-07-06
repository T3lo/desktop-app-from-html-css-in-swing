import java.util.*;
import java.io.*;

public class HTMLParser {
    // Create a list of all the closing and non-closing tags
    static final String []closing_tags = {
        "html",
        "head",
            "title",
            "script",
        "body",
            "div",
                "p",
                "h1", "h2", "h3", "h4", "h5", "h6",
                "span",
            "form",
                "button",
                "label",
            "bold",
            "a",
            "ul", "ol",
                "li",
            "table",
                "tr",
                "td",
                "thead",
                "tbody",
            "dl", "dd", "code", "dt", "tt", "pre", "i", "th",
            "caption", "strong", "noscript", "small", "font", "style"
    };
    static final String []non_closing_tags = {
        "meta", "br", "hr", "img", "input", "link", "param"
    };

    public static void main(String []args) throws Exception {
        String html_file = "I:\\XXX\\HTMLnCSSParser\\src\\resources\\web\\html\\test.html";
        System.out.println("Processing: " + html_file);
        FileReader fr = new FileReader(html_file);
        String file_content = "";
        int ch;
        while((ch=fr.read())!=-1) {
            file_content += (char) ch;
        }
        TreeListNode DOM = parseHTML(file_content);
        // serializeTree(DOM);
        printTree(DOM, " ");
    }

    static TreeListNode parseHTML(String file_content) {
        // Clean Code
        file_content = file_content
        .replaceAll("[ ]+", " ")
        .replaceAll("\n", "")
        .replaceAll("\r", "");
        System.out.println(file_content);
        System.out.println("__________________________________________________________________");
        
        int i = 0, j = 0;
        char []arr = file_content.toCharArray();
        int len = arr.length;
        
        // First parse <!DOCTYPE html>
        while(i<len) {
            if(arr[i]=='<' && arr[i+1]=='!') {
                // Look for >
                j = i + 2;
                while(j<len && arr[j]!='>') {
                    j++;
                }
                if(j>=len) {
                    // Error
                }
                else {
                    System.out.println(file_content.substring(i+1, j));
                }
                i = j;      // Allow this only if First line is "DOCTYPE html"
                break;
            }
            i++;
        }

        // Create the DOM Object Model
        TreeListNode root = new TreeListNode("ROOT");
        Stack<TreeListNode> node_stack = new Stack<>();
        Stack<Integer> start_pos_stack = new Stack<>();
        node_stack.push(root);
        start_pos_stack.push(0);
        TreeListNode temp = root;
        boolean is_closing = false;
        while(i<len) {
            if(arr[i]=='<') {
                // Look for closing >
                j = i + 1;
                while(j<len && arr[j]!='>') {
                    j++;
                }
                if(j>=len) {
                    // Error
                }
                else {
                    // Tags inside comments still work ## Need update
                    if(i<arr.length && arr[i+1]=='!') {
                        // go to ending > and then continue;
                        i = j + 1;
                        continue;
                    }
                    String tag_line = file_content.substring(i+1, j);
                    // get tag-name
                    String tag = extractTag(tag_line);
                    // get classes
                    HashMap<String, ArrayList<String>> attributes = extractAttributes(tag_line);
                    if(attributes!=null) {
                        for(Map.Entry<String, ArrayList<String>> attribute: attributes.entrySet()) {
                            String key = attribute.getKey();
                            ArrayList<String> values = attribute.getValue();
                            System.out.print(tag + " => [ " + key + " = {");
                            for(String value: values) {
                                System.out.print(value + ".");
                            }
                            System.out.print("} ] ");
                        }
                    }
                    else {
                        System.out.print(" Nada");
                    }
                    System.out.println();
                    
                    // If tag in non_closing_tag => don't look for </tag> && "the the tag maybe like <tag> or <tag/>" <= TCs

                    // Replace this with indexOf("</"+tag+">")
                    if(tag.charAt(0)=='/' && ("/"+node_stack.peek().getData()).equals(tag)) {
                        // Error prone ## Must change to check underflow
                        node_stack.pop();
                        temp = node_stack.peek();
                        System.out.println(tag + " <= " + temp.getData());
                        int start = start_pos_stack.pop();
                        temp.setInnerHTML(file_content.substring(start, i));
                    }
                    else {
                        TreeListNode node = new TreeListNode(tag);
                        node.setAttributes(attributes);
                        // System.out.println("SETTING attr to => " + node.getData() + node.printAttributes());
                        
                        // if br hr etc then don't push
                        is_closing = false;
                        for(String _tag: closing_tags) {
                            if(_tag.equals(tag)) {
                                is_closing = true;
                                break;
                            }
                        }
                        System.out.println("%%%%%%%%%%%%%%%%%%%%\t\t" + tag + ": " + is_closing);
                        if(is_closing) {
                            node_stack.push(node);
                            start_pos_stack.push(j+1);
                            temp.add(node);
                            temp = node;
                        }
                        else {
                            temp.add(node);
                        }

                        if(tag.equals("script")) {
                            // search for </script>
                            i = file_content.indexOf("</script>", j);
                            continue;
                        }
                    }
                    System.out.println("Temp : " + temp.getData() + "->" + ((temp.hasLeft())?temp.getLeft().getData():0));
                }
                i = j;
            }
            i++;
        }
        System.out.println();
        root.print();
        root.printAllTags();
        return root;
    }

    static String extractTag(String tag) {
        String tag_name = "";
        for(char ch : tag.toCharArray()) {
            if(ch==' ') {
                break;
            }
            tag_name += ch;
        }
        return tag_name;
    }

    // No More Needed [Deprecated]
    static String extractClasses(String tag) {
        int start = tag.indexOf("class");
        if(start==-1) {
            return "";
        }
        // Search for ending "
        int end = start+7;
        while(end < tag.length() && tag.charAt(end)!='\"') {
            end++;
        }
        return tag.substring(start+7, end);
    }
    static HashMap<String, ArrayList<String>> extractAttributes(String tag_line) {
        HashMap<String, ArrayList<String>> attributes = new HashMap<>();
        char []arr = tag_line.toCharArray();
        int start = tag_line.indexOf(" "), end = 0;
        // Needs improvement for unwanted whitespaces
        if(start != -1) {
            System.out.print(tag_line.substring(start) + " : ");
            start++;
        }
        else {
            return null;
        }
        // search for =
        int i = start;
        while(i<tag_line.length()) {
            while(i<tag_line.length() && arr[i]!='=') {
                i++;
            }
            if(i>=tag_line.length()) {
                return attributes;
            }
            else {
                // Found = at i-th index
                String key = tag_line.substring(start, i);
                String value = "";
                ArrayList<String> list_of_values = new ArrayList<>();
                // Now check whether it is key="val" or key=val
                if(i<tag_line.length() && arr[i+1]=='\"') {
                    // It is key="val"
                    // Find the other "
                    end = tag_line.indexOf("\"", i+2);
                    value = tag_line.substring(i+2, end);
                    // find whether value is like "val" or "v a l" {or "v, a, l" or "v; a; l"} => "later on"
                    StringTokenizer st = new StringTokenizer(value);
                    while(st.hasMoreTokens()) {
                        list_of_values.add(st.nextToken());
                    }
                    end++;
                }
                else {
                    // It is key=val
                    end = tag_line.indexOf(" ", i+1);
                    if(end != -1) {
                        value = tag_line.substring(i+1, end);
                    }
                    else {
                        value = tag_line.substring(i+1);
                    }
                    list_of_values.add(value);
                }
                i = end + 1;
                attributes.put(key, list_of_values);
            }
            start = i;
        }
        return attributes;
    }

    // static void serializeTree(TreeListNode root) {
    //     FileOutputStream fos = new FileOutputStream("I:\\XXX\\HTMLnCSSParser\\DOM");
    //     ObjectOutputStream oos = new ObjectOutputStream(fos);
    //     oos.close();
    //     fos.close();
    // }

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