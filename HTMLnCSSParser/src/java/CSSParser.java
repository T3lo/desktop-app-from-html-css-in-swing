import java.io.*;
import java.util.*;

public class CSSParser {
    public static void main(String []args) throws Exception {
        String css_filename = args[0];
        System.out.println("Processing: " + css_filename);
        File css_file = new File(css_filename);
        FileReader fr = new FileReader(css_filename);
        if(css_file.exists()) {
            System.out.println("Name: " + css_file.getName());
            System.out.println("Length: " + css_file.length());
            System.out.println();
            int ch;
            String file_content = "";
            while((ch = fr.read()) != -1) {
                file_content += (char) ch;
            }

            // Cleaning the code of whitespace
            file_content = file_content
            .replaceAll("\n", "")
            .replaceAll("\r", "")
            .replaceAll("  ", "");
            System.out.println(file_content);
            ArrayList<CSSRule> rules = checkBraces(file_content);
            System.out.println();
            for(CSSRule r: rules) {
                System.out.println(r);
            }
        }
        else {
            System.out.println("File does not exist");
        }
    }

    static ArrayList<CSSRule> checkBraces(String file_content) {
        int len = file_content.length();

        // 1. Check for {...} pattern for putting the dat in the objects
        ArrayList<CSSRule> rules = new ArrayList<>();
        boolean braceFlag = true;
        int start = 0;
        int end = 0;
        String type = "", name = "";
        for(int i=0;i<len;i++) {
            char ch = file_content.charAt(i);
            if(ch=='{' && braceFlag) {
                braceFlag = false;
                start = i;
                // Extract element
                if(end < start) {
                    name = file_content.substring(end, start);
                    if(name.charAt(0)=='.') {
                        // Class
                        type = "class";
                    }
                    else if(name.charAt(0)=='#') {
                        // Id
                        type = "id";
                    }
                    else {
                        type = "tag";
                    }
                }
                else {
                    // Error
                }
            }
            else {
                // Error
                // throw Exception;
            }
            if(ch=='}' && !braceFlag) {
                // Add the content to ArrayList
                braceFlag = true;
                String content = file_content.substring(start+1, i);
                // Parse the content to get <key, value> pair
                StringTokenizer st1 = new StringTokenizer(content, ";");
                String key = "", value = "";
                HashMap<String, String> map = new HashMap<String, String>();
                while(st1.hasMoreTokens()) {
                    StringTokenizer st2 = new StringTokenizer(st1.nextToken(), ":");
                    key = st2.nextToken();
                    value = st2.nextToken();
                    map.put(key, value);
                }
                rules.add(new CSSRule(
                    name,
                    type,
                    content,
                    map
                ));
                end = i+1;
            }
            else {
                // Error
                // throw Exception;
            }
        }
        return rules;
    }
}