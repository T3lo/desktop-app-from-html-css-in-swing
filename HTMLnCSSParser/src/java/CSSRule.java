import java.util.*;

public class CSSRule {
    String type;
    String name;
    String content;     // Deprecate this in next version
    HashMap<String, String> map;

    public CSSRule() {
        map = new HashMap<String, String>();
    }
    public CSSRule(String name, String type, String content, HashMap<String, String> map) {
        this.name = name;
        this.type = type;
        this.content = content;
        this.map = map;
        extractFields();
    }

    void extractFields() {
        for(Map.Entry<String, String> entry: map.entrySet()) {
            System.out.print(String.format("{%s: ", entry.getKey().replace(" ", "")));
            String value = entry.getValue();
            StringTokenizer st = new StringTokenizer(value);
            while(st.hasMoreTokens()) {
                System.out.print("[" + st.nextToken() + "] ");
            }
            System.out.print("} ");
        }
        System.out.println();
    }

    @Override
    public String toString() {
        String result = "Name: " + name +
        "\nType: " + type +
        "\nContent: " + content + 
        "\nMap : [\n";
        for(Map.Entry<String, String> entry: map.entrySet()) {
            result += entry.getKey() + " => " + entry.getValue() + "\n";
        }
        result += "]\n";
        return result;
    }
}