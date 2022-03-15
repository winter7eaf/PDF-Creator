
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class CreatorController {

    //Cases in the switch function
    final int COMMAND = 0;
    final int TEXT = 1;

    //Create a PDF Editor object
    private final PdfEditor pdfEditor = new PdfEditor();

    //Store number of paragraph;
    int paraNum = 1;


    //Create an arraylist to store commands for text
    static ArrayList<String> commandsInput = new ArrayList<>();

    //Create a HashMap to store text with commands as one paragraph
    static HashMap<String, ArrayList<String>> paragraph = new HashMap<>();

    //Detect whether the input is a command or a text to write
    public int detectInput(String textInput) {
        int isCommand = 1;
        if (textInput.charAt(0) == '.') {
            isCommand = 0;
        }
        return isCommand;
    }

    //Check if it is a new paragraph
    public boolean checkIfNewPara() {
        return commandsInput.contains(".paragraph") || commandsInput.contains(".large") || commandsInput.contains(".nofill");
    }

    //Storing commands for later styling text
    public void storeCommand(String inputCommand) {
        commandsInput.add(inputCommand);
    }

    //Store content into paragraph HashMap and allContent LinkedHashMap
    public void storeContent(String textInput) {

        //Store text and commands as a paragraph
        paragraph.put(textInput, commandsInput);

        //Add a paragraph into LinkedHashMap for later repeat content
        PdfEditor.setParaContent("paragraph" + paraNum, paragraph);

        //Create a new commandsInput ArrayList for new paragraph
        commandsInput = new ArrayList<>();
    }

    //Styling text with commands one by one
    public void styleText(ArrayList<String> commandWanted) {
        for (int commandNum = 0; commandNum < commandWanted.size(); commandNum++) {
            String command = commandWanted.get(commandNum);

            // If the command already asked going back to regular formatting,
            // we do not need the ".indent" command to go back to previous formatting
            if (command.contains(".indent") && commandWanted.contains(".nofill")) {
                commandWanted.remove(commandNum);
                break;
            }
            //Set the command in for styling
            pdfEditor.setCommandWanted(command);
            pdfEditor.selectCommand();
        }
    }

    //Write one paragraph onto PDF
    public void WritePara() {
        for (String textToWrite : paragraph.keySet()) {

            //Set text into PDF Editor for styling
            pdfEditor.setTextToEdit(textToWrite);

            //Get the commands wanted for text
            ArrayList<String> commandWanted = paragraph.get(textToWrite);
            styleText(commandWanted);
            //Add text to paragraph
            pdfEditor.styledText();
        }
        //Write the whole paragraph onto PDF
        pdfEditor.writeOnPDF();
    }

    //Check if inputs are in the same paragraph and proceed to writing them onto PDF
    public void WriteText(String textInput) {
        if (!paragraph.isEmpty() && checkIfNewPara()) {
            //When it is not the first text input, and it is a new paragraph
            paraNum++;
            //Write the previous paragraph
            WritePara();
            //Create new paragraph
            paragraph = new HashMap<>();
            //Store the current text input
        }
        //Otherwise, When it is the first text input or the same paragraph
        //Store the current text input
        storeContent(textInput);
    }

    //repeat content after inputting all paragraph
    public void repeatContent() {
        //Add a new page before repeating the content
        pdfEditor.addPageBreak();

        LinkedHashMap<String, HashMap> allParagraphs = PdfEditor.getAllContent();

        for (String para : allParagraphs.keySet()) {
            paragraph = allParagraphs.get(para);
            WritePara();
        }
    }

    //Use switch to call the appropriate method calls
    public void run() {
        String input;
        boolean finished = false;
        do {
            int detectedInput = 2;

            input = Tools.getInput();
            if (!input.isEmpty()) {
                detectedInput = detectInput(input);
            }
            switch (detectedInput) {

                case COMMAND:
                    //Storing commands for later styling text
                    storeCommand(input);
                    break;

                case TEXT:
                    //Start the process of writing text onto PDF
                    WriteText(input);
                    break;

                default:
                    //Write the last paragraph before ending the programme
                    WritePara();
                    //if there is less than 3 pages of content
                    while (!pdfEditor.checkIfMinPage()) {
                        repeatContent();
                    }
                    pdfEditor.closeDocument();
                    finished = true;
                    break;
            }
        } while (!finished);
    }
}
