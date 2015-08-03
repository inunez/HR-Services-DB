package controllers.util;

public class ParagraphText {
     
    private String rawText = null;
    private String updatedText = null;
    private boolean updated = false;
    private int paragraphNumber = 0;
 
    /**
     * Create an instance of the ParagraphText class using the following
     * parameters;
     * 
     * @param paragraphNumber A primitive int whose value indicates the
     *        number of the paragraph.
     * @param text An instance of the String class encapsulating the text
     *        recovered from the document.
     */
    public ParagraphText(int paragraphNumber, String text) {
        // Note that two 'copies' of the document's text are made. This is
        // so that one can be modified whilst the other is unaltered for
        // later use in the replaceText() method. This approach was adopted
        // because a call to the text() method on the final paragraph of the
        // document produced inconsistent results and it is essentil to know the
        // paragraphs original contents in order to update it successfully.
        this.rawText = text;
        this.updatedText = new String(text);
        this.paragraphNumber = paragraphNumber;
        this.updated = false;
    }
     
    /**
     * Get the original - unaltered - contents of the paragraph.
     * 
     * @return A String object that encapsulates the text as it was orginally
     *         recovered from the paragraph.
     */
    public String getRawText() {
        return(this.rawText);
    }
     
    /**
     * Get the contents for the paragraph following any modifications.
     * 
     * @return A String object encapsulating the modified version of the
     *         paragraphs text - that is the version where any and all
     *         'placeholders' have been replaced.
     */
    public String getUpdatedText() {
        return(this.updatedText);
    }
     
    /**
     * Get the number of the paragraph.
     * 
     * @return A primitive int whose value indicates the position of the
     *         paragraph within the entire series of paragraphs read from the
     *         document.
     */
    public int getParagraphNumber() {
        return(this.paragraphNumber);
    }
     
    /**
     * Update or modify the paragraphs contents.
     * 
     * @param text A String object that encapsulates the text the paragraph
     *        should contain following modification.
     */
    public void updateText(String text) {
        this.updatedText = text;
        this.setUpdated(true);
    }
     
    /**
     * Indicates whether the paragraphs text has been updated.
     * 
     * @return A boolean value indicating whether the text has been updated 
     *         - true - or not - false.
     */
    public boolean isUpdated() {
        return(this.updated);
    }
 
    /**
     * Returns a String that represents the current state of the object.
     * 
     * @return A String that encapsulates information about the objects state.
     *         In this case, the contents of it's member variables.
     * 
     * @Override
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("ParagraphText: ");
        buffer.append("Paragraph Number: ");
        buffer.append(this.paragraphNumber);
        buffer.append(" Raw Text: ");
        buffer.append(this.rawText);
        buffer.append(" Updated Text: ");
        buffer.append(this.updatedText);
        buffer.append(" Updated: ");
        buffer.append(this.updated);
        return(buffer.toString().trim());
    }
     
    /**
     * A utility method used to set the state of the updated flag.
     */
    private void setUpdated(boolean updated) {
        this.updated = updated;
    }
}