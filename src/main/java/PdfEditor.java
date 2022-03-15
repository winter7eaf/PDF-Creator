import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;

import java.io.IOException;

public class PdfEditor {

    //Create the PDF file name
    private final static String FILE_NAME = "PDFCreated.pdf";

    //Minimum number of pages
    public final static int MIN_PAGE_AMOUNT = 3;

    //Create a PDF Document
    private static PdfDocument pdfDocument;

    //Create a document
    private static Document document;

    //Create a text
    private Text text;

    //Create a paragraph
    private static Paragraph paragraph;

    //Default format
    static float DEFAULT_MARGIN_RIGHT;
    static float DEFAULT_MARGIN_BOTTOM;
    static float DEFAULT_MARGIN_LEFT;
    static PdfFont DEFAULT_FONT;

    public PdfEditor() {

        try {
            //Initialize PDF document
            pdfDocument = new PdfDocument(new PdfWriter(FILE_NAME));

            //Initialize document
            document = new Document(pdfDocument, pdfDocument.getDefaultPageSize(), false);

            //Get the default margins
            getDefaultFormat();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Save default margins
    public void getDefaultFormat() {
        DEFAULT_MARGIN_RIGHT = document.getRightMargin();
        DEFAULT_MARGIN_BOTTOM = document.getBottomMargin();
        DEFAULT_MARGIN_LEFT = document.getLeftMargin();
        DEFAULT_FONT = pdfDocument.getDefaultFont();
    }

    /**
     * Getter and setter for text and command
     */

    public void setTextToEdit(String textToWrite) {
        text = new Text(textToWrite);
    }

    //Select which command the user want
    public void selectCommand(String command) {

        //Get a copy of the command
        String commandCopy = command;

        //Make indent command without indent number
        if (command.contains(".indent")) {
            commandCopy = ".indent";
        }
        switch (commandCopy) {
            // Large command
            case ".large":
                large();
                break;

            //Paragraph command
            case ".paragraph":
                paragraph();
                break;

            //Fill command
            case ".fill":
                fill();
                break;

            //Nofill command
            case ".nofill":
                noFill();
                break;

            //Normal command
            case ".normal":
                break;

            //Regular command
            case ".regular":
                regular();
                break;

            //Italic command
            case ".italic":
                italic();
                break;

            //Bold command
            case ".bold":
                bold();
                break;

            //Indent command
            case ".indent":
                for (int charNum = command.length() - 1; charNum > 1; charNum--) {
                    //Store the character of command wanted, e.g. "-2"
                    char commandChar = command.charAt(charNum);
                    if (commandChar == '-' || commandChar == '+') {
                        String indentNumber = command.substring(charNum);
                        indent(indentNumber);
                        break;
                    }
                }
                break;
        }
    }

    /**
     * Functions to write on PDF
     */
    //Add text to paragraph
    public void styledText() {
        paragraph.add(text);
    }

    //Write text on PDF
    public void writeOnPDF() {
        //Add paragraph to document
        document.add(paragraph);
    }

    //Add page number onto PDF
    public void addPageNumber() {
        int numOfPages = pdfDocument.getNumberOfPages();

        for (int pageNum = 1; pageNum <= numOfPages; pageNum++) {
            //Get the page to add page number
            PdfPage page = pdfDocument.getPage(pageNum);

            //Create a styled canvas for page number
            try (Canvas canvas = new Canvas(new PdfCanvas(page, true), page.getPageSize())) {
                canvas.showTextAligned(String.valueOf(pageNum),
                        page.getPageSize().getRight() - DEFAULT_MARGIN_RIGHT,
                        DEFAULT_MARGIN_BOTTOM,
                        TextAlignment.RIGHT);
            }
        }
    }

    //Check if the PDF meet the minimum page requirement
    public boolean checkIfMinPage() {
        return pdfDocument.getNumberOfPages() >= MIN_PAGE_AMOUNT;
    }

    //Close the document
    public void closeDocument() {

        //Add page number before closing the document
        addPageNumber();

        //Close document
        document.close();
    }

    //Add a new page before repeating the content
    public void addPageBreak() {
        document.add(new AreaBreak());
    }

    /**
     * Functions of commands
     */

    //Make text as header
    public void large() {
        paragraph();
        text.setFontSize(25);
    }

    //Start a new paragraph
    public void paragraph() {
        paragraph = new Paragraph();
    }

    //Set indentation to fill for paragraphs
    public void fill() {
        paragraph.setMarginLeft(DEFAULT_MARGIN_LEFT);
        //Set all the last character of a line must end at the end of the margin
        paragraph.setTextAlignment(TextAlignment.JUSTIFIED);
    }

    //Set the formatter to regular formatting
    public void noFill() {
        paragraph();
    }

    //Reset the font to the normal font
    public void regular() {
        text.setFont(DEFAULT_FONT);
    }

    //Set the font to italic
    public void italic() {
        text.setItalic();
    }

    //Set the font to bold
    public void bold() {
        text.setBold();
    }

    //Set the indents to the specified amount
    public void indent(String indentNumberAsString) {
        int indentNumber = Integer.parseInt(indentNumberAsString.substring(1));
        if (indentNumberAsString.contains("+")) {
            text.setWordSpacing(indentNumber);
        } else {
            text.setWordSpacing(-indentNumber);
        }
    }
}
