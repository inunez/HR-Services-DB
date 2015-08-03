
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
         
    /**
     * Create a new instance of the InsertText class using the following parameters;
     * 
     * @param filename An instance of the String class encapsulating the path
     *        path to and name of the MS Word file that is to be processed. No
     *        checks are made to ensure that the file is accessible or even
     *        of the correct data type.
     * @param replacementText An instance of the HashMap class that contains
     *        one or more key value pairs. Each pair consists of two Strings
     *        the first encapsulates the placeholder and the second the
     *        text that should replace it.
     * @param outputFileName A String contained the name of the output file
     *        created after process the word document.
     * 
     * @throws NullPointerException if a null value is passed to either parameter.
     * @throws IllegalArgumentException if the value passed to either parameter is empty.    
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
    
    /**
     * Called to replace any named 'placeholders' with their accompanying text.
     */
    public void processFile() {
        Range range = null;
        BufferedInputStream buffInputStream = null;
        BufferedOutputStream buffOutputStream = null;
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        ParagraphText[] paragraphText = null;
 
        try {
            // Open the input file.
            fileInputStream = new FileInputStream(this.inputFile);
            buffInputStream = new BufferedInputStream(fileInputStream);
            this.document = new HWPFDocument(new POIFSFileSystem(buffInputStream));
             
            // Get an instance of the Range class and load all of the Paragrpahs
            // the document contains into a local array of type ParagraphText.
//            range = this.document.getRange();
//            paragraphText = this.loadParagraphs(range);
// 
//            // Step through the paragraph text.
//            for(int i = 0; i < paragraphText.length; i++) {
//                 
//                // Step through the Set of keys that backs the HashMap of
//                // key/value pairs. For each key, test to see whether it exists
//                // in the paragraph of text and if so replace it with the specified text value.
//                for(String key : this.keys) {
//                    if(paragraphText[i].getUpdatedText().contains(key)) {
//                        paragraphText[i].updateText(
//                            this.replacePlaceholders(key, this.replacementText.get(key), paragraphText[i].getUpdatedText()));
//                    }
//                }
//                 
//                // After the paragraph has been tested against all of the keys
//                // for placeholders, check to see if any replacements were nade.
//                // If there were any replacements made then call the replaceText()
//                // method for the specific paragraph. Here the paragraph's
//                // number is recovered by calling the getParagraphNumber()
//                // method - this could easilly be replaced in the current
//                // situation with the number i which is the index of the enclosing
//                // for loop. If the updates were made in another loop however, the
//                // availability of the paragraph number could be useful.
//                //
//                // Note that two further calls are made to get the raw text - the
//                // text that was originallg recovered from the paragraph - and the
//                // updated text - the text with any placeholders replaced. It
//                // proved necessary to copy the text recovered from the paragraph
//                // becuase a call to the text() method of the Paragraph object
//                // only returned part of the text for the final paragraph.
//                if(paragraphText[i].isUpdated()) {
//                    range.getParagraph(paragraphText[i].getParagraphNumber()).replaceText(
//                            paragraphText[i].getRawText(), paragraphText[i].getUpdatedText(), 0);
//                }
//            }
            // Save the document away.
            fileOutputStream = new FileOutputStream(new File(outputFileName));
            buffOutputStream = new BufferedOutputStream(fileOutputStream);
            this.document.write(buffOutputStream);
        }
        catch(IOException ioEx) {
            System.out.println("Caught an: " + ioEx.getClass().getName());
            System.out.println("Message: " + ioEx.getMessage());
            System.out.println("StackTrace follows:");
            ioEx.printStackTrace(System.out);
        }
        finally {
            if(buffInputStream != null) {
                try {
                    buffInputStream.close();
                }
                catch(IOException ioEx) {
                    // I G N O R E //
                }
            }
             
            if(buffOutputStream != null) {
                try {
                    buffOutputStream.flush();
                    buffOutputStream.close();
                }
                catch(IOException ioEx) {
                    // I G N O R E //
                }
            }
        }
    }
     
    /**
     * Called to replace all occurrences of a placeholder.
     * 
     * @param key An instance of the String class that encapsulates the text of the placeholder.
     * @param value An instance of the String class that encapsulates the
     *        text that will be used to replace the placeholder.
     * @param text An instance of the String class that contains the contents
     *        of a paragraph read from a Word document.
     * 
     * @return An instance of the String class containing an updated version
     *         of the text originally recovered from the Word document; one
     *         where all occurrences of the placeholder have been replaced.
     */
    private String replacePlaceholders(String key, String value, String text) {
        int index = 0;
        // This is far from the most efficient way to parse the String as it
        // always starts from the beginning even after one or more substitutions
        // have been made. For now though it does work but will have obvious
        // limitations if the chunks of text are very large.
        while((index = text.indexOf(key)) >= 0) {
            text = text.substring(0, index) + value + text.substring(index + key.length());
        }
        return(text);
    }
     
    /**
     * Reads the contents of the document as a series of Paragraph objects. The
     * text is extracted from each and encapsulated into a instances of the ParagraphText class.
     * 
     * It proved to be problematic to call the text() method on an instance
     * of the Paragraph class - sometimes, such a call would fail to return
     * all of the text the actual paragraph contained. So, it was necessary to
     * read all of the text into local variables so that it could be more
     * effectively and successfully processed and this method was copied -
     * along with it's companion getTextFromPieces() from Nick Burch's WordExtractor class.
     * 
     * @param range An instance of the org.apache.poi.hwpf.usermodel.Range class
     *        that encapsulates information about the Word document - it's
     *        sections, paragraphs, tables, pictures, etc.
     * 
     * @return An array of type ParagraphText. Each element will contain an
     *         instance of the ParagraphText class that encasulates information
     *         about a paragraph of text - the text itself, the number of the
     *         paragraph, whether the text has been modified since it was read, etc.
     */
    private ParagraphText[] loadParagraphs(Range range) {
        ParagraphText[] paragraphText = new ParagraphText[range.numParagraphs()];
        Paragraph paragraph = null;
        String readText = null;
        try{
         
            for(int i = 0; i < range.numParagraphs(); i++) {
                paragraph = range.getParagraph(i);
                readText = paragraph.text();
                if(readText.endsWith("\n")) {
                    readText = readText + "\n";
                }
                paragraphText[i] = new ParagraphText(i, readText);
            }
        }
        catch(Exception ex) {
            paragraphText[0] = this.getTextFromPieces();
        }
        return(paragraphText);
    }
 
    /**
     * Again, with thanks to Nick Burch, this method will reconstruct the 
     * document's text from a series of TextPieces.
     * 
     * @return An instance of the ParagraphText class that encapsulates all
     *         of the text recovered from the document and treats it as one
     *         very large - potentially - paragraph.
     */
    private ParagraphText getTextFromPieces() {
        TextPiece piece = null;
        StringBuffer buffer = new StringBuffer();
        String text = null;
        String encoding = "Cp1252";
         
        Iterator textPieces = this.document.getTextTable().getTextPieces().iterator();
        while (textPieces.hasNext()) {
            piece = (TextPiece)textPieces.next();
            if (piece.isUnicode()) {
                encoding = "UTF-16LE";
            }
            try {
                text = new String(piece.getRawBytes(), encoding);
            buffer.append(text);
            } catch(UnsupportedEncodingException e) {
                throw new InternalError("Standard Encoding " + encoding + " not found, JVM broken");
            }
        }
        text = buffer.toString();
        // Fix line endings (Note - won't get all of them
        text = text.replaceAll("\r\r\r", "\r\n\r\n\r\n");
        text = text.replaceAll("\r\r", "\r\n\r\n");
        if(text.endsWith("\r")) {
            text += "\n";
        }
        return(new ParagraphText(0, text));
      }
}
