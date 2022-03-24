import java.util.*;

public class ParagraphContent {

    //Create a paragraph LinkedHashMap variable to store texts and commands
    private final LinkedHashMap<String, ArrayList<String>> paragraph;

    //Use the constructor to create a new paragraph and store the contents
    public ParagraphContent(String text, ArrayList<String> commands) {
        paragraph = new LinkedHashMap<>();
        addTextWithCommands(text, commands);
    }

    //Put the commands and text into a paragraph
    public void addTextWithCommands(String text, ArrayList<String> commands){
        paragraph.put(text, commands);
    }

    /**
     * Methods to return texts and commands in a paragraph
     */
    public ArrayList<String> getCOMMANDS(Object key) {
        //preventing modification of exits from outside the ParagraphContent instance
        return new ArrayList<>(paragraph.get(key));
    }

    public Set<String> keySet() {
        return paragraph.keySet();
    }
}
