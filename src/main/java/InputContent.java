import java.io.*;
import java.util.*;

public class InputContent {

    //Create the input text file name
    private static final String INPUT_FILE_NAME = "Input.txt";

    //Create a static allParagraphs LinkedHashMap
    static LinkedHashMap<String, ParagraphContent> allParagraphs = new LinkedHashMap<>();

    //Create variable to count number of paragraph
    static int paraNum = 1;

    //Create a constructor to read the input text file and store all the contents
    public InputContent() {

        //Read from Input text file so user can navigate from one paragraph to another
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(INPUT_FILE_NAME))) {

            //Variables for reading lines, text, commands and paragraph ID
            String inputLine;
            String paragraphId = null;
            String text = null;
            ArrayList<String> commands = new ArrayList<>();

            //Check if the input text file has ended
            while ((inputLine = bufferedReader.readLine()) != null) {

                //If the input is a command
                if (detectInput(inputLine)) {
                    //Store the input inside a command ArrayList
                    commands.add(inputLine);
                } else {
                    //Store the input as text
                    text = inputLine;
                }

                //If it is a new paragraph
                if (checkIfNewPara(commands) && checkIfReady(text)) {

                    //Set the paragraph ID
                    paragraphId = "Paragraph " + paraNum;
                    //Store the text with corresponding commands and paragraph ID as a new paragraph
                    allParagraphs.put(paragraphId, new ParagraphContent(text, commands));
                    //Add up the paragraph number
                    paraNum++;

                    //Clear text and commands for next input
                    commands = new ArrayList<>();
                    text = null;

                } else if (!checkIfNewPara(commands) && checkIfReady(text)) {
                    //Otherwise, when it is the same paragraph

                    //Use the paragraph ID to make sure we are in the correct paragraph
                    //Store the text with corresponding commands
                    allParagraphs.get(paragraphId).addTextWithCommands(text, commands);

                    //Clear text and commands for next inputs
                    commands = new ArrayList<>();
                    text = null;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Detect whether the input is a command or a text to write
    public boolean detectInput(String textInput) {
        return textInput.charAt(0) == '.';
    }

    //Check if it is a new paragraph
    public boolean checkIfNewPara(ArrayList<String> commands) {
        return commands.contains(".paragraph") || commands.contains(".large") || commands.contains(".nofill");
    }

    //Check if ready to store as a paragraph
    public boolean checkIfReady(String text) {
        return text != null;
    }

    public Collection<ParagraphContent> values() {
        return allParagraphs.values();
    }
}
