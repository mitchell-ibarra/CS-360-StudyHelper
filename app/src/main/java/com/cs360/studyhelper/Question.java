/**
 * Mitchell Ibarra
 * Southern New Hampshire University
 * CS 350
 *
 * @description Model Class Question.java stores Question id for unique identification,
 * question text, answer text, and the Subject.
 */
package com.cs360.studyhelper;


public class Question {

    private long mId;
    private String mText;
    private String mAnswer;
    private String mSubject;

    /**
     * Retrieve the question.
     * @return String mText Question text.
     */
    public String getText() {
        return mText;
    }

    /**
     * Set the text of the question.
     * @param text String Text for a specific question.
     */
    public void setText(String text) {
        this.mText = text;
    }

    /**
     * Set the question id.
     * @param id long The question id.
     */
    public void setId(long id) {
        mId = id;
    }

    /**
     * Retrieve the question id.
     * @return long mId The question id.
     */
    public long getId() {
        return mId;
    }

    /**
     * Get the answer to the question.
     * @return String mAnswer The String answer to the question.
     */
    public String getAnswer() {
        return mAnswer;
    }

    /**
     * Set the answer to the question.
     * @param answer String The answer for this specific question.
     */
    public void setAnswer(String answer) {
        this.mAnswer = answer;
    }

    /**
     * Get the subject that this question belongs to.
     * @return String mSubject The subject of this question.
     */
    public String getSubject() {
        return mSubject;
    }

    /**
     * Set the subject for this question.
     * @param subject String The subject that this question belongs to.
     */
    public void setSubject(String subject) {
        this.mSubject = subject;
    }
}
