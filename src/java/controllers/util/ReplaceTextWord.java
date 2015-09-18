package controllers.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.TextPiece;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class ReplaceTextWord {

    private File inputFile = null;
    private HWPFDocument document = null;
    private HashMap<String, String> replacementText = null;
    private Set<String> keys = null;
    private String outputFileName;
    private boolean searching;

    public boolean isSearching() {
        return searching;
    }

    public void setSearching(boolean searching) {
        this.searching = searching;
    }

    public HashMap<String, String> getReplacementText() {
        return replacementText;
    }

    public void setReplacementText(HashMap<String, String> replacementText) {
        this.replacementText = replacementText;
    }

    /**
     * Create a new instance of the InsertText class using the following
     * parameters;
     *
     * @param filename An instance of the String class encapsulating the path
     * path to and name of the MS Word file that is to be processed. No checks
     * are made to ensure that the file is accessible or even of the correct
     * data type.
     * @param replacementText An instance of the HashMap class that contains one
     * or more key value pairs. Each pair consists of two Strings the first
     * encapsulates the placeholder and the second the text that should replace
     * it.
     * @param outputFileName A String contained the name of the output file
     * created after process the word document.
     *
     * @throws NullPointerException if a null value is passed to either
     * parameter.
     * @throws IllegalArgumentException if the value passed to either parameter
     * is empty.
     */
    public ReplaceTextWord(String filename, HashMap<String, String> replacementText, String outputFileName) throws NullPointerException, IllegalArgumentException {

        if (filename == null) {
            throw new NullPointerException("Null value passed to the filename parameter of the InsertText class constructor.");
        }
        if (replacementText == null) {
            throw new NullPointerException("Null value passed to the replacementText parameter of the InsertText class constructor.");
        }
        if (filename == null) {
            throw new IllegalArgumentException("An empty String was passed to the filename parameter of the InsertText class constructor.");
        }
        if (replacementText.isEmpty()) {
            throw new IllegalArgumentException("An empty HashMap was passed to the replacementText parameter of the InsertText class constructor.");
        }
        // Copy parameters to local variables. Get the set of keys backing the HashMap and open the file.
        this.replacementText = replacementText;
        this.keys = replacementText.keySet();
        this.inputFile = new File(filename);
        this.outputFileName = outputFileName;
    }

    public ReplaceTextWord(String filename, String outputFileName) {
        this.inputFile = new File(filename);
        this.outputFileName = outputFileName;
        this.replacementText = new HashMap<>();
    }

    /**
     * Called to replace any named 'placeholders' with their accompanying text.
     *
     * @param processFooter
     * @throws java.io.IOException
     */
    public void processFile() throws IOException {
        Range range;
        BufferedInputStream buffInputStream;
        BufferedOutputStream buffOutputStream = null;
        FileInputStream fileInputStream;
        FileOutputStream fileOutputStream;
        ParagraphText[] paragraphText;

        // Open the input file.
        fileInputStream = new FileInputStream(this.inputFile);
        buffInputStream = new BufferedInputStream(fileInputStream);
        this.document = new HWPFDocument(new POIFSFileSystem(buffInputStream));

        range = this.document.getOverallRange();
//        range = this.document.getRange();
        paragraphText = this.loadParagraphs(range);

        // Step through the paragraph text.
        for (int i = 0; i < paragraphText.length; i++) {
            if (searching) {
                String text = paragraphText[i].getUpdatedText();
                int firstPoint = text.indexOf("##");
                int secondPoint = -1;
                while (firstPoint > -1) {
                    secondPoint = text.indexOf("##", firstPoint + 1);
                    if (secondPoint > -1) {
                        String keyName = text.substring(firstPoint + 2, secondPoint);
                        replacementText.put(keyName, keyName);
                    }
                    firstPoint = text.indexOf("##", secondPoint + 1);
                }
            } else {
                if (this.keys == null) {
                    this.keys = replacementText.keySet();
                }
                for (String key : this.keys) {
                    if (paragraphText[i].getUpdatedText().contains(key)) {
                        paragraphText[i].updateText(
                                this.replacePlaceholders(key, this.replacementText.get(key), paragraphText[i].getUpdatedText()));
                    }
                }
                if (paragraphText[i].isUpdated()) {
                    range.getParagraph(paragraphText[i].getParagraphNumber()).replaceText(
                        paragraphText[i].getRawText(), paragraphText[i].getUpdatedText(), 0);
                }
            }
        }

        if (!searching) {
            // Save the document away.
            fileOutputStream = new FileOutputStream(new File(outputFileName));
            buffOutputStream = new BufferedOutputStream(fileOutputStream);
            this.document.write(buffOutputStream);
        }

        if (buffOutputStream != null) {
            try {
                buffOutputStream.flush();
                buffOutputStream.close();
            } catch (IOException ioEx) {
                // I G N O R E //
            }
        }
    }

    private String replacePlaceholders(String key, String value, String text) {
        int index = 0;

        while ((index = text.indexOf(key)) >= 0) {
            text = text.substring(0, index) + value + text.substring(index + key.length());
        }
        return (text);
    }

    private ParagraphText[] loadParagraphs(Range range) {
        Paragraph paragraph;
        String readText;
        int total = range.numParagraphs();
        
        ParagraphText[] paragraphText = new ParagraphText[total];
        try {
            for (int i = 0; i < total; i++) {
                paragraph = range.getParagraph(i);
                readText = paragraph.text();
                if (readText.endsWith("\n")) {
                    readText = readText + "\n";
                }
                paragraphText[i] = new ParagraphText(i, readText);
            }
        } catch (Exception ex) {
            paragraphText[0] = this.getTextFromPieces();
        }
        return (paragraphText);
    }

//    private ParagraphText[] loadParagraphsFooter(Range range) {
//        ParagraphText[] paragraphText = new ParagraphText[range.numParagraphs()];
//        Paragraph paragraph = null;
//        String readText = null;
//        try{
//         
//            for(int i = 0; i < range.numParagraphs(); i++) {
//                paragraph = range.getParagraph(i);
//                readText = paragraph.text();
//                if(readText.endsWith("\n")) {
//                    readText = readText + "\n";
//                }
//                paragraphText[i] = new ParagraphText(i, readText);
//            }
//            
//            
//        }
//        catch(Exception ex) {
//            paragraphText[0] = this.getTextFromPieces();
//        }
//        return(paragraphText);
//    }
    private ParagraphText getTextFromPieces() {
        TextPiece piece = null;
        StringBuffer buffer = new StringBuffer();
        String text = null;
        String encoding = "Cp1252";

        Iterator textPieces = this.document.getTextTable().getTextPieces().iterator();
        while (textPieces.hasNext()) {
            piece = (TextPiece) textPieces.next();
            if (piece.isUnicode()) {
                encoding = "UTF-16LE";
            }
            try {
                text = new String(piece.getRawBytes(), encoding);
                buffer.append(text);
            } catch (UnsupportedEncodingException e) {
                throw new InternalError("Standard Encoding " + encoding + " not found, JVM broken");
            }
        }
        text = buffer.toString();
        // Fix line endings (Note - won't get all of them
        text = text.replaceAll("\r\r\r", "\r\n\r\n\r\n");
        text = text.replaceAll("\r\r", "\r\n\r\n");
        if (text.endsWith("\r")) {
            text += "\n";
        }
        return (new ParagraphText(0, text));
    }
}
