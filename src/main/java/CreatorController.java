import java.util.ArrayList;

public class CreatorController {

    //Create a PDF Editor object and a InputContent object
    private static final PdfEditor pdfEditor = new PdfEditor();
    private static final InputContent inputContent = new InputContent();

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
            pdfEditor.selectCommand(command);
        }
    }

    //Write all the paragraphs onto PDF
    public void writeAllParas() {
        for (ParagraphContent paragraph : inputContent.values()) {
            for (String text : paragraph.keySet()) {
                //Set text into PDF Editor for styling
                pdfEditor.setTextToEdit(text);

                //Get the commands wanted for text
                ArrayList<String> commands = paragraph.getCOMMANDS(text);
                styleText(commands);
                //Add the styled text to paragraph
                pdfEditor.styledText();
            }
            //Write the whole paragraph onto PDF
            pdfEditor.writeOnPDF();
        }
    }

    //Repeat content when the PDF has not meet the minimum page number
    public void repeatContent() {
        //Add a new page before repeating the content
        pdfEditor.addPageBreak();

        //Repeat all the paragraphs
        writeAllParas();
    }

    //Write all the content and determine whether to repeat the content or not
    public void run() {

        //Write all the content for the first time
        writeAllParas();

        //While there is less than 3 pages of content
        while (!pdfEditor.checkIfMinPage()) {
            repeatContent();
        }

        //After writing out all the content, we close the document
        pdfEditor.closeDocument();
    }
}
